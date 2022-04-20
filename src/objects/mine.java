package objects;
import java.awt.Point;

public class mine implements Cloneable {
	private int flagecount =0;
	private int mines;
	private boolean ismine;
	private boolean isflaged;
	private Point n;
	public void makeblank() {
	ismine= false;
	isflaged = false;
	mines = 0;
	}
	public void randomMinemaker()
	{
		int randomC =  (int) ((Math.random() * (20 - 0)) + 0);	
		if(randomC == 1)
		{
			ismine=false;
		}
	}
	public void setpoint(Point n)
	{
		this.n = n;
	}
	public Point getpoint()
	{
		return n;
	}
	public boolean getismine()
	{
		return ismine;
	}
	public boolean setflage()
	{
		boolean repeatcatch = false;
		if(flagecount==1)
		{
			flagecount--;
			repeatcatch=true;
			isflaged=false;
		}
		if(flagecount==0 && repeatcatch == false)
		{
			flagecount++;
			isflaged=true;
		}
		return isflaged; 
	}

	public void setaboutofmine(int i)
	{
		mines = i;
	}
	public int getmines()
	{
		return mines;
	}
}
