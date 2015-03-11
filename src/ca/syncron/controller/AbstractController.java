package ca.syncron.controller;

import ca.syncron.network.message.Message.UserType;
import ca.syncron.utils.Interfaces.RawDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/10/2015.
 */
public abstract class AbstractController implements RawDataAccess, Runnable {
	static                 String nameId      = AbstractController.class.getSimpleName();
	public final static    Logger log         = LoggerFactory.getLogger(nameId);
	public volatile static int[]  analogVals  = new int[12];
	public volatile static int[]  digitalVals = new int[12];
	public static UserType systemType;
	public static String   userName;

	public static UserType getSystemType() {
		return systemType;
	}

	public static void setSystemType(UserType systemType) {
		systemType = systemType;
	}

	public AbstractController() {}

}
