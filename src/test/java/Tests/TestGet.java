package Tests;
import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.Test;
//import lombok.Data;
import static io.restassured.RestAssured.*;
import io.restassured.http.Header;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.Matchers.equalTo;

public class TestGet {
	private final static String BASE_URL = "http://127.0.0.1:7081";
	private final RestTemplate restTemplate = new RestTemplate();

	Book book1 = createBook();
	Book book2 = createBook();
	Book book3 = createBook();
	Book book4 = createBook();
	
	@Test
    void GetAllBooksByUser_ShouldDisplayAllBooks() {
        String path = "/api/books/";

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("user"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(200)
                .log().all();

    }
	@Test
	void GetAllBooksByAdmin_ShouldDisplayAllBooks() {
        String path = "/api/books/";

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("admin"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(200)
                .log().all();

    }
	@Test
	void GetAllBooksWithoutLog_ShouldNotDisplayAllBooks() {
        String path = "/api/books/";

        given()
                .baseUri(BASE_URL)
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(401)
                .log().all();

    }
	@Test
	void GetSingleBookByUser_ShouldDisplaySingleBookDetail() {
        String path = "/api/books/" + book1.getId();

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("user"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(book1.getId()))
                .log().all();

    }
	@Test
	void GetSingleBookByAdmin_ShouldDisplaySingleBookDetail() {
        String path = "/api/books/" + book1.getId();

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("admin"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(book1.getId()))
                .log().all();

    }
	@Test
	void GetSingleBookWithIncorrectIDByAdmin_ShouldDisplayError() {
        String path = "/api/books/" + 123;

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("admin"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(404);

    }
	@Test
	void GetSingleBookWithIncorrectIDByUser_ShouldDisplayError() {
        String path = "/api/books/" + 123;

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("user"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(404);

    }
	@Test
	void GetSingleBookWithIncorrectParameterTypeIDByAdmin_ShouldDisplayError() {
        String path = "/api/books/" + "THIS_IS_A_STRING";

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("admin"))
        .when()
                .get(path)
        .then()
                .assertThat()
                .statusCode(400);

    }
   
	
	private Header getAuthHeaderForRestAssured(String role) {
        return new Header("Authorization", "Basic " + getBasicAuth(role));
    }
	
	public Book createBook() {
	    // Create a Book with random data
	    Book booking = new Book();
	    int rand = (int)Math.round(Math.random() * 100000);
	    booking.setAuthor("Test Book Author " + rand);
	    booking.setTitle("Test Book Title " + rand);

	    booking.setId(0);
	    // Set up HTTP headers with Basic Authentication
	    String basicAuth = getBasicAuth();
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Basic " + basicAuth);

	    // Create an HTTP request entity
	    HttpEntity<Book> request = new HttpEntity<>(booking, headers);

	    // Make the POST request and retrieve the response
	    ResponseEntity<Book> response = restTemplate.exchange(BASE_URL + "/api/books", HttpMethod.POST, request, Book.class);

	    // Return the created Book from the response body
	    return response.getBody();
	}
	
	private String getBasicAuth() {
        return getBasicAuth("admin");
    }
    private String getBasicAuth(String role) {
        String plainCreds = role + ":password";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes);
    }
}
