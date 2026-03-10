package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/detail")
    public String paymentDetailForm(@RequestParam(value = "paymentId", required = false) String paymentId) {
        if (paymentId != null && !paymentId.isEmpty()) {
            return "redirect:/payment/detail/" + paymentId;
        }
        return "PaymentDetail";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "PaymentDetailResult";
    }

    @GetMapping("/admin/list")
    public String adminList(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "PaymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String adminDetail(@PathVariable String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "PaymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setStatus(@PathVariable String paymentId, @RequestParam String status) {
        Payment payment = paymentService.getPayment(paymentId);
        paymentService.setStatus(payment, status);
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}