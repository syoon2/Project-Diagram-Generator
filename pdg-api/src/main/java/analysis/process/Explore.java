/*
 * Copyright (c) Ada and Sung Ho Yoon. All rights reserved.
 * Licensed under the MIT license. See LICENSE-mit file in the project root
 * for details.
 */

package analysis.process;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import analysis.language.actor.GenericClass;
import analysis.language.actor.GenericDefinition;
import analysis.language.actor.GenericEnum;
import analysis.language.actor.GenericInterface;
import analysis.process.file.FileFactory;
import analysis.process.file.GenericFile;

public class Explore implements Runnable {

    private static Logger logger = LogManager.getLogger();

    // Instance Variables

    private List<GenericFile> files;
    private Map<String, GenericClass> classes;
    private Map<String, GenericInterface> interfaces;
    private Map<String, GenericEnum> enums;
    private Cluster parent;
    private String rootPath;

    private Set<String> ignore;

    // Constructors

    public Explore(File root) {
        rootPath = root.getAbsolutePath();
        ignore = new HashSet<String>();
        files = new ArrayList<GenericFile>();
        classes = new HashMap<String, GenericClass>();
        interfaces = new HashMap<String, GenericInterface>();
        enums = new HashMap<String, GenericEnum>();
        parent = new Cluster(ArrayUtils.EMPTY_STRING_ARRAY);
        if (rootPath.charAt(rootPath.length() - 1) != File.separatorChar) {
            rootPath += File.separatorChar;
        }
    }

    // Operations

    public void ignorePackage(String path) {
        ignore.add(path);
    }

    @Override
    public void run() {
        try {
            File use = new File(rootPath);
            logger.info("Begun exploring files");
            explore(use);
            logger.info("Finished exploring files");
            logger.info("Begun processing files");
            for (GenericFile f : files) {
                f.process(getDefinitionMapping(), parent);
            }
            logger.info("Finished processing files");
        } catch (IOException ioe) {
            throw new UncheckedIOException(logger.throwing(ioe));
        }
    }

    private void explore(File root) throws IOException {
        for (File look : root.listFiles()) {
            if (look.isDirectory() && !ignore(look.getAbsolutePath())) {
                explore(look);
            } else if (look.isFile()) {
                List<GenericFile> gfs = FileFactory.generateFile(look, rootPath);
                if (gfs == null) {
                    continue;
                }
                for (GenericFile f : gfs) {
                    if (f == null || f.getDefinition() == null) {
                        continue;
                    }
                    GenericDefinition gd = f.getDefinition();

                    boolean canAdd = false;
                    if (f.isClassFile()) {
                        classes.put(gd.getFullName(), (GenericClass) gd);
                        canAdd = true;
                    } else if (f.isInterfaceFile()) {
                        interfaces.put(gd.getFullName(), (GenericInterface) gd);
                        canAdd = true;
                    } else if (f.isEnumFile()) {
                        enums.put(gd.getFullName(), (GenericEnum) gd);
                        canAdd = true;
                    }
                    if (canAdd)
                        parent.addComponent(gd.getContextArray(), gd.getFullName());
                    files.add(f);
                }
            }
        }
    }

    private boolean ignore(String path) {
        return ignore.contains(formPackagePath(path));
    }

    private String formPackagePath(String path) {
        String out = path.substring(rootPath.length());
        out = out.replaceAll("\\\\", ".");
        return out;
    }

    // Getter Methods

    public Collection<GenericClass> getClasses() {
        return classes.values();
    }

    public Collection<GenericInterface> getInterfaces() {
        return interfaces.values();
    }

    public Collection<GenericEnum> getEnums() {
        return enums.values();
    }

    public List<GenericDefinition> getDefinitions() {
        List<GenericDefinition> out = new ArrayList<>();
        for (String s : classes.keySet())
            out.add(classes.get(s));
        for (String s : interfaces.keySet())
            out.add(interfaces.get(s));
        for (String s : enums.keySet())
            out.add(enums.get(s));
        return out;
    }

    public Map<String, GenericDefinition> getDefinitionMapping() {
        Map<String, GenericDefinition> out = new HashMap<String, GenericDefinition>();
        for (String s : classes.keySet())
            out.put(s, classes.get(s));
        for (String s : interfaces.keySet())
            out.put(s, interfaces.get(s));
        for (String s : enums.keySet())
            out.put(s, enums.get(s));
        return out;
    }

    public Cluster getClusterRoot() {
        return parent;
    }

    // Setter Methods

    public static void setParameters(boolean inst, boolean func, boolean priv, boolean consta) {
        GenericFile.assignProcessStates(inst, func, priv, consta);
    }

}
