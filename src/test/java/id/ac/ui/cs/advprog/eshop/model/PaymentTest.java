package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest {
    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("p-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);

        order = new Order(
                "o-1",
                List.of(product),
                1708560000L,
                "Safira Sudrajat"
        );
    }

    @Test
    void testCreatePaymentStoresAllFields() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("payment-1", order, "VOUCHER_CODE", paymentData);

        assertEquals("payment-1", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals(Payment.STATUS_PENDING, payment.getStatus());
        assertNotNull(payment.getPaymentData());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
    }

    @Test
    void testSetStatusUpdatesStatus() {
        Payment payment = new Payment("payment-2", order, "CASH_ON_DELIVERY",
                Map.of("address", "Jl. Merdeka", "deliveryFee", "10000"));

        payment.setStatus(Payment.STATUS_REJECTED);
        assertEquals(Payment.STATUS_REJECTED, payment.getStatus());
    }
}