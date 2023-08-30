/*
 * This file is part of the Project-Diagram-Generator distribution
 * (https://github.com/syoon2/Project-Diagram-Generator).
 * Copyright (c) 2023 Sung Ho Yoon.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package main;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.*;
import org.apache.commons.collections4.properties.PropertiesFactory;
import org.apache.commons.lang3.SystemUtils;

import image.ConvertVisual;
import ui.PDGWindow;

/**
 * The entry point used by Project Diagram Generator.
 * 
 * @author Sung Ho Yoon
 */
public class PDGLauncher {

    /**
     * Automatically generated Git properties at build time
     */
    private static final Properties gitProperties;
    static {
        try {
            gitProperties = PropertiesFactory.INSTANCE
                    .load(PDGLauncher.class.getClassLoader(), "git.properties");
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * The default directory to save generated images to.
     */
    public static final String ADDRESS_IMAGES = "./Diagram/images/";

    public static void main(String[] args) throws ParseException {
        if (args.length == 0)
            runReal();
        else
            launchHeadless(args);
    }

    /**
     * Launches Project Diagram Generator in headless mode.
     * 
     * @param args command line arguments
     * @throws ParseException if failed to parse command line arguments
     */
    private static void launchHeadless(String[] args) throws ParseException {
        Options cliOptions = new Options();

        Option root = Option.builder("root").hasArg(true).numberOfArgs(1).argName("path-to-root-of-project")
                .desc("Specifies the path to project root.").type(File.class).build();
        Option savename = Option.builder("savename").hasArg(true).argName("image-name")
                .desc("Specifies the filename of the generated diagram.").type(File.class).build();
        Option instanceVariable = new Option("i", false,
                "If this argument is present, the generated diagram will show instance variables.");
        Option privateEntities = new Option("p", false,
                "If this argument is present, the generated diagram will show private entities.");
        Option functions = new Option("f", false,
                "If this argument is present, the generated diagram will show functions.");
        Option constants = new Option("c", false,
                "If this argument is present, the generated diagram will show constants.");
        Option help = new Option("h", "help", false, "Displays this help message then exits.");

        cliOptions.addOption(root);
        cliOptions.addOption(savename);
        cliOptions.addOption(instanceVariable);
        cliOptions.addOption(privateEntities);
        cliOptions.addOption(functions);
        cliOptions.addOption(constants);
        cliOptions.addOption(help);

        CommandLineParser parser = new DefaultParser();

        CommandLine line = parser.parse(cliOptions, args, true);
        if (line.hasOption(help)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar Project-Diagram-Generator.jar", cliOptions, true);
            System.exit(0);
        }
        List<Option> missingOptions = new ArrayList<>();
        if (!line.hasOption(root)) {
            missingOptions.add(root);
        }
        if (!line.hasOption(savename)) {
            missingOptions.add(savename);
        }
        if (!missingOptions.isEmpty()) {
            throw new MissingOptionException(missingOptions);
        }
        boolean inst = line.hasOption(instanceVariable);
        boolean func = line.hasOption(functions);
        boolean priv = line.hasOption(privateEntities);
        boolean consta = line.hasOption(constants);
        runLoose(line.getOptionValue(root), line.getOptionValue(savename), inst, func, priv, consta, line.getArgs());
    }

    private static void runLoose(String path, String name, boolean inst, boolean func, boolean priv, boolean consta,
            String... rem) {
        ConvertVisual.assignPath(ADDRESS_IMAGES);
        List<String> ignore = List.of(rem);
        ConvertVisual.generateUMLDiagram(path, ignore, name, inst, func, priv, consta);
    }

    private static void runReal() {
        PDGWindow disp = new PDGWindow();
    }

}
