package ca.syncron.aspects;

import ca.syncron.network.tcp.client.ClientController;
import com.google.inject.Inject;
import com.jcabi.aspects.Loggable;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static java.lang.System.out;

/**
 * Created by Dawson on 3/26/2015.
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectJAutoProxyConfig {
	static              String           nameId = AspectJAutoProxyConfig.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	@Inject
	public              ClientController c      = new ClientController();

	//public AspectJAutoProxyConfig() {}
	@Bean
	public UserService userService() {

		return new UserService();
	}

	@Bean
	public UserAspect userAspect() {
		return new UserAspect();
	}
}


class UserService {
	public UserService() {}

	@Loggable(logThis = true)
	public void doTask() {
		out.println("DOING INJECTED TASK");
	}
}

class UserAspect {
	public UserAspect() {}
}