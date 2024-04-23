package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.dto.ProductImageDto;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface ProductMapper {

    static ProductImage toImage(ProductImageDto image, Product product) {
        ProductImage productImage = new ProductImage();
        productImage.setImageData(image.getImageData());
        productImage.setProduct(product);
        return productImage;
    }

    static List<ProductImage> toImages(List<ProductImageDto> productImages, Product product) {
        return productImages.stream().map(item -> ProductMapper.toImage(item, product))
                .collect(Collectors.toList());
    }

    static Product toProduct(ProductDto productDto){
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setBrand(productDto.getBrand());
        product.setColor(productDto.getColor());
        product.setProductType(productDto.getProductType());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getProductImages().get(0).getImageData());
        return product;
    }
}
