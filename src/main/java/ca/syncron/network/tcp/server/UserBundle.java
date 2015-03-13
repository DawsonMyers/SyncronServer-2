package ca.syncron.network.tcp.server;

import ca.syncron.network.message.Message;
import ca.syncron.utils.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Dawson on 3/12/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBundle {
	static              String           nameId = UserBundle.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	public              String           name   = "NotSet";
	public              Message.UserType type   = Message.UserType.ANDROID;
	public              String           userId = "NotSet";
	Date timeStamp;// = new Date();
	Constants.Access accessLevel = Constants.Access.USER;

	@JsonCreator
	public UserBundle() { }

	public UserBundle(User user) {
		name = user.getName();
		type = user.getType();
		userId = user.getUserId();
		timeStamp = user.getTimeStamp();
		accessLevel = user.getAccessLevel();

	}

//	public void init() {
//		name = mName;
//		type = mType;
//		userId = mUserId;
//		timeStamp = mTimeStamp;
//		accessLevel = mAccessLevel;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Message.UserType getType() {
		return type;
	}

	public void setType(Message.UserType type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Constants.Access getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Constants.Access accessLevel) {
		this.accessLevel = accessLevel;
	}

	public void init(User user) {
		name = user.getName();
		type = user.getType();
		userId = user.getUserId();
		timeStamp = user.getTimeStamp();
		accessLevel = user.getAccessLevel();
	}
}
