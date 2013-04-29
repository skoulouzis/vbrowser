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

package nl.esciencecenter.vbrowser.vrs.octopus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.esciencecenter.octopus.exceptions.AttributeNotSupportedException;
import nl.esciencecenter.octopus.exceptions.OctopusIOException;
import nl.esciencecenter.octopus.files.AbsolutePath;
import nl.esciencecenter.octopus.files.FileAttributes;
import nl.esciencecenter.ptk.data.StringList;
import nl.esciencecenter.vbrowser.vrs.exceptions.VrsException;
import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
import nl.nlesc.vlet.exception.ResourceAlreadyExistsException;
import nl.nlesc.vlet.vrs.data.VAttribute;
import nl.nlesc.vlet.vrs.vfs.VFSTransfer;
import nl.nlesc.vlet.vrs.vfs.VFile;

/** 
 * 
 */
public class OctopusFile extends VFile
{
    private FileAttributes fileAttrs;
    private AbsolutePath octoPath;

    public OctopusFile(OctopusFS octopusFS, FileAttributes attrs, AbsolutePath path)
    {
       super(octopusFS,octopusFS.createVRL(path));
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
                fileAttrs=getOctoClient().getFileAttributes(octoPath);
            }
            return fileAttrs; 
        }
        catch (OctopusIOException e)
        {
            // Check for File Not Found Here !
            throw new VrsException(e.getMessage(),e); 
        } 
    }
    
    public boolean sync() throws VrsException
    {
        // Caching: do not sync non-existing files 
        if (this.fileAttrs==null) 
            if (exists()==false)
                return true; 
        
        getAttrs(true); 
        return (this.fileAttrs!=null); 
    }
    
    public boolean create(boolean ignoreExisting) throws VrsException
	{
        // existing attributes -> file exist. 
        if (exists()==true) 
        {
            if (ignoreExisting)
            {
                return true; // existing file. 
            }
            else
            {
                throw new ResourceAlreadyExistsException("File already exists:"+this); 
            }
        }
            
		try
        {
	        // Path is immutable, update it here ? 
            AbsolutePath newPath = this.getOctoClient().createFile(octoPath);
            return true;
        }
        catch (OctopusIOException e)
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
        list.add("octoFile"); 
        return list.toArray(); 
    }
    
    public VAttribute getAttribute(String name) throws VrsException
    {
        if ("octoFile".equals(name))
            return new VAttribute (name,true); 
        else
            return super.getAttribute(name); 
    }
    
	@Override
	public long getLength() throws IOException 
	{
	    try
        {
            return getFS().getLength(getAttrs(false),-1);
        }
        catch (VrsException e)
        {
            throw new IOException(e.getMessage(),e); 
        } 
	}

	@Override
	public long getModificationTime() throws VrsException
	{
	    return getFS().getModificationTime(getAttrs(false),System.currentTimeMillis());
	}

	@Override
	public boolean isReadable() throws VrsException 
	{
	    try
        {
            return getAttrs(false).isReadable();
        }
        catch (AttributeNotSupportedException e)
        {
            throw new VrsException(e.getMessage(),e); 
        }
	}

	@Override
	public boolean isWritable() throws VrsException
	{
	    return getFS().isWritable(getAttrs(false),false); 
	}

	public InputStream createInputStream() throws IOException
	{
		return this.getOctoClient().createInputStream(octoPath); 
	}

	public OutputStream createOutputStream() throws IOException 
	{
	    return this.getOctoClient().createOutputStream(octoPath,false); 
	}

	public VRL rename(String newName, boolean renameFullPath)
		throws VrsException
	{
	    VRL vrl=getFS().rename(octoPath,false,newName,renameFullPath);
	    this.fileAttrs=null; // clear cached attributes!
	    return vrl; 
	}

	public boolean delete() throws VrsException
	{
		try
        {
            boolean result = this.getOctoClient().deleteFile(octoPath,true);
            // clear attributes to indicate non existinf file! 
            this.fileAttrs=null; 
            return result; 
        }
        catch (OctopusIOException e)
        {
            throw new VrsException(e.getMessage(),e); 
        } 
	}		

	@Override
	public boolean exists() throws VrsException
	{
	    try
        {
	        // Caching: if file attributes are already fetched, the file exists. 
	        if (this.fileAttrs!=null)
	        {
	            return fileAttrs.isRegularFile();
	        }
	        else
	        {
	            // call exists, do not fetch file attributes from a non existing file
	            // as this might throw an error.  
	            return this.getOctoClient().exists(octoPath); 
	        }
        }
	    catch (AttributeNotSupportedException e)
	    {
	        throw new VrsException(e.getMessage(),e); 
	    }
        catch (OctopusIOException e)
        {
            throw new VrsException(e.getMessage(),e); 
        }
	}

	public String getPermissionsString() throws VrsException
    {
	    return getFS().createPermissionsString(getAttrs(false),false); 
    }
        
	// === 
	// Protected implementation 
	// === 
	
    protected void uploadFrom(VFSTransfer transferInfo, VFile localSource) throws VrsException 
    {
        // localSource is a file on the local filesystem. 
        super.uploadFrom(transferInfo,localSource);          
    }

    protected void downloadTo(VFSTransfer transfer,VFile targetLocalFile)
    	throws VrsException
    {
        // copy contents into local file:
        super.downloadTo(transfer, targetLocalFile); 
    }
    
    // explicit downcast: 
    protected OctopusFS getFS()
    {
    	// downcast from VFileSystem interface to actual (Skeleton) FileSystem object. 
    	return ((OctopusFS)this.getFileSystem()); 
    }
    
    protected OctopusClient getOctoClient()
    {
        return this.getFS().octoClient; 
    }

}
