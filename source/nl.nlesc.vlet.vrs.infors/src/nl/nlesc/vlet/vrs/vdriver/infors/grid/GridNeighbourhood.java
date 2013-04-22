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

package nl.nlesc.vlet.vrs.vdriver.infors.grid;

import nl.esciencecenter.ptk.data.BooleanHolder;
import nl.esciencecenter.ptk.data.StringList;
import nl.esciencecenter.ptk.util.StringUtil;
import nl.nlesc.vlet.VletConfig;
import nl.nlesc.vlet.data.VAttribute;
import nl.nlesc.vlet.exception.VlException;
import nl.nlesc.vlet.vrs.VEditable;
import nl.nlesc.vlet.vrs.VNode;
import nl.nlesc.vlet.vrs.VRSContext;
import nl.nlesc.vlet.vrs.vdriver.infors.CompositeServiceInfoNode;
import nl.nlesc.vlet.vrs.vdriver.infors.InfoConstants;
import nl.nlesc.vlet.vrs.vdriver.infors.net.NetworkNode;
import nl.nlesc.vlet.vrs.vrl.VRL;
import nl.nlesc.vlet.vrs.vrms.ConfigManager;

public class GridNeighbourhood extends CompositeServiceInfoNode<VNode> 
    implements VEditable // editable methods already defined in super class hierarchy  
{
    // === // 
    
    private VOGroupsNode gridServices=null;
    
    // === // 
    
	public GridNeighbourhood(VRSContext context)
	{
		super(context, new VRL(InfoConstants.INFO_SCHEME,null,"/"+InfoConstants.GRID_NEIGHBOURHOOD_TYPE));
		initChilds(); 
	}
	
	private void initChilds()
	{
		//	serverInstances=new ServerInstanceGroup(vrsContext); 
		gridServices=new VOGroupsNode(this,vrsContext);       
		VNode nodes[]=new VNode[1];
	        
		nodes[0]=gridServices; 
		//nodes[1]=serverInstances;
	        
		// update super class internal array! 
		setChilds(nodes);

        this.setEditable(true); 

	} 

	@Override
    public String getName()
    {
        return InfoConstants.GRID_NEIGHBOURHOOD_NAME;  
    }
	
	@Override
	public String getResourceType()
	{
		return InfoConstants.GRID_NEIGHBOURHOOD_TYPE;
	}

	public String getIconURL()
	{
	    return "world-128.png"; 
	}
	
	public String getMimeType(){return null;} 
	

	public String[] getResourceTypes()
	{
	      return new String[] { InfoConstants.NETWORK_INFO}; 
	}

	public String[] getAttributeNames()
    {
        StringList list = new StringList(); 
        list.add(VletConfig.PROP_BDII_HOSTNAME);
        list.add(VletConfig.PROP_BDII_PORT);
        return list.toArray();
    }

    public VAttribute getAttribute(String name) throws VlException
    {
        ConfigManager cmgr = this.vrsContext.getConfigManager(); 
        
        VAttribute attr=null; 
        
        if (name.equals(VletConfig.PROP_BDII_HOSTNAME))
        {
            // As of vlet 1.4 return comma seperated host:port list! 
            attr=new VAttribute(name, cmgr.getBdiiHostInfo());
            attr.setEditable(true); 
        }
        else if (name.equals(VletConfig.PROP_BDII_PORT))
        {
            attr=new VAttribute(name, cmgr.getBdiiServiceURI().getPort());
            attr.setEditable(true);
        }
        
        if (attr!=null)
            return attr;
        
        return super.getAttribute(name);
    }

    public boolean setAttribute(VAttribute attr) throws VlException
    {
        ConfigManager cmgr = this.vrsContext.getConfigManager(); 
        
        String name=attr.getName();
        
        BooleanHolder mustRefresh=new BooleanHolder(false);  
        boolean result=false; 
        
        if (name.equals(VletConfig.PROP_BDII_HOSTNAME))
        {
            cmgr.setAttribute(attr, mustRefresh);
            result=true; 
        }
        else if (name.equals(VletConfig.PROP_BDII_PORT))
        {
            cmgr.setAttribute(attr, mustRefresh);
            result=true; 
        }
        
        if (mustRefresh.value)
            refresh(); 
        
        return result;  
    }
    
    protected void refresh()
    {
        initChilds(); 
        this.fireRefresh();
    }
    
	public VRL getDescriptionLocation() throws VlException
	{
		return null; 
	}

    public VRL createChildVRL(String childName) 
    {
        return new VRL(this.getScheme(),null,null,-1,getPath()+"/"+childName);
        
    }

    public VNode createNode(String type, String name, boolean force)
        throws VlException
    {
        if (StringUtil.equals(type,InfoConstants.NETWORK_INFO))
        {
            return createNetworkNode(name); 
        }
        
        throw new nl.nlesc.vlet.exception.ResourceTypeNotSupportedException("Cannot create resource of type:"+type);
    }

    private NetworkNode createNetworkNode(String name) throws VlException
    {
        if ((name==null) || (name.equals("")))
            name="New Network"; 
        
        VRL childVRL=this.createChildVRL(name);
        NetworkNode node=new NetworkNode(this.getVRSContext(),childVRL);
        // add to internal vector 
        this.addSubNode(node); 
        return node; 
        
    }
}
