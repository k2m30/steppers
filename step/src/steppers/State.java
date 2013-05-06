package steppers;

public class State {
	public double x;
	public double y;
	public double vl; // скорость левого двигателя
	public double vr; // скорость правого двигателя
	public double ll; // длина левого ремня
	public double lr; // длина правого ремня
	public Pins pins; // физические выводы порта
	public String comment;

	State() {
		x = y = vl = vr = ll = lr = 0;
		pins =  new Pins();
		comment = null;
	}
	State(double _x, double _y, Properties p) {
		pins =  new Pins();
		comment = "Added directly";
		x = _x;
		y = _y;
		ll = Math.sqrt(x*x+y*y);
		lr = Math.sqrt((p.canvasSizeX - x)*(p.canvasSizeX - x) + y*y);
	}
}
