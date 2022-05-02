package view;



import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import controller.MinesweeperController;
import javafx.animation.Animation;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
private boolean gameInProgress = false;
private int timebeforelost;
private int timeOflost;
private Space[][] lostgrid;
private int boardsize;
	MinesweeperModel model = new MinesweeperModel();
	MinesweeperController controller = new MinesweeperController(model);
	Space[][] grid = controller.getMinefield();
	private  ImageView headview;
	 private GridPane topscreen = new GridPane();
	 private ArrayList<ToggleButton> map = new ArrayList<ToggleButton>();
	 private static final Image defult = new Image("/defult.png");
	 private static final Image gameOver = new Image("/deadface.png");
	 private static final Image head = new Image("/sileyface.jpg");
	 private static final Image IMAGE = new Image("/face.png");
	 private int boardsize()
	 {
		return boardsize; 
	 }
	 public ArrayList<ToggleButton> setlabel(int x, int y)
	    {
		 int button = x*y;
		 button ++;
	     ArrayList <ToggleButton> number = new ArrayList<>();
	     for(int u =0; u < button; u++)
	     {
	    	 String s=String.valueOf(u);
	    	// System.out.print(s);
	    	 ToggleButton bio = new ToggleButton();
	    	
	            number.add(bio);
	            
	     }
	     boardsize= x;
	        return number;
	    }
	    private static final int COLUMNS  =   20;
	    private static int COUNT    =  3;
	    private static final int OFFSET_X =  100;
	    private static final int OFFSET_Y =  10; // 100 to move y  
	    private static  int WIDTH  =  50;
	    private static final int HEIGHT   = 90;

	    public static void main(String[] args) {
	        launch(args);
	    }
	    public void checkspaceAndSetMine(int minetext, int hero)
	    {
	    	
	    	if (minetext == 0) {
	        	   map.get(hero).setSelected(true);
	        	   map.get(hero).setDisable(true);
	        	   map.get(hero).setText("0");
	        	   //clearrow(hero);
				} else if (minetext == 1) {
				 map.get(hero).setText("1");
					map.get(hero).setTextFill(Paint.valueOf("blue"));
					map.get(hero).setDisable(true);
					
				} else if (minetext == 2) {
					map.get(hero).setText("2");
					map.get(hero).setTextFill(Paint.valueOf("green"));
					map.get(hero).setDisable(true);
				} else if (minetext == 3) {
					map.get(hero).setText("3");
					map.get(hero).setTextFill(Paint.valueOf("red"));
					map.get(hero).setDisable(true);
				} else if (minetext == 4) {
					map.get(hero).setText("4");
					map.get(hero).setTextFill(Paint.valueOf("purple"));
					map.get(hero).setDisable(true);
				} else if (minetext == 5) {
					map.get(hero).setText("5");
					map.get(hero).setTextFill(Paint.valueOf("black"));
					map.get(hero).setDisable(true);
				} else if (minetext == 6) {
					map.get(hero).setText("6");
					map.get(hero).setTextFill(Paint.valueOf("gray"));
					map.get(hero).setDisable(true);
				} else if (minetext == 7) {
					map.get(hero).setText("7");
					map.get(hero).setTextFill(Paint.valueOf("maroon"));
					map.get(hero).setDisable(true);
				} else if (minetext == 8) {
					map.get(hero).setText("8");
					map.get(hero).setTextFill(Paint.valueOf("turquoise"));
					map.get(hero).setDisable(true);
				}
	    }
	    public void releacallallspace()
	    {
	    int	hero =1;
	    int minetext =0;
	    	int x = boardsize;
	    	int ysize = boardsize;
	    	int reset =0;
	    	for(int w = 0; w < x; w++)
	    	{
	    	for(int j= 0; j < ysize; j++)
	    	{
	    		System.out.print(j + ""+w);
	    		Space m = grid[j][w];
	    		minetext = m.adjacentMines();
	    		if(m.hasMine() == false)
				{
					 //map.get(hero).setText("" + minetext);
			       	Font font = Font.font("Times New Roman", 15);
			       	map.get(hero).setFont(font);
			           map.get(hero).setStyle("-fx-padding: 7px;");
			           map.get(hero).setDisable(true);
			          
			           checkspaceAndSetMine(minetext,hero);
			           if(controller.playerWon() == true)
	            		 {
	            	headview.setImage(head);
	            		 }
				}
				if(m.hasMine() == true)
				{
					 if( null!= map.get(hero).getGraphic())
					 {
						 map.get(hero).setGraphic(null);
						 map.get(hero).setText("f"); 
					 }
					 map.get(hero).setText("m");
				       	Font font = Font.font("Times New Roman", 15);
				       	map.get(hero).setFont(font);
				           map.get(hero).setStyle("-fx-padding: 6px;");
				           map.get(hero).setDisable(true);
				           headview.setImage(gameOver);
				}
				hero++;
				} 
	    	
	    	}
	    }
	    
	    public  void clearrow(int hero)
	    {
	    	int thefallen = hero;
	    	 int thehurt = hero;
	    	 int y = topscreen.getColumnIndex(map.get(hero)); 
       	  int x =topscreen.getRowIndex(map.get(hero)); 
       	  int ex =topscreen.getRowIndex(map.get(hero)); 
       	  int ey= topscreen.getColumnIndex(map.get(hero)); 
       	  int count= 0;
       	boolean skip = false;
       		  
       	  while( grid[x][y].adjacentMines() == 0 && grid[x][y].hasMine() == false &&  grid[x][y].isRevealed())
       		  {
       		  if(x < boardsize-2)
       		  {
       			  x=0;
       			  y++;
       			thehurt++;
       			skip=true;
       		  }
       		if(y < boardsize-1)
     		  {
     			  break;
     		  }
       		 // System.out.print("inside");
       		if(thefallen < 240)
       		{
       		thehurt =thehurt+boardsize+1;
       		}
       		x++;
       		//System.out.println("x"+x);
       		//System.out.println("y"+y);
       		map.get(thehurt).setText("0");
       		Font font = Font.font("Times New Roman", 15);
           	map.get(thehurt).setFont(font);
               map.get(thehurt).setStyle("-fx-padding: 7px;");
       		 map.get(thehurt).setDisable(true);
       		skip= false;
       		  
       		  }
       	  while(grid[ex][ey].adjacentMines() == 0 && grid[ex][ey].hasMine() == false &&  grid[ex][ey].isRevealed())
       	  {
       		  if(ex < 2)
       		  {
       			  x=16;
       			  ey--;
       			thefallen--;
       			skip=true;
       		  }if(ey < 1)
       		  {
       			  break;
       		  }
       		if(thefallen > boardsize && skip == false)
       		{
       			thefallen=thefallen-boardsize+1;	
       		}
       		
       		ex--;
       	map.get(thefallen).setText("0");
     	Font font = Font.font("Times New Roman", 15);
       	map.get(thefallen).setFont(font);
           map.get(thefallen).setStyle("-fx-padding: 7px;");
       	 map.get(thefallen).setDisable(true);
       	skip= false;
          
       	  }
	    }
	    @Override
	    public void start(Stage primaryStage) {
	    	/**
	    	 * buids the board and add the mine interation
	    	 */
	        primaryStage.setTitle("minesweaper");
	        controller = new MinesweeperController(model);
	     //   ImageView imageView = new ImageView(defult);
	   //     imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));


	        map = setlabel(16,16);
			
			GridPane topscreen = new GridPane();
			//
			
			// Timer Code starts here
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					controller.setTime(controller.getTime()+1);
				}
			}, 1000);
		
			
			int row= 0;
			int colab=0;
			int h =map.size();
			for(int i = 1; i < h; i++)
			{
				if(row > boardsize-1)
				{
				row= 0;
				
			     colab++;
				}
				//topscreen.add(numbs.get(i), row, colab);
				 
				 map.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
			            @Override
			            public void handle(MouseEvent event) {
			                MouseButton button = event.getButton();
			               // grid = controller.getMinefield(); testing 
			                if(button==MouseButton.PRIMARY){
			                	ToggleButton findme =  (ToggleButton) event.getTarget();
			               
			                int hero =	map.indexOf(findme);
			                
			        
			           try {
			        	   	hero =	map.indexOf(findme);
			        		Space m = grid[topscreen.getRowIndex(map.get(hero))][topscreen.getColumnIndex(map.get(hero))];
							controller.takeStep(topscreen.getRowIndex(map.get(hero)), topscreen.getColumnIndex(map.get(hero)));
						
						int minetext =  m.adjacentMines();
						m.isRevealed();
						System.out.print(m.hasMine());
			
					
						 if(m.hasMine() == true || controller.isGameOver())
						{
							
							 map.get(hero).setText("m");
						       	Font font = Font.font("Times New Roman", 15);
						       	map.get(hero).setFont(font);
						           map.get(hero).setStyle("-fx-padding: 6px;");
						           map.get(hero).setDisable(true);
						           headview.setImage(gameOver);
						}
						 else if(m.hasMine() == false)
							{
				              
								 //map.get(hero).setText("" + minetext);
						       	Font font = Font.font("Times New Roman", 15);
						       	map.get(hero).setFont(font);
						           map.get(hero).setStyle("-fx-padding: 7px;");
						           map.get(hero).setDisable(true);
						          
						           checkspaceAndSetMine(minetext,hero);
						           if(controller.playerWon() == true)
				            		 {
				            	headview.setImage(head);
				            		 }
							}
						} 
			           catch (IllegalArgumentException | IllegalStepException e) {					// XXX: added custom exception class here (you can remove this comment, just a note)
			        	   Space f = grid[topscreen.getRowIndex(map.get(hero))][topscreen.getColumnIndex(map.get(hero))];
			        	  // f.isRevealed();
			        		if(f.hasMine() == true)
			        		{
			        			 map.get(hero).setText("m");
							       	Font font = Font.font("Times New Roman", 15);
							       	map.get(hero).setFont(font);
							           map.get(hero).setStyle("-fx-padding: 6px;");
							           map.get(hero).setDisable(true);
							           headview.setImage(gameOver);
			        		}
			        	 else if(f.hasMine() == false)
			        		{
			        		   checkspaceAndSetMine(f.adjacentMines(),hero);
			        		
			        	 	Font font = Font.font("Times New Roman", 15);
					       	map.get(hero).setFont(font);
					           map.get(hero).setStyle("-fx-padding: 7px;");
			        	   map.get(hero).setDisable(true);
			        	   
			        		}
						}
			                    
			                }else if(button==MouseButton.SECONDARY){
			                	//System.out.print("in2");
			                	/** 
			                	 * Alows user to flag button
			                	 */
			                	ToggleButton findme =  (ToggleButton) event.getTarget();
			                	 int hero =	map.indexOf(findme);
			                	  Image img = new Image("/greatflag.png");
			                	  if(null == map.get(hero).getGraphic())
			                			  {
			                		  
			                      Space m = grid[topscreen.getRowIndex(map.get(hero))][topscreen.getColumnIndex(map.get(hero))];
			 			             
			 			            
			 			               m.placeFlag();
			 			             if(controller.playerWon() == true)
			 			            		 {
			 			            	headview.setImage(head);
			 			            		 }
			 			              			 			            	   
			                      ImageView view = new ImageView(img);
			                      view.setFitHeight(30);
			                      view.setFitWidth(30);
			                      view.setPreserveRatio(true);
			                     map.get(hero).setGraphic(view);
			                    
			                      map.get(hero).setStyle("-fx-padding: 0px;");
			                	
			                			  }
			                	  else if(null != map.get(hero).getGraphic())
			                	  {
			                		 /**
			                		  * resets button if has flag
			                		  */
			 			               Space m = grid[topscreen.getRowIndex(map.get(hero))][topscreen.getColumnIndex(map.get(hero))];
			 			              
			 			               m.removeFlag();
			 			              if(controller.playerWon() == true)
		 			            		 {
		 			            	headview.setImage(head);
		 			            		 }
			                		
			                		  	  map.get(hero).setDisable(false);
			                		  	  
			                		  map.get(hero).setGraphic(null);
			                		  Font font = Font.font("Times New Roman", 6);
			                			map.get(hero).setFont(font);
			                			 map.get(hero).setText("");
			            				map.get(hero).setStyle("-fx-border-style: solid;"+  "-fx-padding: 10px;" +  "-fx-border-color: lightgreen;" );
			            				map.get(hero).setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.4)));
			            				//map.get(i).setWrapText(true);  
			                	  }
			                }
			                
			            }
			        });
				
				Font font = Font.font("Times New Roman", 6);
				map.get(i).setFont(font);
				map.get(i).setStyle("-fx-border-style: solid;"+  "-fx-padding: 11px;" +  "-fx-border-color: lightgreen;" );
				map.get(i).setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.4)));
			
				topscreen.add(map.get(i), colab, row);
				row++;
				
			}
			headview = new ImageView(defult);
			headview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
