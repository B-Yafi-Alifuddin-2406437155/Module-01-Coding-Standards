package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(null, order, method, paymentData);

        String status = Payment.STATUS_REJECTED;
        if ("VOUCHER_CODE".equals(method)) {
            String voucher = getValue(paymentData, "voucherCode");
            status = isValidVoucherCode(voucher) ? Payment.STATUS_SUCCESS : Payment.STATUS_REJECTED;
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            String address = getValue(paymentData, "address");
            String fee = getValue(paymentData, "deliveryFee");
            status = isNonEmpty(address) && isNonEmpty(fee) ? Payment.STATUS_SUCCESS : Payment.STATUS_REJECTED;
        }

        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        Order order = payment.getOrder();
        if (Payment.STATUS_SUCCESS.equals(status)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if (Payment.STATUS_REJECTED.equals(status)) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private String getValue(Map<String, String> data, String key) {
        return data == null ? null : data.get(key);
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null) return false;
        if (voucherCode.length() != 16) return false;
        if (!voucherCode.startsWith("ESHOP")) return false;

        int digits = 0;
        for (int i = 0; i < voucherCode.length(); i++) {
            if (Character.isDigit(voucherCode.charAt(i))) {
                digits++;
            }
        }
        return digits == 8;
    }

    private boolean isNonEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}