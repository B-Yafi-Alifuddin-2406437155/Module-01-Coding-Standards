package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentRepository {
    private final List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        if (payment.getId() == null || payment.getId().isEmpty()) {
            payment = new Payment(UUID.randomUUID().toString(),
                    payment.getOrder(), payment.getMethod(), payment.getPaymentData());
            payment.setStatus(payment.getStatus());
        }

        for (int i = 0; i < paymentData.size(); i++) {
            if (paymentData.get(i).getId().equals(payment.getId())) {
                paymentData.set(i, payment);
                return payment;
            }
        }

        paymentData.add(payment);
        return payment;
    }

    public Payment findById(String id) {
        for (Payment payment : paymentData) {
            if (payment.getId().equals(id)) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentData);
    }
}