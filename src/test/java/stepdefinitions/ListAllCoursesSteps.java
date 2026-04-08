package stepdefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.dwp.ces.models.Course;
import org.testng.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ListAllCoursesSteps {

    private Response response;
    private List<Course> courses;

    private static final String BASE_URL = "https://courseenrollmentapimanagementsystem.onrender.com";

    public ListAllCoursesSteps(){
        RestAssured.baseURI = BASE_URL;
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String endpoint) throws Exception {

        response =
                given()
                        .when()
                        .get("/courses/all")
                        .then()
                        .extract().response();
    }

    @Then("the response status should be {int}")
    public void validateStatus(int expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), expectedStatus);
    }

    @Then("the response should contain a list of courses")
    public void validateCourseList() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        courses = mapper.readValue(
                response.asString(),
                new TypeReference<List<Course>>() {}
        );

        Assert.assertTrue(courses.size() > 0, "Course list is empty");
    }

    @Then("each course should have valid fields")
    public void validateCourseFields() {

        for (Course course : courses) {
            Assert.assertNotNull(course.getId());
            Assert.assertNotNull(course.getTitle());
            Assert.assertNotNull(course.getCourseCode());
            Assert.assertTrue(course.getTotalCapacity() >= 0);
            Assert.assertTrue(course.getAvailableSlots() >= 0);
        }
    }
}