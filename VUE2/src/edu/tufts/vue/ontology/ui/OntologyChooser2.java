
/*
 *
 * * <p><b>License and Copyright: </b>The contents of this file are subject to the
 * Mozilla Public License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.mozilla.org/MPL">http://www.mozilla.org/MPL/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>The entire file consists of original code.  Copyright &copy; 2003-2007
 * Tufts University. All rights reserved.</p>
 *
 *
 */

package edu.tufts.vue.ontology.ui;


import edu.tufts.vue.ontology.OntManager;
import edu.tufts.vue.ontology.OntologyType;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tufts.vue.VueUtil;


/*
 * OntologyChooser2.java
 *
 * Created on June 4, 2007, 2:41 PM
 *
 * @author dhelle01
 */
public class OntologyChooser2 extends javax.swing.JDialog implements java.awt.event.ActionListener {
    
    public static final int FILE = 0;
    public static final int URL = 1;
    
    public static final int ONT_CHOOSER_WIDTH = 500;
    public static final int ONT_CHOOSER_HEIGHT = 420;
    public static final int ONT_CHOOSER_X_LOCATION = 300;
    public static final int ONT_CHOOSER_Y_LOCATION = 300;
    
    public static final String ontTitle = "<html><b>Ontology</b><html>";
    //public static final String cssTitle = "<html><b>Ontology Style Sheet*</b></html>";
    public static final String cssTitle = "<html><b>Ontology Style Sheet</b></html>";
    public static final String browseFileMessage = "Select file:";
    public static final String cssFileMessage = "Select file:";
    public static final String typeURLMessage = "Type in a URL:";
    public static final String styleSheetMessage = "(*can be added later)";
    
    private static String lastDirectory;
   // private static String lastCSSDirectory;
    
    private int ontologySelectionType;
    private int cssSelectionType;
    
    private JPanel mainPanel = null;
    private JPanel ontBrowsePanel = null;
    private JPanel cssBrowsePanel = null;
    private JPanel ontURLPanel = null;
    private JPanel cssURLPanel = null;
    private JPanel buttonPanel = null;
    private JButton cancelButton = null;
    private JButton nextButton = null;
    private JButton cssBrowseButton = null;
    private JButton ontBrowseButton = null;
    private JLabel ontologyLabel = null;
    private JLabel ontLabel = null;
    private JLabel cssLabel = null;
    private JTextField ontBrowseFileField = null;
    private JTextField cssBrowseFileField = null;
    private JTextField typeOntURLField = null;
    private JTextField typeCssURLField = null;
    private JLabel styleSheetMessageLabel = null;
    
    private OntologyBrowser browser = null;
    
    private File ontFile;
    private File cssFile;
    private String ontURLText;
    private String cssURLText;
    private URL ontURL;
    private URL cssURL;
    
    private JComboBox ontChoice;
    private JComboBox cssChoice;
    
    private GridBagLayout gridBag;
    private GridBagConstraints c;
    
    private JLabel lineLabel;
    private JLabel lineLabel2;
    
    private JPanel cssChoicePanel;
    private JPanel ontChoicePanel;
    
    public OntologyChooser2(java.awt.Frame owner,String title,OntologyBrowser browser) 
    {
        super(owner,title);
        this.browser = browser;
        setLocation(ONT_CHOOSER_X_LOCATION,ONT_CHOOSER_Y_LOCATION);
        setModal(true);
        setSize(ONT_CHOOSER_WIDTH,ONT_CHOOSER_HEIGHT);
        
        String[] choices = {"on the web","in a local folder"};
        
        cssChoice = new JComboBox(choices);
        ontChoice = new JComboBox(choices);
        
        typeOntURLField = new JTextField(20);
        typeCssURLField = new JTextField(20);
        
        cancelButton = new JButton("Cancel");
        nextButton = new JButton("Add");
        ontBrowseButton = new JButton("Browse");
        cssBrowseButton = new JButton("Browse");
        
        cssChoice.addActionListener(this);
        ontChoice.addActionListener(this);
        cancelButton.addActionListener(this);
        nextButton.addActionListener(this);
        ontBrowseButton.addActionListener(this);
        cssBrowseButton.addActionListener(this);
        
        setUpPanels();
        
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        getContentPane().add(mainPanel);
        
        setVisible(true);
    }
    
