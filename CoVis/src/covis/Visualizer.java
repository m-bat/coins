/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * The Visualizer class is the main class of Visualizer part
 * 
 */
public class Visualizer extends JPanel {
  /** factory.setAttribute : name */
  static final String JAXP_SCHEMA_LANGUAGE =
  "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  /** factory.setAttribute : value */
  static final String W3C_XML_SCHEMA =
  "http://www.w3.org/2001/XMLSchema"; 

  /** CoVis Environment */
  private VisEnvironment env;
  /** Source program Name of the Unit */
  private String programName;

  /**
   * tabbed pane for a Unit
   */
  private JTabbedPane tabbedPanel;

  /** Data for a Units */
  public ViList  unitDataList;
  /** The current Unit Data */
  public UnitData currentUnitData = null;

  /** The Visualizer list for Units */
  public ViList  visUnitList;
  /** The Visualizer for the Current Unit */
  private VisUnit currentVisUnit = null;


  /**
   * one of constructor.
   * @param xmlFileName XML File name
   * @param visEnv      VisEnviroment
   */
  public Visualizer(String xmlFileName, VisEnvironment visEnv) {
    env = visEnv;
    if (xmlFileName != null) {
      unitDataList = makeData(xmlFileName);
    }
  }
  
  /**
   * Make Visualizer main Panel (each tabbed Panel represents a tag "module")
   * @return JTabbedPane that represents modules
   */
  public JTabbedPane makeVisPanel() {
    tabbedPanel = new JTabbedPane(JTabbedPane.TOP);

    if (unitDataList != null) {
      ViList unitPanelList = new ViList();
      visUnitList = new ViList();

      JPanel  unitPanel;
      UnitData unitData;
      VisUnit visUnit;

      if (unitDataList != null) {
        for (ViList q = unitDataList.first(); !q.atEnd(); q=q.next()) {
          unitData = (UnitData)q.elem();
          unitPanel = new JPanel();
          unitPanelList.add(unitPanel);
          visUnit = new VisUnit(unitData, unitPanel, env);
          visUnitList.add(visUnit);

          if (currentUnitData == null) {
            currentUnitData = unitData;
            currentVisUnit = visUnit;

          }

          tabbedPanel.addTab(unitData.unitName(),unitPanel);
        }
      }
      return tabbedPanel;
    } else {
      return null;
    }
  }


  /**
   * Make Warning Message for not found Source files
   * @return list of the file names that are not found
   */
  public String makeWarningDialog() {
    StringBuffer strs = new StringBuffer();
    UnitData unitData;

    if (unitDataList != null) {
      for (ViList q = unitDataList.first(); !q.atEnd(); q=q.next()) {
        unitData = (UnitData)q.elem();
        if (!unitData.file.canRead())
          strs.append(unitData.unitName()+" : "+unitData.unitSrcName+"\n");
      }
      if (strs.length() > 0) {
        String message = "These Source Files are not found\n\n"
                         + strs.toString();
        return message;
      }
    }
    return null;
  }

  /**
   * make Data from Dom
   * @param xmlFileName Xml File Name
   * @return Dom Data list for Xml File
   **/
  private ViList makeData(String xmlFileName) {
    File xmlFile = new File(xmlFileName);
    if (xmlFile == null) {
      System.out.println(xmlFileName+" cannot create");
      return null;
    } if (!xmlFile.exists()) {
      System.out.println(xmlFileName+" is not found");
      if (env.frame == null) System.out.println("frame is null");
      UIManager.put("OptionPane.okButtonText", "OK");
      JOptionPane.showMessageDialog(env.frame,
                                    xmlFileName+"is not exist",
                                    "Error",
                                    JOptionPane.WARNING_MESSAGE);
      return null;

    } else {
      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute( JAXP_SCHEMA_LANGUAGE , W3C_XML_SCHEMA );

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlFileName));
        Element root = doc.getDocumentElement();

        //System.out.println("Root Tag = "+ root.getTagName());

        programName = new String(root.getAttribute(TagName.NAME));
        
        //System.out.println("VisDriver : "+programName);

        NodeList list = root.getElementsByTagName(TagName.MODULE);
        ViList unitList = new ViList();

        for (int i = 0 ; i<list.getLength() ; ++i) {
          /** get each module data */
          Element element = (Element)list.item(i);
          unitList.add(new UnitData(element, env));
        }
      
        return unitList;

      } catch (ParserConfigurationException pe) {
        System.err.println("Fail to create DocumentBuilder: "+pe.getMessage());
        return null;
      } catch (SAXException se) {
        System.err.println("Parse failed(sax): "+se.getMessage());
        return null;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  }

  /**
   * get the program name (attribute of tag "program")
   *@return program name
   */
  public String getProgramName() {
    if (programName != null)
      return programName;
    else
      return null;
  }
}

