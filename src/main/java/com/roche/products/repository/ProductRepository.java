package com.roche.products.repository;

import com.roche.products.domain.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {

    @Query("{ deleted: false }")
    List<ProductEntity> findAllNotDeleted();
}
