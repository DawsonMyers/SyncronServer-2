package ca.syncron.network.tcp;

import ca.syncron.network.message.Message;
import ca.syncron.network.message.MessageCallbacks;
import ca.syncron.network.message.MessageLooper;
import ca.syncron.network.message.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dawson on 3/7/2015.
 */
public abstract class HandlerAbs extends Thread {
	public final static Logger        log            = LoggerFactory.getLogger(HandlerAbs.class.getName());
	public volatile MessageLooper mMessageLooper = new MessageLooper();
	ExecutorService executor = Executors.newSingleThreadExecutor();
	static public MessageProcessor mapper;
	public static AbstractTcpConnector mConnector;
	public        int              receiveCounter;
	public        int              sendCounter;

	public HandlerAbs(AbstractTcpConnector connector) {
		mConnector = connector;
//		receiveCounter = connector.getReceiveCounter();
//		sendCounter = connector.getSendCounter();
		mapper = new MessageProcessor();
	}

	public HandlerAbs() {
		mapper = new MessageProcessor();
	}

	public synchronized void addToReceiveQueue(String msgString) {
		//	executor.execute(() -> {
		Message msg = null;

		if (!msgString.trim().startsWith("{") && !msgString.trim().endsWith("}")) {
			log.info("addToReceiveQueue - message did not start/end with braces");
			//log.info(msg);
			return;
		}
		//log.info(mapper.smsgString);
		msg = mapper.deserializeMessage(msgString);
		//msg = executor.submit(() -> mapper.deserializeMessage(msgString)).get();
		if (msg != null && msg.getMessageType() != null) {
			mMessageLooper.addToReceiveQue(msg);
		} else {
			log.error("Null deserialization returned a null message");
			msg.setMessageType(Message.MessageType.ERROR);
			//log.info("addtoReceiveQueue - Null deserialization returned a null message");
		}
	}

	public void addToReceiveQueue(Message msg) {
		receiveCounter++;
		log.info("Message #" + receiveCounter + " added to ReceiveQueue");
		mMessageLooper.addToReceiveQue(msg);
	}

	public void addToSendQueue(Message msg) {
		sendCounter++;
		log.info("Message #" + sendCounter + " added to SendQueue");
		mMessageLooper.addToSendQue(msg);
	}

	//  Out to socket
	public synchronized void sendMessage(Message msg) {
		//	executor.execute(() -> {
		String strMsg = null;
		try {
			log.info("Received message submitted to que");
			strMsg = executor.submit(() -> mapper.serializeMessage(msg)).get();
			log.info("Returned serialized message: " + strMsg);
			msg.setSerialMessage(strMsg);
			//  Checks if anything was returned from the mapper. Then, checks if the msg has a user,
			//  if not, then the connector must be operating as a Client and the msg will be sent using
			//  the  Client socket
			if (strMsg != null) {
				if (msg.getTargetUser() != null) msg.getTargetUser().sendMessage(strMsg);
				else mConnector.sendMessage(msg);
			} else {
				log.error("addtoReceiveQueue", "Null deserialization returned a null message");

			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		//});

	}

	public void register(MessageCallbacks.DispatchCallbacks callbacks) {
		mMessageLooper.register(callbacks);
	}
}