    public void setUpPanels()
    {
        ontLabel = new JLabel(ontTitle);
        cssLabel = new JLabel(cssTitle);
        JLabel info = new JLabel(tufts.vue.VueResources.getIcon("helpIcon.raw"));
        info.setToolTipText("Add an Ontology help here - TBD");
        tufts.vue.PolygonIcon lineIcon = new tufts.vue.PolygonIcon(new java.awt.Color(153,153,153));
        lineIcon.setIconWidth(ONT_CHOOSER_WIDTH-40);
        lineIcon.setIconHeight(1);
        /*JLabel*/ lineLabel = new JLabel(lineIcon);
        tufts.vue.PolygonIcon lineIcon2 = new tufts.vue.PolygonIcon(new java.awt.Color(153,153,153));
        lineIcon2.setIconWidth(ONT_CHOOSER_WIDTH-40);
        lineIcon2.setIconHeight(1);
        lineLabel2 = new JLabel(lineIcon2);
        
        mainPanel = new JPanel();
        gridBag = new GridBagLayout();
        c = new GridBagConstraints();
        mainPanel.setLayout(gridBag);
        
        setUpOntBrowsePanel();
        setUpCssBrowsePanel();
        setUpOntURLPanel();
        setUpCssURLPanel();
        setUpButtonPanel();
        
        setUpOntChoicePanel();
        setUpCssChoicePanel();
        
        c.insets = new Insets(10,10,10,10);
        gridBag.setConstraints(ontLabel,c);
        mainPanel.add(ontLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.EAST;
        gridBag.setConstraints(info,c);
        mainPanel.add(info);
        
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(5,5,5,5);
        gridBag.setConstraints(ontChoicePanel,c);
        mainPanel.add(ontChoicePanel);
        c.insets = new Insets(10,10,10,10);

        gridBag.setConstraints(ontURLPanel,c);
        mainPanel.add(ontURLPanel);
        
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10,10,10,10);
        gridBag.setConstraints(lineLabel,c);
        mainPanel.add(lineLabel);
        
        
        gridBag.setConstraints(cssLabel,c);
        mainPanel.add(cssLabel);
        c.fill = GridBagConstraints.NONE;
        
        gridBag.setConstraints(cssChoicePanel,c);
        mainPanel.add(cssChoicePanel);
        
        gridBag.setConstraints(cssURLPanel,c);
        mainPanel.add(cssURLPanel);
        
        //styleSheetMessageLabel.setText(styleSheetMessage);
        //gridBag.setConstraints(styleSheetMessageLabel,c);
        //mainPanel.add(styleSheetMessageLabel);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(lineLabel2,c);
        mainPanel.add(lineLabel2);
        
        c.anchor = GridBagConstraints.WEST;
        gridBag.setConstraints(buttonPanel,c);
        mainPanel.add(buttonPanel);
    }
    
    public void setUpOntChoicePanel()
    {
        ontChoicePanel = new JPanel();
        GridBagLayout ontChoiceGrid = new GridBagLayout();
        GridBagConstraints ontChoiceConstraints = new GridBagConstraints();
        //c.gridwidth = 2;
        JLabel fileLabel = new JLabel("File location: ");
        c.anchor = GridBagConstraints.WEST;
        //ontChoiceConstraints.insets = new Insets(10,30,10,10);
        ontChoiceGrid.setConstraints(fileLabel,ontChoiceConstraints);
        ontChoicePanel.add(fileLabel);
        ontChoiceConstraints.gridwidth = GridBagConstraints.REMAINDER;
        ontChoiceGrid.setConstraints(ontChoice,ontChoiceConstraints);
        ontChoicePanel.add(ontChoice);
    }
    
