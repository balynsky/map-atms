package su.balynsky.android.atms.dao.json;

import org.apache.http.conn.ssl.SSLSocketFactory;
import su.balynsky.android.atms.DEBUG;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                for (X509Certificate aChain : chain) {
                    aChain.checkValidity();
                }

                if (chain.length < 1)
                    throw new CertificateException("There is no trusted certificate!");

                if (!chain[0].getSubjectDN().getName().toLowerCase().contains("cn=online.pumb.ua,") && !DEBUG.DEBUG_MODE)
                    throw new CertificateException("Trusted certificate is not issued to online.pumb.ua! Subject DN is: "
                            + chain[0].getSubjectDN().getName());
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
