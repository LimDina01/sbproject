package dina.com.sbproject.controller;

import dina.com.sbproject.model.Product;
import dina.com.sbproject.model.Category;
import dina.com.sbproject.service.ProductService;
import dina.com.sbproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Serve the Add Product Page
    @RequestMapping("/add_product")
    public String showAddProductForm(org.springframework.ui.Model model) {
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "admin/add_product";
    }

    // Add a new product
    @PostMapping("/add_product")
    @ResponseBody
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getDescription() == null || product.getPrice() == null || product.getCategory() == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        // Validate category
        Category category = categoryService.getCategoryById(product.getCategory().getId());
        if (category == null) {
            return ResponseEntity.badRequest().body("Invalid category ID");
        }
        product.setCategory(category);

        // Save the product
        productService.saveProduct(product);
        return ResponseEntity.ok("Product added successfully");
    }

    // Add a new category
    @PostMapping("/add_category")
    @ResponseBody
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required");
        }
        categoryService.saveCategory(category);
        return ResponseEntity.ok("Category added successfully");
    }

    // Get all products as JSON for frontend
    @GetMapping("/api/products")
    @ResponseBody
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get a single product by ID
    @GetMapping("/api/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/api/products/update")
    @ResponseBody
    public ResponseEntity<String> updateProduct(@RequestBody Product updatedProduct) {
        // Reject if the ID is missing or invalid
        if (updatedProduct.getId() == null || updatedProduct.getId() <= 0) {
            return ResponseEntity.badRequest().body("Invalid product ID for update");
        }

        // Fetch the existing product
        Product existingProduct = productService.getProductById(updatedProduct.getId());
        if (existingProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        // Update fields
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        // Validate category
        Category category = categoryService.getCategoryById(updatedProduct.getCategory().getId());
        if (category == null) {
            return ResponseEntity.badRequest().body("Invalid category ID");
        }
        existingProduct.setCategory(category);

        // Save the updated product
        productService.saveProduct(existingProduct);
        return ResponseEntity.ok("Product updated successfully");
    }




    // Delete a product by ID
    @DeleteMapping("/product/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam Long id) {
        boolean isDeleted = productService.deleteProductById(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }
}
