package com.example.test1;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ProductService(RestTemplate restTemplate, SupplierRepository supplierRepository) {
        this.restTemplate = restTemplate;
        this.supplierRepository = supplierRepository;
    }


    public List<Product> fetchAllProductsFromTunisianet() {
        Supplier supplier = supplierRepository.findByName("Tunisia-Net")
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        String url = supplier.getApiUrl();
        int page = 0;
        List<Product> allProducts = new ArrayList<>();

        int totalPages;
        do {
            String paginatedUrl = String.format("%s?page=%d&size=%d", url, page, 10);
            String response = restTemplate.getForObject(paginatedUrl, String.class);

            List<String> names = JsonPath.read(response, supplier.getProductNamePath());
            List<String> descriptions = JsonPath.read(response, supplier.getProductDescPath());
            List<String> urls = JsonPath.read(response, supplier.getProductUrlPath());
            List<Object> priceObjects = JsonPath.read(response, supplier.getProductPricePath());

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
                    allProducts.add(new Product(name, description, urlProduct, price));
                } else {
                    System.out.println("Le prix est nul pour le produit : " + name);
                }
            }
            totalPages = JsonPath.read(response, "$.totalPages");
            page++;

        } while (page < totalPages);

        return allProducts;
    }
    public List<Product> fetchAllProductsFromFakeProduct() {
        Supplier supplier = supplierRepository.findByName("Fake-Product")
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        String url = supplier.getApiUrl();
        int page = 0;
        List<Product> allProducts = new ArrayList<>();

       int totalPages;
       do {
            String paginatedUrl = String.format("%s?page=%d&size=%d", url, page, 5);
            String response = restTemplate.getForObject(paginatedUrl, String.class);

            List<String> names = JsonPath.read(response, supplier.getProductNamePath());
            List<String> descriptions = JsonPath.read(response, supplier.getProductDescPath());
            List<String> urls = JsonPath.read(response, "$.content[*].image");
            List<Object> priceObjects = JsonPath.read(response, supplier.getProductPricePath());

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
                    allProducts.add(new Product(name, description,url, price));
                } else {
                    System.out.println("Le prix est nul pour le produit : " + name);
                }
            }
           totalPages = JsonPath.read(response, "$.totalPages");
            page++;

        } while (page < totalPages);

        return allProducts;
    }
    public List<Product> fetchProductsFromMyTech() {
        Supplier supplier = supplierRepository.findByName("My-Tech")
                .orElseThrow(() -> new RuntimeException("Fournisseur 'My-Tech' non trouvé"));

        String response;
        try {
            response = restTemplate.getForObject(supplier.getApiUrl(), String.class);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des produits de My-Tech: " + e.getMessage());
            return new ArrayList<>();
        }

        List<String> names = JsonPath.read(response, supplier.getProductNamePath());
        List<String> descriptions = JsonPath.read(response, supplier.getProductDescPath());
        List<String> urls = JsonPath.read(response, supplier.getProductUrlPath());
        List<Object> priceObjects = JsonPath.read(response, supplier.getProductPricePath());

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String description = descriptions.get(i);
            String url = urls.get(i);
            Double price = null;

            if (priceObjects.size() > i) {
                Object priceObj = priceObjects.get(i);
                if (priceObj instanceof Number) {
                    price = ((Number) priceObj).doubleValue();
                } else if (priceObj instanceof String) {
                    try {
                        price = Double.parseDouble((String) priceObj);
                    } catch (NumberFormatException e) {
                        System.err.println("Format de prix invalide pour le produit '" + name + "': " + priceObj);
                    }
                }
            }

            // Ajouter le produit seulement si le prix est valide
            if (price != null) {
                products.add(new Product(name, description, url, price));
            } else {
                System.err.println("Le prix est nul pour le produit: " + name);
            }
        }
        return products;
    }
    public List<Product> fetchAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(fetchAllProductsFromTunisianet());
        allProducts.addAll(fetchAllProductsFromFakeProduct());
        allProducts.addAll(fetchProductsFromMyTech());

        return allProducts;
    }


}
