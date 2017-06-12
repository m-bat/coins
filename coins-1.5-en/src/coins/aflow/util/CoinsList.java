/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * List whose iterator implements <code>CoinsIterator</code> (returned by <code>coinsIterator()</code>). This list maintains the list of CoinsIterator this list has, and every such iterator will be notified when there is a structural modification to this list.
 *
 * @see java.util.LinkedList
 */

// -- The following code is mostly the copy of java.util.LinkedList and its superclasses. That is, this implementation is linked list-based.
public class CoinsList implements Cloneable, List {
    protected transient Entry header = new Entry(null, null, null);
    protected transient int size = 0;
    protected List fIterators = new LinkedList();

    /**
     * Not used in this class.
     */
    protected int modCount; // Not used.

    /**
     * Constructs an empty list.
     */
    public CoinsList() {
        header.next = header.previous = header;
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list.
     */
    public CoinsList(Collection c) {
        this();
        addAll(c);
    }

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list.
     */
    public Object getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return header.next.element;
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list.
     * @throws    NoSuchElementException if this list is empty.
     */
    public Object getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return header.previous.element;
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list.
     * @throws    NoSuchElementException if this list is empty.
     */
    public Object removeFirst() {
        Object first = header.next.element;
        remove(header.next, 0);

        return first;
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list.
     * @throws    NoSuchElementException if this list is empty.
     */
    public Object removeLast() {
        Object last = header.previous.element;
        remove(header.previous, size - 1);

        return last;
    }

    /**
     * Inserts the given element at the beginning of this list.
     *
     * @param o the element to be inserted at the beginning of this list.
     */
    public void addFirst(Object o) {
        addBefore(o, header.next, 0);
    }

    /**
     * Appends the given element to the end of this list.  (Identical in
     * function to the <tt>add</tt> method; included only for consistency.)
     *
     * @param o the element to be inserted at the end of this list.
     */
    public void addLast(Object o) {
        addBefore(o, header, size);
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that <tt>(o==null ? e==null
     * : o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested.
     * @return <tt>true</tt> if this list contains the specified element.
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list.
     */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param o element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of
     * <tt>Collection.add</tt>).
     */
    public boolean add(Object o) {
        addBefore(o, header, size);

        return true;
    }

    /**
     * Removes the first occurrence of the specified element in this list.  If
     * the list does not contain the element, it is unchanged.  More formally,
     * removes the element with the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if such an
     * element exists).
     *
     * @param o element to be removed from this list, if present.
     * @return <tt>true</tt> if the list contained the specified element.
     */
    public boolean remove(Object o) {
        int i = 0;

        if (o == null) {
            for (Entry e = header.next; e != header; e = e.next, i++) {
                if (e.element == null) {
                    remove(e, i);

                    return true;
                }
            }
        } else {
            i = 0;

            for (Entry e = header.next; e != header; e = e.next, i++) {
                if (o.equals(e.element)) {
                    remove(e, i);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean containsAll(Collection c) {
        for (Iterator lIt = c.iterator(); lIt.hasNext();)
            if (!contains(lIt.next())) {
                return false;
            }

        return true;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator.  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in
     * progress.  (This implies that the behavior of this call is undefined if
     * the specified Collection is this list, and this list is nonempty.)
     *
     * @param c the elements to be inserted into this list.
     *
     * @throws IndexOutOfBoundsException if the specified index is out of
     *         range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public boolean addAll(Collection c) {
        return addAll(size, c);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert first element
     *                    from the specified collection.
     * @param c elements to be inserted into this list.
     * @throws IndexOutOfBoundsException if the specified index is out of
     *            range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public boolean addAll(int index, Collection c) {
        int numNew = c.size();

        if (numNew == 0) {
            return false;
        }

        modCount++;

        Entry successor = ((index == size) ? header : entry(index));
        Entry predecessor = successor.previous;
        Iterator it = c.iterator();

        for (int i = 0; i < numNew; i++) {
            Entry e = new Entry(it.next(), successor, predecessor);
            predecessor.next = e;
            predecessor = e;

            notifyIteratorsOfAddition(index++);
        }

        successor.previous = predecessor;

        size += numNew;

        return true;
    }

    public boolean removeAll(Collection c) {
        boolean modified = false;
        Iterator e = iterator();

        while (e.hasNext()) {
            if (c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }

        return modified;
    }

    public boolean retainAll(Collection c) {
        boolean modified = false;
        Iterator e = iterator();

        while (e.hasNext()) {
            if (!c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }

        return modified;
    }

    /**
     * Removes all of the elements from this list.
     */
    public void clear() {
        modCount++;
        header.next = header.previous = header;
        size = 0;

        notifyIteratorsOfClearance();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof List)) {
            return false;
        }

        ListIterator e1 = listIterator();
        ListIterator e2 = ((List) o).listIterator();

        while (e1.hasNext() && e2.hasNext()) {
            Object o1 = e1.next();
            Object o2 = e2.next();

            if (!((o1 == null) ? (o2 == null) : o1.equals(o2))) {
                return false;
            }
        }

        return !(e1.hasNext() || e2.hasNext());
    }

    public int hashCode() {
        int hashCode = 1;
        Iterator i = iterator();

        while (i.hasNext()) {
            Object obj = i.next();
            hashCode = (31 * hashCode) + ((obj == null) ? 0 : obj.hashCode());
        }

        return hashCode;
    }

    // Positional Access Operations

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of element to return.
     * @return the element at the specified position in this list.
     *
     * @throws IndexOutOfBoundsException if the specified index is is out of
     * range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Object get(int index) {
        return entry(index).element;
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if the specified index is out of
     *                  range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Object set(int index, Object element) {
        Entry e = entry(index);
        Object oldVal = e.element;
        e.element = element;

        return oldVal;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     *
     * @throws IndexOutOfBoundsException if the specified index is out of
     *                  range (<tt>index &lt; 0 || index &gt; size()</tt>).
     */
    public void add(int index, Object element) {
        addBefore(element, ((index == size) ? header : entry(index)), index);
    }

    /**
     * Removes the element at the specified position in this list.  Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to removed.
     * @return the element previously at the specified position.
     *
     * @throws IndexOutOfBoundsException if the specified index is out of
     *                   range (<tt>index &lt; 0 || index &gt;= size()</tt>).
     */
    public Object remove(int index) {
        Entry e = entry(index);
        remove(e, index);

        return e.element;
    }

    /**
     * Return the indexed entry.
     */
    protected Entry entry(int index) {
        if ((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " +
                size);
        }

        Entry e = header;

        if (index < (size / 2)) {
            for (int i = 0; i <= index; i++)
                e = e.next;
        } else {
            for (int i = size; i > index; i--)
                e = e.previous;
        }

        return e;
    }

    // Search Operations

    /**
     * Returns the index in this list of the first occurrence of the
     * specified element, or -1 if the List does not contain this
     * element.  More formally, returns the lowest index i such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
     * there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the first occurrence of the
     *                specified element, or -1 if the list does not contain this
     *                element.
     */
    public int indexOf(Object o) {
        int index = 0;

        if (o == null) {
            for (Entry e = header.next; e != header; e = e.next) {
                if (e.element == null) {
                    return index;
                }

                index++;
            }
        } else {
            for (Entry e = header.next; e != header; e = e.next) {
                if (o.equals(e.element)) {
                    return index;
                }

                index++;
            }
        }

        return -1;
    }

    /**
     * Returns the index in this list of the last occurrence of the
     * specified element, or -1 if the list does not contain this
     * element.  More formally, returns the highest index i such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
     * there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the last occurrence of the
     *                specified element, or -1 if the list does not contain this
     *                element.
     */
    public int lastIndexOf(Object o) {
        int index = size;

        if (o == null) {
            for (Entry e = header.previous; e != header; e = e.previous) {
                index--;

                if (e.element == null) {
                    return index;
                }
            }
        } else {
            for (Entry e = header.previous; e != header; e = e.previous) {
                index--;

                if (o.equals(e.element)) {
                    return index;
                }
            }
        }

        return -1;
    }

    public Iterator iterator() {
        return coinsIterator();
    }

    public ListIterator listIterator() {
        return coinsIterator();
    }

    public ListIterator listIterator(int index) {
        return coinsIterator(index);
    }

    /**
     * Returns a <code>CoinsIterator</code> object backed by this list, whose cursor is placed at the beginning of this list.
     */
    public CoinsIterator coinsIterator() {
        return coinsIterator(0);
    }

    /**
     * Returns a <code>CoinsIterator</code> object backed by this list, whose cursor is placed before the given index. Subsequent call to <code>next</code> will return the <code>pIndex</code>th element of this list.
     */
    public CoinsIterator coinsIterator(int pIndex) {
        return new Itr(pIndex);
    }

    /**
     * Notify all the iterators of this list of the addition of an element at point <code>pIndex</code> to this list, so that they can adjust their current positions if necessary.
     */
    protected void notifyIteratorsOfAddition(int pIndex) {
        Itr lItr;

        for (Iterator lIt = fIterators.iterator(); lIt.hasNext();) {
            lItr = (Itr) lIt.next();

            if (pIndex <= lItr.nextIndex) {
                lItr.nextIndex++;
            }
        }
    }

    /**
     * Notify all the iterators of this list of the clearance of this list, so that they can initialize.
     */
    protected void notifyIteratorsOfClearance() {
        Itr lItr;

        for (Iterator lIt = fIterators.iterator(); lIt.hasNext();) {
            lItr = (Itr) lIt.next();
            lItr.next = header;
            lItr.lastReturned = header;
            lItr.nextIndex = 0;
        }
    }

    /**
     * Notify all the iterators of this list of the removal of an element at the specified position to this list, so that they can adjust their current states.
     */
    protected void notifyIteratorsOfRemoval(Entry e, int pIndex) {
        Itr lItr;

        for (Iterator lIt = fIterators.iterator(); lIt.hasNext();) {
            lItr = (Itr) lIt.next();

            if (lItr.next == e) {
                lItr.next = e.next;
            }

            if (e == lItr.lastReturned) {
                lItr.lastReturned = header;
            }

            if (pIndex < lItr.nextIndex) {
                lItr.nextIndex--;
            }
        }
    }

    protected Entry addBefore(Object o, Entry e, int pIndex) {
        Entry newEntry = new Entry(o, e, e.previous);
        newEntry.previous.next = newEntry;
        newEntry.next.previous = newEntry;
        size++;
        modCount++;

        notifyIteratorsOfAddition(pIndex);

        return newEntry;
    }

    protected void remove(Entry e, int pIndex) {
        if (e == header) {
            throw new NoSuchElementException();
        }

        e.previous.next = e.next;
        e.next.previous = e.previous;
        size--;
        modCount++;

        notifyIteratorsOfRemoval(e, pIndex);
    }

    /**
     * Returns a shallow copy of this <tt>LinkedList</tt>. (The elements
     * themselves are not cloned.)
     *
     * @return a shallow copy of this <tt>LinkedList</tt> instance.
     */
    public Object clone() {
        CoinsList clone = null;

        try {
            clone = (CoinsList) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

        // Put clone into "virgin" state
        clone.header = new Entry(null, null, null);
        clone.header.next = clone.header.previous = clone.header;
        clone.size = 0;
        clone.modCount = 0;

        // Initialize clone with our elements
        for (Entry e = header.next; e != header; e = e.next)
            clone.add(e.element);

        return clone;
    }

    /**
     * Returns an array containing all of the elements in this list
     * in the correct order.
     *
     * @return an array containing all of the elements in this list
     *                in the correct order.
     */
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;

        for (Entry e = header.next; e != header; e = e.next)
            result[i++] = e.element;

        return result;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * the correct order.  The runtime type of the returned array is that of
     * the specified array.  If the list fits in the specified array, it
     * is returned therein.  Otherwise, a new array is allocated with the
     * runtime type of the specified array and the size of this list.<p>
     *
     * If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list),
     * the element in the array immediately following the end of the
     * collection is set to null.  This is useful in determining the length
     * of the list <i>only</i> if the caller knows that the list
     * does not contain any null elements.
     *
     * @param a the array into which the elements of the list are to
     *                be stored, if it is big enough; otherwise, a new array of the
     *                 same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list.
     * @throws ArrayStoreException if the runtime type of a is not a
     *         supertype of the runtime type of every element in this list.
     */
    public Object[] toArray(Object[] a) {
        if (a.length < size) {
            a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass()
                                                                .getComponentType(),
                    size);
        }

        int i = 0;

        for (Entry e = header.next; e != header; e = e.next)
            a[i++] = e.element;

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    public List subList(int fromIndex, int toIndex) {
        return new SubList(this, fromIndex, toIndex);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator e = iterator();
        buf.append("[");

        int maxIndex = size() - 1;

        for (int i = 0; i <= maxIndex; i++) {
            buf.append(String.valueOf(e.next()));

            if (i < maxIndex) {
                buf.append(", ");
            }
        }

        buf.append("]");

        return buf.toString();
    }

    protected class Itr implements CoinsIterator {
        protected Entry lastReturned = header;
        protected Entry next;
        protected int nextIndex;
        protected int expectedModCount = modCount;
        protected boolean lNexted;

        protected Itr(int index) {
            if ((index < 0) || (index > size)) {
                throw new IndexOutOfBoundsException("Index: " + index +
                    ", Size: " + size);
            }

            if (index < (size / 2)) {
                next = header.next;

                for (nextIndex = 0; nextIndex < index; nextIndex++)
                    next = next.next;
            } else {
                next = header;

                for (nextIndex = size; nextIndex > index; nextIndex--)
                    next = next.previous;
            }

            fIterators.add(this);
        }

        public boolean hasNext() {
            return nextIndex != size;
        }

        public Object next() {
            checkForComodification();

            if (nextIndex == size) {
                throw new NoSuchElementException();
            }

            lastReturned = next;
            next = next.next;
            nextIndex++;

            lNexted = true;

            return lastReturned.element;
        }

        public boolean hasPrevious() {
            return nextIndex != 0;
        }

        public Object previous() {
            if (nextIndex == 0) {
                throw new NoSuchElementException();
            }

            lastReturned = next = next.previous;
            nextIndex--;
            checkForComodification();

            lNexted = false;

            return lastReturned.element;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            CoinsList.this.remove(lastReturned, nextIndex - (lNexted ? 1 : 0));
            expectedModCount++;
        }

        public Object lastReturned() {
            return lastReturned.element;
        }

        public void set(Object o) {
            if (lastReturned == header) {
                throw new IllegalStateException();
            }

            checkForComodification();
            lastReturned.element = o;
        }

        public void add(Object o) {
            checkForComodification();
            lastReturned = header;
            addBefore(o, next, nextIndex);
            expectedModCount++;
        }

        public void addAfter(Object o) {
            add(o);
            next = next.previous;
            nextIndex--;
        }

        protected final void checkForComodification() {
            if (modCount != expectedModCount) {
                //                            throw new ConcurrentModificationException();
                ;
            }
        }
    }

    protected static class Entry {
        public Object element;
        public Entry next;
        public Entry previous;

        protected Entry(Object element, Entry next, Entry previous) {
            this.element = element;
            this.next = next;
            this.previous = previous;
        }
    }

    class SubList extends CoinsList {
        private CoinsList l;
        private int offset;
        private int size;
        private int expectedModCount;

        SubList(CoinsList list, int fromIndex, int toIndex) {
            if (fromIndex < 0) {
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }

            if (toIndex > list.size()) {
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            }

            if (fromIndex > toIndex) {
                throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
            }

            l = list;
            offset = fromIndex;
            size = toIndex - fromIndex;
            expectedModCount = l.modCount;
        }

        public Object set(int index, Object element) {
            rangeCheck(index);
            checkForComodification();

            return l.set(index + offset, element);
        }

        public Object get(int index) {
            rangeCheck(index);
            checkForComodification();

            return l.get(index + offset);
        }

        public int size() {
            checkForComodification();

            return size;
        }

        public void add(int index, Object element) {
            if ((index < 0) || (index > size)) {
                throw new IndexOutOfBoundsException();
            }

            checkForComodification();
            l.add(index + offset, element);
            expectedModCount = l.modCount;
            size++;
            modCount++;
        }

        public Object remove(int index) {
            rangeCheck(index);
            checkForComodification();

            Object result = l.remove(index + offset);
            expectedModCount = l.modCount;
            size--;
            modCount++;

            return result;
        }

        //   protected void removeRange(int fromIndex, int toIndex) {
        //       checkForComodification();
        //       l.removeRange(fromIndex+offset, toIndex+offset);
        //       expectedModCount = l.modCount;
        //       size -= (toIndex-fromIndex);
        //       modCount++;
        //   }
        public boolean addAll(Collection c) {
            return addAll(size, c);
        }

        public boolean addAll(int index, Collection c) {
            if ((index < 0) || (index > size)) {
                throw new IndexOutOfBoundsException("Index: " + index +
                    ", Size: " + size);
            }

            int cSize = c.size();

            if (cSize == 0) {
                return false;
            }

            checkForComodification();
            l.addAll(offset + index, c);
            expectedModCount = l.modCount;
            size += cSize;
            modCount++;

            return true;
        }

        public Iterator iterator() {
            return listIterator();
        }

        public ListIterator listIterator(final int index) {
            checkForComodification();

            if ((index < 0) || (index > size)) {
                throw new IndexOutOfBoundsException("Index: " + index +
                    ", Size: " + size);
            }

            return new ListIterator() {
                    private ListIterator i = l.listIterator(index + offset);

                    public boolean hasNext() {
                        return nextIndex() < size;
                    }

                    public Object next() {
                        if (hasNext()) {
                            return i.next();
                        } else {
                            throw new NoSuchElementException();
                        }
                    }

                    public boolean hasPrevious() {
                        return previousIndex() >= 0;
                    }

                    public Object previous() {
                        if (hasPrevious()) {
                            return i.previous();
                        } else {
                            throw new NoSuchElementException();
                        }
                    }

                    public int nextIndex() {
                        return i.nextIndex() - offset;
                    }

                    public int previousIndex() {
                        return i.previousIndex() - offset;
                    }

                    public void remove() {
                        i.remove();
                        expectedModCount = l.modCount;
                        size--;
                        modCount++;
                    }

                    public void set(Object o) {
                        i.set(o);
                    }

                    public void add(Object o) {
                        i.add(o);
                        expectedModCount = l.modCount;
                        size++;
                        modCount++;
                    }
                };
        }

        public List subList(int fromIndex, int toIndex) {
            return new SubList(this, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if ((index < 0) || (index >= size)) {
                throw new IndexOutOfBoundsException("Index: " + index +
                    ",Size: " + size);
            }
        }

        private void checkForComodification() {
            if (l.modCount != expectedModCount) {
                ;
            }

            //           throw new ConcurrentModificationException();
        }
    }
}
