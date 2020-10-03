import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class NonApiTests extends BaseTest {

    private static String RESOURCE = "/";

    @Test
    public void Home_Page_Test(){
        request
            .get(RESOURCE)
        .then()
            .statusCode(200);
    }

    @Test
    public void Ping_Test(){
        request
            .get(String.format("%sping", RESOURCE))
        .then()
            .statusCode(200)
            .header("Access-Control-Allow-Origin", equalTo("http://localhost"))
            .body("id", equalTo(3),
            "response", equalTo("pong"));
    }
}
