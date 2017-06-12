/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * ListValuedMap.java
 *
 * Created on February 13, 2003, 6:01 PM
 */
package coins.aflow.util;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author  hasegawa
 */
public class ListValuedMap extends java.util.HashMap {
    /** Creates a new instance of ListValuedMap */
    public ListValuedMap() {
    }

    public Object get(Object pObj) {
		
        if (!containsKey(pObj)){
            List l = new ArrayList();
            put(pObj, l);
        }

        return super.get(pObj);
    }

    public boolean addUnique(Object pKey, Object pNewEntry) {
        List l = (List) get(pKey);

        if (l.contains(pNewEntry)) {
            return false;
        }

        return l.add(pNewEntry);
    }
}
