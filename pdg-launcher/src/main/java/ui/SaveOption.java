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

/**
 * Input option for handling the saved destination of the diagram.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
class SaveOption extends InputOption {

    /** Default placeholder for the text box */
    private static final String DEFAULT_NAME = "[image name]";

    /** The directory to save file */
    private File directory;
    /** The file to be saved */
    private File saveFile;
    /** The filename to be used */
    private String filename;
    /** The generated file format */
    private Format format;

    /**
     * Constructs a new {@code SaveOption}.
     * 
     * @param parent the frame that owns this {@code SaveOption}
     */
    public SaveOption(Frame parent) {
        this(parent, new File(SystemUtils.USER_DIR));
    }

    /**
     * Constructs a new {@code SaveOption}.
     * 
     * @param parent    the frame that owns this {@code SaveOption}
     * @param directory the initial directory
     */
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
                // TODO: Reenable PNG export
                final List<Format> supportedFormats = List.of(/* Format.PNG, */ Format.SVG);
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

    /**
     * Returns the filename to be used.
     * 
     * @return the filename to be used
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the file format of the saved file.
     * 
     * @return the file format of the saved file
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Returns the saved file.
     * 
     * @return the saved file
     */
    public File getSavedFile() {
        return saveFile;
    }
}
