package com.empresita.DTO;

import com.empresita.entity.Family;
import com.empresita.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

  public ProductDTO() {
  }

  private Long id;

  @NotBlank(message = "El nombre no puede estar vacío")
  private String name;

  @NotBlank(message = "La descripción no puede estar vacía")
  private String description;

  @NotNull(message = "El precio no puede estar vacío")
  @Positive(message = "El precio debe ser mayor a cero")
  private Double price;

  @NotNull(message = "El stock no puede estar vacío")
  @Positive(message = "El stock debe ser mayor a cero")
  private Integer stock;

  private String image;

  private Long family;

  public Product toEntity() {
    Product product = new Product();
    product.setId(this.id);
    product.setName(this.name);
    product.setDescription(this.description);
    product.setPrice(this.price);
    product.setStock(this.stock);
    product.setImage(this.image);
    Family family = Family.findById(this.family);
    product.setFamily(family);
    return product;
  }

}
