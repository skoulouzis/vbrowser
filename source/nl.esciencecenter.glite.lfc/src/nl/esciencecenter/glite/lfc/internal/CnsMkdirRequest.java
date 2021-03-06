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

package nl.esciencecenter.glite.lfc.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import nl.esciencecenter.glite.lfc.IOUtil;
import nl.esciencecenter.glite.lfc.LFCServer;



/**
 * <p>
 * Encapsulates LFC server MKDIR command request. Then receives and returns
 * response. 
 * </p>
 * P.T. de Boer: optimized some code 
 * <p>
 * Sends 12 byte header.
 * </p>
 * 
 * @see CnsMkdirResponse
 */
public class CnsMkdirRequest extends AbstractCnsRequest
{
  
  private int uid;
  private int gid;
  private long cwd; /* PdB: According to comments found in C Code: current HSM working directory */
  private String path;
  private String guid;

  /**
   * Creates request for deleting new LFC entry.
   * 
   * @param path of the deleting entry
   */
  public CnsMkdirRequest( final String path ) 
  {
    this.path = path;
    this.guid = UUID.randomUUID().toString();
    this.uid = 0;
    this.gid = 0;
    this.cwd = 0;
    
  }

  /**
   * <p>Sends prepared request to the output stream and then fetch the response</p>
   * 
   * @param out output stream to which request will be written
   * @param in input stream from which response will be read
   * @return object that encapsulates response
   * @throws IOException in case of any I/O problem
   * @see CnsLinkStatResponse
   */
  public CnsVoidResponse sendTo( final DataOutputStream out, final DataInputStream in )
    throws IOException  
    {
    
    LFCServer.staticLogIOMessage( "sending MKDIR-G: " + this.path  ); 
    LFCServer.staticLogIOMessage( "GENERATED GUID: " + this.guid );
    
    int messageSize= 36 + IOUtil.byteSize(path,guid); 
    int startCount=out.size(); 
    
    this.sendHeader( out,                       // header [12b]
                     CnsConstants.CNS_MAGIC2,
                     CnsConstants.CNS_MKDIR,
                     messageSize
                     ); // FIXME set the message size 
    
    out.writeInt( this.uid );  // user id [4b]
    out.writeInt( this.gid );  // group id [4b]
    out.writeShort( 0x755 );   // mask [2b]
    out.writeLong( this.cwd ); // I have no idea what is this [8b]
    
    IOUtil.writeString(out,path); 
    
    out.writeInt( FileDesc.S_IRGRP
                  | FileDesc.S_IROTH
                  | FileDesc.S_IRUSR
                  | FileDesc.S_IWGRP
                  | FileDesc.S_IWUSR 
                  | FileDesc.S_IWGRP 
                  | FileDesc.S_IXOTH
                  | FileDesc.S_IXGRP
                  | FileDesc.S_IXUSR ); // mode [4b] THIS IS NOT MODE, IT'S PREMISSIONS SETTING
    
    IOUtil.writeString(out,guid); 
   
    out.flush();
    int endCount=out.size();
    int realSize=endCount-startCount;
    
    if (realSize!=messageSize) 
    {
    	// PdB: Check consistancy 
    	System.err.println("**** Error: guestimated message size and actual nr. of written bytes do not match:"+messageSize+"!="+realSize); 
    }
    else
    {
    	 LFCServer.staticLogIOMessage("nr of bytes send="+realSize); 
    }
    
    CnsVoidResponse result = new CnsVoidResponse();
    result.readFrom( in );
    return result;
    
  }


}
