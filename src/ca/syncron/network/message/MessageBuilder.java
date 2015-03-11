package ca.syncron.network.message;

import ca.syncron.network.tcp.server.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.syncron.network.message.Message.*;

/**
 * Created by Dawson on 3/7/2015.
 */
public class MessageBuilder {
	static              String nameId = Builder.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);

	public MessageBuilder() {}


	String mSerialMessage;
	User   mTargetUser;

	MessageType messageType;
	String      userId;
	UserType    userType;
	String      senderType;
	String      senderId;
	String      targetType;
	String      targetId;
	String      chatMessage;
	String      login;
	String      adminId;
	Chat        chatType;
	String      pin;
	String      value;
	String      testString;

	String              query;
	Map<String, String> queryResult;

	boolean admin;
	boolean reqResponse;

	int[]     analogValues;
	boolean[] digitalValues;

	String adminCommad;

//	public Message setHeader(Message msg, String senderId, String senderType, String targetId, String targetType) {
//		msg.setSenderId(senderId);
//		msg.setSenderType(senderType);
//		msg.setTargetId(targetId);
//		msg.setSenderType(targetType);
//		return msg;
//	}

	public Message newDigital(Message m, String senderId, String senderType, String targetId, String targetType, String pin, String value) {
		Message msg = newMessage().withPin(pin).withValue(value).build();

		return msg;
	}

	public Message newRegister(Message m, String senderId, UserType senderType, String targetId, String targetType, String pin, String value) {
		Message msg = newMessage().withMessageType(MessageType.REGISTER).withSenderId(senderId).withSenderType(senderType).withPin(pin).withValue(value).build();




		return msg;
	}

	public void setHeader(Message m, String senderId, UserType senderType, String targetId, UserType targetType) {
		m.setSenderId(senderId);
		m.setSenderType(senderType);
		m.setTargetId(targetId);
		m.setSenderType(targetType);
	}

	public void digital(Message m, String pin, String value) {
		m.setMessageType(MessageType.DIGITAL);
		m.setPin(pin);
		m.setValue(value);
	}

	//  reqResponse used to request a list of connected users
	public void chat(Message m, String chatMessage, boolean reqResponse) {
		m.setMessageType(MessageType.CHAT);
		m.setChatMessage(chatMessage);
		m.setReqResponse(reqResponse);
	}

	public void login(Message m, String name, String password) {
		m.setMessageType(MessageType.LOGIN);
		m.setLogin(name + "_" + password);
	}

	public void query(Message m, String query, Map<String, Object> queryResult) {
		m.setMessageType(MessageType.QUERY);
		m.setQuery(query);
		m.setQueryResult(queryResult);
	}

	public void register(Message m, String userId, boolean isAdmin, boolean reqResponse) {
		m.setMessageType(MessageType.REGISTER);
		m.setUserId(userId);
		m.setAdmin(isAdmin);
		m.setReqResponse(reqResponse);
	}

	public void register(Message m, boolean reqResponse) {
		m.setMessageType(MessageType.REGISTER);
		m.setReqResponse(reqResponse);
	}

	public void update(Message m, int[] digitalValues, int[] analogValues) {
		m.setMessageType(MessageType.UPDATE);
		m.setDigitalValues(digitalValues);
		m.setAnalogValues(analogValues);
	}

	public void admin(Message m, String adminCommad) {
		m.setMessageType(MessageType.ADMIN);
		m.setAdminCommad(adminCommad);
	}

	public void checkIn(Message m) {
		m.setMessageType(MessageType.CHECKIN);
	}

	public void status(Message m) {m.setMessageType(MessageType.STATUS);}

	private MessageBuilder(Builder builder) {
		mSerialMessage = builder.mSerialMessage;
		mTargetUser = builder.mTargetUser;
		messageType = builder.messageType;
		userId = builder.userId;
		userType = builder.userType;
		senderType = builder.senderType;
		senderId = builder.senderId;
		targetType = builder.targetType;
		targetId = builder.targetId;
		chatMessage = builder.chatMessage;
		login = builder.login;
		adminId = builder.adminId;
		chatType = builder.chatType;
		pin = builder.pin;
		value = builder.value;
		testString = builder.testString;
		query = builder.query;
		queryResult = builder.queryResult;
		admin = builder.admin;
		reqResponse = builder.reqResponse;
		analogValues = builder.analogValues;
		digitalValues = builder.digitalValues;
		adminCommad = builder.adminCommad;
	}

	public static Builder newMessage() {return new Builder();}


	public static final class Builder {
		private String              mSerialMessage;
		private User                mTargetUser;
		private MessageType         messageType;
		private String              userId;
		private UserType            userType;
		private String              senderType;
		private String              senderId;
		private String              targetType;
		private String              targetId;
		private String              chatMessage;
		private String              login;
		private String              adminId;
		private Chat                chatType;
		private String              pin;
		private String              value;
		private String              testString;
		private String              query;
		private Map<String, String> queryResult;
		private boolean             admin;
		private boolean             reqResponse;
		private int[]               analogValues;
		private boolean[]           digitalValues;
		private String              adminCommad;

		private Builder() {}

		Message m = new Message();


//		@Loggable
//		public Builder withMSerialMessage(String mSerialMessage) {
//			m.setSerialMessage = mSerialMessage;
//			return this;
//		}
//
//		public Builder withMTargetUser(User mTargetUser) {
//			this.mTargetUser = mTargetUser;
//			return this;
//		}

		public Builder withMessageType(MessageType messageType) {
			m.setMessageType(messageType);
			return this;
		}

		public Builder withUserId(String userId) {
			m.setUserId(userId);
			return this;
		}

		public Builder withUserType(UserType userType) {
			m.setUserType(userType);
			return this;
		}

		public Builder withSenderType(UserType senderType) {
			m.setSenderType(senderType);
			return this;
		}

		public Builder withSenderId(String senderId) {
			m.setSenderId(senderId);
			return this;
		}

		public Builder withTargetType(String targetType) {
			m.setTargetId(targetType);
			return this;
		}

		public Builder withTargetId(String targetId) {
			m.setTargetId(targetId);
			return this;
		}

		public Builder withChatMessage(String chatMessage) {
			m.setChatMessage(chatMessage);
			return this;
		}

		public Builder withLogin(String login) {
			m.setLogin(login);
			return this;
		}

		public Builder withAdminId(String adminId) {
			m.setAdminId(adminId);
			return this;
		}

		public Builder withChatType(Chat chatType) {
			m.setChatType(chatType);
			return this;
		}

		public Builder withPin(String pin) {
			m.setPin(pin);
			return this;
		}

		public Builder withValue(String value) {
			m.setValue(value);
			return this;
		}

		public Builder withTestString(String testString) {
			m.setTestString(testString);
			return this;
		}

		public Builder withQuery(String query) {
			m.setQuery(query);
			return this;
		}

		public Builder withQueryResult(Map<String, Object> queryResult) {
			m.setQueryResult(queryResult);
			return this;
		}

		public Builder withAdmin(boolean admin) {
			m.setAdmin(admin);
			return this;
		}

		public Builder withReqResponse(boolean reqResponse) {
			m.setReqResponse(reqResponse);
			return this;
		}

		public Builder withAnalogValues(int[] analogValues) {
			m.setAnalogValues(analogValues);
			return this;
		}

		public Builder withDigitalValues(int[] digitalValues) {
			m.setDigitalValues(digitalValues);
			return this;
		}

		public Builder withAdminCommad(String adminCommad) {
			m.setAdminCommad(adminCommad);
			return this;
		}

		public Message build() { return m;}
	}
}



