public class Client {
    public static void main(String[] args) throws Exception {

        HTTPSUtil loginConnection = new HTTPSUtil("https://10.141.241.34:8081/login", "POST");

        JSONObject obj = new JSONObject();
        obj.put("username:User2");
        obj.put("password:User2Pass");
        String login = obj.toString();

        String token = loginConnection.write(login).split(":")[1].replace("\"", "").replace("}", "");

        HTTPSUtil util = new HTTPSUtil("https://10.141.241.34:8081/users", "POST");
        util.setSessionKey(token);

        JSONObject obj1 = new JSONObject();
        obj1.put("username:User1");
        String test = obj1.toString();
        System.out.println(util.write(test));

    }
}
