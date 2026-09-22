package ru.lakeevda.it.presentation.rest.resource;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import ru.lakeevda.presentation.dto.OrderRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusIntegrationTest
public class OrderResourceIT {

    @Test
    public void testCreateOrder() {
        OrderRequest request = new OrderRequest("CreateTest", "CREATED");
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("name", equalTo("CreateTest"))
                .body("status", equalTo("CREATED"))
                .body("id", notNullValue());
    }

    @Test
    public void testGetOrderById() {
        OrderRequest request = new OrderRequest("GetTest", "COMPLETED");
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("/orders/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("GetTest"))
                .body("status", equalTo("COMPLETED"));
    }

    @Test
    public void testGetAllOrders() {
        OrderRequest req1 = new OrderRequest("AllTest1", "CREATED");
        OrderRequest req2 = new OrderRequest("AllTest2", "COMPLETED");
        Integer id1 = given()
                .contentType(ContentType.JSON)
                .body(req1)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        Integer id2 = given()
                .contentType(ContentType.JSON)
                .body(req2)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2))
                .body("id", hasItems(id1, id2));
    }

    @Test
    public void testDeleteOrder() {
        OrderRequest request = new OrderRequest("DeleteTest", "CREATED");
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .delete("/orders/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/orders/{id}", id)
                .then()
                .statusCode(404);
    }
}
