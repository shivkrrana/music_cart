package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findAll();

    @Query("SELECT p FROM Product p WHERE " +
            "(:color IS NULL OR p.color = :color) " +
            "AND (:brand IS NULL OR p.brand = :brand) " +
            "AND (:productType IS NULL OR p.productType = :productType) " +
            "AND (:search IS NULL OR LOWER(p.title) LIKE CONCAT(LOWER(:search), '%')) " +
            "AND (:minPrice = -1 OR :maxPrice = -1 OR (p.price BETWEEN :minPrice AND :maxPrice)) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'price_asc' THEN p.price END ASC, " +
            "CASE WHEN :sort = 'price_desc' THEN p.price END DESC, " +
            "CASE WHEN :sort = 'name_asc' THEN p.title END ASC, " +
            "CASE WHEN :sort = 'name_desc' THEN p.title END DESC")
            List<Product> findFilteredProductSorted(String color, String brand, String search, String productType,
                                Double minPrice, Double maxPrice, String sort);
}
