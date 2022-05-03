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
import java.util.Scanner;

import controller.MinesweeperController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
	/* The size of the game windows */
	private boolean fordoublemine = false;
	private static final int SCENE_WIDTH = 800;
	private static final int SCENE_HEIGHT = 600;
	private String f;
	private  ImageView headview;
	private File savefile;
	 private static final Image mine = new Image("/mine.png");
	 private static final Image defult = new Image("/defult.png");
	 private static final Image gameOver = new Image("/deadface.png");
	private static final Image head = new Image("/sileyface.jpg");
	 private static final Image greatflag = new Image("/greatflag.png");
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
	 * This field contains the dimension of the mine field.
	 */
	private int FIELD_LENGTH;
	private int FIELD_WIDTH;
	private static final int SPACE_SIZE = 25;
	
	/**
	 * True while game is in progress.
	 */
	private boolean gameInProgress = true;


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
		this.initGame();
	//	int store[] = this.controller.getwidth(16, 16);
		//FIELD_LENGTH=store[0];
		//FIELD_WIDTH=store[1];
		// Label for the Timer Initialized here
		timeLabel = new Label();
		HBox timeBox = new HBox();
		HBox face = new HBox();
		headview = new ImageView(defult);
		headview.setFitHeight(70);
						 headview.setFitWidth(70);
						 headview.setPreserveRatio(true);
						 headview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
								/**
								 * 
								 * add event hanlerd the resets the game
								 */
											     @Override
											     public void handle(MouseEvent event) {
											        /**
											         * restarts game
											         */
											    	 if(headview.getImage() ==  defult)
											    	 {
											    		 controller.saveGameState();
											    		
											    	 }
											         if(headview.getImage() == head)
											         {
											        	 controller.saveGameState();
											        	
											         }
											         if(headview.getImage() == gameOver)
											         {
											        	
											        	 controller.saveGameState();
											        	 gameInProgress = true;
											        	 f="no";
											        	 start(primaryStage);
											        	
											         }
											         			     }
											}
											
											 );
											
		face.getChildren().add(headview);
		face.setAlignment(Pos.CENTER);
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
		vbox.getChildren().add(face);
		vbox.getChildren().add(title);
		vbox.getChildren().add(timeBox);
		vbox.getChildren().add(board);

		Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);

		//scene.setOnKeyReleased(keyEvent -> {
