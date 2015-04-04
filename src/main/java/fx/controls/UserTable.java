package fx.controls;


import ca.syncron.network.tcp.server.UserBundle;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Dawson on 4/3/2015.
 */
public class UserTable extends TableView {
	static              String           nameId = UserTable.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
	ObservableList<UserBundle> list;

	public UserTable() {}

	public UserTable(ObservableList<UserBundle> list) {
		this.list = list;
		init();
	}

	private void init() {
		initColumns();
		initObserver();

	}

	private void initObserver() {

	}

	private void initColumns() {


		for (int i = 0; i < list.get(0).getPropertyList().size(); i++) {
			UserBundle u = list.get(i).getRef();
			StringProperty s = u.getPropertyList().get(i);
			TableColumn<StringProperty, String> c = new TableColumn<>(s.getName());
			c.setCellValueFactory(new PropertyValueFactory<>(u.fieldNames().get(i)));
			getColumns().add(c);
		}
	}
}


