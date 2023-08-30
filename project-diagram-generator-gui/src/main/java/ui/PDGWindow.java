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

package ui;

import static main.PDGLauncher.gitProperties;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.swing.*;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

import analysis.process.Explore;
import image.ConvertVisual;
import image.DotProcess;

/**
 * The main GUI window for Project Diagram Generator.
 *
 * @author Sung Ho Yoon
 */
public class PDGWindow extends JFrame {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private static final String NAME = "Project Diagram Generator";
    private static final String TITLE = String.format(
            "%s (%s - %s%s)",
            NAME,
            Objects.requireNonNullElse(PDGWindow.class.getPackage().getImplementationVersion(), "dev"),
            gitProperties.getProperty("git.commit.id.abbrev"),
            Boolean.valueOf(gitProperties.getProperty("git.dirty")) ? "*" : "");

    private static final String[] BOOLEAN_SELECTION = new String[] {"Show Instance Variables?",
                                                                    "Show Functions?",
                                                                    "Show Private Entities?",
                                                                    "Show Constants?"
                                                                    };

    /** Class logger */
    private static Logger logger = LogManager.getLogger();

    /** Internally used {@link SVGDocumentFactory} object */
    private static SVGDocumentFactory svgFactory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());

    /** The dividing split pane. */
    private JSplitPane panel;

    /** The SVG canvas. */
    private JSVGCanvas svgCanvas;

    /** User configuration graphic element container. */
    private Container configContainer;
    /** User input graphic element container. */
    private Container inputContainer;
    /** Container for diagram generator options. */
    private Container optionContainer;

    private JCheckBox instanceVarCheck, functionCheck, privateEntityCheck, constantCheck;

    /**
     * Constructs a new {@code PDGWindow}.
     */
    public PDGWindow() {
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

        getContentPane().add(panel);

        svgCanvas = new JSVGCanvas();

        panel.setTopComponent(svgCanvas);

        setupConfigContainer();

        panel.setBottomComponent(configContainer);

        panel.setResizeWeight(1d);

        // Update title
        setTitle(TITLE);

        pack();

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupConfigContainer() {
        configContainer = new JPanel();
        configContainer.setLayout(new GridBagLayout());

        setupInputContainer();
        setupOptionContainer();

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5d;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 16, 0, 16);

        configContainer.add(inputContainer, c);

        c.weightx = 0.5d;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(16, 16, 16, 32);
        configContainer.add(optionContainer, c);
    }

    private void setupInputContainer() {
        inputContainer = Box.createVerticalBox();

        DirectoryOption directoryOption = new DirectoryOption(this);
        PackageExcludeOption packageExcludeOption = new PackageExcludeOption(this, directoryOption);
        SaveOption saveOption = new SaveOption(this);
        JButton generateButton = new JButton("Generate image");
        generateButton.addActionListener(event -> {
            new Thread(() -> {
                try {
                    generateButton.setEnabled(false);
                    generateButton.setText("Generating image");
                    Explore.setParameters(instanceVarCheck.isSelected(), functionCheck.isSelected(), privateEntityCheck.isSelected(), constantCheck.isSelected());
                    Explore e = new Explore(directoryOption.getDirectory());
                    e.run();
                    DotProcess.setProject(e);
                    String dot = DotProcess.generateDot();
                    ConvertVisual.draw(dot, saveOption.getFilename(), saveOption.getFormat());

                    svgCanvas.setSVGDocument(loadSVGFromFile(saveOption.getSavedFile()));
                } catch (Throwable exception) {
                    logger.catching(exception);
                }
                generateButton.setEnabled(true);
                generateButton.setText("Generate image");
            }).start();
        });
        inputContainer.add(directoryOption);
        inputContainer.add(packageExcludeOption);
        inputContainer.add(saveOption);
        inputContainer.add(generateButton);
    }

    private void setupOptionContainer() {
        optionContainer = Box.createVerticalBox();

        instanceVarCheck = new JCheckBox(BOOLEAN_SELECTION[0], false);
        functionCheck = new JCheckBox(BOOLEAN_SELECTION[1], false);
        privateEntityCheck = new JCheckBox(BOOLEAN_SELECTION[2], false);
        constantCheck = new JCheckBox(BOOLEAN_SELECTION[3], false);

        optionContainer.add(instanceVarCheck);
        optionContainer.add(functionCheck);
        optionContainer.add(privateEntityCheck);
        optionContainer.add(constantCheck);
    }

    /**
     * Load the generated SVG from filename.
     *
     * @param fileName The name of the image to be loaded
     * @return the SVG document
     * @throws IOException if I/O error occurs
     * @throws NullPointerException if argument is {@code null}
     **/
    public static SVGDocument loadSVGFromFile(String fileName) throws IOException {
        return loadSVGFromFile(new File(fileName));
    }

    /**
     * Load the generated SVG from file.
     *
     * @param file {@link File} that points to the image to be loaded
     * @return the SVG document
     * @throws IOException if I/O error occurs
     * @throws NullPointerException if argument is {@code null}
     **/
    public static SVGDocument loadSVGFromFile(File file) throws IOException {
        return svgFactory.createSVGDocument(Objects.requireNonNull(file).toURI().toString());
    }
}
