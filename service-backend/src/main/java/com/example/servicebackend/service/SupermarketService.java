package com.example.servicebackend.service;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dao.SupermarketDAO;
import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.dto.SupermarketSupplierRelationship;
import com.example.servicebackend.dto.actor.Supplier;
import com.example.servicebackend.repository.SupermarketSupplierRelationshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupermarketService {

    private final SupermarketDAO supermarketDAO;
    private final SupermarketSupplierRelationshipRepository supermarketSupplierRelationshipRepository;

    public Response createNewSupermarket(Supermarket recievedSupermarket) {
        Supermarket clonedSupermarket = new Supermarket();
        clonedSupermarket(clonedSupermarket, recievedSupermarket);
        Supermarket savedSupermarket = supermarketDAO.saveSupermarket(clonedSupermarket);

        if (!CollectionUtils.isEmpty(recievedSupermarket.getSuppliers())) {
            persistSupermarketSupplierRelationship(recievedSupermarket, savedSupermarket.getSupermarketId());
        }

        Response response = new Response();
        response.setId(savedSupermarket.getSupermarketId());
        response.setStatus(RequestStatus.SUCCESS);
        return response;
    }

    public Response updateSupermarket(Supermarket recievedSupermarket, String id) {
        Supermarket clonedSupermarket = new Supermarket();
        clonedSupermarket(clonedSupermarket, recievedSupermarket);
        Supermarket savedSupermarket = supermarketDAO.saveSupermarket(clonedSupermarket);

        if (!CollectionUtils.isEmpty(clonedSupermarket.getSuppliers())) {
            persistSupermarketSupplierRelationship(recievedSupermarket, savedSupermarket.getSupermarketId());
        }

        Response response = new Response();
        response.setId(savedSupermarket.getSupermarketId());
        response.setStatus(RequestStatus.SUCCESS);
        return response;
    }

    public Supermarket getSupermarketByName(String supermarketName) {
        Supermarket supermarket = supermarketDAO.getSuperMarketByName(supermarketName);
        supermarket.setSuppliers(supermarketSupplierRelationshipRepository
                .findSupermarketSupplierRelationshipsBySupermarketId(supermarket.getSupermarketId()));
        return supermarket;
    }

    public Supermarket getSupermarketById(String supermarketId) {
        Supermarket supermarket = supermarketDAO.getSuperMarketById(supermarketId);
        supermarket.setSuppliers(supermarketSupplierRelationshipRepository
                .findSupermarketSupplierRelationshipsBySupermarketId(supermarket.getSupermarketId()));
        return supermarket;
    }

    public List<String> getSupplierIdsBySupermarketId(String supermarketId) {

        List<SupermarketSupplierRelationship> relationships =
                supermarketSupplierRelationshipRepository.findSupplierIdsBySupermarketId(supermarketId);

        if (!CollectionUtils.isEmpty(relationships)) {
            return relationships.stream()
                    .map(SupermarketSupplierRelationship::getSupplierId)
                    .collect(Collectors.toList());
        }

        return null;
    }

    public List<Supermarket> getAllSupermarkets() {
        return supermarketDAO.getAllSupermarkets();
    }

    private void persistSupermarketSupplierRelationship(Supermarket supermarket, String supermarketId) {

        for (SupermarketSupplierRelationship supplier: supermarket.getSuppliers()) {
            SupermarketSupplierRelationship supermarketSupplierRelationship =
                    new SupermarketSupplierRelationship();
            supermarketSupplierRelationship.setSupermarketId(supermarketId);
            supermarketSupplierRelationship.setSupplierId(supplier.getSupplierId());
            supermarketSupplierRelationshipRepository.save(supermarketSupplierRelationship);
        }
    }

    private void clonedSupermarket(Supermarket clonedSupermarket, Supermarket recievedSupermarket) {
        BeanUtils.copyProperties(recievedSupermarket,clonedSupermarket);
        clonedSupermarket.setSuppliers(null);
    }
}
