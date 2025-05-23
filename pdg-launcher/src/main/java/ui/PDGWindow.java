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

import static main.PDGLauncher.NAME;
import static main.PDGLauncher.gitProperties;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.swing.*;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.AbstractPanInteractor;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

import analysis.process.Explore;
import image.ConvertVisual;
import image.DotProcess;
import ui.util.graphviz.GraphvizEngineInitializer;

/**
 * The main GUI window for Project Diagram Generator.
 *
 * @author Sung Ho Yoon
 * @since 2.0
 */
public class PDGWindow extends JFrame {

    /** Default width of a PDG window */
    private static final int DEFAULT_WIDTH = 800;
    /** Default height of a PDG window */
    private static final int DEFAULT_HEIGHT = 600;

    /** Title for the window */
    private static final String TITLE = String.format(
            "%s (%s - %s%s)",
            NAME,
            Objects.requireNonNullElse(PDGWindow.class.getPackage().getImplementationVersion(), "dev"),
            gitProperties.getProperty("git.commit.id.abbrev"),
            Boolean.valueOf(gitProperties.getProperty("git.dirty")) ? "*" : StringUtils.EMPTY);

    /** Toggleable options for the generator */
    private static final String[] BOOLEAN_SELECTION = new String[] { "Show Instance Variables?",
            "Show Functions?",
            "Show Private Entities?",
            "Show Constants?"
    };

    /** Class logger */
    private static Logger logger = LogManager.getLogger();

    /** Internally used {@link SVGDocumentFactory} object */
    private static SVGDocumentFactory svgFactory = new SAXSVGDocumentFactory(
            XMLResourceDescriptor.getXMLParserClassName());

    /**
     * The singleton instance.
     * 
     * @since 2.1.0
     */
    private static PDGWindow INSTANCE;

    /**
     * Returns the singleton instance.
     * 
     * @return the singleton instance
     * 
     * @since 2.1.0
     */
    public static PDGWindow instance() {
        if (INSTANCE == null)
            INSTANCE = new PDGWindow();
        return INSTANCE;
    }

    /** The dividing split pane. */
    private JSplitPane panel;

    /** The SVG canvas. */
    private JSVGCanvas svgCanvas;

    /** User configuration graphic element container. */
    private Container configContainer;

    /** Toggleable option checkbox */
    private JCheckBox instanceVarCheck, functionCheck, privateEntityCheck, constantCheck;

    /** The menu bar. */
    private JMenuBar menuBar;

