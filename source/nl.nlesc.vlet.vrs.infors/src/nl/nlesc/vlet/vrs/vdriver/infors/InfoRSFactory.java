/*
 * Copyrighted 2012-2013 Netherlands eScience Center.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").  
 * You may not use this file except in compliance with the License. 
 * For details, see the LICENCE.txt file location in the root directory of this 
 * distribution or obtain the Apache License at the following location: 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * For the full license, see: LICENCE.txt (located in the root folder of this distribution). 
 * ---
 */
// source: 

package nl.nlesc.vlet.vrs.vdriver.infors;

import nl.nlesc.vlet.exception.VlException;
import nl.nlesc.vlet.vrs.ServerInfo;
import nl.nlesc.vlet.vrs.VRS;
import nl.nlesc.vlet.vrs.VRSContext;
import nl.nlesc.vlet.vrs.VRSFactory;
import nl.nlesc.vlet.vrs.VResourceSystem;
import nl.nlesc.vlet.vrs.vrl.VRL;

/**
 * nl.uva.vlet.vrs.info.InfoRSFactory; 
 * 
 * @author Piter T. de Boer 
 */
public class InfoRSFactory extends VRSFactory
{
    public static String[] schemes={ VRS.INFO_SCHEME,"grid" } ;

    public static String[] types={ "InfoType" } ;

    @Override
    public void clear()
    {
    }

    @Override
    public String getName()
    {
        return "Info Service"; 
    }
    
    @Override
    public String[] getResourceTypes()
    {
        return types; 
    }

    @Override
    public String[] getSchemeNames()
    {
        return schemes;
    }
   
    public ServerInfo updateServerInfo(VRSContext context,ServerInfo info, VRL loc) throws VlException
    {
        info=super.updateServerInfo(context,info,loc); 
        
        info.setNeedHostname(false); 
        info.setNeedPort(false); 
        info.setSupportURIAtrributes(false); 
        
        return info;  
    }
    
	@Override
	public VResourceSystem createNewResourceSystem(VRSContext context,
			ServerInfo info, VRL location) throws VlException 
	{
		// should be one singleton instance per Context ! 
		return new InfoResourceSystem(context); 
	}
}
