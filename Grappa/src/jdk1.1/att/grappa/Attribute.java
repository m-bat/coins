/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */

package att.grappa;

import java.util.Observable;
import java.util.Observer;

/**
 * A class used for representing attributes associated with the graph elements.
 * An attribute consists of a name-value pair.
 * Once an attribute is constructed, the name cannot be changed.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Attribute extends Observable
{
  /*
   * variables for storing name / value info
   */
  private String name;
  private String value;
  private Object object;
  private int nameHash;

  /**
   * Constructs a new attribute from a name / value pair.
   *
   * @param attrName the name of the attribute.
   * @param attrValue the value of the attribute.
   */
  public Attribute(String attrName, String attrValue) {
    super();
    if(attrName == null) {
      throw new IllegalArgumentException("the name of an Attribute pair cannot be null");
    }
    name = new String(attrName);
    nameHash = name.hashCode();
    setValue(attrValue);
  }

  /**
   * Constructs a new attribute from an existing one.
   *
   * @param attr the attribute from which this new one is to be generated
   */
  public Attribute(Attribute attr) {
    this(attr.getName(),attr.getValue());
  }

  /**
   * Get the name of this attribute.
   *
   * @return the name of this attribute.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the value of this attribute.
   *
   * @return the value of the attribute.
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the value of the attribute.  If the value is different than the
   * current value, the Observable changed indicator is set.
   *
   * @param attrValue the new attribute value.
   * @return the old attribute value.
   */
  public String setValue(String attrValue) {
    String oldValue = value;
    boolean changed = false;
    if (attrValue != null) {
      changed = (value == null || !attrValue.equals(value));
      value = new String(attrValue);
    } else {
      changed = (value != null);
      value = null;
    }
    if(changed) {
      setChanged();
      setObject(null);
    }
    return oldValue;
  }

  /**
   * Tests for equality with the given attribute.
   *
   * @param attr the attribute with which to compare this attribute.
   * @return true if the two attributes are equal, false otherwise.
   */
  public boolean equals(Attribute attr) {
    if(attr == null) {
      return false;
    }
    if(this == attr) {
      return true;
    }
    if(!attr.getName().equals(name)) {
      return false;
    }
    String attrValue = attr.getValue();
    if(attrValue == value) {
      return true;
    }
    if(attrValue == null) {
      return false;
    }
    return attrValue.equals(value);
  }

  /**
   * Tests for equality of this attribute's value with the given attribute's
   * value. The attribute names are not compated.
   *
   * @param attr the attribute with which to compare this attribute.
   * @return true if the two attribute values are equal, false otherwise.
   */
  public boolean equalsValue(Attribute attr) {
    if(attr == null) {
      return false;
    }
    if(this == attr) {
      return true;
    }
    String attrValue = attr.getValue();
    if(attrValue == value) {
      return true;
    }
    if(attrValue == null) {
      return false;
    }
    return attrValue.equals(value);
  }

  /**
   * Get the hash value for this attributes name.
   *
   * @return the hash code for the name portion of this attribute
   */
  public int getNameHash() {
    return nameHash;
  }

  /**
   * Use to indicate that this object has changed. 
   * This method is a convenience method that calls the corresponding
   * protected method of the Observable class.
   * 
   * @see java.util.Observable#setChanged()
   */
  public void setChanged() {
    super.setChanged();
  }

  /**
   * Use to indicate that this object has no longer changed, or that it has
   * already notified all of its observers of its most recent change.
   * This method is a convenience method that calls the corresponding
   * protected method of the Observable class.
   * 
   * @see java.util.Observable#clearChanged()
   */
  public void clearChanged() {
    super.clearChanged();
  }

  /**
   * Get the general-purpose Object associated with this attribute.
   * The object is set to null whenever the attribute is changed. However,
   * the object can be set or read by Observers as a convenience.
   * For example, as an Attribute is propagated among Observers,
   * an Observer (usually the first one contacted) can set the object
   * value to some value computed using. perhaps. the attribute value,
   * then other Observers that need to work with the value do not need
   * to incur the overhead of recomputing the same value.
   *
   * @return the value of the object associated with this attributes
   * @see Attribute#setObject(java.lang.Object)
   */
  public Object getObject() {
    return object;
  }

  /**
   * Set the general-purpose Object associated with this attribute.
   *
   * @param obj value to use in setting this Attribute's object
   * @see Attribute#getObject()
   */
  public void setObject(Object obj) {
    object = obj;
  }

  public String toString() {
    return getClass().getName() + "[name=\""+name+"\",value=\""+value+"\"]";
  }

  // for debugging:
  //public synchronized void addObserver(Observer obs) {
  //super.addObserver(obs);
  //System.err.println("Added "+((DrawObject)obs).getElement().getName()+" as #"+countObservers()+" to ("+name+","+value+") as " + ((((DrawObject)obs).getElement().getLocalAttribute(name)==this)?"local (":"default(")+hashCode()+")");
  //}
}
