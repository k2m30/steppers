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
	public boolean isMoveTo = false;
	public double rate;

	State() {
		x = y = vl = vr = ll = lr = 0;
		pins = new Pins();
		comment = null;
	}

	State(double _x, double _y, GProperties p) {
		pins = new Pins();
		comment = "Added directly";
		x = _x;
		y = _y;
		ll = Math.sqrt(x * x + y * y);
		lr = Math.sqrt((p.canvasSizeX - x) * (p.canvasSizeX - x) + y * y);
	}

	public State(Segment s, GProperties p) {
		pins = new Pins();
		comment = "Added directly";

		// вычисление соотношения между длинами отрезков в декартовой и
		// треугольной системах коородинат для определения скорости подачи
		x = s.xStart;
		y = s.yStart;
		double ll_start = Math.sqrt(x * x + y * y); // lStart
		double lr_start = Math.sqrt((p.canvasSizeX - x) * (p.canvasSizeX - x)
				+ y * y); // rStart

		x = s.xEnd;
		y = s.yEnd;
		ll = Math.sqrt(x * x + y * y); // lEnd
		lr = Math.sqrt((p.canvasSizeX - x) * (p.canvasSizeX - x) + y * y); // rEnd

		double triangleSegmentLength = Math.sqrt((ll - ll_start)
				* (ll - ll_start) + (lr - lr_start) * (lr - lr_start));
		rate = s.segmentLength / triangleSegmentLength;
	}
}
