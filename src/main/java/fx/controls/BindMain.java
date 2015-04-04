package fx.controls;

import fx.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Dawson on 4/2/2015.
 */
public class BindMain extends Application {
	//static              String           nameId = BindMain.class.getSimpleName();
//	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public BindMain() {}

	String   fxmlResource = "/main-stage.fxml";
	//	String fxmlResource = "/fx/fxml/indicator-digital.fxml";
	String   cssResource  = "/fx/styles/DarkTheme.css";
	EventBus bus          = EventBus.getBus();

	@Override
	public void start(Stage primaryStage) throws Exception {
		bus.register(this);

		Parent root = FXMLLoader.load(getClass().getResource(fxmlResource));

//
		Scene main = new Scene(root, 740, 480);
		//JdbcSample db = new JdbcSample();
		//TabPane data= ((TabPane) db.getContent(main));
		//pane.getChildren().add(data);


		primaryStage.setTitle("Syncron");
		primaryStage.setScene(main);
		primaryStage.getScene().getStylesheets().add(cssResource);
		primaryStage.show();

	}
}