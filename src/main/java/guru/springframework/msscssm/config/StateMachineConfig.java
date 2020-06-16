package guru.springframework.msscssm.config;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

import static guru.springframework.msscssm.domain.PaymentEvent.AUTHORIZE;
import static guru.springframework.msscssm.domain.PaymentEvent.AUTH_APPROVED;
import static guru.springframework.msscssm.domain.PaymentEvent.AUTH_DECLINED;
import static guru.springframework.msscssm.domain.PaymentEvent.PRE_AUTHORIZE;
import static guru.springframework.msscssm.domain.PaymentEvent.PRE_AUTH_APPROVED;
import static guru.springframework.msscssm.domain.PaymentEvent.PRE_AUTH_DECLINED;
import static guru.springframework.msscssm.domain.PaymentState.AUTH;
import static guru.springframework.msscssm.domain.PaymentState.AUTH_ERROR;
import static guru.springframework.msscssm.domain.PaymentState.NEW;
import static guru.springframework.msscssm.domain.PaymentState.PRE_AUTH;
import static guru.springframework.msscssm.domain.PaymentState.PRE_AUTH_ERROR;

@RequiredArgsConstructor
@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final Action<PaymentState, PaymentEvent> preAuthAction;
    private final Action<PaymentState, PaymentEvent> preAuthApprovedAction;
    private final Action<PaymentState, PaymentEvent> preAuthDeclinedAction;
    private final Action<PaymentState, PaymentEvent> authAction;
    private final Action<PaymentState, PaymentEvent> authApprovedAction;
    private final Action<PaymentState, PaymentEvent> authDeclinedAction;
    private final Guard<PaymentState, PaymentEvent> paymentIdGuard;

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
                .initial(NEW)
                .states(EnumSet.allOf(PaymentState.class))
                .end(AUTH)
                .end(PRE_AUTH_ERROR)
                .end(AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(NEW).target(NEW).event(PRE_AUTHORIZE)
                .action(preAuthAction).guard(paymentIdGuard).and()
                .withExternal().source(NEW).target(PRE_AUTH).event(PRE_AUTH_APPROVED)
                .action(preAuthApprovedAction).guard(paymentIdGuard).and()
                .withExternal().source(NEW).target(PRE_AUTH_ERROR).event(PRE_AUTH_DECLINED)
                .action(preAuthDeclinedAction).guard(paymentIdGuard).and()
                .withExternal().source(PRE_AUTH).target(PRE_AUTH).event(AUTHORIZE)
                .action(authAction).guard(paymentIdGuard).and()
                .withExternal().source(PRE_AUTH).target(AUTH).event(AUTH_APPROVED)
                .action(authApprovedAction).guard(paymentIdGuard).and()
                .withExternal().source(PRE_AUTH).target(AUTH_ERROR).event(AUTH_DECLINED)
                .action(authDeclinedAction).guard(paymentIdGuard);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<PaymentState, PaymentEvent>() {
            @Override
            public void stateChanged(State from, State to) {
                log.info("stateChanged(from: {}, to: {})", from, to);
            }
        };
        config.withConfiguration().listener(adapter);
    }
}
