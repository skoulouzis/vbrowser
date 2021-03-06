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

package test.vrs.vfs;

import test.TestSettings;
import test.TestSettings.TestLocation;
import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
import nl.esciencecenter.vlet.vrs.VRS;

/**
 * Test Local case
 * 
 * TestSuite uses testVFS class to tests Local implementation.
 * 
 * @author P.T. de Boer
 */
public class TestVFS_LocalFS extends TestVFS
{
    static
    {
        initLocalFS();
    }
    
    public static void initLocalFS()
    {
        try
        {
            VRS.getRegistry().registerVRSDriverClass(nl.esciencecenter.vlet.vrs.vdriver.localfs.LocalFSFactory.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }
    
    @Override
    public VRL getRemoteLocation()
    {
        return TestSettings.getTestLocation(TestLocation.VFS_LOCALFS_LOCATION); 
    }

// Use Junit 4 annotation.
//    public static Test suite()
//    {
//        return new TestSuite(testLocalFS.class);
//    }
//
//    public static void main(String args[])
//    {
//        junit.textui.TestRunner.run(suite());
//    }

}
