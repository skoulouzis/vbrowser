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

package nl.esciencecenter.vlet.exception;

/**
 * Credential Exceptions
 */
public class KeyStorePasswordException extends AuthenticationException
{
    private static final long serialVersionUID = -9218600987549414855L;

    public KeyStorePasswordException(String message)
    {
        super(message,null,"Invalid Password Exception");
    }

    /**
     * Create VlException: CrendentialException which keeps original System
     * Exception
     */
    public KeyStorePasswordException(String message, Exception e)
    {
        super(message,e,"Invalid Password Exception");
    }

}
