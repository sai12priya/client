package com.example.ims.Controller;

import java.util.ArrayList;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ims.Entity.Product;
import com.example.ims.Entity.Purchase;
import com.example.ims.Entity.Vendor;
import com.example.ims.dto.PurchaseDTO;

@Controller
@RequestMapping("/admin")
public class PurchaseController {

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/purchase")
	public String showMakePurchase(Model model) {
		String url="http://localhost:8002/vendor/allVendors";
		ResponseEntity<List<Vendor>> response = restTemplate.exchange(
		        url,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<List<Vendor>>() {}
		    );
		List<Vendor> allvendors = response.getBody() ;
		model.addAttribute("vendors", allvendors);
		String url2="http://localhost:8002/purchase/recentVendors";
		ResponseEntity<List<Vendor>> response1 = restTemplate.exchange(
		        url2,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<List<Vendor>>() {}
		    );
		List<Vendor> recentVendors = response1.getBody() ;
		model.addAttribute("all8vendors", recentVendors);
		
		 model.addAttribute("purchaseDTO", new PurchaseDTO());
		return "makePurchase";
	}

	 @PostMapping("/purchase")
	    public String submitPurchase( @ModelAttribute("purchaseDTO") PurchaseDTO purchaseDTO, 
	                               BindingResult bindingResult,
	                               Model model,
	                               RedirectAttributes redirectAttributes) {
	        
	        if (bindingResult.hasErrors()) {
	            // If there are validation errors, return to the form
	            List<Vendor> vendors = fetchVendors(); // You need to implement this method
	            model.addAttribute("vendors", vendors);
	            return "makePurchase";
	        }
	        
	        // Debug print
	        System.out.println("Received purchase data:");
	        System.out.println("Vendor ID: " + purchaseDTO.getVendorId());
	        System.out.println("Product Name: " + purchaseDTO.getProductName());
	        System.out.println("Quantity: " + purchaseDTO.getQuantityAvailable());
	        System.out.println("Price: " + purchaseDTO.getPrice());
	        System.out.println("Total Amount: " + purchaseDTO.getTotalAmount());
	        System.out.println("Purchase Date: " + purchaseDTO.getPurchaseDate());
	        System.out.println("Expected Delivery Date: " + purchaseDTO.getExpectedDeliveryDate());

	        // Convert DTO to Purchase entity
	        Purchase purchaseRequest = new Purchase();
	        
	        // Set product
	        Product product = new Product();
	        product.setName(purchaseDTO.getProductName());
	        
	        product.setPrice(purchaseDTO.getPrice());
	        product.setQuantityAvailable(purchaseDTO.getQuantityAvailable());
	        
	        // Set vendor
	        Vendor vendor = new Vendor();
	        vendor.setId(purchaseDTO.getVendorId());
	        vendor.setProduct(product);
	        purchaseRequest.setVendor(vendor);
	        
	        // Set purchase details
	        purchaseRequest.setPrice(purchaseDTO.getPrice());
	        purchaseRequest.setQuantityAvailable(purchaseDTO.getQuantityAvailable());
	        purchaseRequest.setTotalAmount(purchaseDTO.getTotalAmount());
	        purchaseRequest.setPurchaseDate(purchaseDTO.getPurchaseDate());
	        purchaseRequest.setExpectedDeliveryDate(purchaseDTO.getExpectedDeliveryDate());

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Purchase> request = new HttpEntity<>(purchaseRequest, headers);

	        String publisherUrl = "http://localhost:8002/purchase/add";
	        try {
	            ResponseEntity<Purchase> response = restTemplate.postForEntity(publisherUrl, request, Purchase.class);
	            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	                redirectAttributes.addFlashAttribute("success", "Purchase completed successfully!");
	                return "redirect:/admin/purchase";
	            } else {
	                model.addAttribute("error", "Purchase Failed");
	            }
	        } catch (HttpClientErrorException e) {
	            model.addAttribute("error", "Error: " + e.getMessage());
	        } catch (Exception e) {
	            model.addAttribute("error", "Error: " + e.getMessage());
	        }
	        
	        List<Vendor> vendors = fetchVendors(); // You need to implement this method
	        model.addAttribute("vendors", vendors);
	        return "makePurchase";
	    }
	 private List<Vendor> fetchVendors() {
	        // Implement this method to fetch vendors from your API
	        // For example:
	        // return restTemplate.getForObject("http://localhost:8002/vendors", List.class);
	        return new ArrayList<>(); // placeholder
	    }
	 @GetMapping("/invoice")
	 public String invoicePage(Model model) {
		 String url1="http://localhost:8002/purchase/allPurchases";
			ResponseEntity<List<Purchase>> response = restTemplate.exchange(
			        url1,
			        HttpMethod.GET,
			        null,
			        new ParameterizedTypeReference<List<Purchase>>() {}
			    );
			List<Purchase> allpurchases = response.getBody() ;
			model.addAttribute("purchases", allpurchases);
		 return "invoices";
	 }
}
