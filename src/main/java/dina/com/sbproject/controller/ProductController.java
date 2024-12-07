package dina.com.sbproject.controller;

import dina.com.sbproject.model.Product;
import dina.com.sbproject.model.Category;
import dina.com.sbproject.service.ProductService;
import dina.com.sbproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping("/add_product")
    public String showAddProductForm(Model model) {
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @PostMapping("/add_product")
    @ResponseBody
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        // Validate required fields
        if (product.getName() == null || product.getDescription() == null || product.getPrice() == null || product.getCategory() == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        // Validate and fetch the category
        Category category = categoryService.getCategoryById(product.getCategory().getId());
        if (category == null) {
            return ResponseEntity.badRequest().body("Invalid category ID");
        }

        product.setCategory(category);

        // Save the product with timestamps
        productService.saveProduct(product);

        return ResponseEntity.ok("Product added successfully");
    }


//    @PostMapping("/add_category")
//    public String addCategory(@ModelAttribute Category category) {
//        categoryService.saveCategory(category);
//        return "redirect:/add_product";
//    }

    @PostMapping("/add_category")
    @ResponseBody
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return ResponseEntity.ok("Category added successfully");
    }


    @GetMapping("/api/products")
    @ResponseBody
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    // Show the edit form for a specific product
    @GetMapping("/product/edit")
    public String showEditProductForm(@RequestParam Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/edit_product";
    }

    // Handle the form submission to update the product
    @PostMapping("/product/update")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            @RequestParam String imageUrl) {

        Product product = productService.getProductById(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        Category category = categoryService.getCategoryById(categoryId);
        product.setCategory(category);

        productService.saveProduct(product);

        return "redirect:/products";
    }

    // Handle product deletion with DELETE request
//    @DeleteMapping("/product/delete")
//    public String deleteProduct(@RequestParam Long id) {
//        productService.deleteProductById(id);
//        return "redirect:/products";
//    }

//    @DeleteMapping("/product/delete")
//    public ResponseEntity<String> deleteProduct(@RequestParam Long id) {
//        boolean isDeleted = productService.deleteProductById(id); // Assuming your service returns a boolean
//
//        if (isDeleted) {
//            return ResponseEntity.noContent().build(); // 204 No Content
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"); // 404 Not Found
//        }
//    }

@DeleteMapping("/product/delete")
public ResponseEntity<String> deleteProduct(@RequestParam Long id) {
    boolean isDeleted = productService.deleteProductById(id);

    // Since we always return true now, we can assume success
    return ResponseEntity.noContent().build(); // 204 No Content
}



}
