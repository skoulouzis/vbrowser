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

package test.vrs.vfs;

import org.junit.Before;

import test.TestSettings;
import nl.esciencecenter.vbrowser.vrs.exceptions.VrsException;
import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
//import nl.uva.vlet.gui.dialog.AuthenticationDialog;
import nl.nlesc.vlet.vrs.ServerInfo;

/**
 * Test SRB case
 * 
 * TestSuite uses testVFS class to tests SRB implementation.
 * 
 * @author P.T. de Boer
 */
public class TestVFS_SFTPlocalhost extends TestVFS
{
    static private ServerInfo info;

    public TestVFS_SFTPlocalhost()
    {
        // this.doRename=false;
        // this.doWrites=false;
    }

    @Override
    public VRL getRemoteLocation()
    {
        return TestSettings.getTestLocation(TestSettings.VFS_SFTP_LOCALHOST_TESTUSER); 
    }

    protected void checkAuthentication() throws Exception
    {
        ServerInfo info=this.getVRSContext().getServerInfoFor(this.getRemoteLocation(), true); 
        
        info.setAttribute(ServerInfo.ATTR_SSH_IDENTITY,"test_rsa"); 
        info.store();
    }

    @Before
    public void testSetup()
    {
        
    }

}
