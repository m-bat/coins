/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

import java.util.ListIterator;


/** CoinsIterator.java
 *
 * Created on August 11, 2002, 5:48 PM
 *
  * <p>Iterator that can accomodate the structural modification in any way in the underlying list, without throwing ConcurrentModificationException.</p>
 *
 * <p>When an object is added to the underlying list, what is returned by the subsequent call to <code>next</code>(next element of this iterator) will be the same as when there were no such addition. What is returned by the call to <code>previous</code> will be the same as before, except for the case where the new object was added just before the next element, in which case the newly added object will be returned. The result of the call to <code>nextIndex</code> and <code>previousIndex</code> will be incremented by one if the element was added somewhere before the next element. </p>
 * <p>When an object is removed from the underlying list, what is returned by the subsequent call to <code>next</code> and <code>previous</code> will be altered if what was removed was what would be returned if there was no such removal. In that case, <code>next</code> and <code>previous</code> return the next element and the previous element of what was removed (before its removal), respectively (if any). <code>nextIndex</code> and <code>previousIndex</code> will be decremented by one if the next element of this iterator before the call to <code>remove</code> lied somewhere after what was removed.</p>
 *
 * @see java.util.ListIterator
*/
public interface CoinsIterator extends ListIterator {
    /**
     * Returns the next element, and advances the imaginary cursor by one.
     */
    Object next();

    /**
     * Returns the previous element, and steps back the imaginary cursor by one.
     */
    Object previous();

    /**
     * Returns the index of the next element
     */
    int nextIndex();

    /**
     * Returns the index of the previous element
     */
    int previousIndex();

    /**
     * Remove what was returned by the previous call to <code>next</code> or <code>previous</code>
     *
     * @exception IllegalStateException if either there was no call to either <code>next</code> or <code>previous</code>, what was returned by such a call is already removed from the underlying list, or this iterator's method that structurally modifies the underlying list was called after such a call to <code>next</code> or <code>previous</code>.
     */
    void remove();

    /**
     * Returns what was returned by the previous call to <code>next</code> or <code>previous</code>
     * This is what will be removed by <code>remove</code>. In the situation where <code>remove</code> will throw <code>IllegalArgumentException</code>, this method will return <code>null</code>.
     *
     Object lastReturned();
    
    /**
     * Adds an element to the underlying list <I>before</I> the imaginary cursor of this iterator. That is, the subsequent call to <code>next</code> will be unaffected while the call to <code>previous</code> will return the object added by this method.
     */
    void add(Object o);

    /**
     * Adds an element to the underlying list <I>after</I> the imaginary cursor of this iterator. That is, the subsequent call to <code>previous</code> will be unaffected while the call to <code>next</code> will return the object added by this method.
     */
    void addAfter(Object o);
}
