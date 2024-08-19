import java.net.ConnectException;

public class ClientUtil {
    public static boolean authenticate(String username, String password) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("username:"+username);
            obj.put("password:"+password);
            String login = obj.toString();

            String out = HTTPUtil.write(DataManager.serverIp+"/login", "POST", login);
            String token = out.split(",")[0].split(":")[1].replace("\"", "").replace("}", "");
            Boolean isAdmin = Boolean.parseBoolean(out.split(",")[1].split(":")[1].replace("\"", "").replace("}", ""));

            DataManager.isAdmin = isAdmin;
            DataManager.session = token;
            DataManager.user = username;

            return !out.contains("error");
        }
        catch(ConnectException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            return false;
        }
        return false;
    }

    public static void register(String username, String password) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("username:"+username);
            obj.put("password:"+password);
            String login = obj.toString();

            String response = HTTPUtil.write(DataManager.serverIp+"/register", "POST", login);
            if(!response.contains("error")) {
                System.out.println("User registered successfully");
            }
            else {
                System.out.println(response);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
