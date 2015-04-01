package fx.datafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;


/**
 * Created by Dawson on 3/31/2015.
 */
@FXMLController("/fx/views/table1.fxml")
//@FXMLController("/fx/fxml/datafx.fxml")
public class DataFxController {
	String fxml = "/fx/views/table1";
	static              String           nameId = DataFxController.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public DataFxController() {}

	@FXML
	private Button actionButton;
}
