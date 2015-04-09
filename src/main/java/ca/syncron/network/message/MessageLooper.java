
package ca.syncron.network.message;


import ca.syncron.network.message.Message.MessageType;
import ca.syncron.network.message.MessageCallbacks.DispatchCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Dawson
 */
public class MessageLooper {
	public final static Logger log = LoggerFactory.getLogger(MessageLooper.class.getName());
	DispatchCallbacks mObserver = null;
	MessageLooper me;
	ExecutorService              executor      = Executors.newFixedThreadPool(2);
	LinkedBlockingQueue<Message> mReceiveQueue = new LinkedBlockingQueue<>();
	LinkedBlockingQueue<Message> mSendQueue    = new LinkedBlockingQueue<>();
	public LinkedList<Message> mLinkedList;
	Mode mMode = Mode.RECEIVER;
	private volatile boolean looperStarted = false;

	public enum Mode {
		RECEIVER, SENDER;
	}

	public MessageLooper() {
		log.info("Looper started");
		me = this;
		mLinkedList = new LinkedList<>();
		startLooper();
	}

	public MessageLooper(DispatchCallbacks observer) {
		this();
		mObserver = observer;
	}

	public void register(DispatchCallbacks observer) {mObserver = observer;}

	public synchronized void addToReceiveQue(Message msg) {
		log.info("addToReceiveQue");
		mReceiveQueue.add(msg);
		log.info("Message added to queue");
	}

	public synchronized void addToSendQue(Message msg) {
		mSendQueue.add(msg);
		log.info("Message added to queue");

	}

	public synchronized Message nextFromQue() {
		return mLinkedList.pollFirst();
	}

	public synchronized int queReceiveSize() {
		int size = mReceiveQueue.size();
		if (size > 1) log.info("buffer size = " + size);
		return size;
	}

	void startLooper() {
		if (!looperStarted) {
			looperStarted = true;
			executor.execute(() -> {
				log.info("ReceiverQue", "Receiving queue started");
				looperStarted = true;
				//log.info("\"ReceiverQue\", \"Receiving queue started\"");
				while (true) {
					try {// Blocks when there's no messages
						dispatchReceiveMessage(mReceiveQueue.take());
//					log.info("ReceiverQue, message taken from Receiving queue ");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			executor.execute(() -> {
				log.info("SenderQue", "Sender queue started");
				while (true) {
					try {
						dispatchSendMessage(mSendQueue.take());
						//log.info("SendQue, message taken from send queue");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public synchronized void dispatchReceiveMessage(Message msg) {

		//	executor.execute(() -> {
		MessageType type = msg.getMessageType();
		if (msg.getIsTargeted()) type = MessageType.TARGET;
		if (type == null) type = MessageType.UNKNOWN;
		log.info("Dispatching " + type + " message");
		switch (type) {
			case DIGITAL:
				mObserver.handleDigitalMessage(msg);
				break;
			case ANALOG:
				mObserver.handleAnalogMessage(msg);
				break;
			case ADMIN:
				mObserver.handleAdminMessage(msg);
				break;
			case UPDATE:
				mObserver.handleUpdateMessage(msg);
				break;
			case REGISTER:
				log.info("dispatching Register message");
				mObserver.handleRegisterMessage(msg);
				break;
			case STATUS:
				mObserver.handleStatusMessage(msg);
				break;
			case LOGIN:
				mObserver.handleLoginMessage(msg);
				break;
			case USER:
				mObserver.handleUserMessage(msg);
				break;
			case CHAT:
				log.info("dispatching Chat message");
				mObserver.handleChatMessage(msg);
				break;
			case STREAM:
				log.info("dispatching Stream message");
				mObserver.handleStreamMessage(msg);
				break;
			//
			///////////////////////////////////////////////////////
			case QUERY:
				log.info("dispatching Query message");
				mObserver.handleQueryMessage(msg);
				break;
			case ERROR:
				log.info("dispatching Error message");
				mObserver.handleErrorMessage(msg);
				break;
			case UNKNOWN:
				log.info("dispatching Unknown message");
				mObserver.handleUnknownMessage(msg);
				break;
			case ACCESS:
				log.info("dispatching Access message");
				mObserver.handleAccessMessage(msg);
				break;
			case TARGET:
				log.info("dispatching Target message");
				mObserver.handleTargetMessage(msg);
				break;
			case SUBSCRIBE:
				log.info("dispatching Subcribe message");
				mObserver.handleSubscribeMessage(msg);
				break;
			default:
				log.error("dispatchReceiveMessage", "message could not be identified");
				break;
		}
		//	});
	}

	public void dispatchSendMessage(Message msg) {
		log.debug("dispatchSendMessage", "Dispatching message");
		mObserver.sendMessage(msg);
	}
}
