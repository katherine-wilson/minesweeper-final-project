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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MinesweeperModel;
import utilities.IllegalFlagPlacementException;
import utilities.IllegalStepException;
import utilities.Space;

/**
 * This file contains GUI component of the application for Minesweeper. It initializes the
 * interface of the game and allows the user to interact with it. It represents the minefield
 * with a GridPane of ToggleButtons that the user can left or right click on to take steps or
 * toggle flags respectively. There are several animations and alerts that can occur throughout
 * the game depending on the user's actions.
 * 
 * @author Eleanor Simon
 * @author Giang Huong Pham
 * @author Tanmay Agrawal
 * @author Katherine Wilson
 */
@SuppressWarnings("deprecation")
public class MinesweeperGUIView extends Application implements Observer {
	// ------------------------------------------------------[  FIELDS  ]------------------------------------------------------	
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
	 * Length and width of flag image.
	 */
	private static final int FLAG_SIZE = 25;
	
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
	 * MinesweeperController through which the GUI interacts with the
	 * data in the underlying model.
	 */
	private MinesweeperController controller;
	/**
	 * Grid of <code>ToggleButton</code> objects representing the minefield's
	 * interactive "boxes".
	 */
	private ToggleButton[][] gridpaneMap;
	/**
	 * <code>GridPane</code> that stores each space in the mine field.
	 */
	private GridPane gridpane;
	
	/**
	 * Label that is used to display the current time in seconds.
	 */
	private Label timeLabel;
	
	/**
	 * <code>Timeline</code> object that keeps track of elapsed time.
	 */
	private Timeline timeline;
	
	/**
	 * Label that displays the number of flags available.
	 */
	private Label flagLabel;
	
	/**
	 * Length of mine field.
	 */
	private static int FIELD_LENGTH;
	
	/**
	 * Width of mine field.
	 */
	private static int FIELD_WIDTH;
	
	/**
	 * Size of spaces in the mine field.
	 */
	private static final int SPACE_SIZE = 25;
	
	/**
	 * Duration of flag placement animation.
	 */
	private static final Duration PLACE_ANIM_DURATION = new Duration(200);
	
	/**
	 * Duration of the "quick shake" animation.
	 */
	private static final Duration QUICK_SHAKE_DURATION = new Duration(50);
	
	/**
	 * Duration of the "wave" animation that plays when the player 
	 * wins the game.
	 */
	private static final Duration WAVE_DURATION = new Duration(500);
	
	/**
	 * Duration of the "shake" animation that plays when the player
	 * loses the game.
	 */
	private static final Duration SHAKE_DURATION = new Duration(30);

	/**
	 * Color of the background gradient at the top
	 */
	private static final Color BACKGROUND_COLOR_1 = Color.valueOf("#faacb3");
	/**
	 * Color of the background gradient at the bottom
	 */
	private static final Color BACKGROUND_COLOR_2 = Color.valueOf("#9178bf");
	
	/**
	 * Object used throughout game play to show the user various alerts.
	 */
	private Alert alert = new Alert(AlertType.NONE);
	
	/**
	 * True when the alert at the end of the game has already been shown. False
	 * if not. This prevents end-of-game alerts from being shown more than once.
	 */
	private boolean alertShown = false;
	