    public void setUpCssChoicePanel()
    {
        cssChoicePanel = new JPanel();
        //c.gridwidth = 2;
        GridBagLayout cssChoiceGrid = new GridBagLayout();
        GridBagConstraints cssChoiceConstraints = new GridBagConstraints();
        cssChoicePanel.setLayout(cssChoiceGrid);
        cssChoiceConstraints.anchor = GridBagConstraints.WEST;
        JLabel fileLabel2 = new JLabel("File location: ");
        //cssChoiceConstraints.insets = new Insets(10,30,10,10);
        cssChoiceGrid.setConstraints(fileLabel2,cssChoiceConstraints);
        cssChoicePanel.add(fileLabel2);
        cssChoiceConstraints.weightx = 1.0;
        cssChoiceConstraints.gridwidth = GridBagConstraints.REMAINDER;
        cssChoiceGrid.setConstraints(cssChoice,cssChoiceConstraints);
        cssChoicePanel.add(cssChoice);
    }
    
    public void setUpOntBrowsePanel()
    {
        
        
       JLabel ontBrowseLabel = new JLabel(browseFileMessage);
       ontBrowseFileField = new JTextField(20);
       ontBrowsePanel = new JPanel(); 
       GridBagLayout ontBrowseGrid = new GridBagLayout();
       GridBagConstraints ontBrowseConstraints = new GridBagConstraints();
       ontBrowsePanel.setLayout(ontBrowseGrid);
       
       ontBrowseConstraints.insets = new Insets(5,5,5,5);
       ontBrowseGrid.setConstraints(ontBrowseLabel,ontBrowseConstraints);
       ontBrowsePanel.add(ontBrowseLabel);
       ontBrowseConstraints.weightx = 0.0;
       ontBrowseConstraints.anchor = GridBagConstraints.WEST;
       //ontBrowseConstraints.fill = GridBagConstraints.HORIZONTAL;
       ontBrowseGrid.setConstraints(ontBrowseFileField,ontBrowseConstraints);
       ontBrowsePanel.add(ontBrowseFileField);
       ontBrowseConstraints.weightx = 1.0;
       ontBrowseConstraints.gridwidth = GridBagConstraints.REMAINDER;
       ontBrowseGrid.setConstraints(ontBrowseButton,ontBrowseConstraints);
       ontBrowsePanel.add(ontBrowseButton);
               
    }
    
    public void setUpCssBrowsePanel()
    {
       JLabel cssBrowseLabel = new JLabel(cssFileMessage);
       cssBrowseFileField = new JTextField(20);
       cssBrowsePanel = new JPanel(); 
       GridBagLayout cssBrowseGrid = new GridBagLayout();
       GridBagConstraints cssBrowseConstraints = new GridBagConstraints();
       cssBrowsePanel.setLayout(cssBrowseGrid);
       
       cssBrowseConstraints.insets = new Insets(5,5,5,5);
       cssBrowseGrid.setConstraints(cssBrowseLabel,cssBrowseConstraints);
       cssBrowsePanel.add(cssBrowseLabel);
       cssBrowseConstraints.weightx = 0.0;
       cssBrowseConstraints.anchor = GridBagConstraints.WEST;
       //cssBrowseConstraints.fill = GridBagConstraints.HORIZONTAL;
       cssBrowseGrid.setConstraints(cssBrowseFileField,cssBrowseConstraints);
       cssBrowsePanel.add(cssBrowseFileField);
       cssBrowseConstraints.weightx = 1.0;
       cssBrowseConstraints.gridwidth = GridBagConstraints.REMAINDER;
       cssBrowseGrid.setConstraints(cssBrowseButton,cssBrowseConstraints);
       cssBrowsePanel.add(cssBrowseButton);
    }
    
