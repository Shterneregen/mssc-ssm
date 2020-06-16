package guru.springframework.msscssm.config.actions;

import com.google.common.base.Strings;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import static guru.springframework.msscssm.Utils.LOG_SEPARATOR_COUNT;
import static guru.springframework.msscssm.Utils.LOG_SEPARATOR_STRING;

@Slf4j
@Component
public class PreAuthDeclinedAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        log.info(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
        log.info("Sending Notification of PreAuth DECLINED");
        log.info(Strings.repeat(LOG_SEPARATOR_STRING, LOG_SEPARATOR_COUNT));
    }
}