//			handleKey(keyEvent);
//		});
		  this.controller.saveGameState();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest( ev -> {
			
			this.controller.saveGameState();
			f="yes";
		});
		System.out.println("Stage showing now");
	}

	// --------------------------------------------------[ PRIVATE METHODS]-------------------------------------------------- //

	/**
	 * This method initialize the game when the game is launched: it checks for
	 * saved files of the previous uncompleted game
	 */
	private void initGame() {
		// Trying to load the saved_game when the application is launched
	//	File check = new File("saved_game.dat");
		if (savefile != null && f =="no") {
		System.out.println("New game(Yes or no)");
		Scanner input = new Scanner(System.in);
		{
			if (input.hasNextLine()) {
				f = input.nextLine();
				if(f =="yes")
				{
					savefile.delete();
				
				}
			}
		}
		}
		//File savedGame = new File("saved_game.dat");
		if (savefile != null) {
			try {
				
				System.out.println("Save loaded.");
		
				model = new MinesweeperModel("saved_game.dat");
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
				model = new MinesweeperModel();
			System.out.println("enter board size and width use space");
			Scanner in = new Scanner(System.in);
			if (in.hasNextLine()) {
				String coords[] = in.nextLine().split(" ");
				int x = Integer.parseInt(coords[0]);
				int y = Integer.parseInt(coords[1]);
				model.setboard(x, y);	
				String TEXT_FILE = "/saved_game.dat";
				 savefile = new File(TEXT_FILE);
				 
			}
			
		}
		model.addObserver(this);
		this.controller = new MinesweeperController(model);
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
					if (gameInProgress) {			// BLOCK INPUT
						controller.takeStep(x, y);
					}
				} // right click to put/remove flag/question mark
				// TODO if we implement question: if it's a flag, turn into question
				// if it's a question, turn into open space (unrevealed) 
				// TODO: implement the model also if we decide to do the question mark.
				else if (click == MouseButton.SECONDARY) {
					// place flag
					
					//System.out.println
					
					controller.toggleFlag(x, y);
					Space[][] m = controller.getMinefield();
					if(!m[x][y].hasFlag())
					{
						if(!m[x][y].hasMine())
						{
						target.setGraphic(null);
						target.setText(" "+ m[x][y].adjacentMines());
						}
						else
						{
							target.setGraphic(null);
							 ImageView view = new ImageView(mine);
							 view.setFitHeight(23);
		                     view.setFitWidth(23);
		                     target.setStyle("-fx-padding: 0px;");
		                     view.setPreserveRatio(true);
		                     target.setGraphic(view);
						}
						
					//updateGrid(m[x][y].hasMine(),m);
					}
					//updateGrid(false, model.getMinefield());
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
				if (space.hasFlag()) {	
					// if the space is flagged
					//button.setText("F");
					//button.setSelected(false);
					button.setDisable(false);
					 ImageView view = new ImageView(greatflag);
					 view.setFitHeight(20);
                     view.setFitWidth(15);
                     view.setPreserveRatio(true);
					button.setGraphic(view);
					button.setStyle("-fx-padding: 2px;");
				} else if (space.isRevealed() &&!space.hasMine()) {		// if the space is not flagged and does not have mine
					int adjMine = space.adjacentMines();
					button.getStyleClass().add("grey-button");
					if (adjMine == 0) {
						button.setGraphic(null);
						button.setSelected(true);
						button.setDisable(true);
						button.setText(" ");
					} else if (adjMine == 1) {
						button.setGraphic(null);
						button.setText("1");
						button.setTextFill(Paint.valueOf("blue"));
						button.setSelected(false);
					} else if (adjMine == 2) {
						button.setGraphic(null);
						button.setText("2");
						button.setTextFill(Paint.valueOf("green"));
						button.setSelected(false);
					} else if (adjMine == 3) {
						button.setGraphic(null);
						button.setText("3");
						button.setTextFill(Paint.valueOf("red"));
						button.setSelected(false);
					} else if (adjMine == 4) {
						button.setGraphic(null);
						button.setText("4");
						button.setTextFill(Paint.valueOf("purple"));
						button.setSelected(false);
					} else if (adjMine == 5) {
						button.setGraphic(null);
						button.setText("5");
						button.setTextFill(Paint.valueOf("black"));
						button.setSelected(false);
					} else if (adjMine == 6) {
						button.setGraphic(null);
						button.setText("6");
						button.setTextFill(Paint.valueOf("gray"));
						button.setSelected(false);
					} else if (adjMine == 7) {
						button.setGraphic(null);
						button.setText("7");
						button.setTextFill(Paint.valueOf("maroon"));
						button.setSelected(false);
					} else if (adjMine == 8) {
						button.setGraphic(null);
						button.setText("8");
						button.setTextFill(Paint.valueOf("turquoise"));
						button.setSelected(false);
					}
				} else if (space.hasMine() && revealMine) {
					button.setGraphic(null);
					 ImageView view = new ImageView(mine);
					 view.setFitHeight(23);
                     view.setFitWidth(23);
                     button.setStyle("-fx-padding: 0px;");
                     view.setPreserveRatio(true);
					button.setGraphic(view);
				
					button.setTextFill(Color.MEDIUMVIOLETRED);
				} else {
					button.setText("");
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
		int l= FIELD_LENGTH;
		int w =FIELD_WIDTH;
		int check = l*w;
		System.out.println(model.getFlagsPlaced());
		check= check-model.getNumberofMines();
		int other =model.getSpacesRevealed()-model.getNumberofMines()+model.getFlagsPlaced();
		System.out.println(check +" " + other);
		if(model.getPlayerWon() ||other == check)
		{
			headview.setImage(head);
			//updateGrid(true, model.getMinefield());
		}
		if (model.isGameOver()) {
			System.out.println("GAME OVER");
			gameInProgress = false;
			headview.setImage(gameOver);
			updateGrid(true, model.getMinefield());
		} else {
			//controller.saveGameState(); fixed some issues
			
			updateGrid(false, model.getMinefield());
		}
		
		boolean toSave = (boolean) saveGame;
		
		// update the timeLabel
		// timeLabel != null checked only in case timer starts a few milliseconds before we
		// init the timeLabel in start()
		if(timeLabel != null) {
			int minutes = model.getTime() / 60;
			int seconds = model.getTime() % 60;
			String secondsString = ((int)(seconds/10))==0? "0"+seconds: ""+seconds;
			timeLabel.setText(minutes+" : "+ secondsString);
			controller.saveGameState();
		}
		// TODO: implement function in model to be called when the user exit the game:
		// check if isGameover() -> if not, set arg to a boolean to decide to serialize 
		// the model or not 
		// "saved_game.dat"
		
	}

}