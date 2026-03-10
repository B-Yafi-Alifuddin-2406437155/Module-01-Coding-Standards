package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("p-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);

        order = new Order("o-1", List.of(product), 1708560000L, "Safira");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", data);

        assertEquals(Payment.STATUS_SUCCESS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalid() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "INVALID");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", data);

        assertEquals(Payment.STATUS_REJECTED, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCodValid() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Margonda");
        data.put("deliveryFee", "15000");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", data);
        assertEquals(Payment.STATUS_SUCCESS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCodInvalid() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "");
        data.put("deliveryFee", "15000");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", data);

        assertEquals(Payment.STATUS_REJECTED, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusUpdatesOrder() {
        Payment payment = new Payment("p-1", order, "VOUCHER_CODE",
                Map.of("voucherCode", "ESHOP1234ABC5678"));

        paymentService.setStatus(payment, Payment.STATUS_SUCCESS);

        assertEquals(Payment.STATUS_SUCCESS, payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());

        paymentService.setStatus(payment, Payment.STATUS_REJECTED);
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }
}
