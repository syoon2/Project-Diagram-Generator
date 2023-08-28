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

import java.awt.Frame;
import java.io.*;
import java.util.Objects;

import javax.swing.*;

import org.apache.commons.lang3.SystemUtils;

class DirectoryOption extends InputOption {

    private static final String DEFAULT_SRC_TEXT = "[path to project src]";

    private File directory;

    public DirectoryOption(Frame parent) {
        this(parent, new File(SystemUtils.USER_DIR));
    }

    public DirectoryOption(Frame parent, File directory) {
        super(DEFAULT_SRC_TEXT);
        this.setDirectory(directory);

        addButtonActionListener(e -> {
            synchronized (DirectoryOption.this) {
                JFileChooser fileChooser = new JFileChooser(this.directory);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle("Open project source...");
                int result = fileChooser.showOpenDialog(parent);
                if (result != JFileChooser.APPROVE_OPTION || fileChooser.getSelectedFile() == null)
                    return;
                setDirectory(fileChooser.getSelectedFile());
            }
        });

    }

    public File setDirectory(File directory) {
        if (!Objects.requireNonNull(directory).isDirectory()) {
            throw new IllegalArgumentException("Argument does not represent a directory");
        }
        this.directory = directory;
        getTextField().setText(this.directory.getAbsolutePath());
        return this.directory;
    }

    public File getDirectory() {
        return directory;
    }

}
