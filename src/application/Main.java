package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * @author Faisal Jamil
 * @StudentNumber 766747
 */

public class Main extends Application {
	// Layout for toolbox
	private GridPane toolboxPane;
	// List of circles
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	// List of Rectangles
	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

	// Radius label
	private Label circleRadiusLabel = new Label("Radius:");
	// Radius field
	private Spinner<Integer> circleRadiusField = new Spinner<Integer>();
	private SpinnerValueFactory<Integer> circleRadiusValueFactory = new IntegerSpinnerValueFactory(0, 9999, 50);
	// Radius Measurements
	private ComboBox<String> circleRadiusMeasurement = new ComboBox<String>();
	// Initial radius
	private int circleRadius = 0;

	private Label circleFillColorLabel = new Label("Color:");
	private ColorPicker circleFillColor = new ColorPicker();

	private Label circleStrokeLabel = new Label("Stroke Width:");
	private Spinner<Integer> circleStrokeField = new Spinner<Integer>();
	private SpinnerValueFactory<Integer> circleStrokeValueFactory = new IntegerSpinnerValueFactory(0, 9999, 25);
	private ComboBox<String> circleStrokeMeasurement = new ComboBox<String>();
	private int circleStrokeWidth = 0;

	private Label circleStrokeColorLabel = new Label("Stroke Color:");
	private ColorPicker circleStrokeColor = new ColorPicker();

	private Label rectangleWidthLabel = new Label("Width:");
	private Spinner<Integer> rectangleWidthField = new Spinner<Integer>();
	private SpinnerValueFactory<Integer> rectangleWidthValueFactory = new IntegerSpinnerValueFactory(0, 9999, 50);
	private ComboBox<String> rectangleWidthMeasurement = new ComboBox<String>();
	private int rectangleWidth = 0;

	private Label rectangleLengthLabel = new Label("Length:");
	private Spinner<Integer> rectangleLengthField = new Spinner<Integer>();
	private SpinnerValueFactory<Integer> rectangleLengthValueFactory = new IntegerSpinnerValueFactory(0, 9999, 50);
	private ComboBox<String> rectangleLengthMeasurement = new ComboBox<String>();
	private int rectangleLength = 0;

	private Label rectangleFillColorLabel = new Label("Color:");
	private ColorPicker rectangleFillColor = new ColorPicker();

	private Label rectangleStrokeLabel = new Label("Stroke Color:");
	private Spinner<Integer> rectangleStrokeField = new Spinner<Integer>();
	private SpinnerValueFactory<Integer> rectangleStrokeValueFactory = new IntegerSpinnerValueFactory(0, 9999, 25);
	private ComboBox<String> rectangleStrokeMeasurement = new ComboBox<String>();
	private int rectangleStrokeWidth = 0;

	private Label rectangleStrokeColorLabel = new Label("Stroke Color:");
	private ColorPicker rectangleStrokeColor = new ColorPicker();

	// Rectangle with the size equaling screen size
	// Used for positioning
	private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

	private int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

	private double randomWithRange(double min, double max) {
		double range = (max - min);
		return (Math.random() * range) + min;
	}

