package guru.springframework.msscssm.services;

import com.google.common.base.Strings;
import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static guru.springframework.msscssm.Utils.LOG_SEPARATOR_COUNT;
import static guru.springframework.msscssm.Utils.LOG_SEPARATOR_STRING;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());
        assertTrue(PaymentState.PRE_AUTH.equals(preAuthedPayment.getState())
                || PaymentState.PRE_AUTH_ERROR.equals(preAuthedPayment.getState()));

        System.out.println(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
        System.out.println(preAuthedPayment);
        System.out.println(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
    }

    @Transactional
    @RepeatedTest(3)
    void auth() {
        Payment savedPayment = paymentService.newPayment(payment);
        savedPayment.setState(PaymentState.PRE_AUTH);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.authorizePayment(savedPayment.getId());
        Payment authOrAuthErrorPayment = paymentRepository.getOne(savedPayment.getId());
        assertTrue(PaymentState.AUTH.equals(authOrAuthErrorPayment.getState())
                || PaymentState.AUTH_ERROR.equals(authOrAuthErrorPayment.getState()));

        System.out.println(Strings.repeat("*", LOG_SEPARATOR_COUNT));
        System.out.println(authOrAuthErrorPayment);
        System.out.println(Strings.repeat("*", LOG_SEPARATOR_COUNT));
    }
}
