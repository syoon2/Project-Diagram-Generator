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

package ui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import javax.swing.tree.TreeNode;

/**
 * Implementation of a package in a package tree,
 * represented as a {@link TreeNode}.
 * 
 * @author Sung Ho Yoon
 * @since 2.0
 */
public class PackageTreeNode implements TreeNode {

    /** Parent package */
    private PackageTreeNode parent;
    /** Package directory that this {@code PackageTreeNode} represents. */
    private File dir;
    /** List of subpackages. */
    private List<PackageTreeNode> subNodes;

    /**
     * Constructs a new {@code PackageTreeNode}.
     * 
     * @param dir the root directory of a project
     * 
     * @throws IllegalArgumentException if argument does not represent a valid
     *                                  directory
     * @throws NullPointerException     if argument is {@code null}
     */
    public PackageTreeNode(File dir) {
        this(null, dir);
    }

    /**
     * Constructs a new {@code PackageTreeNode} with the specified parent.
     * 
     * @param dir the directory of a package
     * 
     * @throws IllegalArgumentException if {@code dir} does not represent a valid
     *                                  directory
     * @throws NullPointerException     if {@code dir} is {@code null}
     */
    PackageTreeNode(PackageTreeNode parent, File dir) {
        this.parent = parent;
        if (!Objects.requireNonNull(dir).isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a valid directory");
        }
        this.dir = dir;
        File[] subdirectories = this.dir.listFiles(path -> path.isDirectory() && !path.isHidden());
        if (subdirectories.length > 0) {
            subNodes = new ArrayList<PackageTreeNode>();
            for (File subdirectory : subdirectories) {
                subNodes.add(new PackageTreeNode(this, subdirectory));
            }
        } else
            subNodes = Collections.emptyList();
    }

    /**
     * Returns the child {@code PackageTreeNode} at the specified index.
     * 
     * @param childIndex index of child
     * @return the child at the specified index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    @Override
    public PackageTreeNode getChildAt(int childIndex) throws IndexOutOfBoundsException {
        return subNodes.get(childIndex);
    }

    /**
     * Returns the number of children this {@code PackageTreeNode} has.
     * 
     * @return the number of children
     */
    @Override
    public int getChildCount() {
        return subNodes.size();
    }

    /**
     * Returns the parent {@code PackageTreeNode} of this node.
     * 
     * @return the parent {@code PackageTreeNode}, or {@code null} if this node is
     *         the root
     */
    @Override
    public PackageTreeNode getParent() {
        return parent;
    }

    /**
     * Finds the index of the specified {@code PackageTreeNode}.
     * 
     * @param node a {@code PackageTreeNode}
     * @return index of the specified node, or {@code -1} if the argument
     *         is not a child of this node
     */
    @Override
    public int getIndex(TreeNode node) {
        if (node == null)
            return -1;
        return subNodes.indexOf(node);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * Determines whether this node is a leaf.
     * 
     * @return {@code true} if this node is a leaf
     */
    @Override
    public boolean isLeaf() {
        return subNodes.isEmpty();
    }

    /**
     * Returns the children of this node as an {@link Enumeration}.
     * 
     * @return the children of this node as an {@code Enumeration}
     */
    @Override
    public Enumeration<PackageTreeNode> children() {
        return Collections.enumeration(subNodes);
    }

    /**
     * Returns the directory that this node represents.
     * 
     * @return the directory that this node represents
     */
    public File getDirectory() {
        return dir;
    }

    /**
     * Returns a hash code value for this {@code PackageTreeNode}.
     * 
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(parent, dir);
    }

    /**
     * Determines whether an object is "equal to" this {@code PackageTreeNode}.
     * 
     * @param obj an object
     * @return {@code true} if argument is "equal to" this {@code PackageTreeNode}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj instanceof PackageTreeNode) {
            PackageTreeNode other = (PackageTreeNode) obj;
            return Objects.equals(this.parent, other.parent) && Objects.equals(this.dir, other.dir);
        } else
            return false;
    }

    /**
     * Returns a string representation of this {@code PackageTreeNode}.
     * 
     * @return a string representation of this {@code PackageTreeNode}
     */
    @Override
    public String toString() {
        return dir.getName() + File.separator;
    }
}
