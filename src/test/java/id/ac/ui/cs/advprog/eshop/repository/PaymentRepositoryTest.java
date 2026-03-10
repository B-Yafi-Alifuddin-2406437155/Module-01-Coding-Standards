package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Product product = new Product();
        product.setProductId("p-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);

        Order order = new Order("o-1", List.of(product), 1708560000L, "Safira");

        payments = new ArrayList<>();

        Payment payment1 = new Payment("payment-1", order, "VOUCHER_CODE",
                Map.of("voucherCode", "ESHOP1234ABC5678"));
        payment1.setStatus(Payment.STATUS_SUCCESS);
        payments.add(payment1);
        Payment payment2 = new Payment("payment-2", order, "CASH_ON_DELIVERY",
                Map.of("address", "Jl. Margonda", "deliveryFee", "15000"));
        payment2.setStatus(Payment.STATUS_SUCCESS);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payment.getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveCreateKeepsSamePaymentInstance() {
        Payment payment = payments.get(0);

        Payment result = paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById(payment.getId());

        assertSame(payment, result);
        assertSame(payment, findResult);
    }

    @Test
    void testSaveUpdate() {
        Payment payment = payments.get(1);
        paymentRepository.save(payment);

        Payment updated = new Payment(payment.getId(), payment.getOrder(),
                payment.getMethod(), payment.getPaymentData());
        updated.setStatus(Payment.STATUS_REJECTED);

        Payment result = paymentRepository.save(updated);
        Payment findResult = paymentRepository.findById(payment.getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(Payment.STATUS_REJECTED, findResult.getStatus());
    }

    @Test
    void testSaveWithoutIdThrowsException() {
        Order order = payments.get(0).getOrder();
        Payment payment = new Payment(null, order, "VOUCHER_CODE",
                Map.of("voucherCode", "ESHOP1234ABC5678"));

        assertThrows(IllegalArgumentException.class, () -> paymentRepository.save(payment));
    }

    @Test
    void testFindByIdIfFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        assertEquals(payments.get(0).getId(), findResult.getId());
        assertEquals(payments.get(0).getMethod(), findResult.getMethod());
    }

    @Test
    void testFindByIdIfNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById("missing");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List<Payment> results = paymentRepository.findAll();
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getId().equals("payment-1")));
        assertTrue(results.stream().anyMatch(p -> p.getId().equals("payment-2")));
    }
}
