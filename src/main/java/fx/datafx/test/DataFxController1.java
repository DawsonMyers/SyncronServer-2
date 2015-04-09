package fx.datafx.test;

import org.datafx.controller.FXMLController;

/**
 * Created by Dawson on 4/4/2015.
 */
@FXMLController("/fx/datafx/datafx-layout.fxml")
public class DataFxController1 {
	static              String           nameId = DataFxController1.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public DataFxController1() {}

}
