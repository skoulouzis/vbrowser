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

package nl.esciencecenter.vlet.gui.lobo.resfs;

import nl.esciencecenter.vbrowser.vrs.exceptions.VRLSyntaxException;
import nl.esciencecenter.vbrowser.vrs.exceptions.VrsException;
import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
import nl.esciencecenter.vlet.vrs.VNode;
import nl.esciencecenter.vlet.vrs.VRSContext;
import nl.esciencecenter.vlet.vrs.VResourceSystem;

public class ResResourceSystem implements VResourceSystem
{
    private VRSContext vrsContext;
    private VRL vrl; 
    
    public ResResourceSystem(VRSContext context, VRL location)
    {
        this.vrsContext=context;
        this.vrl=location.replacePath("/"); 
    }

    @Override
    public VRL resolve(String path) throws VRLSyntaxException 
    {
        return vrl.uriResolve(path);
    }
    
    @Override
    public VRL getVRL()
    {
        return this.vrl; 
    }
    
    //@Override
    public String getID()
    {
        return "res-resource"; 
    }

    //@Override
    public VNode openLocation(VRL vrl) throws VrsException
    {
        return new ResFile(this,vrl);
    }

    //@Override
    public VRSContext getVRSContext()
    {
        return vrsContext;
    }

    @Override
    public void connect()
    {
    }
    
    @Override
    public void disconnect()
    {
    }

    @Override
    public void dispose()
    {
    } 
     
}
