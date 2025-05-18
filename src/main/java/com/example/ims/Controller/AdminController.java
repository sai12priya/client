package com.example.ims.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.ims.Entity.Purchase;
import com.example.ims.Entity.User;
import com.example.ims.Entity.Vendor;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/login")
    public String showAdminLoginPage(Model model) {
    	 model.addAttribute("error", null);
        return "adminlogin";
    }
	@GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
    	 model.addAttribute("error", null);
    	 
    	 String url2="http://localhost:8002/purchase/recentVendors";
 		ResponseEntity<List<Vendor>> response1 = restTemplate.exchange(
 		        url2,
 		        HttpMethod.GET,
 		        null,
 		        new ParameterizedTypeReference<List<Vendor>>() {}
 		    );
 		List<Vendor> recentVendors = response1.getBody() ;
 		model.addAttribute("all8vendors", recentVendors);
 		
 		String url3="http://localhost:8002/product/countProducts";
 		ResponseEntity<Integer> response2 = restTemplate.exchange(
 		        url3,
 		        HttpMethod.GET,
 		        null,
 		        new ParameterizedTypeReference<Integer>() {}
 		    );
 		int totalProducts = response2.getBody() ;
 		model.addAttribute("totalProducts", totalProducts);
 		
 		String url5="http://localhost:8002/vendor/countVendors";
 		ResponseEntity<Integer> response4 = restTemplate.exchange(
 		        url5,
 		        HttpMethod.GET,
 		        null,
 		        new ParameterizedTypeReference<Integer>() {}
 		    );
 		int totalVendors = response4.getBody() ;
 		model.addAttribute("totalVendors", totalVendors);
 		
 		
 		String url6="http://localhost:8002/vendor/countActive";
 		ResponseEntity<Integer> response5 = restTemplate.exchange(
 		        url6,
 		        HttpMethod.GET,
 		        null,
 		        new ParameterizedTypeReference<Integer>() {}
 		    );
 		int activeVendors = response5.getBody() ;
 		model.addAttribute("activeVendors", activeVendors);
 		
 		String url4 = "http://localhost:8002/purchase/productStats";
 		ResponseEntity<Map<String, String>> response3 = restTemplate.exchange(
 		    url4,
 		    HttpMethod.GET,
 		    null,
 		    new ParameterizedTypeReference<Map<String, String>>() {}
 		);

 		if (response3.getStatusCode().is2xxSuccessful()) {
 		    Map<String, String> body = response3.getBody();
 		    model.addAttribute("productMessage", body != null ? body.get("productMessage") : "No product data available");
 		    model.addAttribute("purchaseMessage", body != null ? body.get("purchaseMessage") : "No purchase data available");
 		   model.addAttribute("amount", body != null ? body.get("amount") : "No purchase data available");
 		} else {
 		    model.addAttribute("productMessage", "Error loading product statistics");
 		    model.addAttribute("purchaseMessage", "Error loading purchase statistics");
 		   model.addAttribute("amount", "Error loading purchase statistics");
 		}
 		String url7="http://localhost:8002/purchase/recentPurchases";
 		ResponseEntity<List<Purchase>> response6 = restTemplate.exchange(
 		        url7,
 		        HttpMethod.GET,
 		        null,
 		        new ParameterizedTypeReference<List<Purchase>>() {}
 		    );
 		List<Purchase> recentPurchases = response6.getBody() ;
 		model.addAttribute("recent8invoices",recentPurchases);
 		
        return "adminDashboard";
    }
	
	
	@PostMapping("/login")
	public String processAdminLogin(
	    @RequestParam String username,
	    @RequestParam String password,
	    Model model
	) {
	    String serviceUrl = "http://localhost:8002/admin/login";
	    User loginRequest = new User(username, password, "ADMIN"); // Role not needed for login

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        ResponseEntity<User> response = restTemplate.exchange(
	            serviceUrl,
	            HttpMethod.POST,
	            new HttpEntity<>(loginRequest, headers),
	            User.class
	        );

	        return "redirect:/admin/dashboard"; // If we get here, login was successful
	        
	    } catch (HttpClientErrorException e) {
	        // Handle 4xx errors from publisher
	        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
	            model.addAttribute("error", "Invalid username or password");
	        } else {
	            model.addAttribute("error", "Access denied");
	        }
	    } catch (Exception e) {
	        // Handle all other errors (5xx, connection issues)
	        model.addAttribute("error", "Service unavailable. Please try again later.");
	    }
	    
	    return "adminlogin"; // Return to login page with error message
	}
}
