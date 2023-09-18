/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.process.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import analysis.language.Visibility;
import analysis.language.actor.GenericClass;
import analysis.language.actor.GenericDefinition;
import analysis.language.actor.GenericEnum;
import analysis.language.actor.GenericInterface;
import analysis.process.Cluster;

public abstract class GenericFile {

    // Constants

    protected static final String FULL_NAME_SEPARATOR = Character.toString(IOUtils.DIR_SEPARATOR_UNIX);

    protected static final String ASSOCIATION_STAR_IMPORT = "*";

    // Instance Variables

    private static boolean procInstance;
    private static boolean procFunction;
    private static boolean procPrivate;
    private static boolean procConstants;

    private String contents;
    private List<String> lines;
    private String name;
    private String context;
    private GenericDefinition gen;

    // Constructors

    public GenericFile(File in, String root) throws IOException {
        root = FilenameUtils.separatorsToUnix(root);
        lines = new ArrayList<String>();
        StringBuilder contentsBuilder = new StringBuilder();
        try (Scanner sc = new Scanner(in)) {
            while (sc.hasNextLine()) {
                String nex = sc.nextLine();
                if (nex != null) {
                    contentsBuilder.append(nex);
                    contentsBuilder.append(StringUtils.LF);
                }
            }
        }
        contents = contentsBuilder.toString();
        lines = preProcess(contents);
        name = findName();
        context = FilenameUtils.separatorsToUnix(in.getAbsolutePath()).substring(root.length());
        if (context.contains(FULL_NAME_SEPARATOR)) {
            context = context.substring(0, context.lastIndexOf(IOUtils.DIR_SEPARATOR_UNIX));
        }
        context = context.replace(IOUtils.DIR_SEPARATOR_UNIX, '.');
        if (context.equals(in.getName())) {
            context = StringUtils.EMPTY;
        }
        if (isClassFile()) {
            gen = new GenericClass(getName(), getContext());
        } else if (isInterfaceFile()) {
            gen = new GenericInterface(getName(), getContext());
        } else if (isEnumFile()) {
            gen = new GenericEnum(getName(), getContext());
        }
    }

    public GenericFile(List<String> lines, String context) {
        this.lines = lines;
        name = findName();
        this.context = context;
        if (isClassFile()) {
            gen = new GenericClass(getName(), getContext());
        } else if (isInterfaceFile()) {
            gen = new GenericInterface(getName(), getContext());
        } else if (isEnumFile()) {
            gen = new GenericEnum(getName(), getContext());
        }
    }

    // Operations

    public void process(Map<String, GenericDefinition> classRef, Cluster parent) {
        Set<String> neighbors = parent.getCluster(context.split("\\.")).getComponents();
        if (isClassFile()) {
            processClass(classRef, neighbors);
        } else if (isInterfaceFile()) {
            processInterface(classRef, neighbors);
        } else if (isEnumFile()) {
            processEnum(classRef.get(getFullName()), classRef, neighbors);
        }
    }

    public void processClass(Map<String, GenericDefinition> classRef, Set<String> neighbors) {
        handleInheritance(extractInheritance(), classRef);

        ((GenericClass) gen).setAbstract(extractAbstract());
        Set<String> bar = handleRealizations(extractRealizations(), classRef);
        handleAssociations(neighbors, bar, classRef);
        if (getStatusFunction()) {
            extractFunctions();
        }

        if (getStatusInstanceVariable()) {
            extractInstanceVariables();
        }
    }

    public void processInterface(Map<String, GenericDefinition> classRef, Set<String> neighbors) {
        Set<String> bar = handleRealizations(extractRealizations(), classRef);
        handleAssociations(neighbors, bar, classRef);
        if (getStatusFunction()) {
            extractFunctions();
        }
    }

    public void processEnum(GenericDefinition in, Map<String, GenericDefinition> classRef, Set<String> neighbors) {
        Set<String> bar = handleRealizations(extractRealizations(), classRef);
        handleAssociations(neighbors, bar, classRef);
        if (getStatusFunction()) {
            extractFunctions();
        }
    }

    // Other

    private void handleInheritance(String parName, Map<String, GenericDefinition> ref) {
        if (parName == null)
            return;
        for (GenericDefinition gd : ref.values()) {
            if (gd.getName().equals(parName)) {
                ((GenericClass) gen).setInheritance(gd);
                return;
            }
        }
    }

    private Set<String> handleRealizations(List<String> realiz, Map<String, GenericDefinition> ref) {
        Set<String> bar = new HashSet<String>();
        for (String s : realiz) {
            for (GenericDefinition gi : ref.values()) {
                if (gi.getName().equals(s)) {
                    gen.addRealization(gi);
                    bar.add(s);
                }
            }
        }
        return bar;
    }

