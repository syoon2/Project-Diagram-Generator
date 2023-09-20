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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.swing.tree.TreePath;

import ui.util.PackageTreeNode;

/**
 * Input option for handling packages to be excluded from the diagram.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
class PackageExcludeOption extends InputOption {

    /** The default text to go in the text box */
    private final static String DEFAULT_PKG_TEXT = "[packages to ignore]";

    /** The directory option */
    private DirectoryOption directoryOption;
    /** The list of ignored packages */
    private List<String> ignoredPackages = new ArrayList<String>();
    
    /**
     * Constructs a new {@code PackageExcludeOption}.
     * 
     * @param parent          the frame that owns this {@code PackageExcludeOption}
     * @param directoryOption the directory option
     */
    public PackageExcludeOption(Frame parent, DirectoryOption directoryOption) {
        super(DEFAULT_PKG_TEXT);
        this.directoryOption = Objects.requireNonNull(directoryOption);

        addButtonActionListener(e -> {
            PackageExcludePopup pep = new PackageExcludePopup(parent, PackageExcludeOption.this);
        });

    }

    /**
     * Returns the selected root directory.
     * 
     * @return the selected root directory
     * 
     * @see DirectoryOption#getDirectory()
     */
    File getRootDirectory() {
        return directoryOption.getDirectory();
    }

    /**
     * Processes the packages to ignore.
     * 
     * @param ignored the set of packages represented as {@link TreePath}s
     * @return the packages to ignore, represented as a list of strings
     */
    List<String> processPackagesIgnore(Set<TreePath> ignored){
        ignoredPackages.clear();
        if (ignored.isEmpty()) {
            return getIgnoredPackages();
        }
        StringBuilder ignoredLabelBuilder = new StringBuilder();
        for (TreePath path : ignored) {
            Object[] nodes = path.getPath();
            if (nodes.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < nodes.length; i++) {
                    PackageTreeNode node = (PackageTreeNode) nodes[i];
                    sb.append(node.getDirectory().getName());
                    if (i < nodes.length - 1)
                        sb.append('.');
                }
                String packageName = sb.toString();
                ignoredPackages.add(packageName);
                ignoredLabelBuilder.append(packageName);
                ignoredLabelBuilder.append(';');
            }
        }
        ignoredLabelBuilder.deleteCharAt(ignoredLabelBuilder.length() - 1);
        getTextField().setText(ignoredLabelBuilder.toString());
        return getIgnoredPackages();
    }

    /**
     * Returns the packages to ignore. The list returned by this method is
     * {@link java.util.Collections#unmodifiableList(List) unmodifiable}.
     * 
     * @return the list of packages to ignore
     */
    public List<String> getIgnoredPackages() {
        return Collections.unmodifiableList(ignoredPackages);
    }
}
