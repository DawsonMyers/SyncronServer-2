package ca.syncron.aspects.test;

import ca.syncron.aspects.MyDoBefore;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by Dawson on 3/26/2015.
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectTest {
	static              String           nameId = AspectTest.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	public AspectTest() {}

	//	public static void main(String[] args) {
//
//	}
//@DoBeforeAspect
	@MyDoBefore
	public void printBefore() {
		log.debug("Inside printBefore");
	}
//
//	@Autowired
//	SimpleService mSimpleServices;

	public static void main(String[] args) {
		AspectTest test = new AspectTest();
		test.printBefore();


//		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-config.xml");
//		SimpleService simpleService = (SimpleService) context.getBean("simpleServiceBean");
//		simpleService.printNameId();
//		System.out.println("---------------");
//		try {
//			simpleService.checkName();
//		} catch (Exception e) {
//			System.out.println("SimpleService checkName() : Exception thrown..");
//		}
//		System.out.println("---------------");
//		simpleService.sayHello("Javacodegeeks");
//		System.out.println("---------------");
//		context.close();
	}

}
