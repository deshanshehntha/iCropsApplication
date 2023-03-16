package com.example.servicebackend.service;

import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.servicebackend.constants.Response;
import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.dto.SupermarketSupplierRelationship;
import com.example.servicebackend.dto.SupplierProductRelationship;
import com.example.servicebackend.dto.actor.Supplier;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.dto.product.ProductReaderDTO;
import com.opencsv.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CSVReaderTest {

    private final UserService userService;
    private final ProductService productService;
    private final SupermarketService supermarketService;

    private static final String CSV_FILE_PATH = "sample.csv";


    public void syncData() {
        List<ProductReaderDTO> productReaderDTOS = readAllDataAtOnce(CSV_FILE_PATH);
        List<ProductReaderDTO> productsWithPrice = productReaderDTOS.stream().filter(productReaderDTO -> !productReaderDTO.getPrice().isEmpty()).collect(Collectors.toList());
        List<ProductReaderDTO> productsWithSupplier = productsWithPrice.stream().filter(productReaderDTO -> !productReaderDTO.getBrand().isEmpty()).collect(Collectors.toList());

        Set<String> set = new HashSet<>(productsWithSupplier.size());
        productsWithSupplier.stream().filter(p -> set.add(p.getBrand())).collect(Collectors.toList());

        Map<Integer,List<String>> sublists = split(set.stream().toList());

        Map<String, String> allSupplierNameToIdMap = new HashMap<>();

        for (List<String> list: sublists.values()) {

            Map<String, String> splittedNameToSupplierIdMap = createSuppliers(list);
            allSupplierNameToIdMap.putAll(splittedNameToSupplierIdMap);
        }

        for (ProductReaderDTO productReaderDTO : productsWithSupplier) {
            Product product = new Product();
            SupplierProductRelationship supplierProductRelationship = new SupplierProductRelationship();
            supplierProductRelationship.setSupplierId(allSupplierNameToIdMap.get(productReaderDTO.getBrand()));
            product.setSupplier(supplierProductRelationship);
            product.setProductName(productReaderDTO.getTitle());
            product.setCategory(productReaderDTO.getCategory());
            product.setDescription(productReaderDTO.getDescription());
            product.setExternalIdentifier(productReaderDTO.getAsin());
            if (productReaderDTO.getImageURLHighRes().isEmpty()) {
                product.setImageLink(productReaderDTO.getImageURL());
            } else {
                product.setImageLink(productReaderDTO.getImageURLHighRes());
            }
            product.setPrice(productReaderDTO.getPrice());
            Response response = productService.createProduct(product);
            Logger.getGlobal().log(Level.INFO, response.getId());
        }

    }

    private Map<String, String>  createSuppliers(List<String> suppliers) {
        Map<String, String> nameToSupplierIdMap = new HashMap<>();
        for (String name: suppliers) {
            User supplier = new Supplier();
            supplier.setName(name);
            supplier.setPassword("123@");
            supplier.setUserName(name);
            supplier.setUserType(UserType.SUPPLIER);
            Response response = userService.createUser(supplier);
            Logger.getGlobal().log(Level.INFO, response.getId());
            nameToSupplierIdMap.put(name, response.getId());
        }

        Supermarket supermarket = new Supermarket();
        supermarket.setSupermarketName("Supermarket" + String.valueOf(Math.random()));

        List<SupermarketSupplierRelationship> supermarketSupplierRelationships = new ArrayList<>();

        for (String supplierId : nameToSupplierIdMap.values()) {
            SupermarketSupplierRelationship supermarketSupplierRelationship = new SupermarketSupplierRelationship();
            supermarketSupplierRelationship.setSupplierId(supplierId);
            supermarketSupplierRelationships.add(supermarketSupplierRelationship);
        }

        supermarket.setSuppliers(supermarketSupplierRelationships);
        supermarketService.createNewSupermarket(supermarket);
        return nameToSupplierIdMap;
    }

//    public static void main(String[] args) {
//        syncData();
//    }



    public static List<ProductReaderDTO> readAllDataAtOnce(String file)
    {
        try {

            // Create an object of filereader class
            // with CSVReaderTest file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object
            // and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();

            List<ProductReaderDTO> productReaderDTOS = new ArrayList<>();

            // print Data
            for (String[] row : allData) {
                ProductReaderDTO product = new ProductReaderDTO(
                        row[0],row[1],row[2],row[3],row[4],row[5],row[6],row[7],row[8],row[9],row[10],row[11],
                        row[12],row[13],row[14],row[15],row[16],row[17],row[18],row[19],row[20]
                );
                productReaderDTOS.add(product);
            }

            return productReaderDTOS;

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Map<Integer,List<String>> split(List<String> suppliers) {
        Map<Integer,List<String>> fourListMap = new HashMap<>();


        Map<Integer, List<String>> firstDivide = splitToTwoLists(suppliers);
        Map<Integer, List<String>> secondDivide = splitToTwoLists(firstDivide.get(1));
        fourListMap.put(1, secondDivide.get(1));
        fourListMap.put(2, secondDivide.get(2));
        Map<Integer, List<String>> thirdDivide = splitToTwoLists(firstDivide.get(2));
        fourListMap.put(3, thirdDivide.get(1));
        fourListMap.put(4, thirdDivide.get(2));
        return fourListMap;
    }

    private static Map<Integer, List<String>> splitToTwoLists(List<String> initialList) {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();

        for (String entry : initialList)
        {
            if (list1.size () > list2.size ())
                list2.add (entry);
            else
                list1.add( entry );
        }

        Map<Integer,List<String>> sublists = new HashMap<>();
        sublists.put(1, list1);
        sublists.put(2, list2);
        return sublists;
    }
}