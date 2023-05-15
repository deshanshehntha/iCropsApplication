package com.example.servicebackend.service;

import java.io.FileReader;
import java.sql.Array;
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
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.dto.product.ProductReaderDTO;
import com.opencsv.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CSVReaderTest {

    private final UserService userService;
    private final ProductService productService;
    private final SupermarketService supermarketService;

    private static final String CSV_FILE_PATH = "sample.csv";


    public void syncData() {

        String [] prods_with_reviews = {"B0000TL6B8", "B0000TL5UK", "B0001BGU0W", "B0001BGU3Y", "B0001BGU0C", "B0001BGTW6", "B0001BGTXU", "B0001FQV7K", "B0002MBG22", "B0002NVLK8", "B0002Q1X6C", "B00061B8G8", "B0006TLIG0", "B0007SMLUM", "B0008IUV0S", "B0009HL7CY", "B000BWSL6W", "B000E5DKLM", "B000ESZWXS", "B000ET1678", "B000G66N4U", "B000LKYTRY", "B000LRKQ0G", "B000M0VJJE", "B000MBM1FO", "B000MGWIKM", "B000MRZI6M", "B000NOCRO0", "B000NPAL74", "B000NSFQJE", "B000NSGULM", "B000NSGUJ4", "B000NSFQJ4", "B000NSH21E", "B000NSIAG0", "B000NSKB7Q", "B000NSKB8K", "B000NSKB80", "B000OLBA9A", "B000P6G0KS", "B000P6G0EO", "B000P6G0GM", "B000P6G0Q2", "B000P6G1AC", "B000P6G13E", "B000P6G11Q", "B000P6G2QK", "B000P6G0FI", "B000P6H29Q", "B000P6G0MQ", "B000P6G11G", "B000P6H1Z6", "B000P6H214", "B000P6H20A", "B000P6H23W", "B000P6G0QW", "B000P6H2E6", "B000P6H21O", "B000P6H2MI", "B000P6H2J6", "B000P6H2PA", "B000P6H3V8", "B000P6H3UY", "B000P6J1CM", "B000P6J19A", "B000P6J1J0", "B000P6J0ZK", "B000P6J1P4", "B000P6G0ZI", "B000P6J2TE", "B000P6G0MG", "B000P6G13Y", "B000P6J1EA", "B000P6J0SC", "B000P6H2DM", "B000P6L3T6", "B000P6J1H2", "B000P6L3SM", "B000P6L440", "B000P6L45Y", "B000P6L5H6", "B000P6L49A", "B000P6L4AY", "B000P6J1A4", "B000P6L3ZK", "B000P6L454", "B000P6L3XM", "B000P6L3IW", "B000P6L4B8", "B000P6L46S", "B000P6L3VO", "B000P6L42M", "B000P6L486", "B000P6J1C2", "B000P6X5F6", "B000P7188G", "B000P7BJMQ", "B000PKMAOY", "B000QT5ZGO", "B000R8ZYLK", "B000RGWJX8", "B000RGWKIM", "B000RGZN54", "B000RH1NF2", "B000RHXHBK", "B000SAUVC4", "B000SE7Z74", "B000SE7ZB0", "B000SE7ZY2", "B000SE9OZU", "B000SE9OYG", "B000STWFS8", "B000V1WXJO", "B0011CXRVK", "B0011CXRWE", "B0011CXROM", "B0012AQN7G", "B001394Z30", "B0015P54R8", "B00176MV3A", "B0019JRLC6", "B0019L8JMU", "B001EO5ZCE", "B001EO7IBU", "B001EQ57SE", "B001EQ5MGQ", "B001FWXMHA", "B001GIP2BM", "B001GQ29V0", "B001HTKNFA", "B001ID1PNE", "B001JK18A6", "B001KNCKOA", "B001L1J0YE", "B001L1KRSC", "B001LA3EY2", "B001PF5E1O", "B001PLET96", "B001PLESVU", "B001PLGQPQ", "B001PLETDC", "B001PLIUJ6", "B001SAWZF4", "B001SAWYYG", "B001UII3PU", "B001V9BR5Q", "B001VNEBAK", "B001W3T2SK", "B001W3T2E4", "B0021HTZJW", "B00286NBJ6", "B0029SBYV0", "B002B9280S", "B002KI3PB6", "B002QY5PB2", "B002QY3U5A", "B002TTJHQS", "B0035APRLO", "B0038OJPOM", "B003AYHFGU", "B003IMLTVQ", "B003IMLTTS", "B003L1AP14", "B003LN4BM6", "B003SNZ3F8", "B003TQ4MXS", "B003TQ59WQ", "B003TQ8FDG", "B003XUFR98", "B0043XEL14", "B00444X8LQ", "B0045IJR3O", "B004DAQPR0", "B004H1OHFW", "B004LG0BBW", "B004VITERM", "B004WZ4F3M", "B004X6ZGAQ", "B0050PRLE8", "B0053W5EMO", "B005CD3B7O", "B005I5F9BC", "B005KS07NM", "B005NEL25U", "B005PDH032", "B005PFGQ4O", "B005STXAIM", "B005W7C3YW", "B005W7ZN12", "B005WBSPK4", "B005WBSMFW", "B006562H0E", "B006Q6MXFC", "B006UCUHRI", "B007HUS8DW", "B007RALZZA", "B007RAM1GW", "B007ZU3JEM", "B008XBHDD0", "B0095SP5IU", "B0096RC2WW", "B0098Q0QEM", "B009IXF596", "B00A9PB8SO", "B00AG3F3V2", "B00AG3WBP8", "B00AH0WO3O", "B00APL8Q2S", "B00APTXF62", "B00AQOM9GI", "B00ARMG1AY", "B00B6QUFK2", "B00B7XP56I", "B00BHWNEG2", "B00BSWNPG0", "B00BSX47MK", "B00BSYLN3U", "B00BVUAE3Q", "B00BVUADVY", "B00BVUAPQ2", "B00BVUAJWM", "B00DCYRRBQ", "B00E3J92PS", "B00E3J9QSG", "B00E3J8XHQ", "B00E3JDXOO", "B00E3JELZ4", "B00E3JD6OQ", "B00E3JDGL4", "B00E3JG94A", "B00E3JDI8K", "B00E3JENDO", "B00E3JGDU0", "B00E3JF2RK", "B00E3JLRG0", "B00E3JLNZU", "B00E3JLSWI", "B00E3JGJYU", "B00E3JL3P0", "B00E3K16HY", "B00E3JJCT4", "B00E3K22M2", "B00E3K25M4", "B00E3KS9NI", "B00E3KWTWU", "B00E3KWX9E", "B00E3KX32K", "B00E3KWQKA", "B00E3KX5S2", "B00E3T4R5I", "B00EG05K7I", "B00EGCE9ZA", "B00EKQT9UM", "B00ERNPE8E", "B00ET2IXBS", "B00EWNPDVC", "B00F3J9DAG", "B00F9WYX02", "B00FCAQT6W", "B00GVFF6IY", "B00GVNZ2BW", "B00GYHWT2A", "B00H8VW81S", "B00IFKUNMW", "B00IIO1LU8", "B00IP6V7DK", "B00J3B131C", "B00JG1ZGAS", "B00JNWZYSY", "B00KMM8FGW", "B00LW1IQO8", "B00MFNKAW8", "B00MOPZ4IW", "B00MWTXLF8", "B00NE85992", "B00NWXCMJY", "B00NYY3LOG", "B00OA5EYE4", "B00OIA79CU", "B00P6ESLI8", "B00PAU86T2", "B00Q2SWZZW", "B00RRCRMIC", "B00S8RIZGS", "B00T2GJXBU", "B00TOM2ESG", "B00UNJELBM", "B00V57U9OE", "B00V6O0FJK", "B00VIYFF6Q", "B00VJ9PGE6", "B00VX7NWEU", "B00VZ5BJNQ", "B00X8A2BS8", "B00Y3JWDYU", "B0122VBTA4", "B012H9Z1SW", "B014300E44", "B014532RGW", "B014I0DHUM", "B014I0TWW4", "B014YP5X9O", "B015U3PXYE", "B017AAOGDO", "B017DXQ13Q", "B017HU168U", "B017JJ09KA", "B018JJTYR8", "B018JJTYSW", "B018JJTYRI", "B018KIJ9Y6", "B018RN84DG", "B018TE2SB2", "B018WLK6CA", "B0197699IG", "B019MDYXDA", "B01CYJ5X60", "B01D1T3A7Q", "B01D5KWCO8", "B01D5NHV7S", "B01D9579W4", "B01DGJPI5I", "B01DI0LVHO", "B01EB1RXVW", "B01EB1ZYCM", "B01EB2SYCI", "B01ETPGXW0", "B01ETQY2PE", "B01H4F9PV8"};

        List<ProductReaderDTO> productReaderDTOS = readAllDataAtOnce(CSV_FILE_PATH);
        List<ProductReaderDTO> productsWithPrice = productReaderDTOS.stream().filter(productReaderDTO ->
                !productReaderDTO.getPrice().isEmpty()).collect(Collectors.toList());
        List<ProductReaderDTO> productsWithSupplier = productsWithPrice.stream().filter(productReaderDTO ->
                !productReaderDTO.getBrand().isEmpty()).collect(Collectors.toList());

        List<ProductReaderDTO> productsWithSupplierAndImages = productsWithSupplier.stream().filter(productReaderDTO ->

                !productReaderDTO.getImageURL().replaceAll( "^\\[|\\]$", "" ).isEmpty() ||
                        !productReaderDTO.getImageURLHighRes().replaceAll( "^\\[|\\]$", "" ).isEmpty()).collect(Collectors.toList());

        List<ProductReaderDTO> prodWithComments = productsWithSupplierAndImages.stream().filter(
                productReaderDTO -> Arrays.asList(prods_with_reviews).contains(productReaderDTO.getAsin())).collect(Collectors.toList());


        Set<String> set = new HashSet<>(prodWithComments.size());
        prodWithComments.stream().filter(p -> set.add(p.getBrand())).collect(Collectors.toList());

        Map<Integer,List<String>> sublists = split(set.stream().toList());

        Map<String, String> allSupplierNameToIdMap = new HashMap<>();

        for (List<String> list: sublists.values()) {
            Map<String, String> splittedNameToSupplierIdMap = createSuppliers(list);
            allSupplierNameToIdMap.putAll(splittedNameToSupplierIdMap);
        }

        for (ProductReaderDTO productReaderDTO : prodWithComments) {
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
            Supplier supplier = new Supplier();
            supplier.setName(name);
            supplier.setPassword("123@");
            supplier.setUserName(name);
            supplier.setUserType(UserType.SUPPLIER);
            Response response = userService.createSupplier(supplier);
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
        Map<Integer,List<String>> twoListMap = new HashMap<>();

        Map<Integer, List<String>> firstDivide = splitToTwoLists(suppliers);
        twoListMap.put(1, firstDivide.get(1));
        twoListMap.put(2, firstDivide.get(2));
        return twoListMap;
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