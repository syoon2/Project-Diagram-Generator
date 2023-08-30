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

/*
 * Adapted from https://stackoverflow.com/a/21851201
 * This StackOverflow post is distributed under CC BY-SA 4.0 International
 * License. Thus, as a derivative work, this file is distributed under the
 * GPLv3 License, a "BY-SA Compatible License" defined by Creative Commons.
 */

package ui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A {@code JTree} with checkboxes for each node.
 *
 * @author Sung Ho Yoon
 * 
 * @see <a href="https://stackoverflow.com/a/21851201">Original source code</a>
 */
public class JCheckBoxTree extends JTree {

    /**
     * Data structure that allows fast check-indicate the state of each node.
     * It totally replaces the "selection" mechanism of the {@link JTree}.
     */
    public static class CheckedNode {
        private boolean isSelected;
        private boolean hasChildren;
        private boolean allChildrenSelected;

        private CheckedNode(boolean isSelected, boolean hasChildren, boolean allChildrenSelected) {
            this.isSelected = isSelected;
            this.hasChildren = hasChildren;
            this.allChildrenSelected = allChildrenSelected;
        }

        /**
         * Returns {@code true} if the node is selected.
         * 
         * @return {@code true} if the node is selected
         */
        public boolean isSelected() {
            return isSelected;
        }

        /**
         * Returns {@code true} if the node has children.
         * 
         * @return {@code true} if the node has children
         */
        public boolean hasChildren() {
            return hasChildren;
        }

        /**
         * Returns {@code true} if all of the children of the node are selected.
         * 
         * @return {@code true} if all of the children of the node are selected
         */
        public boolean allChildrenSelected() {
            return allChildrenSelected;
        }
    }

    private Map<TreePath, CheckedNode> nodesCheckingState;
    private Set<TreePath> checkedPaths;

    /**
     * Listeners that monitor check changes.
     */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * A event type representing the change of the checked state
     * in a {@link JCheckBoxTree}.
     */
    public class CheckChangeEvent extends EventObject {

        private TreePath changedPath;
        private boolean newState;

        /**
         * Constructs a new {@code CheckChangeEvent}.
         * 
         * @param changedPath the changed path
         * @param newState    the new state for the specified path
         */
        private CheckChangeEvent(TreePath changedPath, boolean newState) {
            super(JCheckBoxTree.this);
            this.changedPath = changedPath;
            this.newState = newState;
        }

        /**
         * Returns the path that triggered this event state.
         *
         * @return a tree path
         */
        public TreePath getChangedPath() {
            return changedPath;
        }

        /**
         * Returns the new state for the tree path that triggered this event state.
         * 
         * @return the new state
         */
        public boolean getNewState() {
            return newState;
        }

        /**
         * Returns a String representation of this {@code CheckChangeEvent}.
         *
         * @return a String representation of this {@code CheckChangeEvent}
         */
        @Override
        public String toString() {
            return String.format(
                    "%s(Path: %s, new state = %b)",
                    super.toString(), changedPath.toString(), newState);
        }
    }

    /**
     * Type of listener that monitors check changes.
     */
    @FunctionalInterface
    public interface CheckChangeEventListener extends EventListener {
        /**
         * Invoked when a check change occurs in a {@link JCheckBoxTree}.
         * 
         * @param event the event to be processed
         */
        public void checkStateChanged(CheckChangeEvent event);
    }

    public JCheckBoxTree() {
        this(getDefaultTreeModel());
    }

