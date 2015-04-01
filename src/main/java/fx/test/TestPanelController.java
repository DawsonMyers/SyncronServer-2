package fx.test;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Dawson on 3/30/2015.
 */
public class TestPanelController implements Initializable {


	static              String           nameId = TestPanelController.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
	@FXML
	private AnchorPane pane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public TestPanelController() {}

}
