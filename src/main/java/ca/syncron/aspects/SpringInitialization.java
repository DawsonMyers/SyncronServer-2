package ca.syncron.aspects;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Dawson on 3/26/2015.
 */
public class SpringInitialization {
	static              String           nameId = SpringInitialization.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	public SpringInitialization() {}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		ctx.register(AspectJAutoProxyConfig.class);
		ctx.refresh();

		UserService userService = ctx.getBean(UserService.class);
		userService.doTask();
	}
}
