package guru.springframework.msscssm.config.actions;

import com.google.common.base.Strings;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.services.EventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Random;

import static guru.springframework.msscssm.Utils.LOG_SEPARATOR_COUNT;
import static guru.springframework.msscssm.Utils.LOG_SEPARATOR_STRING;
import static guru.springframework.msscssm.domain.PaymentEvent.PRE_AUTH_APPROVED;
import static guru.springframework.msscssm.domain.PaymentEvent.PRE_AUTH_DECLINED;

@Slf4j
@RequiredArgsConstructor
@Component
public class PreAuthAction implements Action<PaymentState, PaymentEvent> {

    private final EventSender eventSender;

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        log.info(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
        log.info("PreAuth was called");
        if (new Random().nextInt(10) < 8) {
            log.info("Approved!");
            eventSender.sendEvent(context, PRE_AUTH_APPROVED);
        } else {
            log.info("Declined! No credit!");
            eventSender.sendEvent(context, PRE_AUTH_DECLINED);
        }
    }
}
