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

package voms;

import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

import nl.nlesc.vlet.VletConfig;
import nl.nlesc.vlet.grid.globus.GlobusUtil;
import nl.nlesc.vlet.grid.proxy.GridProxy;
import nl.nlesc.vlet.grid.voms.VO;
import nl.nlesc.vlet.grid.voms.VomsProxyCredential;
import nl.nlesc.vlet.grid.voms.VomsUtil;
import nl.nlesc.vlet.grid.voms.VomsProxyCredential.VomsInfo;

public class VomsTest
{
    public static void main(String args[])
    {
        try
        {

           // GlobalConfig.init();

            GridProxy prox = GridProxy.getDefault();
            VletConfig.getRootLogger().setLevelToDebug();

            if (prox.isValid() == false)
            {
                Error("proxy not valid:create please");
                return;
            }

            GlobusCredential globusCred = GlobusUtil.getGlobusCredential(prox);

            // String
            // vourl="https://voms.grid.sara.nl:8443/voms/vlemed/webui/admin";
            // pvierVO=VomsUtil.getVO("vlemed");
            VO pvierVO = nl.nlesc.vlet.grid.voms.VomsUtil.getVO("pvier"); // VlePvierVO();

            if (pvierVO == null)
            {
                System.err.println("***Errpr: Couldn't get VO:pvier");
                return;
            }

            long lifetime = prox.getTimeLeft();

            VomsProxyCredential vomscred = VomsUtil.createVomsCredential(globusCred, pvierVO, lifetime);

            VomsInfo info = vomscred.getVomsInfo();

            if (info != null)
            {
                for (Enumeration<String> keys = info.keys(); keys.hasMoreElements();)
                {
                    String key = keys.nextElement();
                    Message("vomsinfo." + key + "=" + info.get(key));
                }
            }
            else
            {
                Error("NULL voms info:");
            }

            GlobusCredential vomsProx = vomscred.getVomsProxy();
            Message("New voms proxy ID=" + vomsProx.getIdentity());

            Message("voms proxy identity =" + vomsProx.getIdentity());

            // update:
            prox.setGlobusCredential(vomsProx);
            prox.saveProxyTo("/tmp/x509_vomsprox");

            inspect(prox);

            /*
             * AttributeCertificate attrCert =
             * vomscred.getAttributeCertificate(); AttributeCertificateInfo
             * attrInfo = attrCert.getAcinfo();
             * 
             * ASN1Sequence attrs = attrInfo.getAttributes();
             * 
             * for (int i=0;i<attrs.size();i++) { Object
             * obj=attrs.getObjectAt(i);
             * System.out.println("-"+i+"=<"+obj.getClass()+">="+obj);
             * 
             * if (obj instanceof ASN1Sequence) { ASN1Sequence aseq =
             * (ASN1Sequence)obj; for (int j=0;j<aseq.size();j++) { DEREncodable
             * obj2 = aseq.getObjectAt(j);
             * 
             * System.out.println("- - "+j+" = "+ obj2); } } }
             */

        }
        catch (Exception e)
        {
            Error("Exception:" + e);
            Error("--- stacktrace ---");
            e.printStackTrace();
        }

    }

    private static void inspect(GridProxy prox)
    {
        GlobusCredential glob = GlobusUtil.getGlobusCredential(prox);
        X509Certificate cert = glob.getIdentityCertificate();

        Set<String> extSet = cert.getCriticalExtensionOIDs();

        for (String ext : extSet)
        {
            byte[] val = cert.getExtensionValue(ext);
            System.out.println("ext=" + ext + " = " + new String(val));

        }
    }

    private static void Error(String str)
    {
        VletConfig.getRootLogger().errorPrintf("VomsTest:%s\n", str);
    }

    private static void Message(String str)
    {
        VletConfig.getRootLogger().infoPrintf("VomsTest:%s\n", str);
    }
    
}
