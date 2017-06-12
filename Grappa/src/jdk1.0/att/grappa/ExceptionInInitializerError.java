/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */
/*
 * @(#)ExceptionInInitializerError.java	1.2 97/01/20
 *
 * Copyright (c) 1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package att.grappa;

/**
 * Signals that an unexpected exception has occurred in a static initializer.
 *
 * @author  Frank Yellin
 * @version 1.2, 20 Jan 1997
 *
 * @since   JDK1.1
 */
public
class ExceptionInInitializerError extends LinkageError {
    private Throwable exception;

    /**
     * Constructs an ExceptionInInitializerError with no detail message.
     * A detail message is a String that describes this particular exception.
     *
     * @since   JDK1.1
     */
    public ExceptionInInitializerError() {
	super();
    }

    /**
     * Constructs a new ExceptionInInitializerError class initialized to 
     * the specific throwable
     *
     * @param thrown The exception thrown
     * @since   JDK1.1
     */
    public ExceptionInInitializerError(Throwable thrown) {
	this.exception = thrown;
    }

    /**
     * Constructs a ExceptionInInitializerError with the specified detail message.
     * A detail message is a String that describes this particular exception.
     *
     * @param s the detail message
     * @since   JDK1.1
     */
    public ExceptionInInitializerError(String s) {
	super(s);
    }

    /**
     * Returns the exception that occurred during a static initialization that
     * caused this Error to be created.
     */
    public Throwable getException() { 
	return exception;
    }
}
