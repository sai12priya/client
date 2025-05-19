package com.example.ims.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ims.dto.CartResponseDto;
import com.example.ims.dto.OrderRequestDTO;
import com.example.ims.dto.OrderResponseDTO;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user/cart")
public class OrderClientController {

    private final RestTemplate restTemplate;
    private final String publisherUrl;

    @Autowired
    public OrderClientController(RestTemplate restTemplate, 
                               @Value("${publisher.service.url}") String publisherUrl) {
        this.restTemplate = restTemplate;
        this.publisherUrl = publisherUrl;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("id");
        
        // Get cart items
        ResponseEntity<CartResponseDto> cartResponse = restTemplate.getForEntity(
            publisherUrl + "/api/cart/" + userId,
            CartResponseDto.class
        );
        
        // Calculate totals (this could also be done in the publisher service)
        CartResponseDto cart = cartResponse.getBody();
        BigDecimal subtotal = calculateSubtotal(cart.getItems());
        BigDecimal shipping = BigDecimal.ZERO; // Free shipping for now
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.05")); // 5% tax
        BigDecimal total = subtotal.add(shipping).add(tax);
        
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(@ModelAttribute OrderRequestDTO orderRequest,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("id");
        orderRequest.setUserId(userId);
        
        // Call publisher service to place order
        ResponseEntity<OrderResponseDTO> response = restTemplate.postForEntity(
            publisherUrl + "/api/orders",
            orderRequest,
            OrderResponseDTO.class
        );
        
        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
            return "redirect:/user/orders/confirmation?invoice=" + response.getBody().getInvoiceNumber();
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to place order");
            return "redirect:/user/cart";
        }
    }

    @GetMapping("/confirmation")
    public String showConfirmation(@RequestParam String invoice, Model model) {
        ResponseEntity<OrderResponseDTO> response = restTemplate.getForEntity(
            publisherUrl + "/api/orders/" + invoice,
            OrderResponseDTO.class
        );
        
        model.addAttribute("order", response.getBody());
        return "order-confirmation";
    }

    private BigDecimal calculateSubtotal(List<CartResponseDto.CartItemDto> items) {
        return items.stream()
            .map(item -> BigDecimal.valueOf(item.getPrice())  // Convert double to BigDecimal first
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

//@Controller
//@RequestMapping("/user/cart")
//public class OrderClientController {
//	@Autowired
//    private RestTemplate restTemplate;
//
//    @Value("${publisher.service.url}")
//    private String publisherUrl;
//    
//    
//    @PostMapping("/checkout")
//    public String placeOrderPage(
//            @RequestParam BigDecimal subtotal,
//            @RequestParam BigDecimal shipping,
//            @RequestParam BigDecimal tax,
//            @RequestParam BigDecimal total,
//            @ModelAttribute OrderSummaryDTO orderSummary,
//            HttpSession session,
//            Model model) {
//        
//        Integer userId = (Integer) session.getAttribute("id");
//        // Send a POST request to fetch the cart details
//        ResponseEntity<CartResponseDto> response = restTemplate.getForEntity(
//                publisherUrl + "/api/cart/" + userId,
//                CartResponseDto.class
//        );
//
//        CartResponseDto cart = response.getBody();
//        List<CartResponseDto.CartItemDto> items = cart.getItems();
//        model.addAttribute("cartItems", items);
//
//        // You can store these in session if needed
//        model.addAttribute("subtotal", subtotal);
//        model.addAttribute("shipping", shipping);
//        model.addAttribute("tax", tax);
//        model.addAttribute("total", total);
//      
//        return "checkout";
//    }
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

//    @GetMapping("/{id}")
//    public String viewOrder(@PathVariable Long id, Model model, HttpSession session) {
//        Integer userId = (Integer) session.getAttribute("id");
//        if (userId == null) {
//            return "redirect:/user/login";
//        }
//
//        try {
//            ResponseEntity<PurchaseResponseDTO> response = restTemplate.getForEntity(
//                publisherUrl + "/" + id + "?userId=" + userId,
//                PurchaseResponseDTO.class);
//
//            model.addAttribute("order", response.getBody());
//            return "order-details";
//        } catch (Exception e) {
//            model.addAttribute("error", "Error retrieving order: " + e.getMessage());
//            return "redirect:/user/orders";
//        }
//    }
//}