/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


/**
 * A tree structure class.
 */
public class TreeStructure {
    private Map fObjectToNode = new HashMap();

    /**
     * Constructs a new tree structure instance whose nodes are elements of the specified argument <code>Collection</code>. The nodes are not linked yet.
     */
    public TreeStructure(Collection pCollection) {
        Object lObj;

        for (Iterator lIt = pCollection.iterator(); lIt.hasNext();) {
            lObj = lIt.next();
            fObjectToNode.put(lObj, new Node(lObj));
        }
    }

    /**
     * Constructs a new tree structure instance.
     */
    public TreeStructure() {
    }

    /**
     * Returns the parent node of the specified argument node in this tree structure.
     */
    public Object parentOf(Object pObj) {
		Node lParent = ((Node)fObjectToNode.get(pObj)).fParent;
        return lParent == null ? null : lParent.fObj;
    }

    /**
     * Returns the child nodes of the specified argument node in this tree structure.
     */
    public List childrenOf(Object pObj) {
        List lNodes = ((Node) fObjectToNode.get(pObj)).fChildren;
        Node lNextNode;

        for (ListIterator lListIt = lNodes.listIterator(); lListIt.hasNext();) {
            lNextNode = (Node) lListIt.next();
            lListIt.set(lNextNode.fObj);
        }

        return lNodes;
    }

    /**
     * Returns the ancestor nodes of the specified argument node, including the argument node itself, in this tree structure.
     */
    public List ancestorsOf(Object pObj) {
        List lAncestor = new ArrayList();
        Object lParent = pObj;

        while (lParent != null) {
            lAncestor.add(lParent);
            lParent = parentOf(lParent);
        }

        return lAncestor;
    }

    /**
     * Makes a parent-child link between the given nodes.
     */
    public boolean link(Object pParent, Object pChild) {
        inclusionCheck(pParent);
        inclusionCheck(pChild);

        Node lParentNode = (Node) fObjectToNode.get(pParent);
        List lChildrenOfParentNode = lParentNode.fChildren;
        Node lChildNode = (Node) fObjectToNode.get(pChild);

        if (!lChildrenOfParentNode.contains(lChildNode)) {
            lChildrenOfParentNode.add(lChildNode);
            lChildNode.fParent = lParentNode;

            return true;
        }

        return false;
    }

    private boolean inclusionCheck(Object pObj) {
        if (fObjectToNode.containsKey(pObj)) {
            return false;
        }

        fObjectToNode.put(pObj, new Node(pObj));

        return true;
    }

    private static class Node {
        Object fObj;
        Node fParent;
        List fChildren = new ArrayList();

        Node(Object pObj) {
            fObj = pObj;
        }
    }
}
