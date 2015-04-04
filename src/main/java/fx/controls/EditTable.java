package fx.controls;

import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawson on 3/31/2015.
 */
public class EditTable extends TableView {
	static              String           nameId = EditTable.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
	EditTable me;
	List<? extends Object>           fieldList = new ArrayList<>();
	ObservableList<? extends Object> ol        = FXCollections.observableArrayList();
	List<JavaBeanProperty<? extends Object>> propList;
	private TableBean mBean;

	public EditTable(TableBean bean) {
		mBean = bean;
		propList = bean.getPropertyList();
//		propList.add(bean);
	}


	public void init() {
		initRows();

	}

	private void initRows() {

		for (JavaBeanProperty<? extends Object> bean : propList) {
			TableColumn column = new TableColumn(bean.getName());
//			column.setH(bean.getName());
			column.setCellValueFactory(new PropertyValueFactory<TableBean, String>(bean.getName()));
			//getChildren().add(column);
		}
		//setItems(data); // assign the data to the table
		setEditable(true);
	}

	public interface TableBean extends JavaBeanProperty {
		List<? extends Object> getFieldList();
		List<JavaBeanProperty<? extends Object>> getPropertyList();
		String[] getColumnLabels();

	}
}
