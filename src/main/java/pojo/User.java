package pojo;

import java.util.HashMap;
import java.util.Map;

public class User {

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String email;
    public String password;

    public Map<String, Object> getUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("email", "pablo@calvo.com");
        jsonAsMap.put("password", "pablo");
        return jsonAsMap;
    }



}
