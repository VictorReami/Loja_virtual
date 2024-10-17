package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Set;

@Service
public class IgnoreHostNameSSL implements HostnameVerifier, Serializable {

    private static final long serialVersionUID = 1L;

    private static final HostnameVerifier defaultHostNameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    private Set<String> trustedHosts;

    public IgnoreHostNameSSL(Set<String> trustedHosts) {
        this.trustedHosts = trustedHosts;
    }

    @Override
    public boolean verify(String hostName, SSLSession session) {

        if(trustedHosts.contains(hostName)){
            return true;
        }else{
            return defaultHostNameVerifier.verify(hostName, session);
        }
    }

    public static HostnameVerifier getDefaultHostNameVerifier() throws Exception{
        TrustManager[] trustManagers = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        return defaultHostNameVerifier;
    }



}
