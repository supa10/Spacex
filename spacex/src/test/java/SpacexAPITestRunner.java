import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class SpacexAPITestRunner {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    private Logger logger = LoggerFactory.getLogger(SpacexAPITestRunner.class);

    private Response getResponseForGetRequestType() {
        Response response = null;
        try {
            response = given().relaxedHTTPSValidation().when().get(new URI(resourceBundle.getString("url")));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (response != null) {
            logger.info("Response Status code -- " + response.getStatusCode());
            assertThat(response.getStatusCode(), is(200));
        }
        return response;
    }


    @Test
    public void scenario1() {
        Response response = getResponseForGetRequestType().thenReturn();

        String body = response.asString();
        logger.info(body);
        assertThat(body, is(containsString("id")));

        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getBoolean("success"), is(true));
        assertThat(jsonPath.getBoolean("auto_update"), is(true));
    }

    @Test
    public void scenario2() {
        Response response = getResponseForGetRequestType().thenReturn();
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("links.youtube_id"), is(jsonPath.getString("links.webcast").split("/")[3]));

    }

    @Test
    public void scenario3() {
        Response response = getResponseForGetRequestType().thenReturn();
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("links.flickr.original").size(), is(8));
    }


}
