package com.springsec.tutorial.service;

import com.springsec.tutorial.model.Product;
import com.springsec.tutorial.repository.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService
{
    private final ProductRepo productRepo;

    public Product createProductRecord(Product product)
    {
        Optional<Product> product1=productRepo.findByName(product.getName());
        if(product1.isEmpty())
        {
            productRepo.save(product);
        }
        else
        {
            throw new UsernameNotFoundException("Product Already Exist in System!!!");
        }

        return product;
    }

    public List<Product> getAllProducts()
    {
        return productRepo.findAll();
    }



}
