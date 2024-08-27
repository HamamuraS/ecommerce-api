package com.empresita.service;

import com.empresita.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductService {

  @Transactional
  public List<Product> findPagedProducts(int page, int size) {
    return Product.find("order by id")
        .page(page, size)
        .list();
  }

  @Transactional
  public List<Product> findPagedProducts(int page, int size, Long familyId) {
    if (familyId == null) {
      return this.findPagedProducts(page, size);
    }
    return Product.find("prod_family = ?1 order by prod_id", familyId)
        .page(page, size)
        .list();
  }

}