    /**
     * Constructs a new {@code PDGWindow}.
     */
    @SuppressWarnings("unchecked")
    private PDGWindow() {
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        setupMenuBar();

        panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

        getContentPane().add(panel);

        svgCanvas = new JSVGCanvas(null, false, false);

        // Use mouse for translation
        svgCanvas.getInteractors().add(new AbstractPanInteractor() {
            @Override
            public boolean startInteraction(InputEvent ie) {
                int mods = ie.getModifiersEx();
                return ie.getID() == MouseEvent.MOUSE_PRESSED &&
                        (mods & InputEvent.BUTTON1_DOWN_MASK) != 0;
            }
        });

        // Use mouse for zooming in and scrolling
        svgCanvas.addMouseWheelListener(e -> {
            Action action = null;
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0)
                    action = svgCanvas.getActionMap().get(JSVGCanvas.ZOOM_IN_ACTION);
                else if (e.getWheelRotation() > 0)
                    action = svgCanvas.getActionMap().get(JSVGCanvas.ZOOM_OUT_ACTION);
            } else {
                if (e.getWheelRotation() < 0) {
                    action = svgCanvas.getActionMap().get(JSVGCanvas.FAST_SCROLL_UP_ACTION);
                } else if (e.getWheelRotation() > 0) {
                    action = svgCanvas.getActionMap().get(JSVGCanvas.FAST_SCROLL_DOWN_ACTION);
                }
            }
            if (action != null)
                action.actionPerformed(null);
        });

        panel.setTopComponent(svgCanvas);

        setupConfigContainer();

        panel.setBottomComponent(configContainer);

        panel.setResizeWeight(1d);
        panel.setDividerSize(0);

        // Update title
        setTitle(TITLE);
        if (SystemUtils.IS_OS_MAC) {
            // Use transparent title bar
            getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true );
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets up the menu bar.
     */
    private void setupMenuBar() {

        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        if (!SystemUtils.IS_OS_MAC) {
            JMenuItem exitMenuItem = new JMenuItem("Exit");
            exitMenuItem
                    .addActionListener(e -> dispatchEvent(new WindowEvent(PDGWindow.this, WindowEvent.WINDOW_CLOSING)));
            fileMenu.add(exitMenuItem);
        }

        JMenu aboutMenu = new JMenu("About");

        menuBar.add(fileMenu);

        JMenuItem repoMenuItem = new JMenuItem("Open GitHub Repository");
        repoMenuItem.addActionListener(event -> {
            try {
                Desktop.getDesktop().browse(new URI(gitProperties.getProperty("git.remote.origin.url")));
            } catch (IOException | URISyntaxException e) {
                logger.catching(e);
                JOptionPane.showMessageDialog(PDGWindow.this, e.getMessage(), e.getClass().getName(),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        aboutMenu.add(repoMenuItem);
        JMenuItem licenseMenuItem = new JMenuItem("View License");
        licenseMenuItem.addActionListener(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.gnu.org/licenses/gpl-3.0.html#license-text"));
            } catch (IOException | URISyntaxException e) {
                logger.catching(e);
                JOptionPane.showMessageDialog(PDGWindow.this, e.getMessage(), e.getClass().getName(),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        aboutMenu.add(licenseMenuItem);

        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);

    }

    /**
     * Sets up the user input elements.
     */
    private void setupConfigContainer() {
        configContainer = new JPanel();
        configContainer.setLayout(new GridBagLayout());

        setupInputContainer();
        setupOptionContainer();
    }

    /**
     * Sets up the project input elements.
     */
    private void setupInputContainer() {

        DirectoryOption directoryOption = new DirectoryOption(this);
        PackageExcludeOption packageExcludeOption = new PackageExcludeOption(this, directoryOption);
        SaveOption saveOption = new SaveOption(this);
        JButton generateButton = new JButton("Generate image");
        generateButton.setEnabled(GraphvizEngineInitializer.GRAPHVIZ_AVAILABLE);
        generateButton.addActionListener(event -> {
            new Thread(() -> {
                try {
                    generateButton.setEnabled(false);
                    generateButton.setText("Generating image");
                    Explore.setParameters(instanceVarCheck.isSelected(), functionCheck.isSelected(),
                            privateEntityCheck.isSelected(), constantCheck.isSelected());
                    Explore e = new Explore(directoryOption.getDirectory());
                    e.run();
                    DotProcess.setProject(e);
                    String dot = DotProcess.generateDot();
                    ConvertVisual.draw(dot, saveOption.getFilename(), saveOption.getFormat());

                    svgCanvas.setSVGDocument(loadSVGFromFile(saveOption.getSavedFile()));
                } catch (Throwable exception) {
                    logger.catching(exception);
                }
                generateButton.setEnabled(GraphvizEngineInitializer.GRAPHVIZ_AVAILABLE);
                generateButton.setText("Generate image");
            }, "Diagram generator").start();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5d;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(16, 16, 8, 8);

        configContainer.add(directoryOption, c);

        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(8, 16, 8, 8);
        configContainer.add(packageExcludeOption, c);
        c.insets = new Insets(8, 16, 16, 8);
        configContainer.add(saveOption, c);

        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 0d;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(8, 8, 16, 16);
        configContainer.add(generateButton, c);
    }

    /**
     * Sets up the checkboxes for diagram options.
     */
    private void setupOptionContainer() {
        Container optionContainer = Box.createVerticalBox();

        instanceVarCheck = new JCheckBox(BOOLEAN_SELECTION[0], false);
        functionCheck = new JCheckBox(BOOLEAN_SELECTION[1], false);
        privateEntityCheck = new JCheckBox(BOOLEAN_SELECTION[2], false);
        constantCheck = new JCheckBox(BOOLEAN_SELECTION[3], false);

        optionContainer.add(instanceVarCheck);
        optionContainer.add(functionCheck);
        optionContainer.add(privateEntityCheck);
        optionContainer.add(constantCheck);

        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 0d;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(16, 8, 8, 16);
        configContainer.add(optionContainer, c);
    }

    /**
     * Load the generated SVG from filename.
     *
     * @param fileName The name of the image to be loaded
     * @return the SVG document
     * @throws IOException          if an I/O error occurs
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
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if argument is {@code null}
     **/
    public static SVGDocument loadSVGFromFile(File file) throws IOException {
        return svgFactory.createSVGDocument(Objects.requireNonNull(file).toURI().toString());
    }
}
