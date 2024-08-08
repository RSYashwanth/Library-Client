import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class SSLUtils {
    public static SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = { new CustomTrustManager() };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);
        return sslContext;
    }
}