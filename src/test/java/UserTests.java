import com.sun.org.apache.regexp.internal.RE;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserTests extends BaseTest {

    private static String RESOURCE = "/v1/user";

    @Test
    public void Successful_Login_Test(){

        User testUser = new User("pablillo@calvo.com", "pablo");
         request
            .given()
                 .contentType("application/json")
                 .body(testUser)
            .when()
                .post(String.format("%s/login", RESOURCE))
            .then()
                .statusCode(200)
                .body(
                        "message",notNullValue(),
                        "user.id", equalTo(17));

    }

    @Test
    public void Successful_Login_Test_With_Schema(){

        User testUser = new User("pablillo@calvo.com", "pablo");

        Response response =
                request.given().body(testUser)
                .post(String.format("%s/login", RESOURCE));

        assertThat(response.asString(), matchesJsonSchemaInClasspath("userLogin.schema.json"));

    }

    @Test
    public void Successful_Login_Test_With_Map(){

        User testUser = new User("pablillo@calvo.com", "pablo");

        Response response =
                request.given().body(testUser)
                        .post(String.format("%s/login", RESOURCE));

        Map<String, Object> details = response.as(new TypeRef<Map<String, Object>>() {});

        assertThat(details.get("message"), Matchers.<Object>equalTo("User signed in"));

    }

    @Test
    public void Successful_Login_Test_With_Specific_Validation(){

        User testUser = new User("pablillo@calvo.com", "pablo");

        Response response =
                request.given().body(testUser)
                        .post(String.format("%s/login", RESOURCE));

        String message = response.then().extract().path("message");
        String message1 = response.path("message");

        assertThat(response.path("message"), Matchers.<Object>equalTo("User signed in"));
        assertThat(message, equalTo("User signed in"));
        assertThat(message1, equalTo("User signed in"));

        String header1 = response.header("Content-Type");
    }

    @Test
    public void Unsuccessful_Login_User_Not_Exist_Test(){

        User testUserInvalid = new User("pablo@calvos.com", "pablo");

        request
                .body(testUserInvalid)
                .post(String.format("%s/login", RESOURCE))
            .then()
                .statusCode(406)
                .body("message",equalTo("Invalid login details"));
    }

    @Test
    public void Unsuccessful_Login_Missing_Parameters_Not_Exist_Test(){

        User testUserMissingParameter = new User("pablo@calvos.com", "");

        request
                .body(testUserMissingParameter)
                .post(String.format("%s/login", RESOURCE))
                .then()
                .statusCode(406)
                .body("message",equalTo("Invalid form"));
    }

    @Test
    public void Successful_Logout_Test_With_Specific_Validation(){

        User testUser = new User("pablillo@calvo.com", "pablo");

        String authToken =
                request.given().body(testUser)
                        .post(String.format("%s/login", RESOURCE)).path("token.access_token");

        Response response =
                request.given()
                         .header("Authorization","Bearer " + authToken)
                    .get(String.format("%s/logout", RESOURCE));

        assertThat(response.path("message"), Matchers.<Object>equalTo("Successfully logged out"));
    }
}
