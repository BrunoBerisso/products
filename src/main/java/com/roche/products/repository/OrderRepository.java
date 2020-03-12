package com.roche.products.repository;

import com.roche.products.domain.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String> {

    @Query("{ created: {$gte: new ISODate('?0'), $lte: new ISODate('?1')} }")
    List<OrderEntity> findByCreatedDateBetween(LocalDateTime from, LocalDateTime to);
}
