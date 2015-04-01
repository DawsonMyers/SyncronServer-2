package ca.syncron.controller;

import ca.syncron.network.message.Message;
import ca.syncron.network.message.Message.UserType;
import ca.syncron.network.tcp.server.UserBundle;
import ca.syncron.utils.Constants;
import ca.syncron.utils.Interfaces.RawDataAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Dawson on 3/10/2015.
 */
public abstract class AbstractController implements RawDataAccess, Runnable {
	static              String nameId = AbstractController.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);

	public static AbstractController me;
	public volatile static int[]                 analogVals     = new int[12];
	public volatile static int[]                 digitalVals    = new int[12];
	public                 UserType              mUserType      = UserType.NODE;
	public static          boolean               mConnected     = false;
	public static          String                mServerAddress = Constants.IpAddresses.IP;
	public                 String                mUserName      = "Not set";
	public static ArrayList<UserBundle> mUserBundles = new ArrayList<>();

	public static ObservableList<UserBundle> mUserObserver;

	public AbstractController() {
		me = this;
		initObservers();
	}

	private void initObservers() {
		mUserObserver = FXCollections.observableArrayList();
		mUserObserver.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				log.debug("User list Invalidated");
				String s = "\n\tConnected Users:";
				for (UserBundle b : mUserObserver) {
					s += "\n\t" + b.getUserId();
				}
				log.debug(s);
				//printUsers();
			}
		});
	}


	public static AbstractController getInstance() {
		return me;
	}


	//  getters and setters
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	long sampleRate = 500;

	volatile boolean streamEnabled;

	public long getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate;
	}

	public boolean isStreamEnabled() {
		return streamEnabled;
	}

	public void setStreamEnabled(boolean streamEnabled) {
		this.streamEnabled = streamEnabled;
	}

	public int[] getAnalogVals() {
		return analogVals;
	}

	public void setAnalogVals(int[] analogVals) {
		AbstractController.analogVals = analogVals;
	}

	public int[] getDigitalVals() {
		return digitalVals;
	}

	public void setDigitalVals(int[] digitalVals) {
		AbstractController.digitalVals = digitalVals;
	}

	public boolean isConnected() {
		return mConnected;
	}

	public void setConnected(boolean connected) {
		mConnected = connected;
	}

	public String getServerAddress() {
		return mServerAddress;
	}

	public void setServerAddress(String serverAddress) {
		mServerAddress = serverAddress;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		mUserName = userName;
	}

	public UserType getUserType() {
		return mUserType;
	}

	public void setUserType(UserType userType) {
		userType = userType;
	}

	public void invalidateStatus() {
		log.info("Status invalidated");
	}

	@Override
	public int[] getAnalog() {
		return analogVals;
	}

	@Override
	public int[] getDigital() {
		return digitalVals;
	}

	@Override
	public void run() {

	}

	public void updateStatus(Message msg) {
		setAnalogVals(msg.getAnalogValues());
		setDigitalVals(msg.getDigitalValues());
		setUserBundles(msg.getUserBundles());
		//printUsers();
	}

	;

	public void printUsers() {
		String users = "Logged in users: ";
		for (UserBundle bundle : getUserBundles()) {
			users += bundle.getName() + ", ";
		}
		log.debug(users);
	}


	public void setUserBundles(ArrayList<UserBundle> userBundles) {
		if (userBundles == null) return;
		mUserBundles = userBundles;


		mUserObserver.clear();
		mUserObserver.addAll(userBundles);
//		for (int i = 0; i < userBundles.size(); i++) {
//			if (!mUserObserver.contains(userBundles.get(i))) {
//				{
//					mUserObserver.add(userBundles.get(i));
//				}
//			}
//		}
//		for (int i = 0; i < mUserObserver.size(); i++) {
//			if (!userBundles.contains(mUserObserver.get(i))) {
//				mUserObserver.remove(i);
//			}
//		}


		//syncUsers();

	}

	private void syncUsers() {

	}


	public ArrayList<UserBundle> getUserBundles() {
		return mUserBundles;

	}

	public ObservableList<UserBundle> getUserObserver() {
		return mUserObserver;
	}

	public void setUserObserver(ObservableList<UserBundle> userObserver) {
		mUserObserver = userObserver;
	}
}
