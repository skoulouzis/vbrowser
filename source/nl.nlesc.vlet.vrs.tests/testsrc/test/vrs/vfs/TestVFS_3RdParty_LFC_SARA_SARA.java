package test.vrs.vfs;

import nl.esciencecenter.vbrowser.vrs.vrl.VRL;
import test.TestSettings;

public class TestVFS_3RdParty_LFC_SARA_SARA extends TestVFS_3rdPartyTransfers
{
    @Override
    public VRL getRemoteLocation()
    {
        return TestSettings.getTestLocation(TestSettings.VFS_LFC_SARA_LOCATION); 
    }
    

    @Override
    public VRL getOtherRemoteLocation()
    {
        return TestSettings.getTestLocation(TestSettings.VFS_LFC_SARA_OTHER_LOCATION); 
    }

}