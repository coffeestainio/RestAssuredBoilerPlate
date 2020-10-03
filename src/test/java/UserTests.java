import com.sun.org.apache.regexp.internal.RE;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class UserTests extends BaseTest {

    private static String RESOURCE = "/v1/user";

    @Test
    public void Successful_Login_Test(){

//        JSONObject requestParams = new JSONObject();
//        requestParams.put("email", "pablo@calvo.com");
//        requestParams.put("password", "pablo");

        User testUser = new User("pablo@calvo.com", "pablo");
         request
                .given()
                 .contentType("application/json")
                .body(testUser)
        .when()
                .post(String.format("%s/login", RESOURCE))
        .then()
            .statusCode(200)
            .body("message",equalTo("User signed in"));

//        Assert.assertEquals(response.equals(test));
//        Assert.assertEquals(response.equals(test));
//        Assert.assertEquals(response.equals(test));
//        Assert.assertEquals(response.equals(test));
    }

    @Test
    public void Unsuccessful_Login_User_Not_Exist_Test(){

        User testUserInvalid = new User("pablo@calvos.com", "pablo");

        request
                .body(testUserInvalid.getUser())
                .post(String.format("%s/login", RESOURCE))
            .then()
                .statusCode(403)
                .body("message",equalTo("User signed in"));
    }

    @Test
    public void Unsuccessful_Login_Missing_Parameters_Not_Exist_Test(){

        User testUserMissingParameter = new User("pablo@calvos.com", "");

        request
                .body(testUserMissingParameter.getUser())
                .post(String.format("%s/login", RESOURCE))
                .then()
                .statusCode(200)
                .body("message",equalTo("User signed in"));
    }
}
