package com.example.test1;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    private final RestTemplate restTemplate;
    private final SupplierRepository supplierRepository;
    private final ApiConfigurationRepository apiConfigRepo;

    @Autowired
    public ProductService(RestTemplate restTemplate, SupplierRepository supplierRepository, ApiConfigurationRepository apiConfigRepo) {
        this.restTemplate = restTemplate;
        this.supplierRepository = supplierRepository;
        this.apiConfigRepo = apiConfigRepo;
    }
    public List<Product> fetchProductsFromMyTech() {

        ApiConfiguration config = apiConfigRepo.findBySupplier_NameAndApiType("My-Tech", "REST")
                .orElseThrow(() -> new RuntimeException("Configuration REST non trouvée"));

        String response = restTemplate.getForObject(config.getApiUrl(), String.class);
        List<String> names = JsonPath.read(response, config.getProductNamePath());
        List<String> descriptions = JsonPath.read(response, config.getProductDescPath());
        List<String> urls = JsonPath.read(response, config.getProductUrlPath());
        List<Object> priceObjects = JsonPath.read(response, config.getProductPricePath());


        List<Product> products = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String description = descriptions.get(i);
            String urlProduct = urls.get(i);
            Double price = null;
            if (priceObjects.size() > i) {
                Object priceObj = priceObjects.get(i);
                if (priceObj instanceof Number) {
                    price = ((Number) priceObj).doubleValue();
                } else if (priceObj instanceof String) {
                    try {
                        price = Double.parseDouble((String) priceObj);
                    } catch (NumberFormatException e) {
                        System.out.println("Format de prix invalide pour le produit : " + name);
                    }
                }
            }
            if (price != null) {
                products.add(new Product(name, description, urlProduct, price));
            } else {
                System.out.println("Le prix est nul pour le produit : " + name);
            }
        }
        return products;
    }

    public List<Product> fetchProductsFromTunisianet() {
        List<Product> products = new ArrayList<>();
        Optional<ApiConfiguration> configOptional = apiConfigRepo.findBySupplier_NameAndApiType("Tunisianet", "REST");

        if (configOptional.isEmpty()) {
            return products;
        }

        ApiConfiguration config = configOptional.get();
        String apiUrl = config.getApiUrl();
        int page = 0;
        int size = 10;
        int totalPages;

        do {
            String paginatedUrl = String.format("%s?%s=%d&%s=%d", apiUrl, config.getPageParamName(), page, config.getSizeParamName(), size);
            String response = restTemplate.getForObject(paginatedUrl, String.class);

            if (response == null) {
                break;
            }

            List<String> names = JsonPath.read(response, config.getProductNamePath());
            List<String> descriptions = JsonPath.read(response, config.getProductDescPath());
            List<String> urls = JsonPath.read(response, config.getProductUrlPath());
            List<Object> priceObjects = JsonPath.read(response, config.getProductPricePath());

            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String description =descriptions.get(i);
                String urlProduct = urls.get(i) ;
                Double price = extractPrice(priceObjects, i, name);

                if (price != null) {
                    products.add(new Product(name, description, urlProduct, price));
                } else {
                    System.out.println("Le prix est nul pour le produit : ");
                }
            }

            totalPages = JsonPath.read(response, "$.totalPages");
            page++;
        } while (page < totalPages);

        return products;
    }

    private Double extractPrice(List<Object> priceObjects, int index, String productName) {
        if (priceObjects.size() > index) {
            Object priceObj = priceObjects.get(index);
            try {
                if (priceObj instanceof Number) {
                    return ((Number) priceObj).doubleValue();
                } else if (priceObj instanceof String) {
                    return Double.parseDouble((String) priceObj);
                }
            } catch (NumberFormatException e) {
                System.out.println("Format de prix invalide pour le produit : " + productName);
            }
        }
        return null;
    }
    public List<Product> fetchProductsFromFakeProduct() {
        List<Product> products = new ArrayList<>();
        Optional<ApiConfiguration> configOptional = apiConfigRepo.findBySupplier_NameAndApiType("Fake-Product", "REST");

        if (configOptional.isEmpty()) {
            return products;
        }

        ApiConfiguration config = configOptional.get();
        String apiUrl = config.getApiUrl();
        int page = 0;
        int size = 10;
        int totalPages;

        do {
            String paginatedUrl = String.format("%s?%s=%d&%s=%d", apiUrl, config.getPageParamName(), page, config.getSizeParamName(), size);
            String response = restTemplate.getForObject(paginatedUrl, String.class);

            if (response == null) {
                break;
            }

            List<String> names = JsonPath.read(response, config.getProductNamePath());
            List<String> descriptions = JsonPath.read(response, config.getProductDescPath());
            List<String> urls = JsonPath.read(response, "$.content[*].image");
            List<Object> priceObjects = JsonPath.read(response, config.getProductPricePath());

            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String description =descriptions.get(i);
                String urlProduct = urls.get(i) ;
                Double price = extractPrice(priceObjects, i, name);

                if (price != null) {
                    products.add(new Product(name, description, urlProduct, price));
                } else {
                    System.out.println("Le prix est nul pour le produit : ");
                }
            }

            totalPages = JsonPath.read(response, "$.totalPages");
            page++;
        } while (page < totalPages);

        return products;
    }
    public List<Product> fetchAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(fetchProductsFromTunisianet());
        allProducts.addAll(fetchProductsFromFakeProduct());
        allProducts.addAll(fetchProductsFromMyTech());

        return allProducts;
    }
}
