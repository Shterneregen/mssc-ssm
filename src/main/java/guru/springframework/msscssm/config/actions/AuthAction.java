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

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthAction implements Action<PaymentState, PaymentEvent> {

    private final EventSender eventSender;

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        log.info(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
        log.info("Auth was called");
        if (new Random().nextInt(10) < 8) {
            log.info("Approved!");
            eventSender.sendEvent(context, PaymentEvent.AUTH_APPROVED);
        } else {
            log.info("Declined!");
            eventSender.sendEvent(context, PaymentEvent.AUTH_DECLINED);
        }
        log.info(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
    }
}
