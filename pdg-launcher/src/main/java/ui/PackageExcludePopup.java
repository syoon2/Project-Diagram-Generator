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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ui.util.JCheckBoxTree;
import ui.util.PackageTreeNode;

/**
 * Popup window for users to input ignored packages.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
class PackageExcludePopup extends JDialog {

    /** The default width of this popup. */
    private static final int DEFAULT_WIDTH = 300;
    /** The default height of this popup. */
    private static final int DEFAULT_HEIGHT = 600;

    /**
     * The checkbox tree for selecting packages to exclude.
     */
    private JCheckBoxTree tree;
    /** The pane used for scrolling support. */
    private JScrollPane scrollPane;
    /** The {@link PackageExcludeOption} that owns this popup. */
    private PackageExcludeOption peo;
    /** The "submit" button. */
    private JButton submitButton;
    /** The set of excluded packages. */
    private Set<TreePath> excludedPackages;

    /**
     * Constructs a new {@code PackageExcludePopup}.
     * 
     * @param parent the frame that owns this popup
     * @param peo    the {@link PackageExcludeOption} that owns this popup
     * 
     * @throws NullPointerException     if {@code parent} is {@code null}
     * @throws IllegalArgumentException if no root directory is set in {@code peo}
     */
    PackageExcludePopup(Frame parent, PackageExcludeOption peo) {
        super(parent);
        setResizable(false);
        this.peo = Objects.requireNonNull(peo);
        File rootDir = this.peo.getRootDirectory();
        Objects.requireNonNull(rootDir);
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("Argument does not represent a directory");
        }
        setLayout(new BorderLayout());
        tree = new JCheckBoxTree(new PackageTreeNode(rootDir));
        scrollPane = new JScrollPane(tree);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        add(scrollPane, BorderLayout.CENTER);
        setupSubmitButton();
        add(submitButton, BorderLayout.SOUTH);
        pack();
        setModal(true);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Sets up the "submit" button in this popup.
     */
    private void setupSubmitButton() {
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            excludedPackages = new HashSet<>();
            Queue<TreePath> paths = new ArrayDeque<>();
            paths.add(new TreePath(root));
            while (!paths.isEmpty()) {
                TreePath currPath = paths.poll();
                if (tree.isSelected(currPath)) {
                    TreeNode currNode = (TreeNode) currPath.getLastPathComponent();
                    for (int i = 0; i < currNode.getChildCount(); i++) {
                        paths.add(currPath.pathByAddingChild(currNode.getChildAt(i)));
                    }
                } else {
                    excludedPackages.add(currPath);
                }
            }
            peo.processPackagesIgnore(excludedPackages);
            PackageExcludePopup.this.dispose();
        });
        submitButton.setAlignmentX(1f);
    }
}
