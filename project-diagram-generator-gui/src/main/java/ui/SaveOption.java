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
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import guru.nidi.graphviz.engine.Format;
import image.ConvertVisual;

class SaveOption extends InputOption {

    private static final String DEFAULT_NAME = "[image name]";

    private File directory;
    private File saveFile;
    private String filename;
    private Format format;

    public SaveOption(Frame parent) {
        this(parent, new File(SystemUtils.USER_DIR));
    }

    public SaveOption(Frame parent, File directory) {
        super(DEFAULT_NAME);
        this.directory = directory;

        addButtonActionListener(e -> {
            synchronized (SaveOption.this) {
                JFileChooser fileChooser = new JFileChooser() {
                    @Override
                    public void approveSelection() {
                        // Overwrite protection
                        if (getSelectedFile().exists() && getDialogType() == SAVE_DIALOG) {
                            int result = JOptionPane.showConfirmDialog(this, "Selected file exists, overwrite?",
                                    "Existing file", JOptionPane.YES_NO_OPTION);
                            switch (result) {
                                case JOptionPane.YES_OPTION:
                                    break;
                                case JOptionPane.CANCEL_OPTION:
                                    cancelSelection();
                                case JOptionPane.NO_OPTION:
                                case JOptionPane.CLOSED_OPTION:
                                    return;
                            }
                        }
                        super.approveSelection();
                    }
                };
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setDialogTitle("Save as...");
                final List<Format> supportedFormats = List.of(/*Format.PNG,*/ Format.SVG);
                for (Format f : supportedFormats) {
                    fileChooser.addChoosableFileFilter(
                            new FileNameExtensionFilter(f.name() + " file", f.fileExtension));
                }
                if (directory != null)
                    fileChooser.setCurrentDirectory(directory);

                /* Prompt user to select a filename */

                int result = fileChooser.showSaveDialog(parent);

                /* No file was selected */

                if (result != JFileChooser.APPROVE_OPTION || fileChooser.getSelectedFile() == null)
                    return;

                FileNameExtensionFilter usedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();

                if (!FilenameUtils.isExtension(fileChooser.getSelectedFile().getName(), usedFilter.getExtensions())) {
                    fileChooser.setSelectedFile(new File(
                            fileChooser.getSelectedFile().getAbsolutePath()
                                    + FilenameUtils.EXTENSION_SEPARATOR
                                    + usedFilter.getExtensions()[0]));
                }
                this.directory = fileChooser.getCurrentDirectory();
                ConvertVisual.assignPath(this.directory.getAbsolutePath());
                this.filename = FilenameUtils.removeExtension(fileChooser.getSelectedFile().getName());
                this.format = Format.valueOf(usedFilter.getExtensions()[0].toUpperCase());
                getTextField().setText(fileChooser.getSelectedFile().getName());
                this.saveFile = fileChooser.getSelectedFile();
            }
        });
    }

    public String getFilename() {
        return filename;
    }

    public Format getFormat() {
        return format;
    }

    public File getSavedFile() {
        return saveFile;
    }
}
