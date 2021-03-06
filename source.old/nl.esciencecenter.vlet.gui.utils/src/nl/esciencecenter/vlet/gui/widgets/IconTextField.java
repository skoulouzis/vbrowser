/*
 * Copyright 2006-2010 Virtual Laboratory for e-Science (www.vl-e.nl)
 * Copyright 2012-2013 Netherlands eScience Center.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at the following location:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * For the full license, see: LICENSE.txt (located in the root folder of this distribution).
 * ---
 */
// source:

package nl.esciencecenter.vlet.gui.widgets;

/*
 * Copyright 2006-2010 The Virtual Laboratory for e-Science (VL-e) 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").  
 * You may not use this file except in compliance with the License. 
 * For details, see the LICENCE.txt file location in the root directory of this 
 * distribution or obtain the Apache Licence at the following location: 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * See: http://www.vl-e.nl/ 
 * See: LICENCE.txt (located in the root folder of this distribution). 
 * ---
 * $Id: IconTextField.java,v 1.1 2013/01/22 15:42:16 piter Exp $  
 * $Date: 2013/01/22 15:42:16 $
 */ 
// source: 


import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.esciencecenter.vlet.gui.dnd.ComponentDragGestureListener;
import nl.esciencecenter.vlet.gui.dnd.VTransferHandler;

public class IconTextField extends JPanel implements ActionListener 
{
    private static final long serialVersionUID = -3502306954828479242L;
    private AutoCompleteTextField textField;
    private JLabel iconLabel;
    private ActionListener textFieldListener;
    private String comboBoxEditedCommand;
    private String comboBoxUpdateSelectionCommand;

    public IconTextField()
    {
    	super(); 
        initGUI(); 
        initDnD();  
    }
    
    private void initGUI() 
    {
        {
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }
        {
            iconLabel = new JLabel();
            this.add(iconLabel);
        }
        {
            textField = new AutoCompleteTextField();
            this.add(textField);
            textField.setText("TextField");
            textField.setLocation(16,0); 
        }
        
        // move border from textfield to panel: 
        this.setBackground(textField.getBackground()); 
        this.setBorder(textField.getBorder()); 
        textField.setBorder(null); 
        
    }
    
    private void initDnD()
    {
        // One For All: Transfer Handler: 
        setTransferHandler(VTransferHandler.getDefault());
            
        // reuse draglistener from iconsPanel:
        DragSource dragSource=DragSource.getDefaultDragSource();
        DragGestureListener dragListener = new ComponentDragGestureListener(this);
         
        dragSource.createDefaultDragGestureRecognizer(
                    this, 
                    DnDConstants.ACTION_COPY_OR_MOVE, 
                    dragListener );
    }
    
    public void setText(String txt)
    {
        this.textField.setText(txt); 
    }
    
    public void setIcon(Icon icon)
    {
        iconLabel.setIcon(icon);
        this.revalidate();
        this.repaint(); 
    }
    
    public void setComboActionCommand(String str)
    {
        this.textField.setActionCommand(str); 
    }
    
    public String getText()
    {
        return this.textField.getText(); 
    }


    public void setURI(java.net.URI uri)
    {
        this.setText(uri.toString());
    }
 
    public void setTextActionListener(ActionListener listener)
    {
        // wrap textfield listener: 
        this.textFieldListener=listener; 
        this.textField.removeActionListener(this);
        this.textField.addActionListener(this);
    }
   
    public void setComboEditedCommand(String str)
    {
        this.comboBoxEditedCommand=str;   
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd=e.getActionCommand();
        
        if (cmd.equals(AutoCompleteTextField.COMBOBOXEDITED)) 
            cmd=this.comboBoxEditedCommand;
        
        if (cmd.equals(AutoCompleteTextField.UPDATESELECTION))  
            cmd=this.comboBoxUpdateSelectionCommand;  
            
        if (cmd==null)
            return; // filter out combo command.  
        
        ActionEvent wrapEvent=new ActionEvent(this,
                e.getID(),
                cmd,
                e.getWhen(),
                e.getModifiers());
        
        this.textFieldListener.actionPerformed(wrapEvent); 
    }
}
