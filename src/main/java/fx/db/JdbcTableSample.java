package fx.db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Dawson on 3/31/2015.
 */

/**
 * @author johan
 */
public class JdbcTableSample extends Application {
	static              String           nameId = JdbcTableSample.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	private static Connection conn;
	private static final String dbURL = "jdbc:derby:memory:myDB;create=true";

	public static void main(String[] args) {
		Application.launch(JdbcTableSample.class, args);
	}

	@Override
	public void start(Stage stage) {
		// createDatabase();
		stage.setTitle("JdbcTable Samples");
		final Scene scene = new Scene(new Group(), 875, 700);
		scene.setFill(Color.LIGHTGRAY);
		Group root = (Group) scene.getRoot();

		root.getChildren().add(getContent(scene));

		stage.setScene(scene);
		stage.show();
	}

	public Node getContent(Scene scene) {
		createDatabase();
		// TabPane
		final TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setPrefWidth(scene.getWidth());
		tabPane.setPrefHeight(scene.getHeight());

		tabPane.prefWidthProperty().bind(scene.widthProperty());
		tabPane.prefHeightProperty().bind(scene.heightProperty());

		Tab localTab = new Tab("embedded");
		buildLocalTab(localTab);
		tabPane.getTabs().add(localTab);


		return tabPane;
	}

	private void buildLocalTab(Tab tab) {

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setHgap(5);
		grid.setVgap(5);

		final TableView tableView = new TableView();

//		JdbcSource ds = new JdbcSource(dbURL, "PERSON", "firstName", "lastName", "country");
//
//		tableView.setItems(ds.getData());
//		ObservableList<TableColumn<Map, ?>> cols = ds.getColumns();
//
//		TableColumn<Map, String> firstCol = (TableColumn<Map, String>) ds.getNamedColumn("firstName");
//		TableColumn<Map, String> lastCol = (TableColumn<Map, String>) ds.getNamedColumn("lastName");
//		TableColumn<Map, String> countryCol = (TableColumn<Map, String>) ds.getNamedColumn("country");
//		ObservableList<TableColumn<Map, String>> myColumns = FXCollections.observableArrayList();
//		TableColumn nameCol = new TableColumn("Name");
//		nameCol.getColumns().setAll(firstCol, lastCol);
//		myColumns.add(nameCol);
//		myColumns.add(countryCol);
//		tableView.getColumns().addAll(myColumns);
//
//		grid.add(Utils.createLabel("TableView<String>"), 0, 0);
//		grid.add(tableView, 0, 1);
//		GridPane.setVgrow(tableView, Priority.ALWAYS);
//		tab.setContent(grid);


	}

	private static void createDatabase() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			//Get a connection
			conn = DriverManager.getConnection(dbURL);
			conn.createStatement().execute("create table PERSON (FIRSTNAME varchar(255), LASTNAME varchar(255), COUNTRY varchar(255))");
			conn.createStatement().execute("INSERT INTO PERSON values ('Jonathan', 'Giles', 'New Zealand')");
			conn.createStatement().execute("INSERT INTO PERSON values ('Johan', 'Vos', 'Belgium')");
		} catch (Exception except) {
			except.printStackTrace();
		}

	}
}