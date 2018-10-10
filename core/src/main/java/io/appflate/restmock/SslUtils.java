/*
 * Copyright (C) 2016 Appflate.io
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appflate.restmock;

import java.net.InetAddress;
import java.net.UnknownHostException;
import okhttp3.tls.HandshakeCertificates;
import okhttp3.tls.HeldCertificate;


//DISCLAIMER
// since android does not support ECDSA by default, I've copied this code from OkHttp's tests and replaced the algorithm with RSA
public class SslUtils {

    private static HandshakeCertificates localhost; // Lazily initialized.

    private SslUtils() {
    }

    /** Returns an SSL client for this host's localhost address. */
    public static synchronized HandshakeCertificates localhost() {
        if (localhost != null) return localhost;

        try {
            // Generate a self-signed cert for the server to serve and the client to trust.
            HeldCertificate heldCertificate = new HeldCertificate.Builder()
                .rsa2048()
                .commonName("localhost")
                .addSubjectAlternativeName(InetAddress.getByName("localhost").getCanonicalHostName())
                .build();

            localhost = new HandshakeCertificates.Builder()
                .heldCertificate(heldCertificate)
                .addTrustedCertificate(heldCertificate.certificate())
                .build();

            return localhost;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
