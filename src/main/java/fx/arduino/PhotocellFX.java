package fx.arduino;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Chapter 10. JavaFX and Arduino
 *
 * @author pereda
 */
public class PhotocellFX extends Application {

	private LineChart<Number, Number> chart;
	private Series<Number, Number>    luxSeries;
	private Series<Number, Number>    aveLuxSeries5;
	private Series<Number, Number>    aveLuxSeries10;
	private NumberAxis                xAxis;
	private NumberAxis                yAxis;
	private final int tamMax   = 300; // 30'' / 0.1''/reading = 300 readings
	private final int tamAve5  = 50;  // 5''
	private final int tamAve10 = 100;  // 10''

	/* Check in the Arduino IDE the port used by Arduino or leave it empty
	   if you don't know it:  */
	private final Serial serial = new Serial();
	private ChangeListener<String> listener;
	private final BooleanProperty connection = new SimpleBooleanProperty(false);

	@Override
	public void start(Stage primaryStage) {
		xAxis = new NumberAxis();
		xAxis.setLabel("Time");
		xAxis.setAutoRanging(true);


		xAxis.setForceZeroInRange(false);
		xAxis.setTickLabelFormatter(new StringConverter<Number>() {
			@Override
			public String toString(Number t) {
				return new SimpleDateFormat("HH:mm:ss").format(new Date(t.longValue()));
			}

			@Override
			public Number fromString(String string) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		});
		yAxis = new NumberAxis();
		yAxis.setLabel("Light (lux)");
		chart = new LineChart<>(xAxis, yAxis);
		chart.setCreateSymbols(false);
		chart.setAnimated(false);
		chart.setLegendVisible(true);
		chart.setTitle("Photocell Readings");
		luxSeries = new Series<>();
		luxSeries.setName("Light Level (lux)");
		aveLuxSeries5 = new Series<>();
		aveLuxSeries5.setName("Moving Average 5'' (lux)");
		aveLuxSeries10 = new Series<>();
		aveLuxSeries10.setName("Moving Average 10'' (lux)");
		chart.getData().addAll(luxSeries, aveLuxSeries5, aveLuxSeries10);
		listener = (ov, t, t1) -> {
			Platform.runLater(() -> {
				try {
					String[] data = t1.split(Serial.SEPARATOR);
					if (data.length == 2) {
						long time = new Long(data[0]);
						luxSeries.getData().add(new Data(time, new Double(data[1])));
						if (luxSeries.getData().size() > tamMax) {
							luxSeries.getData().remove(0);
						}
						if (luxSeries.getData().size() >= tamAve5) {
							aveLuxSeries5.getData().add(
									new Data<>(time, average(tamAve5)));
							if (aveLuxSeries5.getData().size() > tamMax) {
								aveLuxSeries5.getData().remove(0);
							}
						}
						if (luxSeries.getData().size() >= tamAve10) {
							aveLuxSeries10.getData().add(
									new Data<>(time, average(tamAve10)));
							if (aveLuxSeries10.getData().size() > tamMax) {
								aveLuxSeries10.getData().remove(0);
							}
						}
					}
				} catch (NumberFormatException nfe) {
					System.out.println("NFE: " + t1 + " " + nfe.toString());
				}
			});
		};
		serial.getLine().addListener(listener);

		BorderPane root = new BorderPane();
		StackPane stack = new StackPane();
		stack.setPadding(new Insets(10, 0, 10, 0));
		stack.getChildren().add(chart);
		root.setCenter(stack);
		Label lbl = new Label("Not connected");
		connection.addListener((ov, b, b1) -> lbl.setText(b1 ?
		                                                  "Connected to: " + serial.getPortName() : "Not connected"));
		root.setBottom(lbl);
//        root.setStyle("-fx-background-color: derive(goldenrod,60%); "
//		                      + "-fx-font: 16 \"Courier New\";");


		root.getStylesheets().add("/styles/DarkTheme.css");
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setTitle("Arduino Test. Photocell");
		primaryStage.setScene(scene);
		primaryStage.show();

		startSerial();
	}

	@Override
	public void stop() {
		System.out.println("Closing serial port");
		serial.getLine().removeListener(listener);
		stopSerial();
	}

	private void startSerial() {
		serial.connect();
		connection.set(!serial.getPortName().isEmpty());
	}

	private void stopSerial() {
		if (connection.get()) {
			serial.disconnect();
			connection.set(false);
		}
	}

	private double average(int tam) {
		return luxSeries.getData()
		                .stream()
		                .skip(luxSeries.getData().size() - tam)
		                .mapToDouble(d -> d.getYValue().doubleValue())
		                .sum() / tam;
	}

	public static void main(String[] args) {
		launch(args);
	}

}