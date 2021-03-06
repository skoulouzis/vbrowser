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

package nl.esciencecenter.vbrowser.vrs.xenon;


import nl.esciencecenter.ptk.data.StringList;
import nl.esciencecenter.ptk.task.ITaskMonitor;
import nl.esciencecenter.vbrowser.vrs.data.Attribute;
import nl.esciencecenter.vbrowser.vrs.exceptions.VrsException;
import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
import nl.esciencecenter.vlet.exception.ResourceAlreadyExistsException;
import nl.esciencecenter.vlet.vrs.vfs.VDir;
import nl.esciencecenter.vlet.vrs.vfs.VFSNode;
import nl.esciencecenter.xenon.XenonException;
import nl.esciencecenter.xenon.files.FileAttributes;
import nl.esciencecenter.xenon.files.Path;

/**
 * Minimal implementation of the VDir class. 
 */
public class XenonDir extends VDir
{
	private FileAttributes fileAttrs;
    private Path octoPath;

    public XenonDir(XenonVFS vfs, FileAttributes attrs, Path path) throws VrsException
	{
		super(vfs, vfs.createVRL(path));
		this.fileAttrs=attrs;
		this.octoPath=path;
	}
	
    /** 
     * Get file attributes, if file does not exists
     * @param update
     * @return
     * @throws VrsException 
     */
    public FileAttributes getAttrs(boolean update) throws VrsException
    {
        try
        {
            if ((fileAttrs==null) || (update==true))
            {
                fileAttrs=getXenonClient().getFileAttributes(octoPath);
            }
            return fileAttrs; 
        }
        catch (XenonException e)
        {
            // Check for File Not Found Here !
            throw new VrsException(e.getMessage(),e); 
        } 
    }
    	
	@Override
	public boolean create(boolean force) throws VrsException
	{
	    boolean exists=exists(); 
	        
	    if (exists)
	    {
	        if ((force==true) && (exists))
	        {
	            return true; //already exist. Ok. 
	        }
	        else
	        {
	            // exist, but force==false may not ignore existing. 
	            throw new ResourceAlreadyExistsException("Directory already exists:"+this); 
	        }
	    }
		try
        {
            this.getXenonClient().mkdir(octoPath);
        }
        catch (XenonException e)
        {
            throw new VrsException(e.getMessage(),e); 
        } 
		return true; // no exception -> true; 
	}
	
	@Override
	public boolean exists() throws VrsException
	{
	    try
        {
            // if file attributes are already fetched the path exists, now check attributes. 
            if (this.fileAttrs!=null)
                return fileAttrs.isDirectory();
            else
            {
                // call exists, do not fetch file attributes from a non existing file
                // as this might throw an error.  
                return this.getXenonClient().exists(octoPath); 
            }
        }
        catch (XenonException e)
        {
            throw new VrsException(e.getMessage(),e); 
        }
	}
	
	@Override
	public VFSNode[] list() throws VrsException 
	{
	    //return this.getFS().listNodes(octoPath); 
        return getFileSystem().listNodesAndAttrs(octoPath); 
	}
	
	public XenonVFS getFileSystem()
	{
	    // Downcast from VFileSystem interface to actual FileSystem object. 
	    return (XenonVFS)super.getFileSystem(); 
	}
	
	public VRL rename(String newName, boolean renameFullPath)
            throws VrsException
    {
        VRL vrl=getFileSystem().rename(octoPath,true,newName,renameFullPath);
        this.fileAttrs=null; // clear cached attributes!
        return vrl; 
    }

    public boolean delete(boolean recurse) throws VrsException
    {
        if (recurse)
        {
            // my recursive delete
            //DebuggingMonitor monitor=new DebuggingMonitor("Deleting Octopus Directory:" + this.getPath(),1);
            ITaskMonitor monitor = getVRSContext().getTaskWatcher().getCurrentThreadTaskMonitor("Deleting Octopus Directory:" + this.getPath(), 1);
            getTransferManager().recursiveDeleteDirContents(monitor, this,true); 
        }
        
        // delete single empty directory:
        try
        {
            this.getXenonClient().rmdir(octoPath);
            // clear attributes to indicate non existing dir! 
            this.fileAttrs=null; 
            return true; 
        }
        catch (XenonException e)
        {
            throw new VrsException(e.getMessage(),e);  
        } 
    }
    
	// ===
    // Attributes 
    // ===
    public String[] getAttributeNames()
    {
        StringList list=new StringList(super.getAttributeNames());
        list.add("octoDir"); 
        return list.toArray(); 
    }
    
    public Attribute getAttribute(String name) throws VrsException
    {
        if ("octoDir".equals(name))
            return new Attribute (name,true); 
        else
            return super.getAttribute(name); 
    }
    
	@Override
	public long getModificationTime() throws VrsException
	{
	    return getFileSystem().getModificationTime(getAttrs(false),System.currentTimeMillis());
	}
	
	@Override
	public String getPermissionsString() throws VrsException
	{
	    return getFileSystem().createPermissionsString(getAttrs(false),true); 
    }
	   
	@Override
	public boolean isReadable() throws VrsException 
	{
	    return getFileSystem().isReadable(getAttrs(false),true);
	}

	@Override
	public boolean isWritable() throws VrsException
	{
		return this.getFileSystem().isWritable(getAttrs(false),false); 
	}

	public long getNrOfNodes() throws VrsException
	{
		// count number of nodes. Faster implementation is recommended. 
		VFSNode[] files = this.list();
		
		if (files==null)
			return 0; 
		
		return files.length; 
	}

	// ===
	// Protected 
	// === 
    
    protected XenonClient getXenonClient()
    {
        return this.getFileSystem().xenonClient; 
    }
    
    public boolean isSymbolicLink() throws VrsException
    {
        try
        { 
            return this.getAttrs(false).isSymbolicLink();
        }
        catch (Throwable e)
        {
            throw new VrsException(e.getMessage(),e); 
        }
    }
    
    public boolean isHidden() throws VrsException
    {
        try
        {
            return this.getAttrs(false).isHidden(); 
        }
        catch (Throwable e)
        {
            throw new VrsException(e.getMessage(),e); 
        }

    }
   
    
}
