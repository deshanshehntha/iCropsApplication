package com.example.servicebackend.service;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.dto.SupermarketSupplierRelationship;
import com.example.servicebackend.dto.SupplierProductRelationship;
import com.example.servicebackend.dto.actor.Supplier;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.dto.product.ExplanationReqDTO;
import com.example.servicebackend.dto.product.ExplanationResDTO;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.dto.product.RecommendedProductReqDTO;
import com.example.servicebackend.repository.ProductRepository;
import com.example.servicebackend.repository.SupplierProductRelationshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierProductRelationshipRepository supplierProductRelationshipRepository;
    private final UserService userService;
    private final SupermarketService supermarketService;

    private final RestTemplate restTemplate;

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

    public Product getProductById(String productId, boolean isWithRecommendations) {

        Product product = productRepository.getProductByProductId(productId);
        if (isWithRecommendations) {
            SupplierProductRelationship supplierProductRelationship =
                    supplierProductRelationshipRepository.getSupplierProductRelationshipByProductId(productId);
            product.setSupplier(supplierProductRelationship);

            List<String> asins = getSimilarProductIds(product.getExternalIdentifier());
            if (!CollectionUtils.isEmpty(asins)) {
                List<Product> recommendations = getProductByExternalIdentifiers(asins);
                product.setRecommendedProducts(recommendations);
            }
        }


        return product;
    }

    private List<String> getSimilarProductIds(String asin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        RecommendedProductReqDTO dto = new RecommendedProductReqDTO();
        dto.setAsin(asin);

        HttpEntity<RecommendedProductReqDTO> entity = new HttpEntity<RecommendedProductReqDTO>(dto,headers);

        return restTemplate.exchange("http://127.0.0.1:5000/api",
                HttpMethod.POST, entity, List.class).getBody();
    }

    private List<Product> getProductByExternalIdentifiers(List<String> externalIds) {
        return productRepository.getProductsByExternalIds(externalIds);
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

    public ExplanationResDTO getRecommendationExplanation(ExplanationReqDTO explanationReqDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ExplanationReqDTO> entity =
                new HttpEntity<ExplanationReqDTO>(explanationReqDTO, headers);

        return restTemplate.exchange("http://127.0.0.1:5001/api",
                HttpMethod.POST, entity, ExplanationResDTO.class).getBody();
    }
}
