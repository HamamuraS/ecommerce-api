package com.empresita.resource;

import com.empresita.DTO.ProductDTO;
import com.empresita.entity.Family;
import com.empresita.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductResourceTest {

  @InjectMock
  Session session;

  Family testFamily;
  Product testProduct;

  @BeforeEach
  public void setup() {
    PanacheMock.mock(Product.class);
    PanacheMock.mock(Family.class);

    testFamily = new Family(1L, "Test Family");

    testProduct = new Product(
        1L,
        "Test Product",
        "Test Description",
        10.0,
        100,
        testFamily
    );

  }

  @Test
  @DisplayName("A product can be retrieved by id")
  public void testGetProducts() {

    Mockito.when(Product.findById(1L)).thenReturn(testProduct);

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
  @DisplayName("Products can be retrieved with pagination")
  public void testGetPagedProducts() {

    List<Product> products = IntStream.range(0, 10)
        .mapToObj(i -> new Product(
            (long) i,
            "Product " + i,
            "Description " + i,
            10.0,
            100,
            testFamily
        ))
        .toList();

    // no hay solucion mejor para mockear consultas encadenadas de PanacheEntity
    // leer : https://github.com/quarkusio/quarkus/issues/13251
    PanacheQuery query = Mockito.mock(PanacheQuery.class);
    Mockito.when(Product.find("order by id")).thenReturn(query);
    Mockito.when(query.page(Mockito.anyInt(), Mockito.anyInt())).thenReturn(query);
    Mockito.when(query.list()).thenReturn(products);

    given()
        .when().get("/products?page=0&size=10")
        .then()
        .statusCode(200)
        .body("size()", is(10))
        .body("[0].id", is(0))
        .body("[0].name", is("Product 0"))
        .body("[0].description", is("Description 0"))
        .body("[0].family.name", is("Test Family"))
        .body("[0].family.id", is(1))
        .body("[0].price", is(10.0f))
        .body("[0].stock", is(100))
        .body("[9].id", is(9))
        .body("[9].name", is("Product 9"));
  }

  @Test
  @TestSecurity(user = "testuser", roles = {"VENDOR"})
  @DisplayName("A product can be created by a vendor")
  public void testCreateProduct() {

    Mockito.when(Family.findById(1L)).thenReturn(testFamily);
    Mockito.doAnswer(invocation ->
        {
          // aqui hibernate asigna el id al objeto
          Product entity = invocation.getArgument(0);
          entity.setId(1L);
          return null;
        })
        .when(session).persist(Mockito.any(Product.class));

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

    Mockito.verify(session, Mockito.times(1)).persist(Mockito.any(Product.class));
  }

  // Más pruebas aquí...

  @AfterEach
  public void tearDown() {
    Mockito.reset(session);
  }
}