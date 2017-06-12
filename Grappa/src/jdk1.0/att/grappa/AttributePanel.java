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

public class AttributePanel extends Panel {
  Vector attrList = null;

  List nameList = null;
  TextField nameText = null;
  TextField dfltText = null;
  TextField crntText = null;
  Button apply = null;
  Button cancel = null;
  Frame root = null;

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
    nameList.setBackground(Color.white);
    pnl.add("North",lbl);
    pnl.add("Center",nameList);
    gb.setConstraints(pnl,gbc);
    add(pnl);

    // default value
    pnl = new Panel();
    pnl.setLayout(new BorderLayout());
    lbl = new Label("Default value:",Label.LEFT);
    dfltText = new TextField("",40);
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
    cancel = new Button("Cancel");
    pnl.add("West",apply);
    pnl.add("East",cancel);
    gb.setConstraints(pnl,gbc);
    add(pnl);
  }

  public void loadAttributePanel(DotElement element, Vector editList) {
    PropertyGroup pg = null;
    String name = null;
    String dflt = null;
    String crnt = null;
    Attribute attr = null;
    String empty = "";

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
    nameList.clear();
    dfltText.setText("");
    crntText.setText("");

    nameText.setText(element.getAppObject().getHandle());

    for(int i = 0; i < attrList.size(); i++) {
      nameList.addItem(((PropertyGroup)attrList.elementAt(i)).getName());
    }

    if(attrList.size() > 0) {
      nameList.select(0);
      Event evt = new Event(nameList,Event.LIST_SELECT,new Integer(0));
      handleEvent(evt);
    }
  }

  public void loadAttributePanel(DotElement elem) {
    loadAttributePanel(elem,null);
  }

  public boolean handleEvent(Event evt) {
    if(evt.target instanceof List) {
      List lst = (List)evt.target;
      if(lst == nameList) {
	switch(evt.id) {
	case Event.LIST_SELECT:
	  Integer slot = (Integer)evt.arg;
	  PropertyGroup pg = (PropertyGroup)attrList.elementAt(slot.intValue());
	  dfltText.setText(pg.getDefaultValue());
	  crntText.setText(pg.getCurrentValue());
	  //crntText.selectAll();
	  crntText.requestFocus();
	  return true;
	}
      }
    } else if(evt.target instanceof Button) {
      if(evt.id == Event.ACTION_EVENT) {
	Button btn = (Button)evt.target;
	if(btn == cancel) {
	  root.hide();
	  return true;
	} else if(btn == apply) {
	  root.hide();
	  return true;
	}
      }
    } else if(evt.target instanceof TextField) {
      TextField tf = (TextField)evt.target;
      if(tf == dfltText) {
      } else if(tf == crntText) {
      } else if(tf == nameText) {
      }
    }
    return super.handleEvent(evt);
  }

  public void setNameText(String text) {
    nameText.setText(text);
  }
}

class PropertyGroup {
  private String name = null;
  private String defaultValue =  null;
  private String currentValue = null;

  public PropertyGroup(String name, String defaultValue, String currentValue) {
    this.name =  name;
    setDefaultValue(defaultValue);
    setCurrentValue(currentValue);
  }

  public String setDefaultValue(String value) {
    String oldValue = defaultValue;
    defaultValue = value;
    return oldValue;
  }

  public String setCurrentValue(String value) {
    String oldValue = currentValue;
    currentValue = value;
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
}