/**
 * 
 * add eventhanler the resets the game
 */
			     @Override
			     public void handle(MouseEvent event) {
			        /**
			         * restarts game
			         */
			    	 if(headview.getImage() ==  defult)
			    	 {
			    		 start(primaryStage);
			    		
			    	 }
			         if(headview.getImage() == head)
			         {
			        	 start(primaryStage);
			        	
			         }
			         if(headview.getImage() == gameOver)
			         {
			        	 releacallallspace();
			        	  //start(primaryStage);
			         }
			         			     }
			}
			
			 );
			 headview.setFitHeight(100);
			 headview.setFitWidth(100);
			 headview.setPreserveRatio(true);
			Label toptext = new Label("Mine Sweaper");
			Font tittlefont = Font.font("Times New Roman", 30);
			toptext.setFont(tittlefont);
			HBox doom = new HBox();
			VBox vbox = new VBox();
			HBox title = new HBox();
			HBox board = new HBox();
			doom.getChildren().add(headview);
			title.getChildren().add(toptext);
			board.getChildren().add(topscreen);
			title.setAlignment(Pos.TOP_CENTER);
			doom.setAlignment(Pos.TOP_CENTER);
			board.setAlignment(Pos.CENTER);
			vbox.getChildren().add(title);
			vbox.getChildren().add(doom);
			vbox.getChildren().add(board);

	        primaryStage.setScene(new Scene(vbox));
	        primaryStage.show();
	    }
	    
	  @Override
		public void update(Observable o, Object saveGame) {
			MinesweeperModel model = (MinesweeperModel) o;
			if (!controller.isGameOver()) {
				gameInProgress = true;
			grid= model.getMinefield();
			timebeforelost = controller.getTime(); // line may not be needed
			lostgrid = grid;
			
			}
			else
			{
				gameInProgress= false;	
			}
			if(gameInProgress == false ){
				timeOflost = controller.getTime();
				
	
			}
			
	  }
}
