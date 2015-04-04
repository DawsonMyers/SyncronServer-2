package fx.controls;

import ca.syncron.controller.AbstractController;
import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.network.tcp.server.UserBundle;
import fx.eventbus.EventBus;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * Created by Dawson on 4/2/2015.
 */
public class BindController implements Initializable {
	//	static              String           nameId = BindController.class.getSimpleName();
//	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
	String[][] genData;
	ObservableList<RemoteUser> oList = FXCollections.observableArrayList();
	private String[] genName;
	private String[] genIp1;
	ArrayList<String> genIp  = new ArrayList<>();
	Color             GREEN1 = Color.valueOf("1fff93");
	Color             RED1   = Color.RED;
	public static SimpleBooleanProperty connectedProperty = new SimpleBooleanProperty(false);// = mController.connectedProperty();


	public static final ObservableList names    = FXCollections.observableArrayList();
	public static final ObservableList listData = FXCollections.observableArrayList();
	public static       ObservableList userData = FXCollections.observableArrayList();
	;
	static ClientController mClient;

	private static AbstractController mController = AbstractController.getInstance();
	//public static  SimpleBooleanProperty connectedProperty = mController.connectedProperty();
	EventBus bus = EventBus.getBus();
	public static UserBundle selectedUser1;


	public BindController() {}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connectedProperty.bind(mController.connectedProperty());
		txt1.textProperty().bindBidirectional(txtToolbar.textProperty());
		init();
		conIndicator.setFill(RED1);
		connectedProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					conIndicator.setFill(GREEN1);
				} else conIndicator.setFill(RED1);
			}
		});

	}

	@FXML
	void bConnectClicked(ActionEvent e) {
		connectedProperty.set(true);
	}

	@FXML
	void bDisconnectClicked(ActionEvent e) {
		connectedProperty.set(false);
	}

	@FXML
	Rectangle conIndicator;
	@FXML
	Label     conLabel;

	private void init() {
		initData();
		initBindings();
		//initUsers();
		initTable();
	}

	private void initBindings() {
		mClient = ClientController.getInstance();
		userData = mClient.getUserObserver();
	}

	private void initUsers() {

		for (int i = 0; i < genName.length; i++) {
			RemoteUser u = genRandomItem();//new RemoteUser(genName[i], genIp.get(i));
			oList.add(u);
		}
	}

	private void initData() {
		genData = new String[][]
				{
						{"dawson", "bob", "Dave", "Rick", "Dave",
						 "Adam", "Alex", "Alfred", "Albert", "Richard",
						 "Brenda", "Connie", "Derek", "Donny", "Mike",
						 "Lynne", "Myrtle", "Rose", "Rudolph", "Nick",
						 "Tony", "Trudy", "Williams", "Zach", "Pat",
						 "John", "Eric", "Joey", "Kenny", "Larry"
						},
						{"192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4"}
				};
		genName = new String[]{"dawson", "bob", "Dave", "Rick", "Dave",
		                       "Adam", "Alex", "Alfred", "Albert", "Richard",
		                       "Brenda", "Connie", "Derek", "Donny", "Mike",
		                       "Lynne", "Myrtle", "Rose", "Rudolph", "Nick",
		                       "Tony", "Trudy", "Williams", "Zach", "Pat",
		                       "John", "Eric", "Joey", "Kenny", "Larry"
		};
		//genIp = new String[]{"192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4"};


		Random r = new Random();
		String o = ".";
		// Generate Ip addresses
		for (int i = 0; i < genName.length; i++) {
			genIp.add((r.nextInt(255) + o +
					           r.nextInt(255) + o +
					           r.nextInt(255) + o +
					           r.nextInt(255))
			         );
		}
	}


	private void initTable() {
		table.setItems(oList);
		initColumns(table, oList);


		//for (int i = 0; i < oList.size(); i++) {

//			TableRow<RemoteUser> = new TableRow<>(oList.get())
//			oList.get(i).getPropList(i).
		//	}
//		setCellValueFactory(new PropertyValueFactory<UserBundle, String>("userId")
	}

	private void initColumns(TableView<StringProperty> t, ObservableList<RemoteUser> list) {
		int col = t.getColumns().size();

		t.getColumns().remove(col1);
		t.getColumns().remove(col2);
		for (int i = 0; i < list.get(0).getPropertyList().size(); i++) {
			RemoteUser u = list.get(i).getRef();
			StringProperty s = u.getPropertyList().get(i);
			TableColumn<StringProperty, String> c = new TableColumn<>(s.getName());
			c.setCellValueFactory(new PropertyValueFactory<>(u.fieldNames().get(i)));
			t.getColumns().add(c);
		}
	}


	@FXML
	private Button bAdd;

	@FXML
	private TextField txt1;

	@FXML
	private Button bAdd1;

	@FXML
	private Button bAdd2;

	@FXML
	private Button bAdd3;

	@FXML
	private TableView table;

	@FXML
	private TableColumn<?, ?> col1;

	@FXML
	private TableColumn<?, ?> col2;

	@FXML
	private ListView<?> list;

	@FXML
	private TextField txtToolbar;

	@FXML
	private TextField txtToolbar1;
	@FXML
	private TextField txt2;
	@FXML
	private TextField txt3;

	@FXML
	void addClicked(ActionEvent event) {
		oList.add(genRandomItem());
	}

	@FXML
	void bRemoveItemClicked(ActionEvent event) {
		RemoteUser u = (RemoteUser) table.getSelectionModel().getSelectedItem();
		//oList.remove(u);
		oList.remove(table.getSelectionModel().getSelectedIndex());
	}

	private RemoteUser genRandomItem() {
		Random r = new Random();
		int i = genName.length - 1;
		return new RemoteUser(genName[r.nextInt(i)], genIp.get(r.nextInt(i)));
	}

	StringProperty selectedName = new SimpleStringProperty();
	StringProperty selectedIp   = new SimpleStringProperty();

	@FXML
	void listClicked(Event event) {
		RemoteUser u = (RemoteUser) event.getSource();
		selectedName = u.nameProperty();
		selectedName = u.ipProperty();
		txt2.textProperty().bindBidirectional(selectedName);
		txt3.textProperty().bindBidirectional(selectedIp);

	}

	RemoteUser selectedUser = new RemoteUser();
	RemoteUser lastUser     = new RemoteUser();

	@FXML
	void tableClicked(Event event) {
		//RemoteUser u = (RemoteUser) event.getSource();

		lastUser = selectedUser;

		TableView t = (TableView) event.getSource();
		t.setEditable(true);
		RemoteUser u = (RemoteUser) t.getSelectionModel().getSelectedItem();

//		txt2.textProperty().unbindBidirectional(lastUser);
//		txt3.textProperty().unbindBidirectional(lastUser);

		txt2.textProperty().unbind();
		txt3.textProperty().unbind();
		selectedUser = u;
		selectedName = u.nameProperty();
		selectedIp = u.ipProperty();
//
//
//		selectedName.bind(txt2.textProperty());
//		selectedIp.bind(txt3.textProperty());
//		txt2.textProperty().bindBidirectional(selectedName);
//		txt3.textProperty().bindBidirectional(selectedIp);
		txt2.textProperty().bind(selectedName);
		txt3.textProperty().bind(selectedIp);
	}


	public interface ITableItem {
		ObservableList<StringProperty> getPropertyList();
		ArrayList<String> propertyNames = new ArrayList<>();
		ITableItem getRef();
		ArrayList<String> fieldNames();
	}

	public class StringProp extends SimpleStringProperty {
		private String name = "NULL";

		public StringProp(Object bean, String name, String initialValue) {
			super(bean, name, initialValue);

		}
	}

	public class RemoteUser {
		StringProperty name;//        = new SimpleStringProperty("Bob");
		StringProperty ip;//          = new SimpleStringProperty("192.168.1.1");
		ArrayList<StringProperty>      propList1   = new ArrayList<>();
		ObservableList<StringProperty> propList    = FXCollections.observableArrayList();
		boolean                        initialized = false;
		ArrayList<String>              fieldNames  = new ArrayList<>();

		public RemoteUser() {
			initProperties();
			fieldNames.add("name");
			fieldNames.add("ip");
			propList.add(name);
			propList.add(ip);
		}


		public RemoteUser(StringProperty name, StringProperty ip) {
			this();
			initProperties();
			this.ip = ip;
			this.name = name;
		}

		private void initProperties() {
			if (initialized) return;
			name = new SimpleStringProperty(this, "Name", "null");
			ip = new SimpleStringProperty(this, " IP Address", "192.168.1.-1");
			initialized = true;
		}

		public RemoteUser(String name, String ip) {
			this();
			this.ip.set(ip);
			this.name.set(name);
		}

		public String getIp() {
			return ip.get();
		}

		public StringProperty ipProperty() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip.set(ip);
		}

		public String getName() {
			return name.get();
		}

		public StringProperty nameProperty() {
			return name;
		}

		public void setName(String name) {
			this.name.set(name);
		}

		public ObservableList<StringProperty> getPropList() {
			return propList;
		}

		public void setPropList(ObservableList<StringProperty> propList) {
			this.propList = propList;
		}


		public ObservableList<StringProperty> getPropertyList() {
			return propList;
		}


		public RemoteUser getRef() {
			return this;
		}


		public ArrayList<String> fieldNames() {
			return fieldNames;
		}
	}
}
