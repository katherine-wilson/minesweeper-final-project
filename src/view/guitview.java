/**
 * NAME: guitview.java
 * COURSE: CSC 335 - Spring 2022
 * DESCRIPTION: This file contains GUI component of the application for Minesweeper. It initializes the
 * 				interface of the game and allows the user to interact with it. It presents the 
 * 				mine field with a gridpane of ToggleButton
 * USAGE: to be used with the whole project including MinesweeperController.java, MinesweeperLauncher.java
 * 		  MinesweeperModel.java along with other utilities.
 * 
 * @author Eleanor Simon
 * @author Giang Huong Pham
 * @author Tanmay Agrawal
 */
package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import controller.MinesweeperController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MinesweeperModel;
import utilities.IllegalFlagPlacementException;
import utilities.IllegalStepException;
import utilities.Space;

public class guitview extends Application implements Observer {
	/* The size of the game windows */
	private static final int SCENE_WIDTH = 800;
	private static final int SCENE_HEIGHT = 600;

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
	 * XXX This contains the underlying grid of Space object that the model has
	 */
	private Space[][] grid;
	/**
	 * This field contains the grid of ToggleButton representing the minfield's
	 * interactable "boxes".
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
	 * This field contains the dimension of the mine field.
	 */
	private int FIELD_LENGTH;
	private int FIELD_WIDTH;
	private static final int SPACE_SIZE = 50;

//	 model = new MinesweeperModel();
//	 controller = new MinesweeperController(model);
//	Space[][] grid;
//	private ArrayList<ToggleButton> gridpaneMap = new ArrayList<ToggleButton>();
//	private static final Image IMAGE = new Image("/face.png");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
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
		this.initGame();
		
		// Label for the Timer Initialized here
		timeLabel = new Label();
		// HBox that contains the GridPane
		HBox board = new HBox();
		gridpane = new GridPane();
		gridpaneMap = setlabel(FIELD_LENGTH, FIELD_WIDTH, gridpane);
		board.getChildren().add(gridpane);
		board.setAlignment(Pos.CENTER);

		// adding 2 HBox into the VBox
		VBox vbox = new VBox();
		vbox.getChildren().add(title);
		vbox.getChildren().add(timeLabel);
		vbox.getChildren().add(board);

		Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);

