/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.process.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import analysis.language.Visibility;

public class JavaFile extends GenericFile {

    // Constants

    private static final String[] KEY_BUFFER_PHRASES = new String[] { "(", ")", "<", ">" };
    private static final String[] REMOVE_TERMS = new String[] { "volatile", "abstract", "static", "final" };
    private static final String REGEX_VISIBILITY_FILE_DEF = "((public|private|protected) )?";

    private static Logger logger = LogManager.getLogger();

    // Constructors

    public JavaFile(File in, String root) throws IOException {
        super(in, root);
    }

    public JavaFile(List<String> lines, String context) {
        super(lines, context);
    }

    // Operations

    @Override
    public boolean detectInternalClasses() {
        int counter = 0;
        for (String s : getFileContents()) {
            if (isFileDefinition(s)) {
                counter++;
            }
        }
        return counter > 1;
    }

    @Override
    protected List<String> preProcess(String in) {
        List<String> out = new ArrayList<String>();
        while (in.contains("\\\\")) {
            in = in.replace("\\\\", StringUtils.EMPTY); // remove instances of \\ (double backslashes) as being
                                                        // redundant to important \" searching
        }
        in = in.replaceAll("\\\\\"", StringUtils.EMPTY); // remove \" String occurrences
        in = in.replaceAll("\"[^\"]*?\"", "\"\""); // remove String literals
        in = in.replaceAll("//.*?\n", StringUtils.LF); // remove comments

        in = in.replaceAll("(?<=@.*)\n", ";\n"); // Buffer @ lines preceding something to be on a separate line

        in = in.replaceAll(StringUtils.LF, " "); // remove new lines, add space gaps
        in = in.replaceAll("/\\*.*?\\*/", StringUtils.EMPTY); // remove multi-line comments (/* ... */) with non-greedy
                                                              // regex (? symbol) for minimal removal
        in = in.replaceAll("\t", StringUtils.EMPTY); // remove tabs
        in = in.replaceAll("  ", " "); // shorten whitespace
        in = in.replaceAll(";", ";\n"); // add newlines back in at all ;
        in = in.replaceAll("\\{", "\\{\n"); // add newlines around {
        in = in.replaceAll("\\}", "\n}\n"); // add newlines around }

        in = bufferCharacter(in, "\\{");
        in = bufferCharacter(in, "\\}");
        in = bufferCharacter(in, "\\)");
        in = bufferCharacter(in, "\\(");
        while (in.contains("  ")) {
            in = in.replaceAll("  ", " ");
        }
        in = in.replaceAll("(\n|$) ", StringUtils.LF);
        String[] parsed = in.trim().split(StringUtils.LF);
        for (String s : parsed) {
            if (s != null && s.trim() != null && !s.trim().isEmpty()) {
                out.add(s.trim());
            }
        }
        return out;
    }

    private String removeEquals(String in) {
        if (in.contains("=")) {
            return in.substring(0, in.indexOf("="));
        }
        return in;
    }

    private String processImportName(String line) {
        String[] use = cleanInput(line)[1].split("\\.");
        String nom = use[use.length - 1];
        String context = use[0];
        for (int i = 1; i < use.length - 1; i++) {
            context += "." + use[i];
        }
        return formFullName(context, nom);
    }

    private void processInstanceVariable(String in) {
        logger.debug(in);
        in = removeEquals(in);
        boolean underline = false;
        boolean fina = false;
        if (in.contains("static")) {
            underline = true; // TODO: Do the formatting here
        }
        if (in.contains("final")) {
            fina = true;
        }
        String[] cont = cleanInput(in);
        logger.debug(Arrays.toString(cont));
        Visibility vis = processVisibility(cont[0]);
        String typ = compileType(cont, 1);
        addInstanceVariableToClass(vis, cont[cont.length - 1], typ, underline, fina);
    }

    private void processFunction(String in) {
        boolean stat = false;
        boolean abs = false;
        boolean fin = false;
        if (in.contains(" static ")) {
            stat = true; // TODO: Do the formatting here
        }
        if (in.contains(" abstract ")) {
            abs = true;
        }
        if (in.contains(" final ")) {
            fin = true;
        }
        String[] cont = cleanInput(in);
        int argStart = ArrayUtils.indexOf(cont, "(");
        Visibility vis = processVisibility(cont[0]);
        String name = cont[argStart - 1];
        int typeIndex = 1;
        if (cont[typeIndex].equals("<")) {
            int depth = 1;
            typeIndex++;
            while (depth != 0) {
                if (cont[typeIndex].equals("<")) {
                    depth++;
                }
                if (cont[typeIndex].equals(">")) {
                    depth--;
                }
                typeIndex++;
            }
        }
        String ret = argStart == typeIndex ? StringUtils.EMPTY : compileType(cont, typeIndex);
        List<String> argNom = new ArrayList<String>();
        List<String> argTyp = new ArrayList<String>();
        for (int i = argStart + 1; i < cont.length - 2; i += 1) {
            if (cont[i].equals(")"))
                break;
            String type = compileType(cont, i);
            i = compileTypeLength(cont, i);
            String nom = cont[i].replaceAll(",", StringUtils.EMPTY);
            argNom.add(nom);
            argTyp.add(type);
        }
        if (ret.isEmpty()) {
            addConstructorToDef(vis, name, argNom, argTyp);
        } else {
            addFunctionToDef(vis, name, ret, argNom, argTyp, stat, abs, fin);
        }
    };

