/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */

package coins.ast;

/**
 * A helper class for traversing a tree.
 */
public class TreeWalker {
    static class NodeList {
  NodeList parent;
  ASTree node;

  NodeList(NodeList p, ASTree n) {
      parent = p;
      node = n;
  }
    }

    private NodeList nodeList;

    public TreeWalker(ASTree root) {
  nodeList = new NodeList(null, root);
    }

    private TreeWalker() {}

    /**
     * Makes a copy of the TreeWalker object.  The current position
     * is also copied.
     */
    public TreeWalker copy() {
  TreeWalker w = new TreeWalker();
  w.nodeList = nodeList;
  return w;
    }

    /**
     * Moves to the leftmost leaf node.  This method must be called
     * before next() is called.
     */
    public ASTree begin() {
  while (left() != null)
      ;

  return current();
    }

    /**
     * Move to the next node in the post-order traversal.
     * This method skips a Pair node.
     */
    public ASTree next() {
  ASTree node;
  do {
      node = next0();
  } while (node instanceof Pair);
  return node;
    }

    private ASTree next0() {
  ASTree c = nodeList.node;
  ASTree p = parent();
  if (p == null)
      return null;
  else
      if (nodeList.node.getRight() == c)
    return p;

  if (right() == null)
      return current();
  else
      return begin();
    }

    /**
     * Returns the object at the current position.  This method
     * returns null if parent() is called when the current position
     * is a root node.
     */
    public ASTree current() {
  if (nodeList == null)
      return null;
  else
      return nodeList.node;
    }

    /**
     * Move to the left child.
     * This method returns null if the current position is a leaf node.
     */
    public ASTree left() {
  if (nodeList == null)
      return null;
  else {
      ASTree child = nodeList.node.getLeft();
      if (child == null)
    return null;
      else {
    nodeList = new NodeList(nodeList, child);
    return child;
      }
  }
    }

    /**
     * Move to the right child.
     * This method returns null if the current position is a leaf node.
     */
    public ASTree right() {
  if (nodeList == null)
      return null;
  else {
      ASTree child = nodeList.node.getRight();
      if (child == null)
    return null;
      else {
    nodeList = new NodeList(nodeList, child);
    return child;
      }
  }
    }

    /**
     * Move to the parent.
     * This method returns null if the current position is a root node.
     */
    public ASTree parent() {
  if (nodeList == null)
      return null;
  else {
      NodeList p = nodeList.parent;
      nodeList = p;
      if (p == null)
    return null;
      else
    return nodeList.node;
  }
    }
}
