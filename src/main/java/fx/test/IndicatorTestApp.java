package fx.test;/**
 * Created by Dawson on 3/30/2015.
 */

import com.google.common.eventbus.Subscribe;
import fx.controllers.DigitalIndicatorPane;
import fx.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static java.lang.System.out;

/**
 * A Guice based sample FXML Application
 */
public class IndicatorTestApp extends Application {
	/**
	 * Create the Guice Injector, which is going to be used to supply
	 * the Person model.
	 */

	String   fxmlResource = "/fx/fxml/test-pane.fxml";
	//	String fxmlResource = "/fx/fxml/indicator-digital.fxml";
	String   cssResource  = "/fx/styles/DarkTheme.css";
	EventBus bus          = EventBus.getBus();

	@Override
	public void start(Stage primaryStage) throws Exception {
		bus.register(this);

		Parent root = FXMLLoader.load(getClass().getResource(fxmlResource));

		DigitalIndicatorPane indicatorPane = new DigitalIndicatorPane(this);
		//root.
		AnchorPane pane = ((AnchorPane) root);
		HBox b = new HBox();
		//	b.setFillHeight(true);
		b.getChildren().add(indicatorPane);

		indicatorPane.bindDimensions(pane);
		Button btnAdd = new Button("Add");
//		bp.getChildren().add(btnAdd);
		BorderPane bp = new BorderPane(b, btnAdd, null, null, null);
		pane.getChildren().add(bp);

		btnAdd.setOnAction(event -> indicatorPane.createIndicator("Digital"));
		Scene main = new Scene(root, 740, 480);
		//JdbcSample db = new JdbcSample();
		//TabPane data= ((TabPane) db.getContent(main));
		//pane.getChildren().add(data);


		primaryStage.setTitle("Syncron");
		primaryStage.setScene(main);
		primaryStage.getScene().getStylesheets().add(cssResource);
		primaryStage.show();
	}

	@Subscribe
	public void digitalEvent(EventBus.DigitalEvent e) {
		out.println("Digital event received in TestApp \n" + "Source: " + e.getId() + " Value: " + e.getVal());
	}

	// Standard launcher
	public static void main(String[] args) {
		launch(args);
	}
}