    public JCheckBoxTree(TreeModel newModel) {
        super(newModel);
        // Disable toggling by double-click
        this.setToggleClickCount(0);
        // Override cell renderer with CheckBoxCellRenderer
        this.setCellRenderer(new CheckBoxCellRenderer());

        // Override selection model by an empty one
        this.setSelectionModel(JTree.EmptySelectionModel.sharedInstance());
        // Calling checking mechanism on mouse click
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                TreePath tp = JCheckBoxTree.this.getPathForLocation(arg0.getX(), arg0.getY());
                if (tp == null) {
                    return;
                }
                boolean checkMode = !nodesCheckingState.get(tp).isSelected;
                checkSubTree(tp, checkMode);
                updatePredecessorsWithCheckMode(tp, checkMode);
                // Firing the check change event
                fireCheckChangeEvent(new CheckChangeEvent(tp, checkMode));
                // Repainting tree after the data structures were updated
                JCheckBoxTree.this.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });
    }

    public JCheckBoxTree(TreeNode root) {
        this(new DefaultTreeModel(root));
    }

    public JCheckBoxTree(TreeNode root, boolean asksAllowsChildren) {
        this(new DefaultTreeModel(root, asksAllowsChildren));
    }

    /**
     * Adds a new check change listener.
     *
     * @param listener a check change listener
     */
    public void addCheckChangeEventListener(CheckChangeEventListener listener) {
        listenerList.add(CheckChangeEventListener.class, listener);
    }

    /**
     * Removes the specified check change listener.
     *
     * @param listener a check change listener
     */
    public void removeCheckChangeEventListener(CheckChangeEventListener listener) {
        listenerList.remove(CheckChangeEventListener.class, listener);
    }

    /**
     * Sends the specified check change event to all listeners.
     *
     * @param evt a check change event
     */
    void fireCheckChangeEvent(CheckChangeEvent evt) {
        CheckChangeEventListener[] listeners = listenerList.getListeners(CheckChangeEventListener.class);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].checkStateChanged(evt);
        }
    }

    @Override
    public void setModel(TreeModel newModel) {
        super.setModel(newModel);
        resetCheckingState();
    }

    /**
     * Returns only the checked paths. This method totally ignores original
     * {@link JTree#getSelectionPaths() "selection" mechanism}.
     *
     * @return the checked paths
     */
    public TreePath[] getCheckedPaths() {
        return checkedPaths.toArray(new TreePath[checkedPaths.size()]);
    }

    /**
     * Checks whether a (sub)tree is fully selected.
     *
     * @param path a (sub)tree represented as a tree path
     * @return {@code true} if the node <i>and</i> all of its children are selected
     */
    public boolean isSelected(TreePath path) {
        CheckedNode cn = nodesCheckingState.get(path);
        return cn.isSelected;
    }

    /**
     * Checks whether a (sub)tree is partially selected.
     *
     * @param path a (sub)tree represented as a tree path
     * @return {@code true} if the node is selected, has children but not all of
     *         them are selected
     */
    public boolean isSelectedPartially(TreePath path) {
        CheckedNode cn = nodesCheckingState.get(path);
        return cn.isSelected && cn.hasChildren && !cn.allChildrenSelected;
    }

    private void resetCheckingState() {
        nodesCheckingState = new LinkedHashMap<TreePath, CheckedNode>();
        checkedPaths = new LinkedHashSet<TreePath>();
        TreeNode node = (TreeNode) getModel().getRoot();
        if (node == null) {
            return;
        }
        addSubtreeToCheckingStateTracking(node);
    }

    /**
     * Create data structure of the current model for the checking mechanism.
     *
     * @param node
     */
    private void addSubtreeToCheckingStateTracking(TreeNode node) {
        TreePath tp = buildTreePath(node);
        CheckedNode cn = new CheckedNode(true, node.getChildCount() > 0, true);
        nodesCheckingState.put(tp, cn);
        for (int i = 0; i < node.getChildCount(); i++) {
            addSubtreeToCheckingStateTracking(
                    (TreeNode) tp.pathByAddingChild(node.getChildAt(i)).getLastPathComponent());
        }
    }

    /**
     * Cell renderer for {@code JCheckBoxTree}.
     */
    private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {
        private JCheckBox checkBox;

        public CheckBoxCellRenderer() {
            super();
            this.setLayout(new BorderLayout());
            checkBox = new JCheckBox();
            add(checkBox, BorderLayout.CENTER);
            setOpaque(false);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            TreeNode node = (TreeNode) value;
            TreePath tp = buildTreePath(node);
            CheckedNode cn = nodesCheckingState.get(tp);
            if (cn == null) {
                return this;
            }
            checkBox.setSelected(cn.isSelected);
            checkBox.setText(node.toString());
            checkBox.setOpaque(cn.isSelected && cn.hasChildren && !cn.allChildrenSelected);
            return this;
        }
    }

    /**
     * When a node is checked/unchecked, update the states of the predecessors.
     *
     * @param tp    the predecessors represented as a tree path
     * @param check whether to check the specified subtree
     */
    protected void updatePredecessorsWithCheckMode(TreePath tp, boolean check) {
        TreePath parentPath = tp.getParentPath();
        // If it is the root, stop the recursive calls and return
        if (parentPath == null)
            return;
        CheckedNode parentCheckedNode = nodesCheckingState.get(parentPath);
        TreeNode parentNode = (TreeNode) parentPath.getLastPathComponent();
        parentCheckedNode.allChildrenSelected = true;
        // parentCheckedNode.isSelected = false;
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            TreePath childPath = parentPath.pathByAddingChild(parentNode.getChildAt(i));
            CheckedNode childCheckedNode = nodesCheckingState.get(childPath);
            // It is enough that even one subtree is not fully selected
            // to determine that the parent is not fully selected
            if (!childCheckedNode.allChildrenSelected) {
                parentCheckedNode.allChildrenSelected = false;
            }
            // If at least one child is selected, selecting also the parent
            if (childCheckedNode.isSelected) {
                parentCheckedNode.isSelected = true;
            }
        }
        if (parentCheckedNode.isSelected) {
            checkedPaths.add(parentPath);
        } else {
            checkedPaths.remove(parentPath);
        }
        // Go to upper predecessor
        updatePredecessorsWithCheckMode(parentPath, check);
    }

    /**
     * Recursively checks/unchecks a subtree.
     *
     * @param tp    a subtree represented as a tree path
     * @param check whether to check the specified subtree
     */
    protected void checkSubTree(TreePath tp, boolean check) {
        CheckedNode cn = nodesCheckingState.get(tp);
        cn.isSelected = check;
        TreeNode node = (TreeNode) tp.getLastPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            checkSubTree(tp.pathByAddingChild(node.getChildAt(i)), check);
        }
        cn.allChildrenSelected = check;
        if (check) {
            checkedPaths.add(tp);
        } else {
            checkedPaths.remove(tp);
        }
    }

    /**
     * Builds the tree path for a generic tree node.
     * @param tn a tree node
     * @return the path to the specified node
     */
    static TreePath buildTreePath(TreeNode tn) {
        if (tn instanceof DefaultMutableTreeNode) {
            return new TreePath(((DefaultMutableTreeNode) tn).getPath());
        }
        Deque<TreeNode> pathBuilder = new ArrayDeque<>();
        TreeNode currNode = tn;
        while (currNode != null) {
            pathBuilder.addFirst(currNode);
            currNode = currNode.getParent();
        }
        return new TreePath(pathBuilder.toArray(new TreeNode[0]));
    }

}