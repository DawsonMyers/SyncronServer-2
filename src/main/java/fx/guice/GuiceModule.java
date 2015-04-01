package fx.guice;/**
 * Created by Dawson on 3/30/2015.
 */

import ca.syncron.network.message.Message;
import com.google.inject.AbstractModule;


// The Module, essentially, just tells Guice “hey, if somebody comes looking for
// a Person class, give it to them”. You can read more about it on the Guice website.
// But once I’ve created my Injector, it is time to use it.
public class GuiceModule extends AbstractModule {
	@Override
	protected void configure() {
		// Class to bind
		bind(Message.class);
	}
}