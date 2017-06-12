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

import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A frame for displaying several categories of information about an item.
 * The frame uses a card layout to hold some number of panels that serve
 * to display whatever information is needed.  By default, <code>Grappa</code>
 * uses a <code>PopUpCard</code> and an <code>AttributePanel</code> to
 * display attribute information about graph elements.
 *
 * @see AppObject#properties(MouseEvent,Point)
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class PopUpCard extends Frame {
  private Panel cardPanel = null;
  private Panel choicePanel = null;
  private String[] cardLabels = null;
  private Vector panels = null;
  private Choice choices = null;
  private String title = null;
  private WindowObserver observer = null;


  /**
   * Create a <code>PopUpCard</code> instance with the given title.
   *
   * @param title  the frame title
   */
  public PopUpCard(String title) {
    this(title,null);
  }

  /**
   * Create a <code>PopUpCard</code> instance with the given title
   * and panel labels.
   *
   * @param title  the frame title
   * @param lbls  labels for identifying and selecting the panels managed
   *              by the card layout.
   */
  public PopUpCard(String title, String[] lbls) {
    super(title);
    this.title = title;

    observer = new WindowObserver();

    cardLabels = lbls;
    setLayout(new BorderLayout());

    choicePanel = new Panel();
    choices = new Choice();
    choices.addItemListener(observer);
    panels = new Vector(1+labels(),5);
    for(int i = 0; i < labels(); i++) {
      choices.addItem(cardLabels[i]);
      cardPanel = new Panel();
      panels.addElement(cardPanel);
      layoutPanel(i,cardLabels[i],cardPanel);
    }

    if(labels() > 1) {
      choicePanel.add(choices);
      add("North", choicePanel);
    } else if(labels() == 1) {
      setTitle(cardLabels[0]);
    }

    cardPanel = new Panel();
    cardPanel.setLayout(new CardLayout());

    for(int i = 0; i < labels(); i++) {
      cardPanel.add(cardLabels[i], (Panel)panels.elementAt(i));
    }
    add("Center", cardPanel);

    addWindowListener(observer);
  }

  // should this be an enumeration instead?
  /**
   * Get the panels associated with this card.
   *
   * @return a vector of <code>Panel</code> objects
   */
  public Vector getPanels() {
    return panels;
  }

  /**
   * Get a count of panel labels.
   *
   * @return the number of labels for selecting and identifying panels
   */
  public int labels() {
    if(cardLabels == null) return 0;
    return cardLabels.length;
  }

  /**
   * Set the frame title.
   * This method sets the frame title to the supplied argument when there
   * is only one label, otherwise it sets the frame title to the title
   * supplied when this object was created.
   *
   * @param title the candidate frame title
   */
  public void setTitle(String title) {
    if(labels() == 1) super.setTitle(title);
    else super.setTitle(this.title);
  }

  /**
   * Used when extending the <code>PopUpCard</code> class.
   * It could have been abstract since as defined it is a no-op, but then
   * one could not use the
   * base class which is usable by means of addLabelAndPanel.
   *
   * @param nbr identifies the panel to be laid out
   * @param label label to use for Choice
   * @param the panel to which components will be added
   */
  protected void layoutPanel(int nbr, String label, Panel panel) {
    return;
  }

  /**
   * Add a panel to this card.
   * It is added as the last panel.
   *
   * @param label the label for selecting and identifying the panel
   * @param panel the panel to be added
   */
  public int addLabelAndPanel(String label, Panel panel) {
    return insertLabelAndPanelAt(label,panel,labels());
  }

  /**
   * Add a panel to this card.
   * It is added at the specified position.
   *
   * @param label the label for selecting and identifying the panel
   * @param panel the panel to be added
   * @param pos the position at which the panel is to be placed
   * @return the actual position assigned (in case the supplied position was
   *         out-of-bounds).
   */
  public int insertLabelAndPanelAt(String label, Panel panel, int pos) {
    int sz = labels();

    if(pos > sz) pos = sz;
    else if(pos < 0) pos = 0;

    panels.insertElementAt(panel,pos);

    String[] lbls = new String[sz+1];
    int bump = 0;
    lbls[pos] = label;
    for(int i = 0; i<sz; i++) {
      if(i == pos) bump = 1;
      lbls[i+bump] = cardLabels[i];
    }
    cardLabels = lbls;

    cardPanel.add(cardLabels[pos], panel);

    if(sz == 1) {
      choicePanel.add(choices);
      add("North", choicePanel);
    }
    setTitle(label);
    return pos;
  }

  class WindowObserver extends WindowAdapter implements ItemListener {

    public void windowClosing(WindowEvent evt) {
      dismiss();
    }

    private void dismiss() {
      setVisible(false);
      dispose();
    }

    public void itemStateChanged(ItemEvent evt) {
      // can assume from choices
      if(evt.getStateChange() == ItemEvent.SELECTED) {
	((CardLayout)(cardPanel.getLayout())).show(cardPanel,choices.getSelectedItem());
      }
    }
  }
}
