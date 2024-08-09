import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtil {
    public static String write(String endpoint, String method, String data) throws IOException {
        System.out.println(endpoint);
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        if (DataManager.session != null) {
            connection.setRequestProperty("Authorization", "Bearer " + DataManager.session);
        }
        if (DataManager.user != null) {
            connection.setRequestProperty("User", DataManager.user);
        }

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                (responseCode == 200)? connection.getInputStream() : connection.getErrorStream()
            )
        );

        String line = reader.readLine();
        StringBuilder response = new StringBuilder();
        while (line != null) {
            response.append(line);
            line = reader.readLine();
        }
        reader.close();
        connection.disconnect();

        return ((responseCode == 200)? "" : "ERROR: " + responseCode) + response;
    }
}
