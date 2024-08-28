package com.empresita.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Family extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "fami_id")
  @Getter @Setter
  private Long id;

  @NotBlank(message = "El nombre no puede estar vacío")
  @Column(name = "fami_name", nullable = false)
  @Getter @Setter
  private String name;

  @JsonIgnore
  @OneToMany(mappedBy = "family")
  private List<Product> products = new ArrayList<>();

  public Family() {
  }

  public Family(String name) {
    this.name = name;
  }

  public Family(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public void addProduct(Product product) {
    products.add(product);
  }

  public void removeProduct(Product product) {
    products.remove(product);
  }

}
