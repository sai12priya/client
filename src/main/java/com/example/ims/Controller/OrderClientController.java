package com.example.ims.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.example.ims.dto.CartResponseDto;
import com.example.ims.dto.OrderSummaryDTO;
import com.example.ims.dto.PurchaseResponseDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/cart")
public class OrderClientController {

    private RestTemplate restTemplate;
    private final String apiBaseUrl = "http://localhost:8080/api/orders";
    

    @Autowired
    public void CartController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    
    @PostMapping("/checkout")
    public String placeOrderPage(@ModelAttribute OrderSummaryDTO orderSummary,
            HttpSession session,
           Model model) {
    	Integer userId = (Integer) session.getAttribute("id");
    	ResponseEntity<CartResponseDto> response = restTemplate.postForEntity(
    			"http://localhost:8080/api/cart/" + userId,
    			null,
                CartResponseDto.class
            );

            CartResponseDto cart = response.getBody();
            List<CartResponseDto.CartItemDto> items = cart.getItems();
            model.addAttribute("cartItems", items);
            
            BigDecimal subtotal = orderSummary.getSubtotal();
            BigDecimal shipping = orderSummary.getShipping();
            BigDecimal tax = orderSummary.getTax();
            BigDecimal total = orderSummary.getTotal();
            
            // You can store these in session if needed
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("shipping", shipping);
            model.addAttribute("tax", tax);
            model.addAttribute("total", total);
            // Add to model if redirecting
            //redirectAttributes.addFlashAttribute("orderSummary", orderSummary);
            
    	return "checkout";
    }
//    @PostMapping("/checkout")
//    public String processCheckout(
//            @ModelAttribute OrderSummaryDTO orderSummary,
//            HttpSession session,
//           Model model) {
//        
        // Access the values from the form
//        BigDecimal subtotal = orderSummary.getSubtotal();
//        BigDecimal shipping = orderSummary.getShipping();
//        BigDecimal tax = orderSummary.getTax();
//        BigDecimal total = orderSummary.getTotal();
//        
//        // Example usage:
//        System.out.println("Subtotal: " + subtotal);
//        System.out.println("Shipping: " + shipping);
//        System.out.println("Tax: " + tax);
//        System.out.println("Total: " + total);
//        
//        // You can store these in session if needed
//        model.addAttribute("subtotal", subtotal);
//        model.addAttribute("shipping", shipping);
//        model.addAttribute("tax", tax);
//        model.addAttribute("total", total);
//        // Add to model if redirecting
//        //redirectAttributes.addFlashAttribute("orderSummary", orderSummary);
//        
//        return "checkout"; // Redirect to checkout details page
 //   }

    
//    @PostMapping("/checkout")
//    public String placeOrder(
//            @ModelAttribute("purchaseRequest") PurchaseRequestDTO purchaseRequest,
//            @ModelAttribute("shippingAddress") ShippingAddressDTO shippingAddress,
//            @RequestParam("paymentMethod") String paymentMethod,
//            HttpSession session,
//            RedirectAttributes redirectAttributes) {
//
//        Integer userId = (Integer) session.getAttribute("id");
//        if (userId == null) {
//            return "redirect:/user/login";
//        }
//
//        try {
//            // Set the user ID and payment method
//            purchaseRequest.setUserId(userId.longValue());
//            purchaseRequest.setPaymentMethod(paymentMethod);
//            purchaseRequest.setShippingAddress(shippingAddress);
//
//            // Call the REST API to place the order
//            ResponseEntity<PurchaseResponseDTO> response = restTemplate.postForEntity(
//                apiBaseUrl,
//                purchaseRequest,
//                PurchaseResponseDTO.class);
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
//                return "redirect:/user/orders/" + response.getBody().getPurchaseId();
//            } else {
//                redirectAttributes.addFlashAttribute("error", "Failed to place order");
//                return "redirect:/user/checkout";
//            }
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Error placing order: " + e.getMessage());
//            return "redirect:/user/checkout";
//        }
//    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/user/login";
        }

        try {
            ResponseEntity<PurchaseResponseDTO> response = restTemplate.getForEntity(
                apiBaseUrl + "/" + id + "?userId=" + userId,
                PurchaseResponseDTO.class);

            model.addAttribute("order", response.getBody());
            return "order-details";
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving order: " + e.getMessage());
            return "redirect:/user/orders";
        }
    }
}