/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PosetImpl extends HashSet implements Poset {
    Map directLessersAndGreatersInfo = new HashMap();

    public boolean add(Object o) {
        if (contains(o)) {
            return false;
        }

        directLessersAndGreatersInfo.put(o,
            new DirectInfo(new HashSet(), new HashSet()));
        super.add(o);

        return true;
    }

    public boolean add(Set directLessers, Set directGreaters, Object o) {
        return add(new DirectInfo(directLessers, directGreaters), o);
    }

    public boolean add(DirectInfo info, Object o) {
        Set directLessers = info.directLessers;
        Set directGreaters = info.directGreaters;

        boolean added;
        added = add(o);

        Iterator it;

        for (it = directLessers.iterator(); it.hasNext();) {
            added |= connect(it.next(), o);
        }

        for (it = directGreaters.iterator(); it.hasNext();) {
            added |= connect(o, it.next());
        }

        return added;
    }

    public boolean addMin(Set directGreaters, Object o) {
        return add(new HashSet(), directGreaters, o);
    }

    public boolean addMax(Set directLessers, Object o) {
        return add(directLessers, new HashSet(), o);
    }

    public boolean addMin(Object directGreater, Object o) {
        Set directGreaters = new HashSet();
        directGreaters.add(directGreater);

        return addMin(directGreaters, o);
    }

    public boolean addMax(Object directLesser, Object o) {
        Set directLessers = new HashSet();
        directLessers.add(directLesser);

        return addMax(directLessers, o);
    }

    public boolean addNew(Object o) {
        return add(new HashSet(), new HashSet(), o);
    }

    public boolean connect(Object lesser, Object greater) {
        DirectInfo info;
        boolean success;

        if (greatersOf(lesser).contains(greater)) {
            return false;
        }

        info = (DirectInfo) directLessersAndGreatersInfo.get(lesser);
        success = ((Set) info.directGreaters).add(greater);
        info = (DirectInfo) directLessersAndGreatersInfo.get(greater);

        try {
            success |= ((Set) info.directLessers).add(lesser);
        } catch (NullPointerException e) {
            throw new RuntimeException();
        }

        if (!success) {
            throw new FlowError();
        }

        return success;
    }

    public Set directLessersOf(Object o) {
        return ((DirectInfo) directLessersAndGreatersInfo.get(o)).directLessers;
    }

    public Set directGreatersOf(Object o) {
		try{
        return ((DirectInfo) directLessersAndGreatersInfo.get(o)).directGreaters;
		} catch (NullPointerException e)
		{
			System.out.println(this);
			throw e;
		}
    }

    public Set minimals() {
        Set minimals = new HashSet();
        Object element;

        for (Iterator it = iterator(); it.hasNext();) {
            element = it.next();

            if (strictLessersOf(element).isEmpty()) {
                minimals.add(element);
            }
        }

        return minimals;
    }

    public Set maximals() {
        Set maximals = new HashSet();
        Object element;

        for (Iterator it = iterator(); it.hasNext();) {
            element = it.next();

            if (strictGreatersOf(element).isEmpty()) {
                maximals.add(element);
            }
        }

        return maximals;
    }

    public Set lessersOf(Object entry) {
        List list = new ArrayList();
        dfSearch(list, entry, false);

        Set set = new HashSet();
        set.addAll(list);

        return set;
    }

    public Set greatersOf(Object entry) {
        List list = new ArrayList();
        dfSearch(list, entry, true);

        Set set = new HashSet();
        set.addAll(list);

        return set;
    }

    public Set strictLessersOf(Object entry) {
        Set lSet = lessersOf(entry);
        lSet.remove(entry);

        return lSet;
    }

    public Set strictGreatersOf(Object entry) {
        Set lSet = greatersOf(entry);
        lSet.remove(entry);

        return lSet;
    }

    public Iterator dfoIterator() {
        Iterator it;
        Iterator it0;
        List order = new ArrayList();

        for (it = minimals().iterator(); it.hasNext();) {
            Object nextMin = it.next();
            dfSearch(order, nextMin, true);
        }

        return order.iterator();
    }

    public Iterator reverseDFOIterator() {
        Iterator it;
        Iterator it0;
        List order = new ArrayList();

        for (it = maximals().iterator(); it.hasNext();) {
            Object nextMax = it.next();
            dfSearch(order, nextMax, false);
        }

        return order.iterator();
    }

    private void dfSearch(List order, Object entry, boolean pAscending) {
        if (!order.contains(entry)) {
            order.add(entry);

            Set lDirectNeighbors = pAscending ? directGreatersOf(entry)
                                              : directLessersOf(entry);

            for (Iterator it = lDirectNeighbors.iterator(); it.hasNext();) {
                dfSearch(order, it.next(), pAscending);
            }
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        for (Iterator it = dfoIterator(); it.hasNext();) {
            buffer.append(info(it.next()));
        }

        return buffer.toString();
    }

    public String info(Object o) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer();
        buffer.append(o);
        buffer.append(":" + lineSeparator);
        buffer.append(" Direct Lessers:" + lineSeparator);

        for (Iterator it = ((DirectInfo) directLessersAndGreatersInfo.get(o)).directLessers.iterator();
                it.hasNext();) {
            buffer.append("  " + it.next() + lineSeparator);
        }

        buffer.append(" Direct Greaters:" + lineSeparator);

        for (Iterator it = ((DirectInfo) directLessersAndGreatersInfo.get(o)).directGreaters.iterator();
                it.hasNext();) {
            buffer.append("  " + it.next() + lineSeparator);
        }

        return buffer.toString();
    }

    public static class DirectInfo {
        Set directLessers;
        Set directGreaters;

        public DirectInfo(Set directLessers, Set directGreaters) {
            this.directLessers = directLessers;
            this.directGreaters = directGreaters;
        }
    }
}