    private void handleAssociations(Set<String> neighbors, Set<String> bar, Map<String, GenericDefinition> ref) {
        List<String> noms = extractAssociations(neighbors);
        for (String s : noms) {
            if (ref.get(s) != null) {
                if (!bar.contains(breakFullName(ref.get(s).getFullName())[1])) { // TODO: While I only allow one
                                                                                 // association
                    gen.addAssociation(ref.get(s));
                }
            } else if (!s.contains(ASSOCIATION_STAR_IMPORT)) {
                for (GenericDefinition gd : ref.values()) {
                    if (gd.getName().equals(s) && !bar.contains(gd.getName())) {
                        gen.addAssociation(gd);
                    }
                }
            } else {
                String path = s.substring(0, s.length() - 1);
                for (String con : ref.keySet()) {
                    if (con.matches(path + "/.*") && !gen.hasAssociate((ref.get(con)))
                            && !bar.contains(breakFullName(ref.get(con).getFullName())[1])) {
                        gen.addAssociation(ref.get(con));
                    }
                }
            }
        }
    }

    // Subclass Implement

    public abstract boolean isClassFile();

    public abstract boolean isInterfaceFile();

    public abstract boolean isEnumFile();

    public abstract boolean detectInternalClasses();

    public abstract List<GenericFile> extractInternalClasses();

    protected abstract String findName();

    /**
     * According to whatever rules of the language the file is for, process the lump
     * sum String file contents into
     * significant lines of single spaced text.
     *
     */

    protected abstract List<String> preProcess(String contents);

    protected abstract boolean extractAbstract();

    protected abstract void extractFunctions();

    protected abstract void extractInstanceVariables();

    protected abstract String extractInheritance();

    protected abstract List<String> extractRealizations();

    protected abstract List<String> extractAssociations(Set<String> neighbor);

    // Support Methods

    protected String stripContext(String in) {
        return in.substring(in.lastIndexOf('.') + 1);
    }

    protected String formFullName(String context, String name) {
        return context + IOUtils.DIR_SEPARATOR_UNIX + name;
    }

    protected void addFunctionToDef(int vis, String name, String returnType, List<String> argNames,
            List<String> argTypes, boolean statStatic, boolean statAbstract, boolean isFinal) {
        if (privateCheck(vis)) {
            gen.addFunction(Visibility.valueOf(vis), name, returnType, argNames, argTypes, statStatic, statAbstract,
                    isFinal);
        }
    }

    /**
     * 
     * @param vis
     * @param nom
     * @param ret
     * @param argNames
     * @param argTypes
     * @param statStatic
     * @param statAbstract
     * @param isFinal
     * 
     * @since 2.0
     */
    protected void addFunctionToDef(Visibility vis, String name, String returnType, List<String> argNames,
            List<String> argTypes, boolean statStatic, boolean statAbstract, boolean isFinal) {
        if (privateCheck(vis)) {
            gen.addFunction(vis, name, returnType, argNames, argTypes, statStatic, statAbstract, isFinal);
        }
    }

    protected void addConstructorToDef(int vis, String name, List<String> argNames, List<String> argTypes) {
        if (privateCheck(vis)) {
            gen.addConstructor(Visibility.valueOf(vis), name, argNames, argTypes);
        }
    }

    /**
     * 
     * @param vis
     * @param name
     * @param argNames
     * @param argTypes
     * 
     * @since 2.0
     */
    protected void addConstructorToDef(Visibility vis, String name, List<String> argNames, List<String> argTypes) {
        if (privateCheck(vis)) {
            gen.addConstructor(vis, name, argNames, argTypes);
        }
    }

    protected void addInstanceVariableToClass(int vis, String type, String name, boolean statStatic,
            boolean statFinal) {
        if (privateCheck(vis) && constantCheck(statFinal)) {
            ((GenericClass) gen).addInstanceVariable(Visibility.valueOf(vis), type, name, statStatic, statFinal);
        }
    }

    /**
     * 
     * @param vis
     * @param type
     * @param name
     * @param statStatic
     * @param statFinal
     * 
     * @since 2.0
     */
    protected void addInstanceVariableToClass(Visibility vis, String type, String name, boolean statStatic,
            boolean statFinal) {
        if (privateCheck(vis) && constantCheck(statFinal)) {
            ((GenericClass) gen).addInstanceVariable(vis, type, name, statStatic, statFinal);
        }
    }

    private boolean privateCheck(int vis) {
        return privateCheck(Visibility.valueOf(vis));
    }

    /**
     * 
     * @param vis
     * @return
     * 
     * @since 2.0
     */
    private boolean privateCheck(Visibility vis) {
        return getStatusPrivate() || vis != Visibility.PRIVATE;
    }

    private boolean constantCheck(boolean isFinal) {
        return getStatusConstant() || !isFinal;
    }

    // Setter Methods

    public static void assignProcessStates(boolean inst, boolean func, boolean priv, boolean constant) {
        procInstance = inst;
        procFunction = func;
        procPrivate = priv;
        procConstants = constant;
    }

    // Getter Methods

    public List<String> getFileContents() {
        return lines;
    }

    public GenericDefinition getDefinition() {
        return gen;
    }

    public String[] breakFullName(String in) {
        return in.split(FULL_NAME_SEPARATOR);
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public String getFullName() {
        return getContext() + IOUtils.DIR_SEPARATOR_UNIX + getName();
    }

    protected boolean getStatusInstanceVariable() {
        return procInstance;
    }

    protected boolean getStatusPrivate() {
        return procPrivate;
    }

    protected boolean getStatusFunction() {
        return procFunction;
    }

    protected boolean getStatusConstant() {
        return procConstants;
    }

}
