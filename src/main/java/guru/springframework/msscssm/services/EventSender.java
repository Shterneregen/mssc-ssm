package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;

import static guru.springframework.msscssm.services.PaymentServiceImpl.PAYMENT_ID_HEADER;

@Service
public class EventSender {

    public boolean sendEvent(StateContext<PaymentState, PaymentEvent> context, PaymentEvent paymentEvent) {
        return context.getStateMachine().sendEvent(MessageBuilder.withPayload(paymentEvent)
                .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                .build());
    }
}
