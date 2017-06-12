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

public class PopUpCard extends Frame {
  private Panel cardPanel = null;
  private Panel choicePanel = null;
  private String[] cardLabels = null;
  private Vector panels = null;
  private Choice choices = null;

  public PopUpCard(String title) {
    this(title,null);
  }

  public PopUpCard(String title, String[] labels) {
    super(title);

    cardLabels = labels;
    setLayout(new BorderLayout());

    choicePanel = new Panel();
    choices = new Choice();
    panels = new Vector(1+length(),5);
    for(int i = 0; i < length(); i++) {
      choices.addItem(cardLabels[i]);
      cardPanel = new Panel();
      panels.addElement(cardPanel);
      layoutPanel(i,cardLabels[i],cardPanel);
    }

    if(length() > 1) {
      choicePanel.add(choices);
      add("North", choicePanel);
    }

    cardPanel = new Panel();
    cardPanel.setLayout(new CardLayout());

    for(int i = 0; i < length(); i++) {
      cardPanel.add(cardLabels[i], (Panel)panels.elementAt(i));
    }
    add("Center", cardPanel);
  }

  // should this be an enumeration instead?
  public Vector getPanels() {
    return panels;
  }

  public int length() {
    if(cardLabels == null) return 0;
    return cardLabels.length;
  }

  /**
   * used when extending PopUpCard class; could have been abstract, but then
   * could not use base class which is usable by means of addLabelAndPanel
   *
   * @arg nbr identifies the panel to be laid out
   * @arg label label to use for Choice
   * @arg the panel to which components will be added
   */
  protected void layoutPanel(int nbr, String label, Panel panel) {
    return;
  }

  public int addLabelAndPanel(String label, Panel panel) {
    return insertLabelAndPanelAt(label,panel,length());
  }

  public int insertLabelAndPanelAt(String label, Panel panel, int pos) {
    int sz = length();

    if(pos > sz) pos = sz;
    else if(pos < 0) pos = 0;

    panels.insertElementAt(panel,pos);

    String[] labels = new String[sz+1];
    int bump = 0;
    labels[pos] = label;
    for(int i = 0; i<sz; i++) {
      if(i == pos) bump = 1;
      labels[i+bump] = cardLabels[i];
    }
    cardLabels = labels;

    cardPanel.add(cardLabels[pos], panel);

    if(sz == 1) {
      choicePanel.add(choices);
      add("North", choicePanel);
    }
    return pos;
  }

  public boolean action(Event evt, Object arg) {
    if (evt.target instanceof Choice) {
      Choice chc = (Choice)evt.target;
      if(chc == choices) {
	((CardLayout)(cardPanel.getLayout())).show(cardPanel,(String)arg);
	return true;
      }
    }
    return super.action(evt,arg);
  }

  protected void dismiss() {
    hide();
    dispose();
  }

  public boolean handleEvent(Event evt) {
    if (evt.id == Event.WINDOW_DESTROY) {
      dismiss();
      return true;
    }   
    return super.handleEvent(evt);
  }
}
