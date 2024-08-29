package com.lgarzona.repository;

import com.lgarzona.model.entity.CategoryEntity;
import com.lgarzona.model.entity.DeletedProduct;
import com.lgarzona.model.entity.ProductEntity;
import com.lgarzona.model.entity.ProductStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    @Query("from ProductEntity where deleted = com.lgarzona.model.entity.DeletedProduct.CREATED and ((:status is null) or (status = :status))")
    List<ProductEntity> findAll(@Param("status") ProductStatus status);

    List<ProductEntity> findByCategoryAndDeleted(CategoryEntity category, DeletedProduct deleted);

}
