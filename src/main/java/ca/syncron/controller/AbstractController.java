package ca.syncron.controller;

import ca.syncron.network.message.Message.UserType;
import ca.syncron.utils.Constants;
import ca.syncron.utils.Interfaces.RawDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/10/2015.
 */
public abstract class AbstractController implements RawDataAccess, Runnable {
	static              String nameId = AbstractController.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);

	public static AbstractController me;
	public volatile static int[]    analogVals     = new int[12];
	public volatile static int[]    digitalVals    = new int[12];
	public           UserType mUserType      = UserType.NODE;
	public static          boolean  mConnected     = false;
	public static          String   mServerAddress = Constants.IpAddresses.IP;
	public           String   mUserName      = "Not set";

	public AbstractController() { me = this;}


	public static AbstractController getInstance() {
		return me;
	}


	//  getters and setters
	//////////////////////////////////////////////////////////////////////////////////////////////////////////

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
}
