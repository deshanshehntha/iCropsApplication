package com.example.servicebackend.dao;

import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.repository.SupermarketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class SupermarketDAO {

    private final SupermarketRepository supermarketRepository;

    public Supermarket saveSupermarket(Supermarket supermarket) {
        return supermarketRepository.save(supermarket);
    }

    public Supermarket getSuperMarketByName(String supermarketName) {
        return supermarketRepository.findFirstBySupermarketName(supermarketName);
    }

    public Supermarket getSuperMarketById(String id) {
        return supermarketRepository.findBySupermarketId(id);
    }

    public List<Supermarket> getAllSupermarkets() {
        return supermarketRepository.findAll();
    }
}
