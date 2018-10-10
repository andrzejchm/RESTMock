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

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class RESTMockOptions {
    private boolean useHttps;
    private SSLSocketFactory socketFactory;
    private X509TrustManager trustManager;

    private RESTMockOptions(final Builder builder) {
        setUseHttps(builder.useHttps);
        setSocketFactory(builder.socketFactory);
        setTrustManager(builder.trustManager);
    }

    public boolean isUseHttps() {
        return useHttps;
    }

    public void setUseHttps(final boolean useHttps) {
        this.useHttps = useHttps;
    }

    /**
     * Returns the sslSocketFactory used by RESTMockServer. If you didn't specify your own with #setSSlSocketFactory(SSSLSocketFactory)
     * method, then a default one will be created
     * You can set this socket factory in your HTTP client in order to be able to perform proper SSL handshakes with mockwebserver
     *
     * @return SSLSocketFactory used by the RESTMockServer
     */
    public SSLSocketFactory getSocketFactory() {
        return socketFactory;
    }

    public void setSocketFactory(final SSLSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    /**
     * Returns the trustManager used in conjunction with sslSocketFactory. It is set up to trust the certificates produces by the
     * sslSocketFactory returned in `getSocketFactory().
     */
    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    public void setTrustManager(final X509TrustManager trustManager) {
        this.trustManager = trustManager;
    }

    public static final class Builder {
        private boolean useHttps;
        private SSLSocketFactory socketFactory;
        private X509TrustManager trustManager;

        public Builder() {
        }

        public Builder useHttps(final boolean val) {
            useHttps = val;
            return this;
        }

        public Builder socketFactory(final SSLSocketFactory val) {
            socketFactory = val;
            return this;
        }

        public Builder trustManager(final X509TrustManager val) {
            trustManager = val;
            return this;
        }

        public RESTMockOptions build() {
            return new RESTMockOptions(this);
        }
    }
}