    // -- Extraction ------------------------------------------

    @Override
    public List<GenericFile> extractInternalClasses() {
        List<GenericFile> out = new ArrayList<GenericFile>();
        String context = getContext();
        List<String> headerInfo = new ArrayList<String>();
        for (String s : getFileContents()) {
            if (s.matches("import .*")) {
                headerInfo.add(s);
            }
        }
        searchFile(getFileContents(), out, headerInfo, 0, context);
        for (int i = 0; i < out.size(); i++) {
            if (out.get(i).getName() == null) {
                out.remove(i);
                i--;
            }
        }
        return out;
    }

    private int searchFile(List<String> contents, List<GenericFile> out, List<String> header, int currLine,
            String context) {
        if (currLine >= contents.size()) {
            return currLine;
        }
        List<String> lines = new ArrayList<String>();
        for (String s : header) {
            lines.add(s);
        }
        int dep = 0;
        boolean active = false;
        for (int i = currLine; i < contents.size(); i++) {
            String line = contents.get(i);
            if (isFileDefinition(line) && i != currLine) {
                if (!active) {
                    i = searchFile(contents, out, header, i, context);
                } else {
                    active = true;
                }
            } else {
                lines.add(line);
            }
            if (active) {
                if (line.contains("{")) {
                    dep++;
                }
                if (line.contains("}")) {
                    dep--;
                }
                if (dep == 0) {
                    out.add(new JavaFile(lines, context));
                    return i + 1;
                }
            }
        }
        out.add(new JavaFile(lines, context));
        return contents.size();
    }

