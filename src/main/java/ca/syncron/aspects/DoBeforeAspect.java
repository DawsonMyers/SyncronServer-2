package ca.syncron.aspects;

import com.jcabi.aspects.Loggable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by Dawson on 3/26/2015.
 */
@Aspect
public class DoBeforeAspect {

	//@Before("execution(* ca.syncron.aspects.beans.SimpleService.sayHello(..))")
	//@Before("")//("@annotation(MyDoBefore)"/* ca.syncron.aspects.beans.SimpleService.sayHello(..))*/)
	@Loggable()
	@Before("execution(* @MyDoBefore *.*(..))")
	public void doBefore(JoinPoint joinPoint) {


		System.out.println("***AspectJ*** DoBefore() is running!! intercepted : " + joinPoint.getSignature().getName());
	}

}
