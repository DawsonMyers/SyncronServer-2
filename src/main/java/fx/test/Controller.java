package fx.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void hello(ActionEvent e) {
		System.out.println("Button center click");
	}


	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private ToolBar toolbarBottomMain;

	@FXML
	private ToolBar toolbarTopMain;

	@FXML
	private TitledPane titlePaneUsers;

	@FXML
	private ListView<?> userListView;

	@FXML
	private TitledPane titlePaneLogs;

	@FXML
	private Button btnCenter;

	@FXML
	private TreeTableColumn<?, ?> tblConnection;

	@FXML
	private Button btnConnect;

	@FXML
	private Button btnDisconnect;

	@FXML
	private Accordion centerPane;

	@FXML
	private TitledPane paneDigital;

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
	private TitledPane titlePaneAnalog;

	@FXML
	private LineChart<?, ?> graphAnalog;

	@FXML
	private TabPane tabPaneMessaging;

	@FXML
	private Button btnSend;

	@FXML
	private TableView<?> tblMessage;

	@FXML
	private TableColumn<?, ?> msgColFields;

	@FXML
	private TableColumn<?, ?> msgColValues;


	@FXML
	void digitalClicked(MouseEvent event) {
		Rectangle indicator = (Rectangle) event.getSource();
		indicator.setFill(Color.RED);
		System.out.println("Digital Indicator clicked");
	}


	@FXML
	void connectClick(ActionEvent event) {
		System.out.println("Button connect click");

	}

	@FXML
	void disconnectClick(ActionEvent event) {
		System.out.println("Button disconnect click");
	}


	@FXML
	void sendClicked(ActionEvent event) {
		System.out.println("Button send click");
	}

}
