package steppers;

public class Properties {
	public double canvasSizeX;
	public double canvasSizeY;
	public double maxV;
	public double a;
	public double tickSize; 
	public double radius ;
	public double stepsPerRound;
	public double linearVelocity;

	public double initialXTicks;
	public double initialYTicks;
	
	public double dl; //изменение длины ремня за один тик
	
	public void calculate ()
	{
		dl = 2*Math.PI*radius/stepsPerRound;
		
	}
}
