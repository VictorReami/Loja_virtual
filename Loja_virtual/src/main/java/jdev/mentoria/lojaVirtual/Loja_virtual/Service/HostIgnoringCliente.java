package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class HostIgnoringCliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String hostName;

    public HostIgnoringCliente(String hostName) {
        this.hostName = hostName;
    }

   /*public HostIgnoringCliente(String hostName) {
        this.hostName = hostName;
    }*/

    public Client hostIgnoringCliente() throws Exception {
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

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());

        Set<String> hostNameList = new HashSet<String>();
        hostNameList.add(this.hostName);
        HttpsURLConnection.setDefaultHostnameVerifier(new IgnoreHostNameSSL(hostNameList));

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        DefaultClientConfig config = new DefaultClientConfig();
        Map<String, Object> properties = config.getProperties();
        HTTPSProperties httpsProperties = new HTTPSProperties(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }, sslContext);

        properties.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, httpsProperties);
        config.getClasses().add(JacksonJsonProvider.class);
        config.getClasses().add(MultiPartWriter.class);

        return Client.create(config);
    }

}
