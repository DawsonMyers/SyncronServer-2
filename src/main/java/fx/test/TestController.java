package fx.test;

import ca.syncron.controller.AbstractController;
import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.network.tcp.server.UserBundle;
import com.google.common.eventbus.Subscribe;
import fx.arduino.PhotocellChart;
import fx.controllers.DigitalIndicatorPane;
import fx.eventbus.EventBus;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.out;

/**
 * Created by Dawson on 3/29/2015.
 */

// green #47ed45

public class TestController implements Initializable {
	static              String         nameId   = TestController.class.getSimpleName();
	private             int            itemNum  = 0;
	//public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
	public static final ObservableList names    = FXCollections.observableArrayList();
	public static final ObservableList listData = FXCollections.observableArrayList();
	public static ObservableList   userData;
	static        ClientController mClient;

	Color GREEN1 = Color.valueOf("1fff93");
	Color RED1   = Color.RED;
	private static AbstractController    mController       = AbstractController.getInstance();
	public static  SimpleBooleanProperty connectedProperty = new SimpleBooleanProperty(false);
	//mController.connectedProperty();
	EventBus bus = EventBus.getBus();
	public static UserBundle selectedUser;
	PhotocellChart chart;

	public TestController() {
		bus.register(this);

	}


	ObservableList<MsgEntry> data = FXCollections.observableArrayList();
	@FXML AnchorPane monitorAnchorPane;
	@FXML Rectangle  conIndicator;
	@FXML Label      conLabel;
	@FXML AnchorPane controlTabAncPane;
	@FXML Button     btnStartSerial;
	@FXML Button     btnStopSerial;

	@Subscribe
	public void connectionStatus(EventBus.ConnectionEvent e) {
//		if (e.isConnected()) {
//			conIndicator.setFill(GREEN1);
//		} else conIndicator.setFill(RED1);
		updateConnection(e.isConnected());
	}

	public void setConnectionStatus(boolean b) {
		if (b) {
			conIndicator.setFill(GREEN1);
		} else conIndicator.setFill(RED1);
	}

	public void updateConnection() {
		Platform.runLater(() -> setConnectionStatus(mController.mConnected));
	}