	/**
	 * Starts the GUI-based view of the Minesweeper game. Initialized everything
	 * needed for the stage and shows it. Also initializes the controller and 
	 * model needed to manage the gameplay state. If valid command-line arguments
	 * were provided by the player in the launcher, then a minefield will be displayed
	 * according to the measurements and number of mines that were specified.
	 * 
	 * @param primaryStage <code>Stage</code> that is displayed to the user over the
	 * course of gameplay.
	 */
	@Override
	public void start(Stage primaryStage) {
		System.out.println("Start of stage");
		
		// Start the timer
		startTime();
		
		// Displaying the title "Mine Sweeper"
		Label toptext = new Label("Minesweeper");
		toptext.setFont(Font.font("Century Gothic", FontWeight.EXTRA_BOLD, 35));
		toptext.setTextFill(Color.WHITE);
		//Font tittlefont = Font.font("Times New Roman", 30);
		//toptext.setFont(tittlefont);
		HBox title = new HBox();
		title.getChildren().add(toptext);
		title.setAlignment(Pos.TOP_CENTER);

		// load the saved game if there is any, or
		// set the dimension of the new game with the user input if there is no saved game
		this.initGame(Integer.parseInt(getParameters().getUnnamed().get(0)),
				      Integer.parseInt(getParameters().getUnnamed().get(1)),
				      Integer.parseInt(getParameters().getUnnamed().get(2)));
		
		// Label for the Timer 
		timeLabel = new Label();
		HBox infoBox = new HBox();
		infoBox.getChildren().add(timeLabel);
		timeLabel.setAlignment(Pos.CENTER_LEFT);
		flagLabel = new Label();
		flagLabel.setAlignment(Pos.CENTER_RIGHT);
		infoBox.getChildren().add(flagLabel);
		infoBox.setAlignment(Pos.CENTER);
		
		// HBox that contains the GridPane
		HBox board = new HBox();
		gridpane = new GridPane();
		gridpaneMap = setlabel(FIELD_LENGTH, FIELD_WIDTH, gridpane);
		board.getChildren().add(gridpane);
		board.setAlignment(Pos.CENTER);

		// adding 2 HBox into the VBox
		VBox vbox = new VBox();
		vbox.getChildren().add(title);
		vbox.getChildren().add(infoBox);
		vbox.getChildren().add(board);
		vbox.setStyle("-fx-background-color: transparent;");
		
		Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
		// set the scene background to be gradient
		Stop[] stop1 = new Stop[] { new Stop(0, BACKGROUND_COLOR_1), new Stop(1, BACKGROUND_COLOR_2)};
		LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stop1);
		scene.setFill(lg1);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		primaryStage.setOnCloseRequest( ev -> {
			this.controller.saveGameState();
		});
		
