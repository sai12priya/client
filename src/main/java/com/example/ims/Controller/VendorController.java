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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ims.Entity.Product;
import com.example.ims.Entity.Vendor;
import com.example.ims.dto.VendorProductDTO;


@Controller
@RequestMapping("/admin")
public class VendorController {
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/vendor")
	public String showVendors(Model model) {
		model.addAttribute("vpdto", new VendorProductDTO());
		String url1="http://localhost:8002/vendor/allVendors";
		ResponseEntity<List<Vendor>> response = restTemplate.exchange(
		        url1,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<List<Vendor>>() {}
		    );
		List<Vendor> allvendors = response.getBody() ;
		
		model.addAttribute("vendors", allvendors);
		

		return "vendors";
	}
	
	@GetMapping("/vendor/edit/{pid}")
	public String showEditVendors(Model model,@PathVariable Long pid) {
		String url1="http://localhost:8002/vendor/byid?id=" + pid;
		ResponseEntity<Vendor> response = restTemplate.exchange(
		        url1,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<Vendor>() {}
		    );
		Vendor v = response.getBody() ;
		model.addAttribute("vendor", v);
		return "editVendors";
	}
	
	@PostMapping("/vendor/update")
	public String updateProduct(@ModelAttribute Vendor vendor, Model model) {
		try {
	        String url = "http://localhost:8002/vendor/update"; 

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<Vendor> request = new HttpEntity<>(vendor, headers);

	        ResponseEntity<Vendor> response = restTemplate.postForEntity(url, request,Vendor.class);

	        if (response.getStatusCode().is2xxSuccessful()) {
	        	model.addAttribute("success", "Updated");
	            return "redirect:/admin/vendor";
	        } else {
	            model.addAttribute("error", "Failed to update product. Try again.");
	            return "editProducts";
	        }

	    } catch (Exception e) {
	        model.addAttribute("error", "Server error: " + e.getMessage());
	        return "editProducts";
	    }
	}
	


	@PostMapping("/save")
	public String addVendor(@ModelAttribute VendorProductDTO vpdto, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("Received DTO: " + vpdto);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<VendorProductDTO> request = new HttpEntity<>(vpdto, headers);

		String publisherUrl = "http://localhost:8002/vendor/add";
		try {
			ResponseEntity<Vendor> response = restTemplate.postForEntity(publisherUrl, request, Vendor.class);
			System.out.println("Response received: " + response.getStatusCode());
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				redirectAttributes.addFlashAttribute("success", "Vendor added successfully!");
				return "redirect:/admin/vendor";
			} else {
				System.out.println("Failed to add vendor, status: " + response.getStatusCode());
				model.addAttribute("error", "Failed to add vendor");
			}
		} catch (HttpClientErrorException e) {
			System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			model.addAttribute("error", "Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("General error: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error: " + e.getMessage());
		}
		return "vendors"; // Return to form on error
	}

}
