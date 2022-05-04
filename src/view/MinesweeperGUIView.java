/**
 * NAME: MinesweeperGUIView.java
 * COURSE: CSC 335 - Spring 2022
 * DESCRIPTION: This file contains GUI component of the application for Minesweeper. It initializes the
 * 				interface of the game and allows the user to interact with it. It presents the 
 * 				mine field with a GridPane of ToggleButton
 * USAGE: to be used with the whole project including MinesweeperController.java, MinesweeperLauncher.java
 * 		  MinesweeperModel.java along with other utilities.
 * 
 * @author Eleanor Simon
 * @author Giang Huong Pham
 * @author Tanmay Agrawal
 * @author Katherine Wilson
 */
package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import controller.MinesweeperController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MinesweeperModel;
import utilities.IllegalFlagPlacementException;
import utilities.IllegalStepException;
import utilities.Space;

@SuppressWarnings("deprecation")
public class MinesweeperGUIView extends Application implements Observer {
	/**
	 * Width of the game window.
	 */
	private static final int SCENE_WIDTH = 800;
	
	/**
	 * Height of the game window.
	 */
	private static final int SCENE_HEIGHT = 600;

	/**
	 * Relative path to image used for flags.
	 */
	private static final Image GREAT_FLAG = new Image("/greatflag.png");
	
	/**
	 * Relative path to image used for mines.
	 */
	private static final Image MINE = new Image("/mine.png");
	
	/**
	 * Name of the save file.
	 */
	private static final String SAVE_NAME = "saved_game.dat";
	
	/**
	 * This is the model where all game's data is stored.
	 */
	private MinesweeperModel model;
	/**
	 * This is the controller of the game, through which the gui change the
	 * underlying data in the model
	 */
	private MinesweeperController controller;
	/**
	 * This field contains the grid of ToggleButton representing the minfield's
	 * interactive "boxes".
	 */
	private ToggleButton[][] gridpaneMap;
	/**
	 * This field is the GridPane representing the mine field.
	 */
	private GridPane gridpane;
	/**
	 * This field is basically used to display the current time in seconds
	 */
	private Label timeLabel;

	/**
	 * Color of background (top).
	 */
	private static final Color BACKGROUND_COLOR_1 = Color.rgb(185, 197, 228);
	/**
	 * Color of background (bottom).
	 */
	private static final Color BACKGROUND_COLOR_2 = Color.rgb(255, 240, 245);
	
	/**
	 * This field contains the dimension of the mine field.
	 */
	private int FIELD_LENGTH;
	private int FIELD_WIDTH;
	private static final int SPACE_SIZE = 25;
	
	/**
	 * True while game is in progress.
	 */
	private boolean gameInProgress = true;
	
	/**
	 * Object used throughout gameplay to show the user various alerts.
	 */
	private Alert alert = new Alert(AlertType.NONE);
	
	/**
	 * True when the alert at the end of the game has already been shown. False
	 * if not.
	 */
	private boolean alertShown = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		System.out.println("Start of stage");
		// Start the timer
		startTime();
		// Displaying the title "Mine Sweeper"
		Label toptext = new Label("Mine Sweeper");
		Font tittlefont = Font.font("Times New Roman", 30);
		toptext.setFont(tittlefont);
		HBox title = new HBox();
		title.getChildren().add(toptext);
		title.setAlignment(Pos.TOP_CENTER);

		// load the saved game if there is any, and
		// set the dimension of the game
		this.initGame(Integer.parseInt(getParameters().getUnnamed().get(0)),
				      Integer.parseInt(getParameters().getUnnamed().get(1)),
				      Integer.parseInt(getParameters().getUnnamed().get(2)));
		
		// Label for the Timer Initialized here
		timeLabel = new Label();
		HBox timeBox = new HBox();
		timeBox.getChildren().add(timeLabel);
		timeLabel.setAlignment(Pos.CENTER);
		timeBox.setAlignment(Pos.CENTER);
		// HBox that contains the GridPane
		HBox board = new HBox();
		gridpane = new GridPane();
		gridpaneMap = setlabel(FIELD_LENGTH, FIELD_WIDTH, gridpane);
		board.getChildren().add(gridpane);
		board.setAlignment(Pos.CENTER);