	public void updateConnection(boolean b) {
		Platform.runLater(() -> setConnectionStatus(b));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bus.register(this);
		connectedProperty.bind(mController.connectedProperty());
		Platform.runLater(() -> setConnectionStatus(mController.mConnected));

		chart = new PhotocellChart();
		chart.setResize(monitorAnchorPane);
		monitorAnchorPane.getChildren().add(chart.get());

		chart.get().prefHeightProperty().bind(monitorAnchorPane.heightProperty());
		chart.get().prefWidthProperty().bind(monitorAnchorPane.widthProperty());
		//conIndicator.setFill(RED1);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				connectedProperty.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						updateConnection(newValue);
//						if (newValue) {
//							conIndicator.setFill(GREEN1);
//						} else conIndicator.setFill(RED1);
					}
				});
			}
		});
		mClient = ClientController.getInstance();
		userData = mClient.getUserObserver();

		names.addAll(
				"Adam", "Alex", "Alfred", "Albert",
				"Brenda", "Connie", "Derek", "Donny",
				"Lynne", "Myrtle", "Rose", "Rudolph",
				"Tony", "Trudy", "Williams", "Zach"
		            );
		initList();
		initTable();
		initUserTable(userTable1);
		initControls();
	}

	private void initControls() {
		AnchorPane pane = bottomAnchorPane;
		DigitalIndicatorPane digiPane = new DigitalIndicatorPane(this);
		digiPane.createIndicator("Digital", 12);
		pane.getChildren().add(digiPane);

		digiPane.prefWidthProperty().bind(pane.widthProperty());
		digiPane.prefHeightProperty().bind(pane.heightProperty());
//		digiPane.setPrefHeight(pane.getPrefHeight());
		//digiPane.createIndicator("Dawson");

		Button btnAdd = new Button("Add");
		//digiPane.getChildren().add(btnAdd);
		btnAdd.setOnAction(event -> digiPane.createIndicator("Digital"));
	}

	private void genNewRow() {
		// Add to the data any time, and the table will be updated
		MsgEntry entry = new MsgEntry();
		entry.setUserId("Dawson@192.168.1.1");
		entry.setMsgType("DIGITAL");
		entry.setPin("1");
		entry.setValue("1");
		entry.setChat("Hello! this is a chat msg");

		itemNum++;
		data.add(entry);
	}

	void initTable() {
		userIdCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("userId"));
		msgTypeCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("type"));
		pinCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("pin"));
		valueCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("value"));
		chatCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("chat"));

		// create the data
		userTable.setItems(data); // assign the data to the table
		userTable.setEditable(true);
		initColumn(userTable);
	}

	void initColumn(TableView<MsgEntry> tc) {

		userIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
		userIdCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setUserId(t.getNewValue())
		                         );
		msgTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
		msgTypeCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setMsgType(t.getNewValue())
		                          );
		pinCol.setCellFactory(TextFieldTableCell.forTableColumn());
		pinCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setPin(t.getNewValue())
		                      );
		valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
		valueCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setValue(t.getNewValue())
		                        );
		chatCol.setCellFactory(TextFieldTableCell.forTableColumn());
		chatCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setChat(t.getNewValue())
		                       );
	}

	@FXML TableView                       userTable1;
	@FXML TableColumn<UserBundle, String> userIdCol1;
	@FXML TableColumn<UserBundle, String> typeCol;
	@FXML TableColumn<UserBundle, String> nameCol;
	@FXML TableColumn<UserBundle, String> timestampCol;
	@FXML TableColumn<UserBundle, String> accessCol;

	void initUserTable(TableView<UserBundle> u) {

		userIdCol1.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("userId"));
		typeCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("type"));
		nameCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("name"));
		timestampCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("timestamp"));
		accessCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("access"));


		// create the data
		userTable1.setItems(userData); // assign the data to the table
		userTable1.setEditable(true);
	}

	@FXML ListView<UserBundle> userList1;
	@FXML Label                nameDet;
	@FXML Label                accessDet;
	@FXML Label                timeDet;
	@FXML Label                typeDet;
	@FXML GridPane             detailsGrid;


	public void initList() {

		userList1.setItems(FXCollections.observableArrayList(userData));
		userList1.setEditable(true);

		userList.setCellFactory(TextFieldListCell.forListView());

		userList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
			@Override
			public void handle(ListView.EditEvent<String> t) {
				userList.getItems().set(t.getIndex(), t.getNewValue());
				out.println("setOnEditCommit");
			}

		});

	}

	@FXML public  TitledPane                    statesPane;
	@FXML private Button                        btnSend;
	@FXML private Button                        btnGen;
	@FXML private Button                        btnTest;
	@FXML private TableView<MsgEntry>           userTable;
	@FXML private TableColumn<MsgEntry, String> userIdCol;
	@FXML private TableColumn<MsgEntry, String> msgTypeCol;
	@FXML private TableColumn<MsgEntry, String> pinCol;
	@FXML private TableColumn<MsgEntry, String> valueCol;
	@FXML private TableColumn<MsgEntry, String> chatCol;
	@FXML private ListView                      userList;
	@FXML private GridPane                      indicatorGrid;
	@FXML private Label                         lblDigital0;
	@FXML private Label                         lblDigital1;
	@FXML private Label                         lblDigital2;
	@FXML private Label                         lblDigital3;
	@FXML private Label                         lblDigital4;
	@FXML private Rectangle                     digitalIndicator0;
	@FXML private Rectangle                     digitalIndicator1;
	@FXML private Rectangle                     digitalIndicator2;
	@FXML private Rectangle                     digitalIndicator3;
	@FXML private Rectangle                     digitalIndicator4;

	@FXML private Rectangle digitalIndicator5;
	@FXML private Rectangle digitalIndicator6;
	@FXML private Rectangle digitalIndicator7;
	@FXML private Rectangle digitalIndicator8;
	@FXML private Rectangle digitalIndicator9;
	@FXML private Label     lblDigital5;
	@FXML private Rectangle digitalIndicator11;
	@FXML private Rectangle digitalIndicator10;
	@FXML private Label     lblDigital8;
	@FXML private Label     lblDigital6;
	@FXML private Label     lblDigital7;
	@FXML private Label     lblDigital11;
	@FXML private Label     lblDigital9;
	@FXML private Label     lblDigital10;

	@FXML void digitalClicked(MouseEvent event) {
		out.println("Digital indicator clicked");
	}

	@FXML void indicatorGridClicked(Event event) {
		out.println("Indicator Clicked");
	}

	@FXML void genClicked(ActionEvent event) {
		genNewRow();
		out.println("Row #" + itemNum + " added");
	}

	@FXML void sendClicked(ActionEvent event) {
		MsgEntry me = userTable.getSelectionModel().getSelectedItem();
		Message m = new Message();
		m.setUserId(me.getUserId());
		m.setMessageType(Message.MessageType.DIGITAL);
		m.setPin(me.getPin());
		m.setValue(me.getValue());
		m.setTargetId(me.getUserId());
		m.setIsTargeted(true);
		out.println(m.serializeMessage());
		ClientController.getInstance().mClient.sendMessage(m);
	}

	@FXML void startSerialClicked(ActionEvent e) {
		EventBus.newSerialEvent(true);
	}

	@FXML void stopSerialClicked(ActionEvent e) {
		EventBus.newSerialEvent(false);
	}

	@FXML void testClicked(ActionEvent event) {
	}

	@FXML
	AnchorPane bottomAnchorPane;

	public void listClicked(Event event) {
		out.println("List Clicked");
		UserBundle u;
		ListView<UserBundle> lv = (ListView<UserBundle>) event.getSource();
		u = (UserBundle) lv.getSelectionModel().getSelectedItem();
		out.println("List clicked");

		nameDet.setText(u.getName());
		accessDet.setText(u.getAccessProp());
		timeDet.setText(u.getTimeProp());
		typeDet.setText(u.getTypeProp());
//		out.println(
//				((ListView<String>) event.getSource()).getSelectionModel().getSelectedItem());
	}


	public class MsgEntry {
		public SimpleStringProperty itemName = new SimpleStringProperty("<Name>");
		public SimpleStringProperty pin      = new SimpleStringProperty();
		public SimpleStringProperty value    = new SimpleStringProperty();
		public SimpleStringProperty chat     = new SimpleStringProperty();
		public SimpleStringProperty userId   = new SimpleStringProperty();

		public String getMsgType() {
			return msgType.get();
		}

		public SimpleStringProperty msgTypeProperty() {
			return msgType;
		}

		public void setMsgType(String msgType) {
			this.msgType.set(msgType);
		}

		public SimpleStringProperty msgType = new SimpleStringProperty();

		public String getChat() {
			return chat.get();
		}

		public SimpleStringProperty chatProperty() {
			return chat;
		}

		public void setChat(String chat) {
			this.chat.set(chat);
		}

		public String getItemName() {
			return itemName.get();
		}

		public SimpleStringProperty itemNameProperty() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName.set(itemName);
		}

		public String getPin() {
			return pin.get();
		}

		public SimpleStringProperty pinProperty() {
			return pin;
		}

		public void setPin(String pin) {
			this.pin.set(pin);
		}

		public String getUserId() {
			return userId.get();
		}

		public SimpleStringProperty userIdProperty() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId.set(userId);
		}

		public String getValue() {
			return value.get();
		}

		public SimpleStringProperty valueProperty() {
			return value;
		}

		public void setValue(String value) {
			this.value.set(value);
		}
	}
}




