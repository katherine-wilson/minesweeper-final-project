import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
// doing this to pushe again
public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
private int hold;
    private int lastIndex;

    public SpriteAnimation(
            ImageView imageView, 
            Duration duration, 
            int count,   int columns,
            int offsetX, int offsetY,
            int width,   int height) {
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.width     = width;
        this.height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
    ///	System.out.print(k);
       int random = lastIndex;
        hold++;
        	 double hero =  Math.sin(10*random)*90;
        	//.k. System.out.print(hero);
        	 
            int x = (int) hero +offsetX;
          //= 86;
            System.out.println(x);
         
        	   
        	  
        	 
           
           
            final int y =  offsetY  + 5;
           imageView.setViewport(new Rectangle2D(x, y, width, height));
        if(hold == 25)
        {
        	hold=0;
            lastIndex++;
           
            
        }
        	
           
    }
}