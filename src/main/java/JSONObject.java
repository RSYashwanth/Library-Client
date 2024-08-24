import java.util.ArrayList;

public class JSONObject {

    ArrayList<String> objects = new ArrayList<String>();

    public void put(String object) {
        String[] keyValPair = object.split(":");
        if(keyValPair.length!=2)
            throw new IllegalArgumentException("Element must only have a single \":\"");
        objects.add("\""+keyValPair[0]+"\""+":"+"\""+keyValPair[1]+"\"");
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("{");
        for(String object : objects) {
            output.append(object+",");
        }
        output.deleteCharAt(output.length()-1);
        output.append("}");
        return output.toString();
    }
}
