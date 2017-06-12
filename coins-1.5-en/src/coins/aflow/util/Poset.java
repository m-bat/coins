/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

import java.util.Iterator;
import java.util.Set;


public interface Poset extends Set {
    /**
     * If <var>o</var> is already included, will do nothing.
     */
    public boolean add(Set directLessers, Set directGreaters, Object o);

    public boolean addMin(Set directGreaters, Object o);

    public boolean addMax(Set directLessers, Object o);

    public boolean addMin(Object directGreater, Object o);

    public boolean addMax(Object directLesser, Object o);

    /**
     * If <var>o</var> is already included, will overwrite with a fresh one (without any order relatation links).
     */
    public boolean addNew(Object o);

    public boolean connect(Object lesser, Object greater);

    public Set directLessersOf(Object o);

    public Set directGreatersOf(Object o);

    public Set minimals();

    public Set maximals();

    public Set lessersOf(Object o);

    public Set greatersOf(Object o);

    public Set strictLessersOf(Object o);

    public Set strictGreatersOf(Object o);

    public Iterator dfoIterator();

    public Iterator reverseDFOIterator();
}
