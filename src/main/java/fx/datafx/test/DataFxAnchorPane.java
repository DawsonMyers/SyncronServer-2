package fx.datafx.test;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Created by Dawson on 4/4/2015.
 */
public class DataFxAnchorPane extends AnchorPane {
	static              String           nameId = DataFxAnchorPane.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	Button startButton;
	Label  title;
	VBox   vBox;

	public DataFxAnchorPane() {

		init();
	}

	private void init() {
		startButton = new Button("Start");
		title = new Label("Test Title");
		vBox = new VBox(10.0, startButton, title);
		vBox.fillWidthProperty().setValue(true);

	}

}
