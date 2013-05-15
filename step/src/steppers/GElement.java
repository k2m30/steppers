package steppers;

enum EType {
	line, // x1 y1 x2 y2
	rectangle, // x y width height
	ellipse, // cx cy rx ry
	path, bezier, moveTo,  paintTo
};

public class GElement {
	public double getP1() {
		return p1;
	}

	public void setP1(double p1) {
		this.p1 = p1;
	}

	public double getP2() {
		return p2;
	}

	public void setP2(double p2) {
		this.p2 = p2;
	}

	public double getP3() {
		return p3;
	}

	public void setP3(double p3) {
		this.p3 = p3;
	}

	public double getP4() {
		return p4;
	}

	public void setP4(double p4) {
		this.p4 = p4;
	}

	public EType getType() {
		return type;
	}

	public void setType(EType type) {
		this.type = type;
	}

	private double p1;
	private double p2;
	private double p3;
	private double p4;
	public EType type;

	GElement(EType e, double _p1, double _p2, double _p3, double _p4) {
		p1 = _p1;
		p2 = _p2;
		p3 = _p3;
		p4 = _p4;
		type = e;
		return;
	}

	GElement(EType e, String _p1, String _p2, String _p3, String _p4) {
		p1 = Double.parseDouble(_p1);
		p2 = Double.parseDouble(_p2);
		p3 = Double.parseDouble(_p3);
		p4 = Double.parseDouble(_p4);
		type = e;
		return;
	}

	public void set(int index, double p) {
		switch (index) {
		case 1:
			p1 = p;
		case 2:
			p2 = p;
		case 3:
			p3 = p;
		case 4:
			p4 = p;
		}

		return;
	}

	public double getP(int index) {
		switch (index) {
		case 1:
			return p1;
		case 2:
			return p2;
		case 3:
			return p3;
		case 4:
			return p4;
		}
		return -1;
	}

	public void trace() {
		System.out.print(type.name() + ": ");
		System.out.print(p1 + " ");
		System.out.print(p2 + " ");
		System.out.print(p3 + " ");
		System.out.print(p4 + " ");
		System.out.println(" ");
	}
}
