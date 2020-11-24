package uk.gov.hmcts.probate.functional;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestRetryRule implements TestRule {

    private int retryCount;

    public Boolean firstAttempt = true;

    public TestRetryRule(int retryCount) {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }

    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;

                for (int i = 0; i < retryCount; i++) {
                    firstAttempt = i == 0;

                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        Thread.sleep(10000);
                        System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed.");
                    }
                }
                System.err.println(description.getDisplayName() + ": giving up after " + retryCount + " failures.");
                throw caughtThrowable;
            }
        };
    }
}