package com.example.ims.Controller;

import java.util.HashMap;
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

import com.example.ims.Entity.Product;
import com.example.ims.Entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/register")
    public String showUserRegisterPage(Model model) {
    	 model.addAttribute("error", null);
        return "customerregister";
    }
	@GetMapping("/products")
    public String showUserProductsPage(Model model) {
    	 //model.addAttribute("error", null);
    	 String url1="http://localhost:8002/product/allProducts";
 		ResponseEntity<List<Product>> response = restTemplate.exchange(
 		        url1,
 		        HttpMethod.GET,
 		        null,
 		        new ParameterizedTypeReference<List<Product>>() {}
 		    );
 		List<Product> allproducts = response.getBody() ;
 		model.addAttribute("products", allproducts);
        return "userAllProducts";
    }
	@GetMapping("/orders")
    public String showUserOrdersPage(Model model) {
    	 model.addAttribute("error", null);
        return "orders";
    }
	
	@GetMapping("/invoices")
    public String showUserInvoicePage(Model model) {
    	 model.addAttribute("error", null);
        return "userInvoices";
    }

    

	@PostMapping("/register")
    public String processRegisterUser(@ModelAttribute("user") User user,
                             @RequestParam("confirmpassword") String confirmpassword,
                             Model model) {
        
        try {
            // Prepare request
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", user.getUsername());
            requestBody.put("password", user.getPassword());
            requestBody.put("confirmpassword", confirmpassword);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            String serviceUrl = "http://localhost:8002/user/register";
            
            // Make API call
            ResponseEntity<Map> response = restTemplate.exchange(
                serviceUrl,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                Map.class
            );
            
            // If successful, redirect to login
            return "redirect:/user/login";
            
        } catch (HttpClientErrorException e) {
            // Handle API errors
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Parse the error message from the response
                String errorMessage = "Registration failed";
                try {
                    // Assuming the error response is in JSON format
                    Map<String, String> errorResponse = e.getResponseBodyAs(Map.class);
                    if (errorResponse != null && errorResponse.containsKey("message")) {
                        errorMessage = errorResponse.get("message");
                    }
                } catch (Exception ex) {
                    // Fallback if parsing fails
                    errorMessage = e.getResponseBodyAsString();
                }
                model.addAttribute("error", errorMessage);
            } else {
                model.addAttribute("error", "Registration failed. Please try again later.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Service unavailable. Please try again later.");
        }
        
        return "customerregister";
    }
	
	@GetMapping("/login")
    public String showUserLoginPage(Model model) {
    	 model.addAttribute("error", null);
        return "customerlogin";
    }
	
	
	
	@PostMapping("/login")
	public String processUserLogin(
		    @RequestParam String username, 
		    @RequestParam String password,
		    Model model,
		    HttpSession session
		) {
		    

		    try {
		        // Prepare request
		        Map<String, String> requestBody = new HashMap<>();
		        requestBody.put("username", username);
		        requestBody.put("password", password);
		        
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);
		        String serviceUrl = "http://localhost:8002/user/login";
		        
		        // Make API call
		        ResponseEntity<Map> response = restTemplate.exchange(
		            serviceUrl,
		            HttpMethod.POST,
		            new HttpEntity<>(requestBody, headers),
		            Map.class
		        );
		        
		        // Store user info in session
		        session.setAttribute("username", username);
		        Integer userId = Integer.parseInt((String) response.getBody().get("id"));
		        session.setAttribute("id", userId);
		  
		        session.setAttribute("role", response.getBody().get("role"));
		        
		        return "redirect:/user/dashboard";
		        
		    } catch (HttpClientErrorException e) {
		        String errorMessage = "Login failed";
		        
		        try {
		            // Parse the error response
		            ObjectMapper mapper = new ObjectMapper();
		            Map<String, String> errorResponse = mapper.readValue(
		                e.getResponseBodyAsString(),
		                new TypeReference<Map<String, String>>() {}
		            );
		            
		            if (errorResponse != null && errorResponse.containsKey("message")) {
		                errorMessage = errorResponse.get("message");
		                
		                // Map specific error messages
		                switch (errorMessage) {
		                    case "User not found":
		                        errorMessage = "Username not found. Please check your username";
		                        break;
		                    case "Invalid password":
		                        errorMessage = "Incorrect password. Please try again";
		                        break;
		                    case "Access denied. Customer account required":
		                        errorMessage = "This account is not authorized for customer access";
		                        break;
		                }
		            }
		        } catch (Exception ex) {
		            // Fallback based on HTTP status code
		            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
		                errorMessage = "Username not found. Please check your username";
		            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
		                errorMessage = "Incorrect password. Please try again";
		            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
		                errorMessage = "This account is not authorized for customer access";
		            }
		        }
		        
		        model.addAttribute("error", errorMessage);
		    } catch (Exception e) {
		        model.addAttribute("error", "Service unavailable. Please try again later.");
		    }
		    
		    return "customerlogin";
		}
	
	@GetMapping("/dashboard")
    public String showUserDashboard(Model model) {
    	 model.addAttribute("error", null);
        return "userDashboard";
    }
	

}
