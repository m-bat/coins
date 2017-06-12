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

/**
 * An attribute consists of a name and value pair.
 * Once constructed, the name cannot be changed.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class Attribute
{
  /*
   * variables for storing name / value info
   */
  private String name;
  private String value;

  // indicates if value has been changed, it is automatically set, but
  // needs to be manually reset.
  private boolean changed;

  /**
   * @param attrName the name of the attribute.
   * @param attrValue the value of the attribute.
   */
  public Attribute(String attrName, String attrValue) {
    if(attrName == null) {
      throw new IllegalArgumentException("the name of an Attribute pair cannot be null");
    }
    name = new String(attrName);
    setValue(attrValue);
  }

  /**
   * @return the name of this attribute.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the value of the attribute.
   */
  public String getValue() {
    return value;
  }

  /**
   * Resets the changed indicator to false.  The changed indicator is
   * automatically set to true when the attribute value is changed.
   */
  public void resetChanged() {
    changed = false;
  }

  /*
   * Sets the changed indicator to the given value.
   */
  private void setChanged(boolean newValue) {
    changed = newValue;
  }

  /**
   * @return the value of the attribute change indicator.
   */
  public boolean hasChanged() {
    return changed;
  }

  /**
   * Set the value of the attribute.  If the value is different than the
   * current value, the changed indicator is set to true.
   * @param attrValue the new attribute value.
   * @return the old attribute value.
   */
  public String setValue(String attrValue) {
    String oldValue = value;
    if (attrValue != null) {
      changed = (value == null || !attrValue.equals(value));
      value = new String(attrValue);
    } else {
      changed = (value != null);
      value = null;
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
   * value.
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
   * @return a copy of this attribute.
   */
  public Attribute copy() {
    Attribute newAttr = new Attribute(name,value);
    newAttr.setChanged(changed);
    return newAttr;
  }
}
