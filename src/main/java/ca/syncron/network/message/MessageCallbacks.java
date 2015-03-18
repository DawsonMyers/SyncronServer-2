/**
 *
 */
package ca.syncron.network.message;

/**
 * @author Dawson
 */
public interface MessageCallbacks {
	public abstract void handleDigitalMessage(Message msg);

	public abstract void handleAnalogMessage(Message msg);

	public abstract void handleChatMessage(Message msg);

	public	interface CompatCallbacks<T> {
		Message.MessageType getSysMessageType();

		String getSysMessage();

		String getSysUserId();
	}

	public	interface QueueCallbacks {
		void handleMessage();

		void processMessage(Message msg);

	}


	public interface DispatchCallbacks {
		public abstract void handleDigitalMessage(Message msg);

		public abstract void handleAnalogMessage(Message msg);

		public abstract void handleChatMessage(Message msg);

		public abstract void handleAdminMessage(Message msg);

		public abstract void handleUpdateMessage(Message msg);

		public abstract void handleRegisterMessage(Message msg);

		public abstract void handleStatusMessage(Message msg);
		public abstract void handleStreamMessage(Message msg);

		public abstract void handleLoginMessage(Message msg);

		public abstract void handleUserMessage(Message msg);
		public abstract void handleErrorMessage(Message msg);

		public abstract void sendMessage(Message msg);
		public abstract void sendUpdateMessage(Message msg);
		public abstract void sendChatMessage(Message msg);
		public abstract void sendSystemMessage(Message msg);
		public abstract void sendRegisterMessage(Message msg);
		public abstract void sendStreamMessage(Message msg);
		public abstract void sendDigitalMessage(int pin, int value);
		public abstract void sendDigitalMessage(String pin, String value);
		public abstract void sendAnalogMessage(Message msg);

		<T> void processMessage(T msg);
	}


}