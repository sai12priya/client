package com.example.ims.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.example.ims.Entity.Product;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/all")
	public String showProductsPage(Model model) {
		String url1="http://localhost:8002/product/allProducts";
		ResponseEntity<List<Product>> response = restTemplate.exchange(
		        url1,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<List<Product>>() {}
		    );
		List<Product> allproducts = response.getBody() ;
		model.addAttribute("products", allproducts);
		return "allProducts";
	}
	@GetMapping("/edit/{pid}")
	public String showEditProductsPage(Model model,@PathVariable Long pid) {
		String url1="http://localhost:8002/product/byid?id=" + pid;
		ResponseEntity<Product> response = restTemplate.exchange(
		        url1,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<Product>() {}
		    );
		Product p = response.getBody() ;
		model.addAttribute("product", p);
	return "editProducts";
	}
	@PostMapping("/update")
	public String updateProduct(@ModelAttribute Product product, Model model) {
		try {
	        String url = "http://localhost:8002/product/update"; 

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<Product> request = new HttpEntity<>(product, headers);

	        ResponseEntity<Product> response = restTemplate.postForEntity(url, request, Product.class);

	        if (response.getStatusCode().is2xxSuccessful()) {
	        	model.addAttribute("success", "Updated");
	            return "redirect:/admin/product/all";
	        } else {
	            model.addAttribute("error", "Failed to update product. Try again.");
	            return "editProducts";
	        }

	    } catch (Exception e) {
	        model.addAttribute("error", "Server error: " + e.getMessage());
	        return "editProducts";
	    }
	}
}
