package com.empresita.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import com.empresita.DTO.ProductDTO;

@Entity
@Getter
@Setter
public class Product extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "prod_id")
  private Long id;

  @NotBlank(message = "El nombre no puede estar vacío")
  @Column(name = "prod_name", nullable = false)
  private String name;

  @NotBlank(message = "La descripción no puede estar vacía")
  @Column(name = "prod_description", nullable = false)
  private String description;

  @NotNull(message = "El precio no puede estar vacío")
  @Positive(message = "El precio debe ser mayor a cero")
  @Column(name = "prod_price", nullable = false)
  private Double price;

  @NotNull(message = "El stock no puede estar vacío")
  @Positive(message = "El stock debe ser mayor a cero")
  @Column(name = "prod_stock", nullable = false)
  private Integer stock;

  @Column(name = "prod_image")
  private String image;

  @ManyToOne
  @JoinColumn(name = "prod_family", nullable = false)
  private Family family;

  public Product() {
  }

  public Product(Long id, String name, String description, Double price, Integer stock, Family family) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.family = family;
  }

  public void setValues(Product product) {
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.stock = product.getStock();
    this.image = product.getImage();
    this.family = product.getFamily();
  }

  public ProductDTO toDTO() {
    ProductDTO dto = new ProductDTO();
    dto.setId(this.id);
    dto.setName(this.name);
    dto.setDescription(this.description);
    dto.setPrice(this.price);
    dto.setStock(this.stock);
    dto.setImage(this.image);
    dto.setFamily(this.family.getId());
    return dto;
  }

}
