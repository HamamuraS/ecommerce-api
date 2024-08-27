package com.empresita.resource;

import com.empresita.DTO.ProductDTO;
import com.empresita.entity.Family;
import com.empresita.entity.Product;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductResourceTest {

  @Test
  @DisplayName("A product can be retrieved by id")
  public void testGetProducts() {
    given()
      .when().get("/products/1")
      .then()
      .statusCode(200)
      .body("name", is("Test Product"))
      .body("family.name", is("Test Family"))
      .body("family.id", is(1))
      .body("price", is(10.0f))
      .body("stock", is(100))
    ;
  }

  @Test
  @TestSecurity(user = "testuser", roles = {"VENDOR"})
  @DisplayName("A product can be created by a vendor")
  public void testCreateProduct() {

    ProductDTO newProduct = new ProductDTO();
    newProduct.setName("Newly created product");
    newProduct.setPrice(100.0);
    newProduct.setStock(10);
    newProduct.setDescription("Newly created product description");
    newProduct.setFamily(1L);

    given()
        .contentType(ContentType.JSON)
        .body(newProduct)
        .when()
        .post("/products")
        .then()
        .statusCode(201)
        .body("name", is("Newly created product"));
  }

  // Más pruebas aquí...
}