package com.lgarzona.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.lgarzona.configuration.error.ResourceNotFoundException;
import com.lgarzona.model.dto.ProductCreateRequestDto;
import com.lgarzona.model.dto.ProductResponseDto;
import com.lgarzona.model.dto.ProductUpdateRequestDto;
import com.lgarzona.model.dto.ProductUpdateStockRequestDto;
import com.lgarzona.model.entity.CategoryEntity;
import com.lgarzona.model.entity.DeletedProduct;
import com.lgarzona.model.entity.ProductEntity;
import com.lgarzona.model.entity.ProductStatus;
import com.lgarzona.model.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgarzona.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll(ProductStatus status, int port){
        log.info("findAll");
        var list = repository.findAll(status);
        log.info("finded");
        return list.stream().map(p -> mapper.entityToResponse(p, port)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id, int port) {
        log.info("findById");
        return repository.findById(id).
                filter(p -> p.getDeleted() == DeletedProduct.CREATED)
                .map(p -> mapper.entityToResponse(p, port)).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByIdCategory(Long id, int port) {
        log.info("findByCategory");
        var list = repository.findByCategoryAndDeleted(CategoryEntity.builder().id(id).build(), DeletedProduct.CREATED);
        return list.stream().map(p -> mapper.entityToResponse(p, port)).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto productRequest, int port) {
        log.info("create");
        ProductEntity productEntity = mapper.requestToEntity(productRequest);
        repository.save(productEntity);
        log.info("saved");
        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductUpdateRequestDto productRequest, int port) {
        log.info("update");
        ProductEntity productEntity = getProductById(id);
        BeanUtils.copyProperties(productRequest, productEntity);
        productEntity.setCategory(CategoryEntity.builder().id(productRequest.getCategoryId()).build());
        repository.save(productEntity);
        log.info("updated");
        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public ProductResponseDto updateStock(Long id, ProductUpdateStockRequestDto productRequest, int port) {
        log.info("updateStock");
        ProductEntity productEntity = getProductById(id);
        productEntity.setStock(productEntity.getStock() + productRequest.getStock());
        repository.save(productEntity);
        log.info("updated");
        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public void delete(Long id, int port) {
        log.info("delete");
        ProductEntity productEntity = getProductById(id);
        productEntity.setDeleted(DeletedProduct.DELETED);
        repository.save(productEntity);
        log.info("deleted");
    }

    private ProductEntity getProductById(Long id) {
        Optional<ProductEntity> productEntityOp = repository.findById(id).
                filter(p -> p.getDeleted() == DeletedProduct.CREATED);
        if(productEntityOp.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return productEntityOp.get();
    }

}
