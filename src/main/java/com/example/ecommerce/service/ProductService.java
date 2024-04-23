package com.example.ecommerce.service;

import com.example.ecommerce.dto.FilterProductDto;
import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void addNewProduct(ProductDto productDto){
        Product product = ProductMapper.toProduct(productDto);
        List<ProductImage> productImages = ProductMapper.toImages(productDto.getProductImages(), product);
        product.setProductImages(productImages);
        productRepository.save(product);
    }

    public Product getProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new RuntimeException("No product found with productId=" + productId));
    }

    public List<Product> getFilteredProduct(FilterProductDto filterProductDto){
        return productRepository.findFilteredProductSorted(
                filterProductDto.getColor()
                ,filterProductDto.getBrand()
                ,filterProductDto.getSearch()
                ,filterProductDto.getProductType()
                ,filterProductDto.getMinPrice()
                ,filterProductDto.getMaxPrice()
                , filterProductDto.getSort()
        );
    }

}
