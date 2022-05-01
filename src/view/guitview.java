package view;



import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MinesweeperModel;
import utilities.IllegalFlagPlacementException;
import utilities.IllegalStepException;
import utilities.Space;


public class guitview extends Application  {

	MinesweeperModel model = new MinesweeperModel();
	MinesweeperController controller = new MinesweeperController(model);
	Space[][] grid;
	private  ImageView headview;
	
	 private ArrayList<ToggleButton> map = new ArrayList<ToggleButton>();
	 private static final Image gameOver = new Image("/deadface.png");
	 private static final Image head = new Image("/sileyface.jpg");
	 private static final Image IMAGE = new Image("/face.png");
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
	    @Override
	    public void start(Stage primaryStage) {
	        primaryStage.setTitle("The Horse in Motion");
	      //  MinesweeperModel model = new MinesweeperModel();
	        controller = new MinesweeperController(model);
	        ImageView imageView = new ImageView(IMAGE);
	        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

	        final Animation animation = new SpriteAnimation(
	                imageView,
	                Duration.millis(1000),
	                COUNT, COLUMNS,
	                OFFSET_X, OFFSET_Y,
	                WIDTH, HEIGHT
	        );
	        animation.setCycleCount(Animation.INDEFINITE);
	        animation.play();
	        map = setlabel(16,16);
			//bottem = setlabel();
			GridPane topscreen = new GridPane();
			//
			
			//ArrayList<Label> numbs = new ArrayList<>();
		//	ArrayList<Label> running = new ArrayList<>();
			
			int row= 0;
			int colab=0;
			int h =map.size();
			for(int i = 1; i < h; i++)
			{
				if(row > 15)
				{
				row= 0;
				
			     colab++;
				}
				//topscreen.add(numbs.get(i), row, colab);
				 
				 map.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
			            @Override
			            public void handle(MouseEvent event) {
			                MouseButton button = event.getButton();
			                grid = controller.getMinefield();
			                if(button==MouseButton.PRIMARY){
			                	ToggleButton findme =  (ToggleButton) event.getTarget();
			                	
			                int hero =	map.indexOf(findme);
			                
			                 int ho= hero/16;
			              if(ho == 16)
			              {
			            	  ho--;
			              }
			                int  y= hero%16;
			              
			            	   //y = 15;  
			              
			               
			             //  controller.takeStep(ho, y);
			              System.out.print("y" + y+ "x" +ho);
			              

			           try {
							controller.takeStep(ho, y);
							Space m = grid[ho][y];
						int minetext =  m.adjacentMines();
						m.isRevealed();
						
						
						if(m.hasMine() == false)
						{
							 map.get(hero).setText("" + minetext);
					       	Font font = Font.font("Times New Roman", 15);
					       	map.get(hero).setFont(font);
					           map.get(hero).setStyle("-fx-padding: 7px;");
					           map.get(hero).setDisable(true);
						}
						if(m.hasMine() == true)
						{
							 map.get(hero).setText("m");
						       	Font font = Font.font("Times New Roman", 15);
						       	map.get(hero).setFont(font);
						           map.get(hero).setStyle("-fx-padding: 6px;");
						           map.get(hero).setDisable(true);
						           headview.setImage(gameOver);
						}
						} 
			           catch (IllegalArgumentException | IllegalStepException e) {					// XXX: added custom exception class here (you can remove this comment, just a note)
			        	   //Space m = grid[ho][y];
			        	   map.get(hero).setDisable(true);
			        	  
			           
							//System.out.println("Invalid step! Try again. You cannot step on flags or revealed spaces.");
						}
			                    
			                }else if(button==MouseButton.SECONDARY){
			                	//System.out.print("in2");
			                	ToggleButton findme =  (ToggleButton) event.getTarget();
			                	 int hero =	map.indexOf(findme);
			                	  Image img = new Image("/greatflag.png");
			                	  if(null == map.get(hero).getGraphic())
			                			  {
			                		  
			                		   int ho= hero/16;
			 			               System.out.println(topscreen.getRowIndex(map.get(hero)) + " " + topscreen.getColumnIndex(map.get(hero)));
			 			              if(ho == 16)
			 			              {
			 			            	  ho--;
			 			              }
			 			                int  y= hero%16;  
			 			               Space m = grid[ho][y];
			 			             
			 			            
			 			               m.placeFlag();
			 			             if( true == controller.isGameOver()) // line needs work
			 			              {
			 			 	           Alert alert = new Alert(AlertType.CONFIRMATION);
										alert.setTitle("You Won!");
										 alert.setContentText("See Board?");
										  alert.showAndWait();
										  if(alert.getResult() == ButtonType.CANCEL)
											{
										Platform.exit();
											}  
			 			              }
			 			              			 			            	   
			                      ImageView view = new ImageView(img);
			                      view.setFitHeight(30);
			                      view.setFitWidth(30);
			                      view.setPreserveRatio(true);
			                     map.get(hero).setGraphic(view);
			                    //  map.get(hero).setText("F");
			                      map.get(hero).setStyle("-fx-padding: 0px;");
			                	 //map.get(hero).setGraphicTextGap(0);
			                	 //map.get(hero).setDisable(true);
			                			  }
			                	  else if(null != map.get(hero).getGraphic())
			                	  {
			                		  int ho= hero/16;
			 			              // System.out.print("" + ho);
			 			              if(ho == 16)
			 			              {
			 			            	  ho--;
			 			              }
			 			                int  y= hero%16;  
			 			               Space m = grid[ho][y];
			 			              
			 			               m.removeFlag();
			                		  
			                		  System.out.print("in second");
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
				 //jght
					// System.out.print(io);
				 //push issue working on
				Font font = Font.font("Times New Roman", 6);
				map.get(i).setFont(font);
				map.get(i).setStyle("-fx-border-style: solid;"+  "-fx-padding: 11px;" +  "-fx-border-color: lightgreen;" );
				map.get(i).setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.4)));
				//map.get(i).setWrapText(true);
			//	map.get(i).setTextFill(Color.web("#FBF7F5"));
				//map.get(i).setTextAlignment(TextAlignment.JUSTIFY);
				//running.get(io).setMaxWidth(-0.01);
				topscreen.add(map.get(i), colab, row);
				row++;
				
			}
			headview = new ImageView(head);
			headview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			     @Override
			     public void handle(MouseEvent event) {
			        
			         if(headview.getImage() == head)
			         {
			        	 System.out.println("reset board with simley"); 
			         }
			         
			         if(headview.getImage() == gameOver)
			         {
			        	 System.out.println("reset board after gamover");  
			         }
			         //event.consume();
			     }
			});
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
	   /* @Override
		public void update(Observable o, Object arg) {
			
			grid = 
	    }*/
	}
