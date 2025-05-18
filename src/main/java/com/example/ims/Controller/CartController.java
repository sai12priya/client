package com.example.ims.Controller;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ims.dto.CartItemRequestDto;
import com.example.ims.dto.CartResponseDto;
import com.example.ims.dto.CartResponseDto.CartItemDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    @Value("${publisher.service.url}")
    private String publisherUrl;
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
    	Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/user/login";
        }

        try {
            ResponseEntity<CartResponseDto> response = restTemplate.getForEntity(
                publisherUrl + "/api/cart/" + userId,
                CartResponseDto.class
            );

            CartResponseDto cart = response.getBody();
            List<CartResponseDto.CartItemDto> items = cart.getItems();
            model.addAttribute("cartItems", items);

            // ✅ Map to hold productId -> available quantity
            Map<Long, Integer> availableQuantities = new HashMap<>();

            for (CartResponseDto.CartItemDto item : items) {
                Long productId = item.getProductId();

                // Make call to: publisherUrl + "/api/products/{id}/available-quantity"
                ResponseEntity<Integer> quantityResponse = restTemplate.getForEntity(
                    publisherUrl + "/product/" + productId + "/availableQuantity",
                    Integer.class
                );

                availableQuantities.put(productId, quantityResponse.getBody());
            }

            model.addAttribute("availableQuantities", availableQuantities);

            // Totals
            BigDecimal subtotal = calculateSubtotal(items);
            BigDecimal shipping = calculateShipping(subtotal);
            BigDecimal tax = calculateTax(subtotal);
            BigDecimal total = subtotal.add(shipping).add(tax);

            model.addAttribute("subtotal", subtotal);
            model.addAttribute("shipping", shipping);
            model.addAttribute("tax", tax);
            model.addAttribute("total", total);

        } catch (Exception e) {
            model.addAttribute("error", "Unable to load cart. Please try again.");
        }

        return "cart"; // cart.html
    }
    
   
    @PostMapping("/add/{pid}")
    public String addToCart(
        @PathVariable("pid") Long productId,
        @RequestParam("quantity") @Min(1) int quantity,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/user/login";
        }

        try {
            CartItemRequestDto request = new CartItemRequestDto();
            request.setUserId(userId);
            request.setProductId(productId);
            request.setQuantity(quantity);
            
            ResponseEntity<CartResponseDto> response = restTemplate.postForEntity(
                publisherUrl + "/api/cart/add",
                request,
                CartResponseDto.class
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Product added to cart successfully!");
        } catch (HttpClientErrorException e) {
            // This handles 4xx errors
            try {
                // Parse the error message from the response body
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> errorResponse = mapper.readValue(e.getResponseBodyAsString(), 
                    new TypeReference<Map<String, String>>() {});
                redirectAttributes.addFlashAttribute("error", errorResponse.get("message"));
            } catch (Exception ex) {
                // Fallback if JSON parsing fails
                redirectAttributes.addFlashAttribute("error", e.getResponseBodyAsString());
            }
        } catch (Exception e) {
            // Handle other exceptions
            redirectAttributes.addFlashAttribute("error",
                "Failed to add product to cart: " + e.getMessage());
        }
        
        return "redirect:/user/products";
    }

    @PostMapping("/update")
    public String updateCartItem(
        @RequestParam("itemId") Long itemId,
        @RequestParam("quantity") @Min(1) int quantity,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/user/login";
        }

        try {
            CartItemRequestDto request = new CartItemRequestDto();
            request.setUserId(userId);
            request.setProductId(itemId); // Using itemId as productId in this case
            request.setQuantity(quantity);
            
            ResponseEntity<?> response = restTemplate.postForEntity(
                publisherUrl + "/api/cart/update",
                request,
                CartResponseDto.class
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Cart updated successfully!");
        } catch (HttpClientErrorException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> errorResponse = mapper.readValue(e.getResponseBodyAsString(), 
                    new TypeReference<Map<String, String>>() {});
                redirectAttributes.addFlashAttribute("error", errorResponse.get("message"));
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("error", e.getResponseBodyAsString());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "Failed to update cart: " + e.getMessage());
        }
        
        return "redirect:/user/cart";
    }
    @PostMapping("/remove/{itemId}")
    public String removeCartItem(
        @PathVariable Long itemId,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            restTemplate.postForEntity(
                publisherUrl + "/api/cart/remove/" + itemId,
                null,
                CartResponseDto.class
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "Item removed from cart");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "Failed to remove item: " + e.getMessage());
        }
        
        return "redirect:/user/cart";
    }

    private BigDecimal calculateSubtotal(List<CartItemDto> items) {
        return items.stream()
            .map(item -> BigDecimal.valueOf(item.getPrice())
                          .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateShipping(BigDecimal subtotal) {
        // Free shipping for orders over $50
        return subtotal.compareTo(new BigDecimal("300")) > 0 
            ? BigDecimal.ZERO 
            : new BigDecimal("5.99");
    }

    private BigDecimal calculateTax(BigDecimal subtotal) {
        // Sample tax calculation (8%)
        return subtotal.multiply(new BigDecimal("0.05"));
    }
}