//		scene.setOnKeyReleased(keyEvent -> {
//			handleKey(keyEvent);
//		});

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// --------------------------------------------------[ PRIVATE METHODS]-------------------------------------------------- //

	/**
	 * This method initialize the game when the game is launched: it checks for
	 * saved files of the previous uncompleted game
	 */
	private void initGame() {
		// Trying to load the saved_game when the application is launched
		File savedGame = new File("saved_game.dat");
		if (savedGame.exists()) {
			try {
				model = new MinesweeperModel("saved_game.dat");
			} catch (FileNotFoundException e) {
				System.out.println("Cannot find the saved game file.");
			} catch (ClassNotFoundException e) {
				System.out.println("Cannot load the data from the saved game.");
			} catch (IOException e) {
				System.out.println("Fail to load the saved game from file.");
			}
		} else {
			model = new MinesweeperModel();
		}
		model.addObserver(this);
		controller = new MinesweeperController(model);
		// get dimension of the mine field and create
		// a 2D array of ToggleButton with the same dimension
		int[] dimension = model.getDimensions();
		this.FIELD_LENGTH = dimension[0];
		this.FIELD_WIDTH = dimension[1];
	}

	/**
	 * This method initialize toggle buttons in the grid pane and add them to an
	 * array list to keep track
	 * 
	 * @param x    integer representing the width of the mine field
	 * @param y    integer representing the length of the mine field
	 * @param pane GirdPane representing the mine field.
	 * @return a 2D array of ToggleButtons displayed on the grid pane
	 */
	private ToggleButton[][] setlabel(int x, int y, GridPane pane) {
		ToggleButton[][] ret = new ToggleButton[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				ToggleButton button = new ToggleButton();
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
			ToggleButton target = (ToggleButton) mouseEvent.getTarget();
			
			int y = GridPane.getRowIndex(target);
			int x = GridPane.getColumnIndex(target);
			
			try {
				// left click to reveal a space
				if (click == MouseButton.PRIMARY) {
					controller.takeStep(x, y);
				} // right click to put/remove flag/question mark
				// TODO if we implement question: if it's a flag, turn into question
				// if it's a question, turn into open space (unrevealed) 
				// TODO: implement the model also if we ddecide to do the question mark.
				else if (click == MouseButton.SECONDARY) {
					// remove flag if the Space is already flagged
					if (grid[x][y].hasFlag()) {
						controller.removeFlag(x, y);
					}// place flag in a unrevealed space 
					else {
						controller.placeFlag(x, y);
					}
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
	 * This method updates the gridpane of ToggleButton 
	 * @param revealMine boolean to tell whether to show the mine on the map or not
	 * @param spaceGrid  Space[][] representing the underlying mine map. 
	 */
	private void updateGrid(boolean revealMine, Space[][] spaceGrid) {
		// TODO: update with "question mark"
		for (int i = 0; i< this.FIELD_LENGTH; i++) {
			for (int j = 0; j  < this.FIELD_WIDTH; j++) {
				Space space = spaceGrid[i][j];
				ToggleButton button = gridpaneMap[i][j];
				// if the space is flagged
				if (space.isRevealed() && space.hasFlag()) {
					updateImage(button, "flag");
				} // if the space is not flagged and does not have mine
				else if (space.isRevealed() && !space.hasMine()) {
					int adjMine = space.adjacentMines();
					button.getStyleClass().add("grey-button");
					if (adjMine == 1) {
						button.setText("1");
						button.setTextFill(Paint.valueOf("blue"));
					} else if (adjMine == 2) {
						button.setText("2");
						button.setTextFill(Paint.valueOf("green"));
					} else if (adjMine == 2) {
						button.setText("3");
						button.setTextFill(Paint.valueOf("red"));
					} else if (adjMine == 2) {
						button.setText("4");
						button.setTextFill(Paint.valueOf("purple"));
					} else if (adjMine == 2) {
						button.setText("5");
						button.setTextFill(Paint.valueOf("black"));
					} else if (adjMine == 2) {
						button.setText("6");
						button.setTextFill(Paint.valueOf("gray"));
					} else if (adjMine == 2) {
						button.setText("7");
						button.setTextFill(Paint.valueOf("maroon"));
					} else if (adjMine == 2) {
						button.setText("8");
						button.setTextFill(Paint.valueOf("turquoise"));
					}
				}
			}
		}
	}
	
	/**
	 * This method basically is used to update time in the background by 
	 * prompting the controller to set a chain of events that update time every second. 
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
	 * This method updates the given ToggleButton according to 
	 * the passed in string: "flag" or "mine" or "question"
	 * @param button ToggleButton to change the image of 
	 * @param string name of image to put onto the button display
	 */
	private void updateImage(ToggleButton button, String string) {
		// TODO
		
	}

	/**
	 * This method overrides the update method in the observer. 
	 * @param o Observable object, in this case, the game model
	 * @param saveGame (expected to be boolean) tells whether or not to save the game 
	 * 		  the game should be saved when game is not Over
	 */
	@Override
	public void update(Observable o, Object saveGame) {
		MinesweeperModel model = (MinesweeperModel) o;
		boolean toSave = (boolean) saveGame;
		grid = model.getMinefield();
		updateGrid(model.isGameOver(), model.getMinefield());
		
		// update the timeLabel
		// timeLabel != null checked only in case timer starts a few milliseconds before we
		// init the timeLabel in start()
		if(timeLabel != null) {
			int minutes = model.getTime() / 60;
			int seconds = model.getTime() % 60;
			timeLabel.setText(minutes+":"+seconds);
		}
		// TODO: implement function in model to be called when the user exit the game:
		// check if isGameover() -> if not, set arg to a boolean to decide to serialize 
		// the model or not 
		// "saved_game.dat"
		
	}

}