	/**
	 * Takes a screenshot of the scene argument and saves to a user defined path
	 * 
	 * @param drawingBoard
	 *            The scene associated with the drawing board
	 * @param toolbox
	 *            The stage associated with the toolbox
	 */
	private void saveDrawing(Scene drawingBoard, Stage toolbox) {
		// Sets the toolbox behind the file location window
		toolbox.setAlwaysOnTop(false);

		FileChooser imagePath = new FileChooser();

		// Set type of file extension
		imagePath.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

		// User selects file location
		File drawing = imagePath.showSaveDialog(null);

		if (drawing != null) {
			try {
				WritableImage writableImage = new WritableImage((int) drawingBoard.getWidth(),
						(int) drawingBoard.getHeight());
				// Takes a snapshot of the scene
				drawingBoard.snapshot(writableImage);
				// Writes the snapshot to the chosen file
				ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", drawing);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Sets the toolbox back on top of the drawing board
		toolbox.setAlwaysOnTop(true);

	}

	/**
	 * Sets the properties of the circle nodes
	 */
	public void setCircleProperties() {
		// Sets minimum value, maximum value, and initial set value
		circleRadiusField.setValueFactory(circleRadiusValueFactory);
		circleRadiusField.setPrefWidth(100);
		// Allows user to manually edit the value
		circleRadiusField.setEditable(true);
		// Forces the the spinner to set the radius value once focus is lost
		circleRadiusField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {
					circleRadiusField.increment(0); // Won't change value
					if (circleRadiusField.getValue() < 0) {
						circleRadiusField.getValueFactory().setValue(0);
					}
				}
				// If a non-number is entered, value is set to 0
				catch (Exception E) {
					circleRadiusField.getValueFactory().setValue(0);
				}
			}
		});
		circleRadiusMeasurement.getItems().addAll("px", "cm");
		// Default value is the first choice
		circleRadiusMeasurement.getSelectionModel().selectFirst();

		// Adds color picker with the black color as default value
		circleFillColor.setValue(Color.BLACK);
		circleFillColor.setPrefWidth(100);