    @Override
    protected boolean extractAbstract() {
        for (String line : getFileContents()) {
            if (isFileDefinition(line) && line.contains(" abstract ")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void extractFunctions() {
        boolean skip = false;
        for (String line : getFileContents()) {
            if (skip) {
                skip = false;
                continue;
            }
            if (isFunction(line)) {
                processFunction(line);
            } else if (line.contains("@Override")) {
                skip = true;
            }
        }
    }

    @Override
    protected void extractInstanceVariables() {
        for (String line : getFileContents()) {
            if (isInstanceVariable(line)) {
                processInstanceVariable(line);
            }
        }
    }

    @Override
    protected String extractInheritance() {
        for (String line : getFileContents()) {
            if (isClassDefinition(line) && line.contains("extends")) {
                String[] use = cleanInput(line);
                int posit = ArrayUtils.indexOf(use, "extends");
                String name = use[posit + 1];
                return name;
            }
        }
        return null;
    }

    @Override
    protected List<String> extractRealizations() {
        List<String> out = new ArrayList<String>();
        for (String line : getFileContents()) {
            if (isClassDefinition(line) && line.contains(getRealizationTerm())) {
                String[] use = cleanInput(line);
                int posit = ArrayUtils.indexOf(use, getRealizationTerm());
                while (++posit < use.length && use[posit].matches("[\\w><]*")) {
                    String name = use[posit].replaceAll("<[^>]*>", StringUtils.EMPTY);
                    out.add(name);
                }
            }
        }
        return out;
    }

    private String getRealizationTerm() {
        return isInterfaceFile() ? "extends" : "implements";
    }

    @Override
    protected List<String> extractAssociations(Set<String> neighbors) {
        List<String> out = new ArrayList<String>();
        for (String line : getFileContents()) {
            if (line.matches("import .*;")) {
                String name = processImportName(line);
                if (!name.contains("*")) {
                    out.add(name);
                } else {
                    String cont = name.substring(0, name.length() - 1);
                    out.add(cont.replace("/", ASSOCIATION_STAR_IMPORT));
                }
            } else {
                for (String gd : neighbors) {
                    String exNom = breakFullName(gd)[1];
                    if (isInPackageDependency(line, exNom) && !out.contains(gd)) {
                        if (!findName().equals(exNom) || (!isFileDefinition(line) && !isConstructor(line)))
                            out.add(gd);
                    }
                }
            }
        }
        return out;
    }

    @Override
    protected String findName() {
        for (String line : getFileContents()) {
            if (isFileDefinition(line)) {
                String[] use = cleanInput(line);
                int posit = ArrayUtils.indexOf(use, "class");
                posit = (posit == ArrayUtils.INDEX_NOT_FOUND ? ArrayUtils.indexOf(use, "interface") : posit);
                posit = (posit == ArrayUtils.INDEX_NOT_FOUND ? ArrayUtils.indexOf(use, "enum") : posit);
                String out = use[posit + 1];
                if (out.contains("<")) {
                    out = out.substring(0, out.indexOf("<"));
                }
                return out;
            }
        }
        return null;
    }

    // Analyze Type

    @Override
    public boolean isClassFile() {
        for (String s : getFileContents()) {
            if (s.matches(REGEX_VISIBILITY_FILE_DEF
                    + "(static )?(final )?(abstract )?(static )?(final )?(static )?class .*")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInterfaceFile() {
        for (String s : getFileContents()) {
            if (s.matches(REGEX_VISIBILITY_FILE_DEF + "(static )?interface .*")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnumFile() {
        for (String s : getFileContents()) {
            if (s.matches(REGEX_VISIBILITY_FILE_DEF + "(static )?enum .*")) {
                return true;
            }
        }
        return false;
    }

    // Tester Methods

    private boolean isFileDefinition(String line) {
        return line.matches(REGEX_VISIBILITY_FILE_DEF
                + "(static )?(final )?(static )?(abstract )?(static )?(final )?(class|interface|enum) .*");
    }

    private boolean isInPackageDependency(String line, String name) {
        return line.matches(".*([^a-zA-Z\\d]+|^)" + name + "[^a-zA-Z\\d]+.*") && !line.matches("package .*");
    }

    private boolean isClassDefinition(String line) {
        return line.matches(REGEX_VISIBILITY_FILE_DEF + "(abstract )?class .*");
    }

    private boolean isConstructor(String line) {
        return line.matches(REGEX_VISIBILITY_FILE_DEF + getName() + "\\s*\\(.*");
    }

    private boolean isInstanceVariable(String in) {
        in = removeEquals(in);
        return in.matches("(private|public|protected)[^{]*") && !in.contains("abstract") && !in.contains("(");
    }

    private boolean isFunction(String in) {
        return in.matches("(private|public|protected).*") && !in.contains(" new ") && in.contains("(")
                && !in.contains("=");
    }

    // Support Methods

    private String[] cleanInput(String in) {
        String out = in.replaceAll("  ", " ").replaceAll(";", StringUtils.EMPTY).trim();
        for (String s : REMOVE_TERMS) {
            out = out.replaceAll(" " + s + " ", " ");
        }
        for (String s : KEY_BUFFER_PHRASES) {
            out = bufferCharacter(out, "\\" + s);
        }
        out = out.replaceAll("\\.\\.\\.", "... ");
        while (out.contains(" ...")) {
            out = out.replaceAll(" \\.\\.\\.", "...");
        }
        String[] fin = out.split("\\s+");
        for (int i = 0; i < fin.length; i++) {
            fin[i] = fin[i].trim();
        }
        return fin;
    }

    private String bufferCharacter(String out, String in) {
        while (out.contains(in + " ")) {
            out = out.replaceAll(in + " ", in);
        }
        while (out.contains(" " + in)) {
            out = out.replaceAll(" " + in, in);
        }
        return out.replaceAll(in, " " + in + " ");
    }

    private String compileType(String[] line, int start) {
        String out = line[start++];
        if (line[start].equals("<")) {
            int depth = 1;
            out += line[start++];
            while (depth != 0) {
                out += line[start];
                if (line[start].equals("<")) {
                    depth++;
                }
                if (line[start].equals(">")) {
                    depth--;
                }
                start++;
            }
        }
        return out;
    }

    private int compileTypeLength(String[] line, int start) {
        String out = line[start++];
        if (line[start].equals("<")) {
            int depth = 1;
            out += line[start++];
            while (depth != 0) {
                out += line[start];
                if (line[start].equals("<")) {
                    depth++;
                }
                if (line[start].equals(">")) {
                    depth--;
                }
                start++;
            }
        }
        return start;
    }

    private Visibility processVisibility(String in) {
        switch (in) {
            case "public":
                return Visibility.PUBLIC;
            case "private":
                return Visibility.PRIVATE;
            case "protected":
                return Visibility.PROTECTED;
            default:
                return Visibility.PACKAGE;
        }
    }

}
