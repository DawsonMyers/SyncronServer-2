package fx.arduino;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Chapter 10. JavaFX and Arduino
 *
 * @author pereda
 */
public class PhotocellChart extends AnchorPane {

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

	private final static String LED_ON  = "H";
	private final static String LED_OFF = "L";

	private final Label lblLux      = new Label();
	private final Label lblLuxAve5  = new Label();
	private final Label lblLuxAve10 = new Label();
	long runtime   = 0;
	long startTime = 0;
	PhotocellChart me;

	public PhotocellChart() {
		me = this;
		//public void start(Stage primaryStage) {
		xAxis = new NumberAxis();
		xAxis.setLabel("Time");
		xAxis.setAutoRanging(true);
		xAxis.setForceZeroInRange(false);
		xAxis.setTickLabelFormatter(new StringConverter<Number>() {
			@Override
			public String toString(Number t) {
				return (System.currentTimeMillis() - startTime) / 1000.00 + "";
//                return new SimpleDateFormat("HH:mm:ss").format(new Date(t.longValue()));
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
						long time = new Long(data[0]) - startTime;
//                        long time=System.currentTimeMillis() - new Long(data[0]);
						luxSeries.getData().add(new Data(time, new Double(data[1])));
						lblLux.setText("Light Level: "
								               + String.format("%6.2f", new Double(data[1])));
						if (luxSeries.getData().size() > tamMax) {
							luxSeries.getData().remove(0);
						}
						if (luxSeries.getData().size() >= tamAve5) {
							double av = average(tamAve5);
							aveLuxSeries5.getData().add(new Data<>(time, av));
							lblLuxAve5.setText("Light Level (Average 5''): "
									                   + String.format("%6.2f", av));
							if (aveLuxSeries5.getData().size() > tamMax) {
								aveLuxSeries5.getData().remove(0);
							}
						}
						if (luxSeries.getData().size() >= tamAve10) {
							double av = average(tamAve10);
							aveLuxSeries10.getData().add(new Data<>(time, av));
							lblLuxAve10.setText("Light Level (Average 10''): "
									                    + String.format("%6.2f", av));
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
		Button btnStart = new Button("Start");
		btnStart.setOnAction(t -> {startSerial(); startTime = System.currentTimeMillis();});
		btnStart.disableProperty().bind(connection);
		Button btnStop = new Button("Stop");
		btnStop.setOnAction(t -> stopSerial());
		btnStop.disableProperty().bind(connection.not());
		Button btnSnap = new Button("Snap");
		btnSnap.setOnAction(t -> saveSnapshot());

		ToggleButton tglLed = new ToggleButton("LED Off");
		tglLed.disableProperty().bind(connection.not());
		tglLed.selectedProperty().addListener((ov, t, t1) -> {
			// serial.write(t1?LED_ON:LED_OFF);
			tglLed.setText(t1 ? "LED On " : "LED Off");
		});
		ToolBar tUp = new ToolBar();
		tUp.getItems().addAll(btnStart, btnStop, new Separator(), btnSnap, new Separator(), tglLed);
		root.setTop(tUp);

		StackPane stack = new StackPane();
		stack.setPadding(new Insets(10, 0, 10, 0));
		stack.getChildren().add(chart);
		root.setCenter(stack);
		Label lbl = new Label("Not connected");
		connection.addListener((ov, b, b1) -> lbl.setText(b1 ? "Connected to: " + serial.getPortName() : "Not connected"));
		lbl.setPrefWidth(250);
		lblLux.setPrefWidth(120);
		lblLuxAve5.setPrefWidth(180);
		lblLuxAve10.setPrefWidth(180);
		ToolBar tBottom = new ToolBar();
		tBottom.getItems().addAll(lbl, new Separator(), lblLux, new Separator(),
		                          lblLuxAve5, new Separator(), lblLuxAve10);
		root.setBottom(tBottom);
		getChildren().add(root);

		root.prefHeightProperty().bind(heightProperty());
		//root.maxHeightProperty().bind(heightProperty());
		//root.setMaxHeight(getPrefHeight());
		// root.maxWidthProperty().bind(widthProperty());
		root.prefWidthProperty().bind(widthProperty());


//		 root.layoutXProperty().bind(widthProperty());
//		root.layoutYProperty().bind(widthProperty());

		//Scene scene = new Scene(root, 800, 600);
		//  scene.getStylesheets().add(getClass().getResource("photocell.css").toExternalForm());
		getStylesheets().add("/styles/DarkTheme.css");
//		primaryStage.setTitle("Arduino Test 2. Photocell&LED");
//		primaryStage.setScene(scene);
//		primaryStage.show();
	}

	public void setResize(AnchorPane n) {
		n.setTopAnchor(this, 0.0);
		n.setRightAnchor(this, 0.0);
		n.setLeftAnchor(this, 0.0);
		n.setBottomAnchor(this, 0.0);
	}

	public PhotocellChart get() {
		return me;
	}

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

	private void saveSnapshot() {
		System.out.println("Saving snapshot... ");
		final WritableImage image = chart.snapshot(null, null);
		final BufferedImage fromFXImage = SwingFXUtils.fromFXImage(image, null);
		if (fromFXImage != null) {
			try {
				ImageIO.write(fromFXImage, "png", new File("image.png"));
			} catch (IOException ex) {
				System.out.println("error img: " + ex.toString());
			}
		}
	}

//	public static void main(String[] args) {
//		launch(args);
//	}

}