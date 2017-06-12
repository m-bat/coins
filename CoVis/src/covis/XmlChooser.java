/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import java.io.File;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.UIManager;

/**
 * The XmlChooser class popup the dialog for Xml File Loader
 */
public class XmlChooser  {
  /** FileChooser dialog */
  JFileChooser fc;
  /** condition returned by FileChooser */
  int returnVal;

  /**
   *  Constructor<br>
   *  Create poppup menu to Choose XML File
   *
   *  @param f  Top Directory for Chooser
   */
  public XmlChooser(File f) {
    UIManager.put("FileChooser.openButtonText", "Open");
    UIManager.put("FileChooser.cancelButtonText", "Cancel");
    //UIManager.put("FileChooser.saveButtonText", "Save");
    UIManager.put("FileChooser.filesOfTypeLabelText", "Files of Type:");
    UIManager.put("FileChooser.fileNameLabelText", "File Name:");
    UIManager.put("FileChooser.lookInLabelText", "Look In:");

    UIManager.put("FileChooser.acceptAllFileFilterText", "All Files");
    UIManager.put("FileChooser.cancelButtonText", "Cancel");
    UIManager.put("FileChooser.cancelButtonToolTipText", "exit FileChooser Dialog");
    UIManager.put("FileChooser.directoryDescriptionText", "Directory");
    UIManager.put("FileChooser.directoryOpenButtonText", "Open");
    UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Open the Directory");
    UIManager.put("FileChooser.fileDescriptionText", "Application file");
    UIManager.put("FileChooser.helpButtonText", "Help");
    UIManager.put("FileChooser.helpButtonToolTipText", "help of FileChooser");
    UIManager.put("FileChooser.newFolderErrorText", "Failed to make new folder");
    UIManager.put("FileChooser.openButtonText", "Open");
    UIManager.put("FileChooser.openButtonToolTipText", "Open the File");
    UIManager.put("FileChooser.openDialogTitleText", "Open");
    UIManager.put("FileChooser.saveButtonText", "Save");
    UIManager.put("FileChooser.saveButtonToolTipText", "Save the File");
    UIManager.put("FileChooser.saveDialogTitleText", "Save");
    UIManager.put("FileChooser.updateButtonText", "Update");
    UIManager.put("FileChooser.updateButtonToolTipText", "Updete the table of Directory");

    fc = new JFileChooser(f);

    fc.setAcceptAllFileFilterUsed(false);
    FileFilter ff = new Filter("xml");
    fc.setFileFilter(ff);
  }

  /**
   * Pop-up the Chooser Dialog
   *
   * @param parent the parent component of this dialog
   * @return returnVal the status of this dialog
   */
  public int showDialog(Component parent) {
    returnVal = fc.showOpenDialog(parent);
    return returnVal;
  }

  /**
   * Get the selected file
   *
   * @return selected Xml File
   */
  public File getSelectedXml() {
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      return file;
    } else {
      return null;
    }
  }

  /********************************************************************/
  /**
   * The Filter subclass is the filter for choose Xml File
   */
  class Filter extends FileFilter {
    String target;

    /** Constructor set the Filter
     * @param ext extension for XML file  */
    public Filter(String ext) {
      target = ext;
    }

    /** Check file type
     * @param f  File
     * @return return true if File is directory
     */
    public boolean accept(File f) {
      if (f.isDirectory()) {
        return true;
      }

      String extension = getExtension(f);
      if (extension != null) {
        if (extension.equals(target)) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    /**
     * Get selected file
     * @return  target
     */
    public String getDescription() {
      return target;
    }

    /**
     * Get file type
     * @param f  File
     * @return String of extention part
     */
    private String getExtension(File f) {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 && i < s.length() -1) {
        ext = s.substring(i+1).toLowerCase();
      }
      return ext;
    }
  }

}