    public void setUpOntURLPanel()
    {
        ontURLPanel = new JPanel()
        {
            public java.awt.Dimension getPreferredSize()
            {
                return ontBrowsePanel.getPreferredSize();
            }
        };
        GridBagLayout ontURLGrid = new GridBagLayout();
        GridBagConstraints ontURLConstraints = new GridBagConstraints();
        ontURLPanel.setLayout(ontURLGrid);
        ontURLConstraints.insets = new Insets(5,5,5,5);
        ontURLConstraints.anchor = GridBagConstraints.WEST;
        ontURLConstraints.weightx = 0.0;
        JLabel typeLabel = new JLabel("Type in a URL: ");
        ontURLGrid.setConstraints(typeLabel,ontURLConstraints);
        ontURLPanel.add(typeLabel,ontURLConstraints);
        ontURLConstraints.gridwidth = GridBagConstraints.REMAINDER;
        ontURLConstraints.weightx = 1.0;
        ontURLGrid.setConstraints(typeOntURLField,ontURLConstraints);
        ontURLPanel.add(typeOntURLField); 
    }
    
    public void setUpCssURLPanel()
    {
        cssURLPanel = new JPanel()
        {
           public java.awt.Dimension getPreferredSize()
           {
               return cssBrowsePanel.getPreferredSize();
           }
        };
        GridBagLayout cssURLGrid = new GridBagLayout();
        GridBagConstraints cssURLConstraints = new GridBagConstraints();
        cssURLPanel.setLayout(cssURLGrid);
        cssURLConstraints.insets = new Insets(5,5,5,5);
        cssURLConstraints.anchor = GridBagConstraints.WEST;
        cssURLConstraints.weightx = 0.0;
        JLabel typeLabel = new JLabel("Type in a URL: ");
        cssURLGrid.setConstraints(typeLabel,cssURLConstraints);
        cssURLPanel.add(typeLabel,cssURLConstraints);
        cssURLConstraints.weightx = 1.0;
        cssURLConstraints.gridwidth = GridBagConstraints.REMAINDER;
        cssURLGrid.setConstraints(typeCssURLField,cssURLConstraints);
        cssURLPanel.add(typeCssURLField); 
    }
    
    public void setUpButtonPanel()
    {
        styleSheetMessageLabel = new JLabel();
        buttonPanel = new JPanel();
        
        GridBagLayout buttonLayout = new GridBagLayout();
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonPanel.setLayout(buttonLayout);
        
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.weightx = 1.0;
        buttonLayout.setConstraints(styleSheetMessageLabel,buttonConstraints);
        buttonPanel.add(styleSheetMessageLabel);
        buttonConstraints.weightx = 0.0;
        buttonLayout.setConstraints(cancelButton,buttonConstraints);
        buttonPanel.add(cancelButton);
        buttonLayout.setConstraints(nextButton,buttonConstraints);
        buttonPanel.add(nextButton);
    }
    
    public static org.osid.shared.Type getOntType(URL ontURL)
    {
        String type = ontURL.toString().substring(ontURL.toString().lastIndexOf(".")+1);
        if(type.equals("rdfs"))
            return edu.tufts.vue.ontology.OntologyType.RDFS_TYPE;
        else
        if(type.equals("owl"))
            return edu.tufts.vue.ontology.OntologyType.OWL_TYPE;
        else
            return edu.tufts.vue.ontology.OntologyType.OWL_TYPE;
    }
    
