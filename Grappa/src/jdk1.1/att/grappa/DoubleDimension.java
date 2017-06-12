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
 * A re-implementation of <code>java.awt.Dimension</code> using doubles instead of ints.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DoubleDimension implements java.io.Serializable {
    
  /**
   * The width dimension.
   */
  public double width;

  /**
   * The height dimension.
   */
  public double height;

  /** 
   * Creates an instance of <code>DoubleDimension</code> with a width 
   * of zero and a height of zero. 
   */
  public DoubleDimension() {
    this(0, 0);
  }

  /** 
   * Creates an instance of <code>DoubleDimension</code> whose width  
   * and height are the same as for the specified dimension. 
   * @param    d   the specified dimension for the 
   *               <code>width</code> and 
   *               <code>height</code> values.
   */
  public DoubleDimension(DoubleDimension d) {
    this(d.width, d.height);
  }

  /** 
   * Creates an instance of <code>DoubleDimension</code> whose width  
   * and height are the same as for the specified dimension. 
   * @param    d   the specified dimension for the 
   *               <code>width</code> and 
   *               <code>height</code> values.
   */
  public DoubleDimension(java.awt.Dimension d) {
    this((double)d.width, (double)d.height);
  }


  /** 
   * Constructs a <code>DoubleDimension</code> and initializes it to the specified width and
   * specified height.
   * @param width the specified width dimension
   * @param height the specified height dimension
   */
  public DoubleDimension(double width, double height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Set this dimension as specified.
   * @param    d  the new size for this <code>DoubleDimension</code> object.
   */
  public void setSize(DoubleDimension d) {
    setSize(d.width, d.height);
  }	

  /**
   * Set the size of this <code>DoubleDimension</code> object 
   * to the specified width and height.
   * @param    width   the new width for this dimnesion.
   * @param    height  the new height for this dimension.
   */
  public void setSize(double width, double height) {
    this.width = width;
    this.height = height;
  }	

  /**
   * Rounds the <code>DoubleDimension</code> to a standard (integer) <code>Dimension</code>
   *
   * @return the <code>DoubleDimension</code> rounded to the nearest <code>Dimension</code>
   */
  public java.awt.Dimension getApproximation() {
    return new java.awt.Dimension((int)Math.round(width),(int)Math.round(height));
  }

  /**
   * Returns the hashcode for this point.
   * @return      a hash code for this point.
   */
  public int hashCode() {
    long wbits = Double.doubleToLongBits(width);
    long hbits = Double.doubleToLongBits(height);
    return (int)(wbits ^ (hbits*31));
  }

  /**
   * Checks whether two dimension objects have equal values.
   * Two instances of <code>DoubleDimension</code> are equal
   * if the values of their <code>width</code> and <code>height</code>
   * member fields are the same.
   *
   * @return true if the supplied object equals this object, false otherwise
   */
  public boolean equals(Object obj) {
    if (obj instanceof DoubleDimension) {
      DoubleDimension d = (DoubleDimension)obj;
      return (width == d.width) && (height == d.height);
    }
    return false;
  }

  /**
   * Returns a string that represents this 
   * <code>DoubleDimension</code> object's values.
   * @return     a string representation of this dimension, 
   *                  including the values of <code>width</code> 
   *                  and <code>height</code>.
   */
  public String toString() {
    return getClass().getName() + "[width=" + width + ",height=" + height + "]";
  }
}
