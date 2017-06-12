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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class provides a panel for displaying and editing Element
 * attributes. The information displayed includes:
 * <ul>
 *      <li> the name of the element (obtained via the getHandle() method
 *           of the AppObject associated with the element)
 *      <li> a list of attribute names (possibly restricted using the
 *           editList parameter)
 *      <li> for a selected attribute, its default value is displayed
 *      <li> for a selected attribute, its current value is displayed
 * </ul>
 * Buttons to apply changes and to dismiss the panel are also supplied.
 *
 * @see AppObject#getHandle()
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class AttributePanel extends Panel
{
  Vector attrList = null;

  List nameList = null;
  TextField nameText = null;
  TextField prntText = null;
  TextField dfltText = null;
  TextField crntText = null;
  Button add = null;
  Button apply = null;
  Button dismiss = null;
  Frame root = null;

  final static int NO_CHANGE = 0;
  final static int DFLT_ONLY = 1;
  final static int CRNT_ONLY = 2;
  final static int BOTH_CHNG = 3;

  String empty = "";

  Element crntElem = null;

  Listener listener = new Listener();

  /**
   * Constructs an AttributePanel which is assumed will be contained in
   * the supplied Frame.
   *
   * @param root the Frame containing this panel, used by the Dismiss
   *             button to set its visibility to false.
   */
  public AttributePanel(Frame root) {
    super();

    this.root = root;

    attrList = new Vector(16,8);

    // layout panel
    
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4,4,4,4);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 0;
    setLayout(gb);

    // element name
    Panel pnl = new Panel();
    pnl.setLayout(new BorderLayout());
    Label lbl = new Label("Element name:",Label.LEFT);
    nameText = new TextField("",40);
    nameText.setEditable(false);
    nameText.setBackground(Color.white);
    pnl.add("North",lbl);
    pnl.add("Center",nameText);
    gb.setConstraints(pnl,gbc);
    add(pnl);


    // attribute list
    pnl = new Panel();
    pnl.setLayout(new BorderLayout());
    lbl = new Label("Attributes:",Label.LEFT);
    nameList = new List(10,false);
    nameList.addItemListener(listener);
    nameList.setBackground(Color.white);
    pnl.add("North",lbl);
    pnl.add("Center",nameList);
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gb.setConstraints(pnl,gbc);
    add(pnl);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty = 0;

    // default value
    pnl = new Panel();
    pnl.setLayout(new BorderLayout());
    lbl = new Label("Default value:",Label.LEFT);
    dfltText = new TextField("",40);
    dfltText.addActionListener(listener);
    dfltText.addFocusListener(listener);
    dfltText.setEditable(true);
    dfltText.setBackground(Color.white);
    pnl.add("North",lbl);
    pnl.add("Center",dfltText);
    gb.setConstraints(pnl,gbc);
    add(pnl);

    // current value
    pnl = new Panel();
    pnl.setLayout(new BorderLayout());
    lbl = new Label("Current value:",Label.LEFT);
    crntText = new TextField("",40);
    crntText.addActionListener(listener);
    crntText.addFocusListener(listener);
    crntText.setEditable(true);
    crntText.setBackground(Color.white);
    pnl.add("North",lbl);
    pnl.add("Center",crntText);
    gb.setConstraints(pnl,gbc);
    add(pnl);

    // buttons
    pnl = new Panel();
    pnl.setLayout(new BorderLayout());
    apply = new Button("Apply");
    apply.addActionListener(listener);
    dismiss = new Button("Dismiss");
    dismiss.addActionListener(listener);
    pnl.add("West",apply);
    pnl.add("East",dismiss);
    gb.setConstraints(pnl,gbc);
    add(pnl);
  }

  /**
   * Fills the AttributePanel with information regarding the supplied
   * Element.
   * 
   * @param element the Element about which information is to be displayed 
   * @param editList a Vector of String objects that specifies the names of
   *                 the Element attributes to display.  If editList is null,
   *                 then all attributes for the element are displayed.
   */
  public void loadAttributePanel(Element element, Vector editList) {
    PropertyGroup pg = null;
    String name = null;
    String dflt = null;
    String crnt = null;
    Attribute attr = null;

    crntElem = element;

    attrList.removeAllElements();

    Enumeration enum = null;
    if(editList == null) {
      enum = element.getAttributePairs();
    } else {
      enum = editList.elements();
    }
    while(enum.hasMoreElements()) {
      if(editList == null) {
	attr = (Attribute)enum.nextElement();
	name = attr.getName();
      } else {
	name = (String)enum.nextElement();
      }
      attr = element.getDefaultAttribute(name);
      if(attr == null) {
	dflt = empty;
      } else {
	dflt = attr.getValue();
      }
      attr = element.getLocalAttribute(name);
      if(attr == null) {
	crnt = empty;
      } else {
	crnt = attr.getValue();
      }
      pg = new PropertyGroup(name,dflt,crnt);
      attrList.addElement(pg);
    }

    // slowsort
    PropertyGroup pg1 = null;
    PropertyGroup pg2 = null;
    for(int i = 0; i < (attrList.size()-1); i++) {
      pg1 = (PropertyGroup)attrList.elementAt(i);
      for(int j = i+1; j < attrList.size(); j++) {
	pg2 = (PropertyGroup)attrList.elementAt(j);
	if(pg1.getName().compareTo(pg2.getName()) > 0) {
	  attrList.setElementAt(pg1,j);
	  attrList.setElementAt(pg2,i);
	  pg1 = pg2;
	}
      }
    }

    nameText.setText("");
    nameList.removeAll();
    dfltText.setText("");
    crntText.setText("");

    nameText.setText(element.getAppObject().getHandle());

    for(int i = 0; i < attrList.size(); i++) {
      nameList.addItem(((PropertyGroup)attrList.elementAt(i)).getName());
    }

    if(attrList.size() > 0) {
      nameList.select(0);
      ItemEvent evt = new ItemEvent(nameList,ItemEvent.ITEM_STATE_CHANGED,nameList.getItem(0),ItemEvent.SELECTED);
      listener.itemStateChanged(evt);
    }
  }

  /**
   * A convenience method equivalent to calling loadAttributePanel with
   * a null value for the editList parameter.
   *
   * @param element the Element about which information is to be displayed 
   *
   * @see AttributePanel#loadAttributePanel(Element,Vector)
   */
  public void loadAttributePanel(Element elem) {
    loadAttributePanel(elem,null);
  }

  class Listener implements ItemListener, ActionListener, FocusListener {
    PropertyGroup crntPG = null;

    public void itemStateChanged(ItemEvent evt) {
      if(evt.getStateChange() == ItemEvent.SELECTED) {
	// can assume event was from nameList
	int slot = nameList.getSelectedIndex();
	PropertyGroup pg = (PropertyGroup)attrList.elementAt(slot);
	dfltText.setText(pg.getNewDefaultValue());
	crntText.setText(pg.getNewCurrentValue());
	crntText.selectAll();
	crntText.requestFocus();
      }
    }

    public void actionPerformed(ActionEvent evt) {
      Object src = evt.getSource();
      if(src instanceof Button) {
	Button btn = (Button)src;
	if(btn == dismiss) {
	  root.setVisible(false);
	} else if(btn == apply) {
	  int slot = nameList.getSelectedIndex();
	  PropertyGroup pg = null;
	  int i = 0;
	  try {
	    for(i=0; i<attrList.size(); i++) {
	      pg = (PropertyGroup)attrList.elementAt(i);
	      switch(pg.changeStatus()) {
	      case AttributePanel.NO_CHANGE:
		break;
	      case AttributePanel.DFLT_ONLY:
		crntElem.setDefaultAttribute(pg.getName(),pg.getNewDefaultValue());
		pg.updateDefaultValue();
		break;
	      case AttributePanel.CRNT_ONLY:
		crntElem.setAttribute(pg.getName(),pg.getNewCurrentValue());
		pg.updateCurrentValue();
		break;
	      case AttributePanel.BOTH_CHNG:
		crntElem.setDefaultAttribute(pg.getName(),pg.getNewDefaultValue());
		crntElem.setAttribute(pg.getName(),pg.getNewCurrentValue());
		pg.updateDefaultValue();
		pg.updateCurrentValue();
		break;
	      }
	    }
	    //crntElem.getGraph().redrawGraph();
	    DrawPane.refreshGraph(crntElem.getGraph());
	  } catch(Exception ex) {
	    for(int j=0; j<=i; j++) {
	      pg = (PropertyGroup)attrList.elementAt(j);
	      switch(pg.changeStatus()) {
	      case AttributePanel.NO_CHANGE:
		break;
	      case AttributePanel.DFLT_ONLY:
		crntElem.setDefaultAttribute(pg.getName(),pg.getDefaultValue());
		pg.setNewDefaultValue(pg.getDefaultValue());
		break;
	      case AttributePanel.CRNT_ONLY:
		crntElem.setAttribute(pg.getName(),pg.getCurrentValue());
		pg.setNewCurrentValue(pg.getCurrentValue());
		break;
	      case AttributePanel.BOTH_CHNG:
		crntElem.setDefaultAttribute(pg.getName(),pg.getDefaultValue());
		crntElem.setAttribute(pg.getName(),pg.getCurrentValue());
		pg.setNewCurrentValue(pg.getCurrentValue());
		pg.setNewDefaultValue(pg.getDefaultValue());
		break;
	      }
	    }
	    Grappa.displayException(ex);
	    crntElem.getDrawObject().setBoundsFlag(false);
	    crntElem.getDrawObject().setRedrawFlag(false);
	  }
	  pg = (PropertyGroup)attrList.elementAt(slot);
	  dfltText.setText(pg.getNewDefaultValue());
	  crntText.setText(pg.getNewCurrentValue());
	  crntText.selectAll();
	  crntText.requestFocus();
	}
      } else if(src instanceof TextField) {
	TextField tf = (TextField)src;
	int slot = nameList.getSelectedIndex();
	PropertyGroup pg = (PropertyGroup)attrList.elementAt(slot);
	if(tf == dfltText) {
	  pg.setNewDefaultValue(dfltText.getText());
	} else if(tf == crntText) {
	  pg.setNewCurrentValue(crntText.getText());
	}
      }
    }

    public void focusGained(FocusEvent evt) {
      int slot = nameList.getSelectedIndex();
      crntPG = (PropertyGroup)attrList.elementAt(slot);
      return;
    }

    public void focusLost(FocusEvent evt) {
      if(crntPG == null) return;
      TextField tf = (TextField)(evt.getSource());
      if(tf == dfltText) {
	crntPG.setNewDefaultValue(dfltText.getText());
      } else if(tf == crntText) {
	crntPG.setNewCurrentValue(crntText.getText());
      }
      crntPG = null;
    }
  }

  class PropertyGroup {
    private String name = null;
    private String defaultValue =  null;
    private String currentValue = null;
    private String newDefaultValue =  null;
    private String newCurrentValue = null;

    public PropertyGroup(String name, String defaultValue, String currentValue) {
      this.name =  name;
      setNewDefaultValue(defaultValue);
      setNewCurrentValue(currentValue);
      updateDefaultValue();
      updateCurrentValue();
    }

    public String setNewDefaultValue(String value) {
      String oldValue = newDefaultValue;
      newDefaultValue = value;
      return oldValue;
    }

    public String setNewCurrentValue(String value) {
      String oldValue = newCurrentValue;
      newCurrentValue = value;
      return oldValue;
    }

    public String updateDefaultValue() {
      String oldValue = defaultValue;
      defaultValue = newDefaultValue;
      return oldValue;
    }

    public String updateCurrentValue() {
      String oldValue = currentValue;
      if(newCurrentValue.equals(newDefaultValue)) {
	newCurrentValue = empty;
      }
      currentValue = newCurrentValue;
      return oldValue;
    }

    public String getName() {
      return name;
    }

    public String getDefaultValue() {
      return defaultValue;
    }

    public String getCurrentValue() {
      return currentValue;
    }

    public String getNewDefaultValue() {
      return newDefaultValue;
    }

    public String getNewCurrentValue() {
      return newCurrentValue;
    }

    public int changeStatus() {
      int status = 0;
      if(defaultValue != newDefaultValue && !defaultValue.equals(newDefaultValue)) {
	status |= AttributePanel.DFLT_ONLY;
      }
      if(currentValue != newCurrentValue && !currentValue.equals(newCurrentValue)) {
	status |= AttributePanel.CRNT_ONLY;
      }
      return status;
    }
  }
}
