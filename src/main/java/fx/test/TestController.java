package fx.test;

import ca.syncron.controller.AbstractController;
import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.network.tcp.server.UserBundle;
import com.google.common.eventbus.Subscribe;
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

	public TestController() {
		bus.register(this);
	}

	ObservableList<MsgEntry> data = FXCollections.observableArrayList();
	@FXML
	Rectangle  conIndicator;
	@FXML
	Label      conLabel;
	@FXML
	AnchorPane controlTabAncPane;

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
//		userIdCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("userId"));
//		msgTypeCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("type"));
//		pinCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("pin"));
//		valueCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("value"));
//		chatCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("chat"));
//
//		// create the data
//		userTable.setItems(data); // assign the data to the table
//		userTable.setEditable(true);

		names.addAll(
				"Adam", "Alex", "Alfred", "Albert",
				"Brenda", "Connie", "Derek", "Donny",
				"Lynne", "Myrtle", "Rose", "Rudolph",
				"Tony", "Trudy", "Williams", "Zach"
		            );
//		userList.setEditable(true);
//		for (int i = 0; i < 10; i++) {
//			listData.add("anonym");
//		}
//
//		userList.setItems(listData);
//		userList.setCellFactory(TextFieldListCell.forListView());


		initList();
		initTable();
		initUserTable(userTable1);
		initControls();
	}

	private void initControls() {
		AnchorPane pane = bottomAnchorPane;
		DigitalIndicatorPane digiPane = new DigitalIndicatorPane(this);
		pane.getChildren().add(digiPane);
		digiPane.setPrefWidth(pane.getPrefWidth());
		digiPane.setPrefHeight(pane.getPrefHeight());
		digiPane.createIndicator("Dawson");

		Button btnAdd = new Button("Add");

		digiPane.getChildren().add(btnAdd);

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
//		entry.itemId.set(itemNum);
//		entry.itemName.set("Item " + itemNum);
//		entry.price.set("" + (1.99 + itemNum) );
//		entry.qty.set(itemNum + 10);
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

		//userIdCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
		userIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
		userIdCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setUserId(t.getNewValue())
		                         );
		//msgTypeCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
		msgTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
		msgTypeCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setMsgType(t.getNewValue())
		                          );
		//pinCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
		pinCol.setCellFactory(TextFieldTableCell.forTableColumn());
		pinCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setPin(t.getNewValue())
		                      );
		//valueCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
		valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
		valueCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setValue(t.getNewValue())
		                        );
		//chatCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
		chatCol.setCellFactory(TextFieldTableCell.forTableColumn());
		chatCol.setOnEditCommit(
				t -> ((MsgEntry) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
				).setChat(t.getNewValue())
		                       );
	}

	@FXML
	TableView                       userTable1;
	@FXML
	TableColumn<UserBundle, String> userIdCol1;
	@FXML
	TableColumn<UserBundle, String> typeCol;
	@FXML
	TableColumn<UserBundle, String> nameCol;
	@FXML
	TableColumn<UserBundle, String> timestampCol;
	@FXML
	TableColumn<UserBundle, String> accessCol;

	void initUserTable(TableView<UserBundle> u) {

		userIdCol1.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("userId"));
		typeCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("type"));
		nameCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("name"));
		timestampCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("timestamp"));
		accessCol.setCellValueFactory(new PropertyValueFactory<UserBundle, String>("access"));


//		//userIdCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
//		userIdCol1.setCellFactory(TextFieldTableCell.forTableColumn());
//		userIdCol1.setOnEditCommit(
//				t -> ((UserBundle) t.getTableView().getItems().get(
//						t.getTablePosition().getRow())
//				).setUserId(t.getNewValue())
//		                          );
//		//msgTypeCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
//		typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
//		typeCol.setOnEditCommit(
//				t -> ((UserBundle) t.getTableView().getItems().get(
//						t.getTablePosition().getRow())
//				).setUserType(t.getNewValue())
//		                       );
//		//pinCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
//		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//		nameCol.setOnEditCommit(
//				t -> ((UserBundle) t.getTableView().getItems().get(
//						t.getTablePosition().getRow())
//				).setName(t.getNewValue())
//		                       );
//		//valueCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
//		timestampCol.setCellFactory(TextFieldTableCell.forTableColumn());
//		timestampCol.setOnEditCommit(
//				t -> ((UserBundle) t.getTableView().getItems().get(
//						t.getTablePosition().getRow())
//				).setTimeProp(t.getNewValue())
//		                            );
//		//chatCol.setCellValueFactory(new PropertyValueFactory<MsgEntry, String>("firstName"));
//		accessCol.setCellFactory(TextFieldTableCell.forTableColumn());
//		accessCol.setOnEditCommit(
//				t -> ((UserBundle) t.getTableView().getItems().get(
//						t.getTablePosition().getRow())
//				).setAccessProp(t.getNewValue())
//		                         );

		// create the data
		userTable1.setItems(userData); // assign the data to the table
		userTable1.setEditable(true);
	}

	@FXML
	ListView<UserBundle> userList1;
	@FXML
	Label                nameDet;
	@FXML
	Label                accessDet;
	@FXML
	Label                timeDet;
	@FXML
	Label                typeDet;
	@FXML
	GridPane             detailsGrid;


	public void initList() {
		//userList = new ListView<String>();
//		ObservableList<String> items =FXCollections.observableArrayList (
//				"Single", "Double", "Suite", "Family App");
//		userList.setItems(items);


		userList1.setItems(FXCollections.observableArrayList(userData));
//		userList.setItems(FXCollections.observableArrayList("Item1", "Item2", "Item3", "Item4"));
		userList1.setEditable(true);

		userList.setCellFactory(TextFieldListCell.forListView());

		userList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
			@Override
			public void handle(ListView.EditEvent<String> t) {
				userList.getItems().set(t.getIndex(), t.getNewValue());
				out.println("setOnEditCommit");
			}

		});
