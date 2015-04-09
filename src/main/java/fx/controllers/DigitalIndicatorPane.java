package fx.controllers;

import fx.eventbus.EventBus;
import fx.test.TestController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by Dawson on 3/30/2015.
 */
public class DigitalIndicatorPane extends FlowPane /*implements Initializable*/ {
	static              String           nameId = DigitalIndicatorPane.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
	AnchorPane aPane;

	String fxmlLocation = "/fx/fxml/indicator-digital.fxml";
	ObservableList<Indicator> indicators;
	public List<Indicator> list = new ArrayList<>();
	Application    app;
	TestController testController;
	String GREEN      = "#47ed45";
	String stroke     = "#f0f4f5";
	String strokeType = "INSIDE";
	String arcHeight  = "5.0";
	String arcWidth   = "5.0";


	public DigitalIndicatorPane() {
		initList();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlLocation));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		setAlignment(Pos.CENTER);

		//bindDimensions(aPane);
		setPadding(new Insets(10, 10, 10, 10));

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public DigitalIndicatorPane(Application testApp) {
		this();
		app = testApp;

	}

	public DigitalIndicatorPane(AnchorPane pane) {
		aPane = pane;
	}

	public DigitalIndicatorPane(TestController testController) {
		this();
		this.testController = testController;

	}

	void initList() {
		flowPane = new FlowPane();
		indicators = FXCollections.observableList(list);

		indicators.addListener(new ListChangeListener() {
			@Override
			public void onChanged(ListChangeListener.Change change) {
				out.println("change!");
			}
		});
	}

	public void createIndicator(String name) {
		Indicator indicator = new Indicator(list.size(), name);

		indicator.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Indicator ind = (Indicator) event.getSource();
				int i = flowPane.getChildren().indexOf(ind);
				ind.toggle();
				EventBus.getBus().newDigitalEvent(ind.getDigitalId() + "", ind.getStateStr());
			}
		});

		list.add(indicator);
		if (flowPane == null) initPane();
		getChildren().add(indicator);
		out.println("Indicator #" + list.size() + " added to panel");
	}

	public void createIndicator(String base, int count) {
		for (int i = 0; i < count; i++) {
			createIndicator(base);
		}
	}

	private void initPane() {
		flowPane = new FlowPane();
		flowPane.prefHeightProperty().bind(aPane.heightProperty());
		flowPane.prefWidthProperty().bind(aPane.widthProperty());
	}


	public void digitalClicked(Event event) {
		log.debug("Digital clicked");
	}

	@FXML
	private AnchorPane DigitalIndicatorPane;

	@FXML
	private FlowPane flowPane;

	@FXML
	private HBox indicatorHbox;

	@FXML
	private Rectangle digitalIndicator0;

	@FXML
	private Label lblDigital0;

	@FXML
	private Rectangle digitalIndicator01;

	@FXML
	private Label lblDigital01;

	@FXML
	private Rectangle digitalIndicator02;

	@FXML
	private Label lblDigital02;

	@FXML
	void digitalClicked(ActionEvent event) {

	}

	public void bindDimensions(Pane pane) {
		prefWidthProperty().bind(pane.widthProperty());
		prefHeightProperty().bind(pane.heightProperty());
	}

	public class Indicator extends HBox {
		//		private final int       mId;
		private final String    mLabel;
		public        Rectangle rec;
		public        Label     lbl;
		int id;
		boolean state = false;

		Paint fillOn  = Paint.valueOf(GREEN);
		Paint fillOff = Color.RED;
		private String mState;


		public Indicator(int id, String label) {
			this.id = id;
			mLabel = label + " " + id;
			setup();
		}

		private void setup() {
			//setPadding(new Insets(10,10,10,10));
			setAlignment(Pos.CENTER_LEFT);

			setSpacing(10);
			prefHeight(30);
			prefWidth(140);

			//b.setPadding(indicatorHbox.getPadding());
			rec = new Rectangle();
			rec.setHeight(20);
			rec.setWidth(20);
			rec.setFill(fillOff);
			rec.setArcHeight(5.0);
			rec.setArcWidth(5.0);
			rec.setStroke(Paint.valueOf(stroke));
			rec.setStrokeType(StrokeType.valueOf(strokeType));

			Label lbl = new Label(mLabel);
			lbl.setPrefHeight(24);
			lbl.setPrefWidth(120);


			getChildren().add(rec);
			getChildren().add(lbl);
		}

		public boolean toggle() {
			if (state) {
				rec.setFill(fillOff);
				return state = false;
			} else rec.setFill(fillOn);

			return state = true;
		}

		public int getDigitalId() {
			return id;
		}

		public String getLabel() {
			return mLabel;
		}

		public Rectangle getRec() {
			return rec;
		}

		public void setRec(Rectangle rec) {
			this.rec = rec;
		}

		public boolean isState() {
			return state;
		}

		public void setState(boolean state) {
			this.state = state;
			updateColor();
		}

		private void updateColor() {
			if (state) {
				rec.setFill(fillOn);
			} else rec.setFill(fillOff);
		}


		public void setDigitalId(int id) {
			this.id = id;
		}

		public Label getLbl() {
			return lbl;
		}

		public void setLbl(Label lbl) {
			this.lbl = lbl;
		}

		public String getStateStr() {
			return state ? "1" : "0";
		}

		public boolean isOn() {
			return state;
		}
	}

}

