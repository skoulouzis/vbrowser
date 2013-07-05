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

package nl.esciencecenter.vlet.gui.vbrowser;

import java.awt.Dimension;
import java.awt.Point;

import nl.esciencecenter.ptk.GlobalProperties;
import nl.esciencecenter.ptk.util.logging.ClassLogger;
import nl.esciencecenter.vbrowser.vrs.exceptions.VrsException;
import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
import nl.esciencecenter.vlet.gui.BrowserFactory;
import nl.esciencecenter.vlet.gui.GuiSettings;
import nl.esciencecenter.vlet.gui.UIGlobal;
import nl.esciencecenter.vlet.gui.UILogger;
import nl.esciencecenter.vlet.gui.dialog.ExceptionForm;

public class VBrowserFactory implements BrowserFactory
{
    private static VBrowserFactory instance=null; 
    
    public static VBrowserFactory getInstance()
    {
        if (instance==null)
            instance=new VBrowserFactory();
        
        return instance; 
    }
    
    // ========================================================================
    //
    // ========================================================================
    
    protected VBrowserFactory()
    {
        //singleton!
    }
    
    @Override
    public BrowserController createBrowser()
    {
        return createBrowser((VRL)null); 
    }
    
    // @Override
    public BrowserController createBrowser(String str) throws VrsException
    {
        return createBrowser(new VRL(str));
    }
    
    @Override
    public BrowserController createBrowser(VRL vrl)
    {
        VBrowser vb = new VBrowser(this);

        Point p=GuiSettings.getScreenCenter();
        Dimension size=vb.getSize(); 

        vb.setLocation(p.x-size.width/2,p.y-size.height/2);

        vb.setVisible(true);

        BrowserController bc = vb.getBrowserController();

        VRL rootLoc=null; 

        try
        {
            rootLoc = UIGlobal.getProxyVRS().getVirtualRootLocation();
        }
        catch (VrsException e)
        {
            handle(e); 
        }  

        if (vrl == null)
        {
            vrl = rootLoc; 
        }

        // show it */

        bc.messagePrintln("New browser for:" + vrl);

        // start the populate in a different thread to finish the major task
        // event ask quickly as possible ! 

        bc.asyncOpenLocation(vrl);

        return bc; 
    }


    private void handle(VrsException e)
    {
        ExceptionForm.show(e);
        UILogger.logException(this,ClassLogger.ERROR,e,"Exception:%s\n",e); 
    }

}
