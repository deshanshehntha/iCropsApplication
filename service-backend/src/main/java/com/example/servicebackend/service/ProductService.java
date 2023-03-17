package com.example.servicebackend.service;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.dto.SupermarketSupplierRelationship;
import com.example.servicebackend.dto.SupplierProductRelationship;
import com.example.servicebackend.dto.actor.Supplier;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.repository.ProductRepository;
import com.example.servicebackend.repository.SupplierProductRelationshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierProductRelationshipRepository supplierProductRelationshipRepository;
    private final UserService userService;
    private final SupermarketService supermarketService;

    public Response createProduct(Product receivedProduct) {

        String remark = validateProductCreationRequest(receivedProduct);

        Response response;
        if (remark != null) {
            response = new Response();
            response.setStatus(RequestStatus.FAILED);
            response.setRemark(remark);
            return response;
        }

        Product clonedProduct = new Product();
        cloneProduct(clonedProduct, receivedProduct);

        Product savedProduct = productRepository.save(clonedProduct);

        SupplierProductRelationship supplierProductRelationship = new SupplierProductRelationship();
        supplierProductRelationship.setProductId(savedProduct.getProductId());
        supplierProductRelationship.setSupplierId(receivedProduct.getSupplier().getSupplierId());

        supplierProductRelationshipRepository.save(supplierProductRelationship);

        response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(savedProduct.getProductId());
        return response;
    }

    public Product getProductById(String productId) {

        Product product = productRepository.getProductByProductId(productId);
        SupplierProductRelationship supplierProductRelationship =
                supplierProductRelationshipRepository.getSupplierProductRelationshipByProductId(productId);
        product.setSupplier(supplierProductRelationship);
        return product;
    }

    public List<Product> getProductBySupermarketId(String supermarketId) {

        List<String> supplierIds = supermarketService.getSupplierIdsBySupermarketId(supermarketId);

        if (!CollectionUtils.isEmpty(supplierIds)) {
            List<SupplierProductRelationship> supplierProductRelationships =
                    supplierProductRelationshipRepository.getProductIdsBySupplierIds(supplierIds);

            if (!CollectionUtils.isEmpty(supplierProductRelationships)) {
                List<String> productIds = supplierProductRelationships.stream().map(SupplierProductRelationship::getProductId)
                        .collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(productIds)) {
                    return productRepository.getProductsByProductIds(productIds);
                }
            }
        }
        return null;
    }

    private void cloneProduct(Product clonedProduct, Product receivedProduct) {
        BeanUtils.copyProperties(receivedProduct, clonedProduct);
        clonedProduct.setSupplier(null);
    }

    private String validateProductCreationRequest(Product product) {
        if (product.getSupplier() == null) {
            return "Supplier is compulsory";
        }

        if (product.getSupplier() != null &&
                product.getSupplier().getSupplierId() == null) {
            return "Supplier id compulsory";
        }

        Supplier supplier =
                userService.getSupplierByUserId(product.getSupplier().getSupplierId());

        if (supplier == null) {
            return "Invalid supplier id";
        }

        return null;
    }
}
