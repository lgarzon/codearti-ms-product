package com.lgarzona.model.mapper;

import com.lgarzona.model.dto.ProductCreateRequestDto;
import com.lgarzona.model.dto.ProductResponseDto;
import com.lgarzona.model.entity.CategoryEntity;
import com.lgarzona.model.entity.ProductEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-29T18:08:23-0500",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponseDto entityToResponse(ProductEntity entity, Integer port) {
        if ( entity == null && port == null ) {
            return null;
        }

        ProductResponseDto.ProductResponseDtoBuilder productResponseDto = ProductResponseDto.builder();

        if ( entity != null ) {
            productResponseDto.id( entity.getId() );
            productResponseDto.name( entity.getName() );
            productResponseDto.stock( entity.getStock() );
            productResponseDto.price( entity.getPrice() );
            productResponseDto.creationDate( entity.getCreationDate() );
            productResponseDto.updateDate( entity.getUpdateDate() );
            productResponseDto.status( entity.getStatus() );
            productResponseDto.categoryId( entityCategoryId( entity ) );
        }
        productResponseDto.port( port );

        return productResponseDto.build();
    }

    @Override
    public ProductEntity requestToEntity(ProductCreateRequestDto request) {
        if ( request == null ) {
            return null;
        }

        ProductEntity.ProductEntityBuilder productEntity = ProductEntity.builder();

        productEntity.category( productCreateRequestDtoToCategoryEntity( request ) );
        productEntity.name( request.getName() );
        productEntity.stock( request.getStock() );
        productEntity.price( request.getPrice() );

        return productEntity.build();
    }

    private Long entityCategoryId(ProductEntity productEntity) {
        CategoryEntity category = productEntity.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }

    protected CategoryEntity productCreateRequestDtoToCategoryEntity(ProductCreateRequestDto productCreateRequestDto) {
        if ( productCreateRequestDto == null ) {
            return null;
        }

        CategoryEntity.CategoryEntityBuilder categoryEntity = CategoryEntity.builder();

        categoryEntity.id( productCreateRequestDto.getCategoryId() );

        return categoryEntity.build();
    }
}
