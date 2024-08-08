import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HTTPSUtil {

    public HttpsURLConnection connection;

    public void setSessionKey(String s) {
        connection.setRequestProperty("Authorization", "Bearer " + s);
    }

    public void setUser(String s) {
        connection.setRequestProperty("User", s);
    }

    public HTTPSUtil(String link, String RequestMethod)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {

        SSLContext sslContext = SSLUtils.createSSLContext();
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());


        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        URL url = new URL(link);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(RequestMethod);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
    }

    public String write(String s) throws IOException {

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(s.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        BufferedReader reader;

        if(responseCode == HttpURLConnection.HTTP_OK)
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));



        String line = reader.readLine();
        StringBuilder response = new StringBuilder();

        while (line != null) {
            response.append(line);
            line = reader.readLine();
        }

        reader.close();


        connection.disconnect();

        if (responseCode == HttpURLConnection.HTTP_OK)
            return response.toString();
        else
            return "ERROR: " + responseCode+", "+response.toString();
    }
}
