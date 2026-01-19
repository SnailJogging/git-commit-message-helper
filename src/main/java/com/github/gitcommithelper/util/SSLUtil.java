package com.github.gitcommithelper.util;

import javax.net.ssl.*;
import java.net.HttpURLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * SSL certificate utility for handling corporate proxy environments
 * that use custom certificates (e.g., mitmproxy)
 */
public class SSLUtil {

    private static SSLContext trustAllContext;
    private static HostnameVerifier trustAllHostnameVerifier;

    static {
        try {
            // Create TrustManager that trusts all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            // Trust all client certificates
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            // Trust all server certificates
                        }
                    }
            };

            // Create SSLContext with custom TrustManager
            trustAllContext = SSLContext.getInstance("TLS");
            trustAllContext.init(null, trustAllCerts, new SecureRandom());

            // Create HostnameVerifier that trusts all hostnames
            trustAllHostnameVerifier = (hostname, session) -> true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SSL context", e);
        }
    }

    /**
     * Configures HttpURLConnection to trust all SSL certificates
     * <p>
     * WARNING: This disables SSL certificate verification and should only be used
     * in corporate proxy environments where the proxy uses custom certificates.
     * This makes the connection vulnerable to man-in-the-middle attacks.
     * </p>
     *
     * @param connection the HttpURLConnection to configure
     */
    public static void trustAllCertificates(HttpURLConnection connection) {
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            httpsConnection.setSSLSocketFactory(trustAllContext.getSocketFactory());
            httpsConnection.setHostnameVerifier(trustAllHostnameVerifier);
        }
    }

    /**
     * Checks if SSL certificate trust is enabled in settings
     *
     * @param trustAllCertificates the setting value
     * @return true if SSL certificate verification should be disabled
     */
    public static boolean shouldTrustAllCertificates(boolean trustAllCertificates) {
        return trustAllCertificates;
    }
}
