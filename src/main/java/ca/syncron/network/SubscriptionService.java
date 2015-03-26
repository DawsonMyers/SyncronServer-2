package ca.syncron.network;

import ca.syncron.network.tcp.server.User;
import com.google.inject.Singleton;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.syncron.network.SubscriptionService.getSubjects;

/**
 * Created by Dawson on 3/25/2015.
 */
@Singleton
public class SubscriptionService {
	static              String                  nameId   = SubscriptionService.class.getSimpleName();
	public final static org.slf4j.Logger        log      = LoggerFactory.getLogger(nameId);
	public static       Map<String, Subscriber> subjects = new HashMap<>();

	//@TODO Test SubscriptionService
	//
	public SubscriptionService() {}

	public static boolean subscribeTo(Subscribable subscriber, String targetId) {
		if (subjects.containsKey(targetId)) {
			Subscriber target = subjects.get(targetId);
			target.mySubscribers.add(subscriber.getSubscriptionId());

			Subscriber applicant = subjects.get(subscriber.getSubscriptionId());
			applicant.addSubscription(targetId);

			log.debug("User " + applicant.getId() + " has subscribed to user: " + targetId);
			return true;
		}
		return false;
	}

	public static boolean unsubscribeFrom(Subscribable subscriber, String targetId) {
		if (subjects.containsKey(targetId)) {
			Subscriber target = subjects.get(targetId);
			target.unsubscribe(subscriber.getSubscriptionId());

			Subscriber applicant = subjects.get(subscriber.getSubscriptionId());
			applicant.removeSubscription(targetId);
			log.debug("User " + applicant.getId() + " has unsubscribed from user: " + targetId);
			return true;
		}
		return false;
	}

	public static void register(Subscribable applicant) {
		String id = applicant.getSubscriptionId();
		if (!subjects.containsKey(id)) {
			subjects.put(applicant.getSubscriptionId(), new Subscriber(applicant));
			log.debug("New subscribable : " + id);
		}
	}

	public static void unregister(Subscribable applicant) {
		String id = applicant.getSubscriptionId();
		if (subjects.containsKey(id)) {
			log.debug("Subscribable " + id + " has unregistered");
			subjects.remove(applicant.getSubscriptionId());
		}
	}

	public static Subscriber getSubscriber(String subscriberId) {
		if (subjects.containsKey(subscriberId)) return subjects.get(subscriberId);
		else return null;
	}

	public static List<User> getUserSubscribers(Subscriber s) {
		List<User> myUsers = new ArrayList<>();
		for (String id : s.mySubscribers) {
			User u = subjects.get(id).getInstance();
			myUsers.add(u);
		}
		return myUsers;
	}

	public static List<User> getUserSubscriptions(Subscriber s) {
		List<User> myUsers = new ArrayList<>();
		for (String id : s.mySubscribers) {
			User u = subjects.get(id).getInstance();
			myUsers.add(u);
		}
		return myUsers;
	}

	public static Map<String, Subscriber> getSubjects() {
		return subjects;
	}
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////


	public interface Subscribable {
		//  Abstract methods
		void notifySubscribers();
		String getSubscriptionId();
		public User getUserInstance();

		//  Default methods
		///////////////////////////////////////////////////////
		public default void registerSubscribable(Subscribable s) {
			register(s);
		}
		public default void unregisterSubscribable(Subscribable s) {
			unregister(s);
		}
		public default void subscribe(Subscribable applicant, String targetId) {
			subscribeTo(applicant, targetId);
		}
		public default void unsubscribe(Subscribable applicant, String targetId) {
			unsubscribeFrom(applicant, targetId);
		}
		public default List<User> getMySubscribers(Subscribable me) {
			Subscriber self = subjects.get(me.getSubscriptionId());
			return getUserSubscribers(self);
		}
		public default List<User> getMySubscriptions(Subscribable me) {
			Subscriber self = subjects.get(me.getSubscriptionId());
			return getUserSubscriptions(self);
		}
	}

}

//
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//
class Subscriber {
	List<String> mySubscribers   = new ArrayList<>();
	List<String> mySubscriptions = new ArrayList<>();
	public final String                           id;
	public final SubscriptionService.Subscribable subject;

	public Subscriber(SubscriptionService.Subscribable s) {
		subject = s;
		id = s.getSubscriptionId();
	}

	//  to keep track of those I subscribe to
	///////////////////////////////////////////////////////
	public void addSubscription(String id) {
		if (!mySubscriptions.contains(id)) mySubscriptions.add(id);
	}

	public void removeSubscription(String id) {
		if (!mySubscriptions.contains(id)) mySubscriptions.remove(id);
	}

	//  for those that subscribe to me
	///////////////////////////////////////////////////////
	public void subscribe(String targetId) {
		if (!mySubscribers.contains(targetId)) mySubscribers.add(targetId);
	}

	public void unsubscribe(String targetId) {
		if (!mySubscribers.contains(targetId)) mySubscribers.remove(targetId);
	}

	//  When disconnecting remove all subscriptions
	public void removeAllSubscriptions() {
		for (String targetId : mySubscriptions) {
			getSubjects().get(targetId).unsubscribe(id);
		}
	}

	//
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return id;
//		return super.toString();
	}

	public String getId() {
		return id;
	}

	public User getInstance() {
		return subject.getUserInstance();
	}
}