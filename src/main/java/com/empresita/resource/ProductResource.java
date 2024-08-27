package com.empresita.resource;

import com.empresita.entity.Product;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.awt.PageAttributes;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

  @GET
  public Iterable<Product> getProducts() {
    return Product.listAll();
  }

  @GET
  @Path("/{id}")
  public Product getProduct(@PathParam("id") Long id) {
    return Product.findById(id);
  }

  @POST
  @RolesAllowed("VENDOR")
  @Transactional
  public Response createProduct(@Valid Product product) {
    product.persist();
    return Response.status(Response.Status.CREATED).entity(product).build();
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
    entity.setName(newProduct.getName());
    entity.setDescription(newProduct.getDescription());
    entity.setPrice(newProduct.getPrice());
    entity.setStock(newProduct.getStock());
    entity.setImage(newProduct.getImage());
    return Response.status(Response.Status.OK).entity(entity).build();
  }

  @PATCH
  @RolesAllowed({"VENDOR", "AUTHENTICATED_USER"})
  @Path("/{id}")
  @Transactional
  public Response updateProductStock(
      @PathParam("id") Long id,
      @HeaderParam("stock") Integer stock) {
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