    public void reLayoutStartingAtCSS(boolean keep)
    {
            if(cssChoice.getSelectedIndex() == 0)
            {
                if(!keep)
                {
                  mainPanel.remove(buttonPanel);
                  mainPanel.remove(lineLabel2);
                  mainPanel.remove(cssBrowsePanel);
                  invalidate();
                  validate();
                }
                c.gridwidth = GridBagConstraints.REMAINDER;
                //c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1.0;
                //setUpCssURLPanel();
                gridBag.setConstraints(cssURLPanel,c);
                mainPanel.add(cssURLPanel);
                gridBag.setConstraints(lineLabel2,c);
                mainPanel.add(lineLabel2);
                gridBag.setConstraints(buttonPanel,c);
                mainPanel.add(buttonPanel);
                //invalidate();
                validate();
                //repaint();
            }
            if(cssChoice.getSelectedIndex() == 1)
            {
                if(!keep)
                {
                  mainPanel.remove(buttonPanel);
                  mainPanel.remove(lineLabel2);
                  mainPanel.remove(cssURLPanel);
                  invalidate();
                  validate();
                }
                c.gridwidth = GridBagConstraints.REMAINDER;
                //c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1.0;
                gridBag.setConstraints(cssBrowsePanel,c);
                mainPanel.add(cssBrowsePanel);
                gridBag.setConstraints(lineLabel2,c);
                mainPanel.add(lineLabel2);
                gridBag.setConstraints(buttonPanel,c);
                mainPanel.add(buttonPanel);
                //invalidate();
                validate();
                //repaint();
            }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        if(e.getSource() == ontChoice)
        {
            c.anchor = GridBagConstraints.WEST;
            if(ontChoice.getSelectedIndex() == 0)
            {
                mainPanel.remove(buttonPanel);
                mainPanel.remove(lineLabel2);
                mainPanel.remove(cssBrowsePanel);
                mainPanel.remove(cssURLPanel);
                mainPanel.remove(cssChoicePanel);
                mainPanel.remove(cssLabel);
                mainPanel.remove(lineLabel);
                mainPanel.remove(ontBrowsePanel);
                
                mainPanel.remove(ontURLPanel);
                
                invalidate();
                validate();
                
                c.gridwidth = GridBagConstraints.REMAINDER;
                //c.weightx = 1.0;
                //c.fill = GridBagConstraints.HORIZONTAL;
                gridBag.setConstraints(ontURLPanel,c);
                mainPanel.add(ontURLPanel);
                gridBag.setConstraints(lineLabel,c);
                mainPanel.add(lineLabel);
                gridBag.setConstraints(cssLabel,c);
                mainPanel.add(cssLabel);
                c.weightx = 1.0;
                
                c.anchor = GridBagConstraints.WEST;
                //cssChoicePanel.setOpaque(true);
                //cssChoicePanel.setBackground(java.awt.Color.BLUE);
                
                gridBag.setConstraints(cssChoicePanel,c);
                mainPanel.add(cssChoicePanel);
                reLayoutStartingAtCSS(true);
                //invalidate();
                validate();
                //repaint();
            }
            if(ontChoice.getSelectedIndex() == 1)
            {
                mainPanel.remove(buttonPanel);
                mainPanel.remove(lineLabel2);
                mainPanel.remove(cssBrowsePanel);
                mainPanel.remove(cssURLPanel);
                mainPanel.remove(cssChoicePanel);
                mainPanel.remove(cssLabel);
                mainPanel.remove(lineLabel);
                mainPanel.remove(ontURLPanel);
                
                mainPanel.remove(ontBrowsePanel);
                
                invalidate();
                validate();
                
                c.gridwidth = GridBagConstraints.REMAINDER;
                c.fill = GridBagConstraints.HORIZONTAL;
                gridBag.setConstraints(ontBrowsePanel,c);
                mainPanel.add(ontBrowsePanel);
                gridBag.setConstraints(lineLabel,c);
                mainPanel.add(lineLabel);
                gridBag.setConstraints(cssLabel,c);
                mainPanel.add(cssLabel);
                c.weightx = 1.0;
                gridBag.setConstraints(cssChoicePanel,c);
                mainPanel.add(cssChoicePanel);
                reLayoutStartingAtCSS(true);
                //invalidate();
                validate();
                //repaint();
            }  
        }
        if(e.getSource() == cssChoice)
        {
            reLayoutStartingAtCSS(false);
            validate();
        }
        if(e.getSource() == nextButton)
        {
             
        
            if(ontChoice.getSelectedIndex() == 0)
            {
                   try
                   {
                       ontURL = new URL(typeOntURLField.getText());
                   }
                   catch(MalformedURLException mue)
                   {
                      System.out.println("OntologyChooser: Improper URL" + mue);// aborting ontology file load");
                      //dispose();
                   }
            }
            
            if(ontChoice.getSelectedIndex() == 1)
            {
                   try
                   {
                        if(ontFile!=null)
                        {
                            ontURL = ontFile.toURL();
                        }
                   }
                   catch(MalformedURLException mue2)
                   {
                          System.out.println("OntologyChooser: File load produced Malformed URL for ontology " + mue2);
                   }
            }
            
            if(cssChoice.getSelectedIndex() == 0)
            {
                   try
                   {
                       cssURL = new URL(typeCssURLField.getText());
                   }
                   catch(MalformedURLException mue)
                   {
                      System.out.println("OntologyChooser: Improper URL for css" + mue); //, aborting ontology file load");
                      //dispose();
                   }
            }
            
            if(ontChoice.getSelectedIndex() == 1)
            {
                   try
                   {
                        if(cssFile!=null)
                        {
                            cssURL = cssFile.toURL();
                        }
                   }
                   catch(MalformedURLException mue2)
                   {
                          System.out.println("OntologyChooser: File load produced Malformed URL for css " + mue2);
                   }
            }

               TypeList list = new TypeList();
                   
               if(ontURL!=null && (cssURL == null))
               {
                      
                       tufts.vue.gui.Widget w = browser.addTypeList(list,edu.tufts.vue.ontology.Ontology. getLabelFromUrl(ontURL.getFile()),ontURL);

                                                                
                       list.loadOntology(ontURL,cssURL,getOntType(ontURL),browser,w);                                          
                                                                
                       browser.getViewer().getList().clearSelection();
                       browser.getViewer().getList().setSelectedIndex(-1);

               }
               if(ontURL != null && cssURL!=null)
               {
                       tufts.vue.gui.Widget w = browser.addTypeList(list,edu.tufts.vue.ontology.Ontology. getLabelFromUrl(ontURL.getFile()),ontURL);
                       list.loadOntology(ontURL,cssURL,getOntType(ontURL),browser,w); 
               }
               setVisible(false);

        }
        if(e.getSource() == cancelButton)
        {
            dispose();
        }
        if(e.getSource() == ontBrowseButton)
        {
            JFileChooser ontChooser = new JFileChooser();
            if(lastDirectory != null)
            { 
              try
              {
                // note: this next line didn't work on windows xp!
                //ontChooser.setSelectedFile(new File(lastDirectory));
                ontChooser.setCurrentDirectory(new File(lastDirectory));
              }
              catch(Exception exc)
              {
                  System.out.println("OntologyChooser2: Exception opening last ontology directory " + exc);
              }
            }
            int success = ontChooser.showOpenDialog(tufts.vue.VUE.getDialogParentAsFrame());
            File ontSelectedFile = null;
            if(success == JFileChooser.APPROVE_OPTION)
            {    
              ontSelectedFile = ontChooser.getSelectedFile();
            }
            if(ontSelectedFile !=null)
            {    
              lastDirectory = ontSelectedFile.getParent();
            }
            if(ontSelectedFile!=null)
            {    
                ontFile = ontSelectedFile;
                ontBrowseFileField.setText(ontSelectedFile.getName());
            }
        }
        if(e.getSource() == cssBrowseButton)
        {
            JFileChooser cssChooser = new JFileChooser();
            if(lastDirectory !=null)
            {
              try
              {
                  // see note above for ontology chooser
                  //cssChooser.setSelectedFile(new File(lastDirectory));
                  cssChooser.setCurrentDirectory(new File(lastDirectory));
              }
              catch(Exception exc)
              {
                  System.out.println("OntologyChooser2: Exception opening last css directory " + exc);
              }
            }
            int success = cssChooser.showOpenDialog(tufts.vue.VUE.getDialogParentAsFrame());
            File cssSelectedFile = null; 
            if(success == JFileChooser.APPROVE_OPTION)
            {
              cssSelectedFile = cssChooser.getSelectedFile();
            }
            if(cssSelectedFile!=null)
            {      
              lastDirectory = cssSelectedFile.getParent();
            }
            if(cssSelectedFile!=null)
            {
                cssFile = cssSelectedFile;
                cssBrowseFileField.setText(cssSelectedFile.getName());
            } 
        }
    }
    
}

