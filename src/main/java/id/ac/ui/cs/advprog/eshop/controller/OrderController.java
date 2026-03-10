package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrderSubmit(@RequestParam String author,
                                    @RequestParam String productName,
                                    @RequestParam int productQuantity) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName(productName);
        product.setProductQuantity(productQuantity);

        Order order = new Order(
                UUID.randomUUID().toString(),
                List.of(product),
                System.currentTimeMillis(),
                author
        );

        orderService.createOrder(order);
        return "redirect:/order/pay/" + order.getId();
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String orderHistorySubmit(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "OrderHistoryList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "OrderPay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderSubmit(@PathVariable String orderId,
                                 @RequestParam String method,
                                 @RequestParam(required = false) String voucherCode,
                                 @RequestParam(required = false) String address,
                                 @RequestParam(required = false) String deliveryFee,
                                 Model model) {
        Order order = orderService.findById(orderId);

        Map<String, String> data = new HashMap<>();
        if (voucherCode != null) data.put("voucherCode", voucherCode);
        if (address != null) data.put("address", address);
        if (deliveryFee != null) data.put("deliveryFee", deliveryFee);

        Payment payment = paymentService.addPayment(order, method, data);
        model.addAttribute("payment", payment);

        return "OrderPayResult";
    }
}