		System.out.println("Stage showing now");
	}
	
	/**
	 * This method overrides the update method in the observer. Depending
	 * on the progress made in the game, different elements of the GUI will
	 * be updated to reflect the changes in game state as a result of the player's
	 * actions.
	 * 
	 * @param o Observable object, in this case, the game model.
	 * 
	 * @param saveGame (expected to be boolean) tells whether or not to save the game 
	 * 		  the game should be saved when game is not Over.
	 */
	@Override
	public void update(Observable o, Object saveGame) {
		MinesweeperModel model = (MinesweeperModel) o;
		if (model.isGameOver()) {			// handles game over events
			File savedGame = new File(SAVE_NAME);
			if (savedGame.exists()) {		// deletes saved game when game is over
				savedGame.delete();
			}
			timeline.pause();					// pauses timer
			if (model.getPlayerWon()) {
				if (!alertShown) {
					alert.setAlertType(AlertType.INFORMATION);
					alert.setContentText("You win!"); 							// alert the game has been won
					alert.show();
					alertShown = true;
				}
				updateGrid(true, model.getMinefield(), true);					// updates grid, mines are revealed, player won
			} else {
				if (!alertShown) {
	 				alert.setAlertType(AlertType.INFORMATION);
					alert.setContentText("You stepped on a mine. Try again."); // alert the game has been lost
					alert.show();
					alertShown = true;
				}	
				updateGrid(true, model.getMinefield(), false);					// updates grid, mines are revealed, player lost
			}
		} else {		// game is not over -- updates flag counter and minefield
			flagLabel.setText("\tFlags Left: " + (model.getNumberofMines() - model.getFlagsPlaced()) + "/" + model.getNumberofMines());
			flagLabel.setFont(Font.font("Bodoni MT Condensed Bold", FontWeight.BOLD, 15));
			if (model.getFlagsPlaced() >= .80 * model.getNumberofMines()) {	
				flagLabel.setTextFill(Paint.valueOf("red"));		// flag counter turns red when at least 80% of the flags have been used
			} else {
				flagLabel.setTextFill(Paint.valueOf("white"));
			}
			updateGrid(false, model.getMinefield(), false);
		}
		
		// updates timeLabel
		if(timeLabel != null) {		// checked just in case the timer is started before timeLabel is initialized
			int minutes = model.getTime() / 60;
			int seconds = model.getTime() % 60;
			String secondsString = ((int)(seconds/10))==0? "0"+seconds: ""+seconds;
			timeLabel.setText(minutes+" : "+ secondsString);
			timeLabel.setFont(Font.font("Bodoni MT Condensed Bold", FontWeight.BOLD, 15));
			timeLabel.setTextFill(Paint.valueOf("white"));
		}
	}

	// --------------------------------------------------[ PRIVATE METHODS]-------------------------------------------------- //

	/**
	 * This method initializes the game on launch. It checks for
	 * save files from previous uncompleted games and loads them
	 * if they are found.
	 * 
	 * @param length integer representing the length of the board
	 * @param width integer representing the width of the board
	 * @param numberOfMines integer representing the number of the mines hidden on the board
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
		FIELD_LENGTH = model.getDimensions()[0];
		FIELD_WIDTH = model.getDimensions()[1];
	}

	/**
	 * This method initializes each toggle button in the <code>GridPane</code>
	 * and adds them to an <code>ArrayList</code> for accessibility.
	 * 
	 * @param x    integer representing the width of the mine field.
	 * @param y    integer representing the length of the mine field.
	 * @param pane <code>GridPane</code> representing the mine field.
	 * @return a 2D array of ToggleButtons displayed in the <code>GridPane</code>.
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
	 * This method adds necessary event listeners onto the ToggleButton.
	 * These event listeners detect left and right clicks. If a left click
	 * is made, then a step is taken. If a right click is made, then a flag
	 * is toggled. An alert will be shown if the player tries to place a flag
	 * when they're out of flags.
	 * 
	 * @param button <code>ToggleButton</code> where event is caught.
	 * @param pane   <code>GridPane</code> representing the mine field. 
	 */
	private void setEventListener(ToggleButton button, GridPane pane) {
		button.setOnMouseClicked(mouseEvent -> {
			
			MouseButton click = mouseEvent.getButton();

			ToggleButton target = (ToggleButton) mouseEvent.getSource();
			int y = GridPane.getRowIndex(target);
			int x = GridPane.getColumnIndex(target);
			
			try {
				if (click == MouseButton.PRIMARY) {					// left click to reveal a space
					controller.takeStep(x, y);
				} else if (click == MouseButton.SECONDARY) {		// right click to put/remove flag
					// toggle flag
					if (controller.toggleFlag(x, y)) {
						placeAnimation(button, PLACE_ANIM_DURATION);	// "bounces" flag when placed
					} else if (!alertShown) {
						removeFlag(button);								// removes flag image from button
						button.setText("");
					}
				}
			} catch (IllegalStepException e) {
				System.out.println(e.getMessage());
				return;
			} catch (IllegalFlagPlacementException e) {
				System.out.println(e.getMessage());
				if (e.getMessage().equals("Out of flags!")) {
					alert.setAlertType(AlertType.INFORMATION);			// shows alert if player attempts to place flag when they're out
					alert.setContentText(e.getMessage());
					alert.show();
					quickShake(flagLabel, QUICK_SHAKE_DURATION);
				}
				return;
			}
		});
	}


	/**
	 * This method updates the given ToggleButton according to 
	 * the passed in string: "flag" or "mine".
	 * 
	 * @param img a String representing the prefix of the name of the image to be used
	 * @param button the ToggleButton that will contain the image
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
	private void removeFlag(ToggleButton button) {
		button.setMaxWidth(FLAG_SIZE);
		button.setPrefHeight(FLAG_SIZE);
		button.setGraphic(null);
		button.setSelected(false);
	}
	
	/**
	 * This method updates the GridPane of ToggleButton 
	 * 
	 * @param revealMine boolean to tell whether to show the mine on the map or not.
	 * 
	 * @param spaceGrid  Space[][] representing the underlying mine map. 
	 * 
	 * @param playerWon true if the player found all the mine, false otherwise
	 */
	private void updateGrid(boolean revealMine, Space[][] spaceGrid, boolean playerWon) {
		for (int i = 0; i< FIELD_LENGTH; i++) {
			for (int j = 0; j  < FIELD_WIDTH; j++) {
				Space space = spaceGrid[i][j];
				ToggleButton button = gridpaneMap[i][j];
				if (space.hasFlag()) {										// if the space is flagged...
					setImage("flag", button);
					button.setText("");
					button.setSelected(false);
					if (revealMine && !playerWon && space.hasMine()) {		// flags on mines shake when player loses
						button.setDisable(true);
						shakeAnimation(button, new Duration(30));
					} else if (revealMine && playerWon && space.hasMine()) {	// flags on mines have green backgrounds and wave if player won
						button.setStyle("-fx-background-color: #84e8b4; -fx-padding: 2px;");
						waveAnimation(button, new Duration(500));
					} else if (revealMine && !space.hasMine()) {
						button.setDisable(true);
					}
				} else if (space.isRevealed() && !space.hasMine()) {		// if the space is not flagged and does not have mine...
					if (revealMine) {
						button.setDisable(true);							// prevents user from toggling buttons when game is over
					}
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
				} else if (space.hasMine() && revealMine) {						// shows mines if the game is over
					setImage("mine", button);
					if (playerWon) {											// if player won, mines wave
						waveAnimation(button, WAVE_DURATION);
					} else {													// if player lost, mines shake
						shakeAnimation(button, SHAKE_DURATION);	
					}
				} else if (revealMine && !space.isRevealed()) {
					button.setDisable(true);
				}
			}
		}
	}
	
	/**
	 * Creates a waving animation on a node. Takes a node and a time as
	 * parameters that are used to add wave-like movements to the given
	 * node.
	 * 
	 * @param toWave JavaFX <code>Node</code> to wave.
	 * 
	 * @param timeLimit the duration of the animation.
	 */
	private void waveAnimation(Node toWave, Duration timeLimit) {
		TranslateTransition wave = new TranslateTransition();
		wave.setDuration(timeLimit);
		wave.setNode(toWave);
		wave.setByY(-3);
		wave.setToY(3);
		wave.setCycleCount(1000);
		wave.setAutoReverse(true);
		wave.play();
	}
	
	/**
	 * Creates a longer shaking animation on a node. This shakes the node
	 * horizontally for 1000 cycles.
	 *
	 * @param toShake JavaFX <code>Node</code> to shake.
	 * @param timeLimit the duration of the animation.
	 */
	private void shakeAnimation(Node toShake, Duration timeLimit) {
		TranslateTransition wave = new TranslateTransition();
		wave.setDuration(timeLimit);
		wave.setNode(toShake);
		wave.setByX(3);
		wave.setToX(3);
		wave.setCycleCount(1000);
		wave.setAutoReverse(true);
		wave.play();
	}
	
	/**
	 * Creates a quick shaking animation on a node. This quickly shakes
	 * the node horizontally for 12 cycles.
	 * 
	 * @param toShake JavaFX <code>Node</code> to shake.
	 * @param timeLimit the duration of the animation.
	 */
	private void quickShake(Node toShake, Duration timeLimit) {
		TranslateTransition wave = new TranslateTransition();
		wave.setDuration(timeLimit);
		wave.setNode(toShake);
		wave.setByX(3);
		wave.setToX(3);
		wave.setCycleCount(12);
		wave.setAutoReverse(true);
		wave.play();
	}
	
	/**
	 * Creates an animation when a flag is placed.
	 * 
	 * @param toMove a JavaFX Node.
	 * 
	 * @param time the duration of the animation.
	 */
	private void placeAnimation(Node toMove, Duration time) {
		TranslateTransition move = new TranslateTransition();
		move.setDuration(time);
		move.setNode(toMove);
		move.setByY(-5);
		move.setCycleCount(2);
		move.setAutoReverse(true);
		move.play();
	}
	
	/**
	 * This method basically is used to update time in the background by 
	 * prompting the controller to set a chain of events that update time every second.
	 */
	private void startTime() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			controller.setTime(controller.getTime()+1);
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

}