		// adding 2 HBox into the VBox
		VBox vbox = new VBox();
		vbox.getChildren().add(title);
		vbox.getChildren().add(timeBox);
		vbox.getChildren().add(board);
		//vbox.setStyle("-fx-background-color: rgb(170, 177, 189);");
		
		Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
		//Stop[] stop1 = new Stop[] { new Stop(0, BACKGROUND_COLOR_1), new Stop(1, BACKGROUND_COLOR_2)};
		//LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stop1);
		//scene.setFill(lg1);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest( ev -> {
			this.controller.saveGameState();
		});
		System.out.println("Stage showing now");
	}

	// --------------------------------------------------[ PRIVATE METHODS]-------------------------------------------------- //

	/**
	 * This method initialize the game when the game is launched: it checks for
	 * saved files of the previous uncompleted game
	 */
	private void initGame(int length, int width, int numberOfMines) {
		// Trying to load the saved_game when the application is launched
		File savedGame = new File(SAVE_NAME);
		if (savedGame.exists()) {
			try {
				System.out.println("Save loaded.");
				model = new MinesweeperModel(SAVE_NAME);
			} catch (FileNotFoundException e) {
				System.out.println("Cannot find the saved game file.");
				model = new MinesweeperModel();
			} catch (ClassNotFoundException e) {
				System.out.println("Cannot load the data from the saved game.");
				model = new MinesweeperModel();
			} catch (IOException e) {
				System.out.println("Fail to load the saved game from file.");
				model = new MinesweeperModel();
			}
		} else {
			model = new MinesweeperModel(length, width, numberOfMines);
		}
		model.addObserver(this);
		controller = new MinesweeperController(model);
		// get dimension of the mine field and create
		// a 2D array of ToggleButton with the same dimension
		this.FIELD_LENGTH = model.getDimensions()[0];
		this.FIELD_WIDTH = model.getDimensions()[1];
	}

	/**
	 * This method initialize toggle buttons in the grid pane and add them to an
	 * array list to keep track
	 * 
	 * @param x    integer representing the width of the mine field
	 * @param y    integer representing the length of the mine field
	 * @param pane GridPane representing the mine field.
	 * @return a 2D array of ToggleButtons displayed on the grid pane
	 */
	private ToggleButton[][] setlabel(int x, int y, GridPane pane) {
		ToggleButton[][] ret = new ToggleButton[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				ToggleButton button = new ToggleButton();
				button.setMinWidth(SPACE_SIZE);
				ret[i][j] = button;
				setEventListener(button, pane);
				GridPane.setConstraints(button, j, i);
				pane.getChildren().add(button);
			}
		}
		return ret;
	}

	
	/**
	 * This method adds necessary event listener onto the ToggleButton
	 * 
	 * @param button ToggleButton where event was caught
	 * @param pane   GridPane representing the mine field. 
	 */
	private void setEventListener(ToggleButton button, GridPane pane) {
		button.setOnMouseClicked(mouseEvent -> {
			MouseButton click = mouseEvent.getButton();

			ToggleButton target = (ToggleButton) mouseEvent.getSource();
			int y = GridPane.getRowIndex(target);
			int x = GridPane.getColumnIndex(target);
			
			try {
				// left click to reveal a space
				if (click == MouseButton.PRIMARY) {
					if (gameInProgress) {			// Block input
						controller.takeStep(x, y);
					}
				} // right click to put/remove flag/question mark
				// TODO if we implement question: if it's a flag, turn into question
				// if it's a question, turn into open space (unrevealed) 
				// TODO: implement the model also if we decide to do the question mark.
				else if (click == MouseButton.SECONDARY) {
					// place flag
					controller.toggleFlag(x, y);
				}
			} catch (IllegalStepException e) {
				System.out.println(e.getMessage());
				return;
			} catch (IllegalFlagPlacementException e) {
				System.out.println(e.getMessage());
				return;
			}
		});
	}


	/**
	 * This method updates the given ToggleButton according to 
	 * the passed in string: "flag" or "mine".
	 * 
	 * @param button ToggleButton to change the image of.
	 * @param string name of image to put onto the button display.
	 */
	private void setImage(String img, ToggleButton button) {
		ImageView image = new ImageView();
		if (img.equals("flag")) {
			image = new ImageView(GREAT_FLAG);
			image.setFitHeight(20);
			image.setFitWidth(15);
			button.setStyle("-fx-padding: 2px;");
		} else if (img.equals("mine")) {
			image = new ImageView(MINE);
			image.setFitHeight(23);
			image.setFitWidth(23);
			button.setStyle("-fx-padding: 0px;");
		}
		if (image != null) {
			image.setPreserveRatio(true);
			button.setGraphic(image);
		}
		
	}
	
	/**
	 * Removes the flag image from a button.
	 * 
	 * @param button button in which flag is removed from.
	 */
	void removeFlag(ToggleButton button) {
		button.setMaxWidth(25);
		button.setPrefHeight(25);
		button.setGraphic(null);
		button.setSelected(false);
	}
	
	/**
	 * This method updates the GridPane of ToggleButton 
	 * 
	 * @param revealMine boolean to tell whether to show the mine on the map or not.
	 * 
	 * @param spaceGrid  Space[][] representing the underlying mine map. 
	 */
	private void updateGrid(boolean revealMine, Space[][] spaceGrid, boolean playerWon) {
		for (int i = 0; i< this.FIELD_LENGTH; i++) {
			for (int j = 0; j  < this.FIELD_WIDTH; j++) {
				Space space = spaceGrid[i][j];
				ToggleButton button = gridpaneMap[i][j];
				if (space.hasFlag()) {										// if the space is flagged
					setImage("flag", button);
					button.setText("");
					button.setSelected(false);
					button.setDisable(false);
					if (revealMine && playerWon && space.hasMine()) {
						waveAnimation(button, new Duration(500));
					}
				} else if (space.isRevealed() && !space.hasMine()) {		// if the space is not flagged and does not have mine
					int adjMine = space.adjacentMines();
					button.getStyleClass().add("grey-button");
					if (adjMine == 0) {
						button.setSelected(true);
						button.setDisable(true);
						button.setText("  ");
					} else if (adjMine == 1) {
						button.setText("1");
						button.setTextFill(Paint.valueOf("blue"));
						button.setSelected(false);
					} else if (adjMine == 2) {
						button.setText("2");
						button.setTextFill(Paint.valueOf("green"));
						button.setSelected(false);
					} else if (adjMine == 3) {
						button.setText("3");
						button.setTextFill(Paint.valueOf("red"));
						button.setSelected(false);
					} else if (adjMine == 4) {
						button.setText("4");
						button.setTextFill(Paint.valueOf("purple"));
						button.setSelected(false);
					} else if (adjMine == 5) {
						button.setText("5");
						button.setTextFill(Paint.valueOf("black"));
						button.setSelected(false);
					} else if (adjMine == 6) {
						button.setText("6");
						button.setTextFill(Paint.valueOf("gray"));
						button.setSelected(false);
					} else if (adjMine == 7) {
						button.setText("7");
						button.setTextFill(Paint.valueOf("maroon"));
						button.setSelected(false);
					} else if (adjMine == 8) {
						button.setText("8");
						button.setTextFill(Paint.valueOf("turquoise"));
						button.setSelected(false);
					}
				} else if (space.hasMine() && revealMine) {
					setImage("mine", button);
					if (playerWon) {
						waveAnimation(button, new Duration(500));
					} else {
						shakeAnimation(button, new Duration(10));
					}
				} else {
					removeFlag(button);
					button.setText("");
				}
			}
		}
	}
	
	/**
	 * Creates an animation on a node
	 * Method waveAnimation takes a node and a time as parameters that is used to make
	 * a wave-like animation out of sequential labels that are passed to it.
	 * 
	 * @param toWake JavaFX <code>Node</code> to wave.
	 * 
	 * @param timeLimit the duration of the animation.
	 */
	private void waveAnimation(Node toWave, Duration timeLimit) {
		TranslateTransition wave = new TranslateTransition();
		wave.setDuration(timeLimit);
		wave.setNode(toWave);
		wave.setByY(-5);
		wave.setCycleCount(12);
		wave.setAutoReverse(true);
		wave.play();
	}
	
	/**
	 * Creates an animation on a node. This quickly shakes the nodes horizontally
	 * for 12 cycles.
	 * 
	 * @param toShake JavaFX <code>Node</code> to shake.
	 * @param timeLimit the duration of the animation.
	 */
	private void shakeAnimation(Node toShave, Duration timeLimit) {
		TranslateTransition wave = new TranslateTransition();
		wave.setDuration(timeLimit);
		wave.setNode(toShave);
		wave.setByX(-5);
		wave.setByX(5);
		wave.setCycleCount(12);
		wave.setAutoReverse(true);
		wave.play();
	}
	
	/**
	 * This method basically is used to update time in the background by 
	 * prompting the controller to set a chain of events that update time every second.
	 * 
	 * @param controller the instance of the controller for the gameplay
	 */
	private void startTime() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			controller.setTime(controller.getTime()+1);
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	

	/**
	 * This method overrides the update method in the observer. 
	 * @param o Observable object, in this case, the game model.
	 * 
	 * @param saveGame (expected to be boolean) tells whether or not to save the game 
	 * 		  the game should be saved when game is not Over.
	 */
	@Override
	public void update(Observable o, Object saveGame) {
		MinesweeperModel model = (MinesweeperModel) o;
		if (model.isGameOver()) {
			gameInProgress = false;
			if (model.getPlayerWon()) {
				if (!alertShown) {
					alert.setAlertType(AlertType.INFORMATION);
					alert.setContentText("You win!"); // alert the game is over
					alert.show();
					alertShown = true;
				}
				updateGrid(true, model.getMinefield(), true);
			} else {
				if (!alertShown) {
	 				alert.setAlertType(AlertType.INFORMATION);
					alert.setContentText("You lost!"); // alert the game is over
					alert.show();
					alertShown = true;
				}
				updateGrid(true, model.getMinefield(), false);
			}
			File savedGame = new File(SAVE_NAME);
			if (savedGame.exists()) {
				savedGame.delete();
			}
		} else {
			updateGrid(false, model.getMinefield(), false);
		}
		
		// update the timeLabel
		// timeLabel != null checked only in case timer starts a few milliseconds before we
		// init the timeLabel in start()
		if(timeLabel != null) {
			int minutes = model.getTime() / 60;
			int seconds = model.getTime() % 60;
			String secondsString = ((int)(seconds/10))==0? "0"+seconds: ""+seconds;
			timeLabel.setText(minutes+" : "+ secondsString);
		}
		// TODO: implement function in model to be called when the user exit the game:
		// check if isGameover() -> if not, set arg to a boolean to decide to serialize 
		// the model or not 	
	}

}