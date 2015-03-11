package ca.syncron.network.message;

import ca.syncron.network.tcp.HandlerAbs;

/**
 * Created by Dawson on 3/7/2015.
 */
public class Handler extends HandlerAbs {

	public Handler(){}
	public Handler(MessageCallbacks.DispatchCallbacks callbacks){
		super();
		mMessageLooper.register(callbacks);
	}

	Message msg = MessageBuilder
			.newMessage().withAdmin(true).withChatMessage("chat")
			.withMessageType(Message.MessageType.CHAT)
			.build();
}