//		userList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent e) {
//				UserBundle u = (UserBundle) userList.getSelectionModel().getSelectedItem();
//				out.println("List clicked");
//
//				nameDet.setText(u.getName());
//				accessDet.setText(u.getAccessProp());
//				timeDet.setText(u.getTimeProp());
//				typeDet.setText(u.getTypeProp());
//			}
//
//		});
//		userList.setItems(FXCollections.observableArrayList("Item1", "Item2", "Item3", "Item4"));
////		userList.setItems(FXCollections.observableArrayList("Item1", "Item2", "Item3", "Item4"));
//		userList.setEditable(true);
//
//		userList.setCellFactory(TextFieldListCell.forListView());
//
//		userList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
//			@Override
//			public void handle(ListView.EditEvent<String> t) {
//				userList.getItems().set(t.getIndex(), t.getNewValue());
//				out.println("setOnEditCommit");
//			}
//
//		});

//		userList.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
//			@Override
//			public void handle(ListView.EditEvent<String> t) {
//				out.println("setOnEditCancel");
//			}
//		});

			}

	@FXML
	public  TitledPane statesPane;
	@FXML
	private Button     btnSend;

	@FXML
	private Button btnGen;

	@FXML
	private Button btnTest;

	@FXML
	private TableView<MsgEntry> userTable;

	@FXML
	private TableColumn<MsgEntry, String> userIdCol;

	@FXML
	private TableColumn<MsgEntry, String> msgTypeCol;

	@FXML
	private TableColumn<MsgEntry, String> pinCol;

	@FXML
	private TableColumn<MsgEntry, String> valueCol;
	@FXML
	private TableColumn<MsgEntry, String> chatCol;

	@FXML
	private ListView userList;


	@FXML
	private GridPane indicatorGrid;

	@FXML
	private Label lblDigital0;

	@FXML
	private Label lblDigital1;

	@FXML
	private Label lblDigital2;

	@FXML
	private Label lblDigital3;

	@FXML
	private Label lblDigital4;

	@FXML
	private Rectangle digitalIndicator0;

	@FXML
	private Rectangle digitalIndicator1;

	@FXML
	private Rectangle digitalIndicator2;

	@FXML
	private Rectangle digitalIndicator3;

	@FXML
	private Rectangle digitalIndicator4;

	@FXML
	private Rectangle digitalIndicator5;

	@FXML
	private Rectangle digitalIndicator6;

	@FXML
	private Rectangle digitalIndicator7;

	@FXML
	private Rectangle digitalIndicator8;

	@FXML
	private Rectangle digitalIndicator9;

	@FXML
	private Label lblDigital5;

	@FXML
	private Rectangle digitalIndicator11;

	@FXML
	private Rectangle digitalIndicator10;

	@FXML
	private Label lblDigital8;

	@FXML
	private Label lblDigital6;

	@FXML
	private Label lblDigital7;

	@FXML
	private Label lblDigital11;

	@FXML
	private Label lblDigital9;

	@FXML
	private Label lblDigital10;


	@FXML
	void digitalClicked(MouseEvent event) {
		out.println("Digital indicator clicked");
	}


	@FXML
	void indicatorGridClicked(Event event) {
		out.println("Indicator Clicked");
	}

	@FXML
	void genClicked(ActionEvent event) {
		genNewRow();
		out.println("Row #" + itemNum + " added");

	}

	@FXML
	void sendClicked(ActionEvent event) {

		MsgEntry me = userTable.getSelectionModel().getSelectedItem();
//				((MsgEntry) userTable.getSelectionModel().getTableView().getItems().get(
//				userTable.getFocusModel().getFocusedCell().getRow()));
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

	@FXML
	void testClicked(ActionEvent event) {

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




