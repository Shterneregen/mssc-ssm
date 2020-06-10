package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);
//        assertEquals(PaymentState.NEW, savedPayment.getState());
        System.out.println(savedPayment);

        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        System.out.println(sm.getState().getId());

        Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());
//        assertEquals(PaymentState.PRE_AUTH, savedPayment.getState());
        System.out.println(preAuthedPayment);
    }
}
