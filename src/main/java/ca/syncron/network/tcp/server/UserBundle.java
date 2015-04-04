package ca.syncron.network.tcp.server;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.node.NodeClientBundler;
import ca.syncron.utils.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


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
	public HashMap<String, NodeClientBundler.NodeBundle> nodes = new HashMap<>();

	public SimpleStringProperty idProp     = new SimpleStringProperty(this, "Id", "");
	public SimpleStringProperty nameProp   = new SimpleStringProperty(this, "Name", "");
	public SimpleStringProperty typeProp   = new SimpleStringProperty(this, "Type", "");
	public SimpleStringProperty timeProp   = new SimpleStringProperty(this, "Timestamp", "");
	public SimpleStringProperty accessProp = new SimpleStringProperty(this, "Access Level", "");
	@JsonIgnore
	ObservableList<StringProperty> propList = FXCollections.observableArrayList();
	boolean           initialized = false;
	ArrayList<String> fieldNames  = new ArrayList<>();

	@JsonCreator
	public UserBundle() { }

	public UserBundle(User user) {
		name = user.getName();
		type = user.getType();
		userId = user.getUserId();
		timeStamp = user.getTimeStamp();
		accessLevel = user.getAccessLevel();
		nodes = user.getNodes();

		setIdProp(userId);
		setNameProp(name);
		setTypeProp(Message.UserType.getString(type));
		setTimeProp(timeStamp.toString());
		setAccessProp(accessLevel.getString());

		propList.add(idProp);
		propList.add(typeProp);
		propList.add(timeProp);
		fieldNames.add("idProp");
		fieldNames.add("typeProp");
		fieldNames.add("timeProp");
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
		setNameProp(this.name = name);
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
		idProp.set(userId = this.userId = userId);
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
//		timeProp.set((this.timeStamp = timeStamp).toString());
	}

	public Constants.Access getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Constants.Access accessLevel) {
		this.accessLevel = accessLevel;

		accessProp.set(accessLevel.getString());
	}

	public void init(User user) {
		name = user.getName();
		type = user.getType();
		userId = user.getUserId();
		timeStamp = user.getTimeStamp();
		accessLevel = user.getAccessLevel();
		nodes = user.getNodes();
	}

	@Override
	public String toString() {
		if (getUserId() != null)
			return getUserId();
		else return "null";
	}

	public String getAccessProp() {
		return accessProp.get();
	}

	public SimpleStringProperty accessPropProperty() {
		return accessProp;
	}

	public void setAccessProp(String accessProp) {
		this.accessProp.set(accessProp);
	}

	public String getIdProp() {
		return idProp.get();
	}

	public SimpleStringProperty idPropProperty() {
		return idProp;
	}

	public void setIdProp(String idProp) {
		this.idProp.set(idProp);
	}

	public String getNameProp() {
		return nameProp.get();
	}

	public SimpleStringProperty namePropProperty() {
		return nameProp;
	}

	public void setNameProp(String nameProp) {
		this.nameProp.set(nameProp);
	}

	public String getTimeProp() {
		return timeProp.get();
	}

	public SimpleStringProperty timePropProperty() {
		return timeProp;
	}

	public void setTimeProp(String timeProp) {
		this.timeProp.set(timeProp);
	}

	public String getTypeProp() {
		return typeProp.get();
	}

	public SimpleStringProperty typePropProperty() {
		return typeProp;
	}

	public void setTypeProp(String typeProp) {
		this.typeProp.set(typeProp);
	}

	@JsonIgnore
	public void setUserType(String newValue) {
		//setType(Message.UserType.getFromString(newValue));
		setTypeProp(newValue);

	}

	@JsonIgnore
	public ObservableList<StringProperty> getPropertyList() {return propList;}

	@JsonIgnore
	public UserBundle getRef() { return this;}

	@JsonIgnore
	public ArrayList<String> getFieldNames() {
		return fieldNames;
	}

	@JsonIgnore
	public ArrayList<String> fieldNames() {
		return fieldNames;
	}
}