		circleStrokeField.setValueFactory(circleStrokeValueFactory);
		circleStrokeField.setPrefWidth(100);
		circleStrokeField.setEditable(true);
		circleStrokeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {
					if (circleStrokeField.getValue() < 0) {
						circleStrokeField.getValueFactory().setValue(0);
					}
					circleStrokeField.increment(0); // Won't change value
				}
				// If a non-number is entered, value is set to 0
				catch (Exception E) {
					circleStrokeField.getValueFactory().setValue(0);
				}
			}
		});
		circleStrokeMeasurement.getItems().addAll("px", "cm");
		circleStrokeMeasurement.getSelectionModel().selectFirst();

		circleStrokeColor.setValue(Color.BLACK);
		circleStrokeColor.setPrefWidth(100);
	}

	/**
	 * Adds circle properties to the toolbox
	 */
	private void circleCreator() {
		toolboxPane.add(circleRadiusLabel, 0, 2);
		toolboxPane.add(circleRadiusField, 1, 2);
		toolboxPane.add(circleRadiusMeasurement, 2, 2);
		toolboxPane.add(circleFillColorLabel, 0, 3);
		toolboxPane.add(circleFillColor, 1, 3);
		toolboxPane.add(circleStrokeLabel, 0, 4);
		toolboxPane.add(circleStrokeField, 1, 4);
		toolboxPane.add(circleStrokeMeasurement, 2, 4);
		toolboxPane.add(circleStrokeColorLabel, 0, 5);
		toolboxPane.add(circleStrokeColor, 1, 5);
	}

	/**
	 * Removes all nodes associated with circle properties from the toolbox
	 */
	private void resetCircleCreator() {
		toolboxPane.getChildren().removeAll(circleRadiusLabel, circleRadiusField, circleRadiusMeasurement,
				circleFillColorLabel, circleFillColor, circleStrokeLabel, circleStrokeField, circleStrokeMeasurement,
				circleStrokeColorLabel, circleStrokeColor);
	}

	/**
	 * Sets the properties of the rectangle nodes
	 */
	private void setRectangleProperties() {
		// Sets minimum value, maximum value, and initial set value
		rectangleWidthField.setValueFactory(rectangleWidthValueFactory);
		rectangleWidthField.setPrefWidth(100);
		// Allows user to manually edit the value
		rectangleWidthField.setEditable(true);
		// Forces the the spinner to set the width value once focus is lost
		rectangleWidthField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {
					rectangleWidthField.increment(0); // Won't change value
					// Sets the value to 0 if a negative number is entered
					if (rectangleWidthField.getValue() < 0) {
						rectangleWidthField.getValueFactory().setValue(0);
					}
				}
				// If a non-number is entered, value is set to 0
				catch (Exception E) {
					rectangleWidthField.getValueFactory().setValue(0);
				}
			}
		});
		rectangleWidthMeasurement.getItems().addAll("px", "cm");
		// Default value is the first choice
		rectangleWidthMeasurement.getSelectionModel().selectFirst();

		rectangleLengthField.setValueFactory(rectangleLengthValueFactory);
		rectangleLengthField.setPrefWidth(100);
		// Allows user to manually edit the value
		rectangleLengthField.setEditable(true);
		// Forces the the spinner to set the length value once focus is lost
		rectangleLengthField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {
					rectangleLengthField.increment(0); // Won't change value
					// Sets the value to 0 if a negative number is entered
					if (rectangleLengthField.getValue() < 0) {
						rectangleLengthField.getValueFactory().setValue(0);
					}
				}
				// If a non-number is entered, value is set to 0
				catch (Exception E) {
					rectangleLengthField.getValueFactory().setValue(0);
				}
			}
		});
		rectangleLengthMeasurement.getItems().addAll("px", "cm");
		// Default value is the first choice
		rectangleLengthMeasurement.getSelectionModel().selectFirst();

		// Adds color picker with the black color as default value
		rectangleFillColor.setValue(Color.BLACK);
		rectangleFillColor.setPrefWidth(100);

		rectangleStrokeField.setValueFactory(rectangleStrokeValueFactory);
		rectangleStrokeField.setPrefWidth(100);
		rectangleStrokeField.setEditable(true);
		rectangleStrokeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {
					if (rectangleStrokeField.getValue() < 0) {
						rectangleStrokeField.getValueFactory().setValue(0);
					}
					rectangleStrokeField.increment(0); // Won't change value
				}
				// If a non-number is entered, value is set to 0
				catch (Exception E) {
					rectangleStrokeField.getValueFactory().setValue(0);
				}
			}
		});
		rectangleStrokeMeasurement.getItems().addAll("px", "cm");
		rectangleStrokeMeasurement.getSelectionModel().selectFirst();

		rectangleStrokeColor.setValue(Color.BLACK);
		rectangleStrokeColor.setPrefWidth(100);
	}

	/**
	 * Adds the rectangle properties to the toolbox
	 */
	private void rectangleCreator() {
		toolboxPane.add(rectangleWidthLabel, 0, 2);
		toolboxPane.add(rectangleWidthField, 1, 2);
		toolboxPane.add(rectangleWidthMeasurement, 2, 2);
		toolboxPane.add(rectangleLengthLabel, 0, 3);
		toolboxPane.add(rectangleLengthField, 1, 3);
		toolboxPane.add(rectangleLengthMeasurement, 2, 3);
		toolboxPane.add(rectangleFillColorLabel, 0, 4);
		toolboxPane.add(rectangleFillColor, 1, 4);
		toolboxPane.add(rectangleStrokeLabel, 0, 5);
		toolboxPane.add(rectangleStrokeField, 1, 5);
		toolboxPane.add(rectangleStrokeMeasurement, 2, 5);
		toolboxPane.add(rectangleStrokeColorLabel, 0, 6);
		toolboxPane.add(rectangleStrokeColor, 1, 6);
	}

	/**
	 * Removes the rectangle properties from the toolbox
	 */
	private void resetRectangleCreator() {
		toolboxPane.getChildren().removeAll(rectangleWidthLabel, rectangleWidthField, rectangleWidthMeasurement,
				rectangleLengthLabel, rectangleLengthField, rectangleLengthMeasurement, rectangleFillColorLabel,
				rectangleFillColor, rectangleStrokeLabel, rectangleStrokeField, rectangleStrokeMeasurement,
				rectangleStrokeColorLabel, rectangleStrokeColor);
	}

	private void playPopSound(int numberSelect) {
		try {
			String musicFile = "";

			
			switch (numberSelect) {
			case 1:
				musicFile = "pop1.mp3";
				break;
			case 2:
				musicFile = "pop2.mp3";
				break;
			case 3:
				musicFile = "pop3.mp3";
				break;
			case 4:
				musicFile = "pop4.mp3";
				break;
			case 5:
				musicFile = "pop5.mp3";
				break;
			case 6:
				musicFile = "pop6.mp3";
				break;
			default:
				musicFile = "pop1.mp3";
				break;
			}
			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.setVolume(0.3);
			mediaPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Creates layouts for positioning
			VBox vBox = new VBox();
			BorderPane welcomeScreenPane = new BorderPane(vBox);
			Scene welcomeScreen = new Scene(welcomeScreenPane);

			// Implements CSS sheets
			welcomeScreen.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
			welcomeScreenPane.getStyleClass().add("welcomeScreen");

			// Displays Title
			Text welcomeScreenText = new Text("Make Art");
			welcomeScreenText.getStyleClass().add("welcomeScreenText");

			// Changes the title to a random color every 0.7 seconds
			Timeline changeTitleColor = new Timeline(new KeyFrame(Duration.seconds(0.7), e -> {
				welcomeScreenText.setFill(Color.color(Math.random(), Math.random(), Math.random()));
			}));

			Timeline addRandomShapes = new Timeline(new KeyFrame(Duration.seconds(2 ), e -> {
				// int shapeSelector = randomWithRange(0, 1);
				// if (shapeSelector == 0) {
				Circle circle = new Circle(randomWithRange(0, welcomeScreen.getWidth()),
						randomWithRange(0, welcomeScreen.getHeight()), randomWithRange(10, 150));
				circle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
				welcomeScreenPane.getChildren().add(circle);
				circles.add(circle);
				for (int i = 0; i < circles.size(); i++) {
					if (circles.get(i).intersects(vBox.localToScene(vBox.getBoundsInLocal()))) {
						welcomeScreenPane.getChildren().remove(circles.get(i));
						circles.remove(i);
						break;
					}
				}
				playPopSound(randomWithRange(1, 6));
			}));
			addRandomShapes.setCycleCount(Timeline.INDEFINITE);
			addRandomShapes.play();
			changeTitleColor.setCycleCount(Timeline.INDEFINITE);
			changeTitleColor.play();
			// Creates a functioning button which runs the application once clicked
			Button launch = new Button("Start");
			launch.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					primaryStage.hide();
					addRandomShapes.stop();
					changeTitleColor.stop();
					app();
				}
			});
			
			welcomeScreenPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					for (int i = 0; i < circles.size(); i++) {
						if (circles.get(i).contains(event.getX(), event.getY())) {
							welcomeScreenPane.getChildren().remove(circles.get(i));
							circles.remove(i);
							break;
						}
					}
				}
			});
			// Centers the nodes for the main window
			vBox.setSpacing(10);
			vBox.setMaxSize(400, 400);
			// This works...
			vBox.setAlignment(Pos.CENTER);
			welcomeScreenPane.setCenter(vBox);

			// Adds nodes to the layout
			vBox.getChildren().addAll(welcomeScreenText, launch);

			// Sets properties of the main window
			primaryStage.setScene(welcomeScreen);
			primaryStage.setTitle("Make Art");
			primaryStage.setMaximized(true);
			primaryStage.setResizable(false);
			// Displays the main window
			primaryStage.show();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the main application by displaying the toolbox and drawing board
	 */
	private void app() {
		try {
			// Removes all shapes created in the title screen
			circles.clear();
			rectangles.clear();
			// Sets toolbox stage
			Stage secondaryStage = new Stage();
			// Sets the drawing board
			Stage mainStage = new Stage();
			// Sets the shape node properties
			setCircleProperties();
			setRectangleProperties();

			// Creates layouts for positioning
			toolboxPane = new GridPane();
			ColumnConstraints column1 = new ColumnConstraints();
			column1.setPercentWidth(40);
			toolboxPane.getColumnConstraints().add(column1);
			Scene toolbox = new Scene(toolboxPane);
			BorderPane applicationPane = new BorderPane();
			Scene drawingBoard = new Scene(applicationPane);
			// Adds the CSS file for styling
			drawingBoard.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
			applicationPane.getStyleClass().add("drawingBoard");

			// Sets gaps between each node
			toolboxPane.setVgap(20);
			toolboxPane.setHgap(10);

			// Adds padding from the left side
			toolboxPane.setPadding(new Insets(0, 0, 0, 10));

			Label createText = new Label("Create:");

			// Adds drop-down menu
			ComboBox<String> shapesComboBox = new ComboBox<String>();
			shapesComboBox.getItems().addAll("Circle", "Rectangle");
			shapesComboBox.setOnAction(e -> {
				// If circle is selected
				if (shapesComboBox.getValue() == "Circle") {
					// Removes all rectangle nodes
					resetRectangleCreator();
					// Adds all circle nodes
					circleCreator();
				}
				// If rectangle is selected
				if (shapesComboBox.getValue() == "Rectangle") {
					// Removes all circle nodes
					resetCircleCreator();
					// Adds all rectangle nodes
					rectangleCreator();
				}
			});

			// Adds a functioning button that saves the drawing board
			Button saveImage = new Button("Save Drawing");
			saveImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					saveDrawing(drawingBoard, secondaryStage);
				}

			});

			CheckBox removeBox = new CheckBox("Remove Mode");
			Button removeAllButton = new Button("Remove All");

			// Removes all nodes and objects from the list of circle objects
			removeAllButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					applicationPane.getChildren().clear();
					circles.clear();
					rectangles.clear();
				}
			});
			// Allows user to add and remove shapes to main stage when mouse is pressed
			applicationPane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// Removes selected shape if right mouse button is pressed or remove check box
					// is selected
					if (event.getButton() == MouseButton.SECONDARY || removeBox.isSelected() == true) {
						// Checks all shapes in the rectangle and circle lists until the correct shape
						// is found
						for (int i = 0; i < circles.size(); i++) {
							if (circles.get(i).contains(event.getX(), event.getY())) {
								applicationPane.getChildren().remove(circles.get(i));
								circles.remove(i);
								break;
							}
						}
						for (int j = 0; j < rectangles.size(); j++) {
							if (rectangles.get(j).contains(event.getX(), event.getY())) {
								applicationPane.getChildren().remove(rectangles.get(j));
								rectangles.remove(j);
								break;
							}
						}
					}
					// If remove box is unchecked
					else if (removeBox.isSelected() != true) {
						// If circle is chosen in the drop down menu
						if (shapesComboBox.getValue() == "Circle") {
							// Radius is set to input from the radius spinner
							circleRadius = circleRadiusField.getValue();
							// Converts pixels to centimeters
							if (circleRadiusMeasurement.getValue() == "cm") {
								circleRadius *= 37.795275591;
							}
							circleStrokeWidth = circleStrokeField.getValue();
							if (circleStrokeMeasurement.getValue() == "cm") {
								circleStrokeWidth *= 37.795275591;
							}
							// Adds circle based on mouse position
							Circle circle = new Circle(event.getSceneX(), event.getSceneY(), circleRadius);
							// Sets chosen color
							circle.setFill(circleFillColor.getValue());
							// Sets stroke and stroke color
							circle.setStrokeWidth(circleStrokeWidth);
							circle.setStroke(circleStrokeColor.getValue());
							circles.add(circle);
							applicationPane.getChildren().add(circle);
						} else if (shapesComboBox.getValue() == "Rectangle") {
							rectangleWidth = rectangleWidthField.getValue();
							if (rectangleWidthMeasurement.getValue() == "cm") {
								rectangleWidth *= 37.795275591;
							}
							rectangleLength = rectangleLengthField.getValue();
							if (rectangleLengthMeasurement.getValue() == "cm") {
								rectangleLength *= 37.795275591;
							}
							rectangleStrokeWidth = rectangleStrokeField.getValue();
							if (rectangleStrokeMeasurement.getValue() == "cm") {
								rectangleStrokeWidth *= 37.795275591;
							}
							// Creates a new rectangle
							Rectangle rectangle = new Rectangle();
							// Sets the position of the top left corner based on mouse position
							rectangle.setX(event.getSceneX());
							rectangle.setY(event.getSceneY());
							// Sets the size
							rectangle.setWidth(rectangleWidth);
							rectangle.setHeight(rectangleLength);
							// Sets the fill color
							rectangle.setFill(rectangleFillColor.getValue());
							rectangle.setStrokeWidth(rectangleStrokeWidth);
							// Sets the stroke and stroke color
							rectangle.setStroke(rectangleStrokeColor.getValue());
							// Adds the rectangle to the rectangles list
							rectangles.add(rectangle);
							// Adds the rectangle to the drawing board
							applicationPane.getChildren().add(rectangle);
						}
					}
				}
			});

			// Adds button to show instructions
			Button showInstructions = new Button("Show Instructions");
			// Creates instructions window and layout
			Stage instructionsStage = new Stage();
			instructionsStage.setResizable(false);
			GridPane instructionsPane = new GridPane();
			Text instructions = new Text("How to create shapes:\n" + "\t1. Pick your shape\n"
					+ "\t2. Choose the shape's properties\n" + "\t3. Click on the drawing board to place your shape\n"
					+ "\nHow to remove shapes manually:\n" + "\t1. Check \"Remove Mode\"\n"
					+ "\t2. Click on the shape to be removed\n" + "\n*Shapes can also be removed by right-clicking them"
					+ "\n*Press the \"Remove All\" button to remove all shapes");
			// Adds functioning button to exit window
			Button exitInstructions = new Button("OK");
			exitInstructions.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					instructionsStage.hide();
				}
			});
			// Adds nodes to window
			instructionsPane.add(instructions, 0, 0);
			instructionsPane.add(exitInstructions, 1, 1);
			// Adds gap between nods and padding
			instructionsPane.setPadding(new Insets(10));
			instructionsPane.setVgap(10);
			// Final stage adjustments
			Scene instructionsScene = new Scene(instructionsPane);
			instructionsStage.setScene(instructionsScene);
			instructionsStage.setTitle("Instructions");

			// Creates a new window displaying instructions if button is pressed
			showInstructions.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					instructionsStage.show();
				}
			});

			// Exit button terminates program
			Button exit = new Button("Exit");

			exit.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Platform.exit();
				}
			});

			// Adds nodes to the toolbox
			toolboxPane.add(createText, 0, 1);
			toolboxPane.add(shapesComboBox, 1, 1);
			toolboxPane.add(removeBox, 0, 8);
			toolboxPane.add(removeAllButton, 1, 8);
			toolboxPane.add(saveImage, 0, 9);
			toolboxPane.add(showInstructions, 1, 9);
			toolboxPane.add(exit, 2, 9);

			// Sets toolbox properties
			secondaryStage.setTitle("Toolbox");
			secondaryStage.setScene(toolbox);
			// Adjusts size
			secondaryStage.setWidth(400);
			secondaryStage.setHeight(primaryScreenBounds.getHeight());
			// Sets the toolbox window on top of the drawing board
			secondaryStage.setAlwaysOnTop(true);
			// Sets the toolbox to the top left of the screen
			secondaryStage.setX(0);
			secondaryStage.setY(0);
			secondaryStage.show();

			// Sets drawing board properties
			mainStage.setTitle("Make Art");
			mainStage.setScene(drawingBoard);
			mainStage.setMaximized(true);
			mainStage.show();
			// Terminates the program when toolbox is closed
			secondaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Platform.exit();
				};
			});
			// Terminates the program when the drawing board is closed
			mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Platform.exit();
				};
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
