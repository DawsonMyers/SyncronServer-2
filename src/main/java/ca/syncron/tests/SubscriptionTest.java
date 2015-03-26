package ca.syncron.tests;

import ca.syncron.network.SubscriptionService;
import ca.syncron.network.tcp.server.User;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/25/2015.
 */
public class SubscriptionTest implements SubscriptionService.Subscribable {
	static              String           nameId = SubscriptionTest.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	static String id  = "Dawson";
	static String id1 = "Rick";
	static String id2 = "Bob";

	String myId;

	public SubscriptionTest(String id) {
		this.myId = id;
		registerSubscribable(this);

		init();

	}

	private void init() {
		if (myId == id2) {
			subscribe(this, id);
			subscribe(this, id1);
			notifySubscribers();
		}
	}

	public static void main(String[] args) {

		SubscriptionTest user1 = new SubscriptionTest(id);
		SubscriptionTest user2 = new SubscriptionTest(id1);
		SubscriptionTest user3 = new SubscriptionTest(id2);

	}


	@Override
	public void notifySubscribers() {
		log.debug("notifying");

	}

	@Override
	public String getSubscriptionId() {
		return myId;
	}

	@Override
	public User getUserInstance() {
		return null;
	}
}
