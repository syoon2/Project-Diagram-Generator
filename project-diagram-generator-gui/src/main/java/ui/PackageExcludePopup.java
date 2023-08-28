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

class PackageExcludePopup extends JDialog {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 600;

    private JCheckBoxTree tree;
    private JScrollPane scrollPane;
    private PackageExcludeOption peo;
    private JButton submitButton;
    private Set<TreePath> excludedPackages;

    PackageExcludePopup(Frame parent, PackageExcludeOption peo) {
        super(parent);
        this.peo = Objects.requireNonNull(peo);
        File rootDir = this.peo.getRootDirectory();
        Objects.requireNonNull(rootDir);
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("Argument does not represent a directory");
        }
        Box box = Box.createVerticalBox();
        tree = new JCheckBoxTree(new PackageTreeNode(rootDir));
        scrollPane = new JScrollPane(tree);
        scrollPane.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        box.add(scrollPane);
        setupSubmitButton();
        box.add(submitButton);
        add(box);
        pack();
        setModal(true);
        setVisible(true);
    }

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
    }
}
