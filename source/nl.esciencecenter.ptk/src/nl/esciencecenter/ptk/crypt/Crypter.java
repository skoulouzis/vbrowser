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

package nl.esciencecenter.ptk.crypt;
//package nl.nlesc.ptk.crypt;
//
//import java.security.spec.KeySpec;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKeyFactory;
//
//public class Crypter
//{
//    
//    public static class EncryptionException extends Exception
//    {
//        private static final long serialVersionUID = 1L;
//
//        public EncryptionException(Throwable t)
//        {
//            super(t);
//        }
//        
//        public EncryptionException(String message, Throwable cause)
//        {
//            super(message,cause);
//        }
//    }
//    
//    
//    
//    // ========================================================================
//    // Class Constants  
//    // ========================================================================
//
//    public static final String DES_ENCRYPTION_SCHEME = "DES";
//
//    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
//
//    public static final String DESEDE_ECB_PKCS5 = "DESede/ECB/PKCS5Padding"; 
//
//    public static final String DES_ECB_PKCS5 = "DES/ECB/PKCS5Padding"; 
//
//    // ========================================================================
//    // Instance  
//    // ========================================================================
//    
//    private KeySpec keySpec;
//
//    private SecretKeyFactory keyFactory;
//
//    private Cipher cipher;
//
//}