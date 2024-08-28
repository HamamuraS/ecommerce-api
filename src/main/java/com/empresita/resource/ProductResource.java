package com.empresita.resource;

import com.empresita.DTO.ProductDTO;
import com.empresita.entity.Product;
import com.empresita.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

  @Inject
  ProductService productService;

  @GET
  public Response getProducts(
      @QueryParam("page") Integer page,
      @QueryParam("size") Integer size,
      @QueryParam("family") Long familyId
  ) {
    if (page == null) {
      page = 0;
    }
    if (size == null) {
      size = 10;
    }
    return Response.status(Response.Status.OK)
        .entity(productService.findPagedProducts(page, size, familyId))
        .build();
  }

  @GET
  @Path("/{id}")
  public Product getProduct(@PathParam("id") Long id) {
    return Product.findById(id);
  }

  @POST
  @RolesAllowed("VENDOR")
  @Transactional
  public Response createProduct(@Valid ProductDTO product) {
    Product newProduct = product.toEntity();
    if (newProduct.getFamily() == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("El producto debe tener una familia valida")
          .build();
    }
    newProduct.persist();
    return Response.status(Response.Status.CREATED).entity(newProduct).build();
  }

  @PUT
  @RolesAllowed("VENDOR")
  @Transactional
  public Response updateProduct(@Valid Product newProduct) {
    if (newProduct.getId() == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("El id del producto no puede ser nulo")
          .build();
    }
    Product entity = Product.findById(newProduct.getId());
    entity.setValues(newProduct);
    return Response.status(Response.Status.OK).entity(entity).build();
  }

  @PATCH
  @RolesAllowed({"VENDOR", "AUTHENTICATED_USER"})
  @Path("/{id}")
  @Transactional
  public Response updateProductStock(
      @PathParam("id") Long id,
      @QueryParam("stock") Integer stock) {
    Product entity = Product.findById(id);
    if (entity == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    if (stock == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Se debe proveer el nuevo stock")
          .build();
    }
    entity.setStock(stock);
    return Response.status(Response.Status.OK).entity(entity).build();
  }


}
