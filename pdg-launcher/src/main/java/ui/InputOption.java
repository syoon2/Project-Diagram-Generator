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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.swing.*;

import org.apache.commons.io.IOUtils;

/**
 * Generic graphic component for handling user input options.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
abstract class InputOption extends JComponent {

    /** The "plus icon" used by {@code InputOption}. */
    private static final Icon BUTTON_PLUS_ICON;
    static {
        try {
            BUTTON_PLUS_ICON = new ImageIcon(
                    IOUtils.resourceToURL("plus_icon.png", DirectoryOption.class.getClassLoader()));
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /** A text box for displaying user's input */
    private JTextField textField;
    /** A button */
    private JButton button;

    /**
     * Constructs a new {@code InputOption}.
     */
    InputOption() {
        this(null);
    }

    /**
     * Constructs a new {@code InputOption} with the specified text in the text box.
     * 
     * @param txt text to put in the text box
     */
    InputOption(String txt) {
        textField = new JTextField(txt);
        textField.setMinimumSize(new Dimension(300, 32));
        textField.setPreferredSize(new Dimension(480, 32));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setEditable(false);
        button = new JButton(BUTTON_PLUS_ICON);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1d;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 16);
        add(getTextField(), c);

        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 0d;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        add(getButton(), c);
    }

    /**
     * Returns the button in this component.
     * 
     * @return the button
     */
    protected final JButton getButton() {
        return button;
    }

    /**
     * Returns the text box in this component.
     * 
     * @return the text box
     */
    protected final JTextField getTextField() {
        return textField;
    }

    /**
     * Adds a listener to the underlying button.
     * 
     * @param l a listener
     * 
     * @see JButton#addActionListener(ActionListener)
     */
    protected final void addButtonActionListener(ActionListener l) {
        button.addActionListener(l);
    }
}
