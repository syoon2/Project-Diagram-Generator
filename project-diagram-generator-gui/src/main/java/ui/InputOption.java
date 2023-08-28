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

abstract class InputOption extends JComponent {
    
    private static final Icon BUTTON_PLUS_ICON;
    static {
        try {
            BUTTON_PLUS_ICON = new ImageIcon(
                IOUtils.resourceToURL("plus_icon.png", DirectoryOption.class.getClassLoader())
            );
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private JTextField textField;
    private JButton button;

    InputOption() {
        this(null);
    }

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
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 16);
        add(getTextField(), c);

        c.gridx = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        add(getButton(), c);
    }

    protected final JButton getButton() {
        return button;
    }

    protected final JTextField getTextField() {
        return textField;
    }

    protected final void addButtonActionListener(ActionListener l) {
        button.addActionListener(l);
    }
}
