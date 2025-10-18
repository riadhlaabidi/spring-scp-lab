package accounts.web;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Aspect
@Component
public class AccountAspect {

    private Counter counter;

    public AccountAspect(MeterRegistry meterRegistry) {
        this.counter = meterRegistry.counter("account.fetch", "type", "fromAspect");
    }

    @After("execution(* accounts.web.AccountController.accountSummary())")
    public void countFetches() {
        this.counter.increment();
    }
}
