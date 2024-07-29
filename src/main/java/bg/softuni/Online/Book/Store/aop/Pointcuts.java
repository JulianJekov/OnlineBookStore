package bg.softuni.Online.Book.Store.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("@annotation(WarnIfExecutionExceeds)")
    void warnIfExecutionTimeExceeds(){}
}
