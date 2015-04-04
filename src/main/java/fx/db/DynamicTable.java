package fx.db;
//
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//
///**
// * Created by Dawson on 3/31/2015.
// */
//public class DynamicTable {
//	static              String           nameId = DynamicTable.class.getSimpleName();
//	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);
//
//	public DynamicTable() {}

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @author Narayan
 */

public class DynamicTable extends Application {

	//TABLE VIEW AND DATA
	private ObservableList<ObservableList> data;
	private TableView                      tableview;

	//MAIN EXECUTOR
	public static void main(String[] args) {
		launch(args);
	}

	//CONNECTION DATABASE
	public void buildData() {
		Connection c;
		data = FXCollections.observableArrayList();

		try {
			c = DBConnect.connect();
			//SQL FOR SELECTING ALL OF CUSTOMER
			String SQL = "SELECT id, ts, a0,a1, a2, a3 from syncron.analog LIMIT 30";
			//.String SQL1 = "UPDATE syncron.analog SET device_id = 'NODE' WHERE device_id=NULL ";
			//ResultSet
			//c.createStatement().executeUpdate(SQL1);
			ResultSet rs = c.createStatement().executeQuery(SQL);

			/**********************************
			 * TABLE COLUMN ADDED DYNAMICALLY *
			 **********************************/
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				//We are using non property style for making dynamic table
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});
				tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				tableview.getColumns().addAll(col);
				System.out.println("Column [" + i + "] ");
			}

			/********************************
			 * Data added to ObservableList *
			 ********************************/
			while (rs.next()) {
				//Iterate Row
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					//Iterate Column
					row.add(rs.getString(i));
				}
				System.out.println("Row [1] added " + row);
				data.add(row);

			}
			tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//			//FINALLY ADDED TO TableView
//			tableview.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
//				@Override
//				public Boolean call(TableView.ResizeFeatures param) {
//					return true;
//				}
//			});
			tableview.setItems(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
	}


	@Override
	public void start(Stage stage) throws Exception {
		//TableView
		tableview = new TableView();
		buildData();

		//Main Scene
		Scene scene = new Scene(tableview);

		stage.setScene(scene);
		stage.show();
	}
}