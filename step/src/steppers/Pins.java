package steppers;

public class Pins {
public static boolean[] getPins() {
		return pins;
	}

	public static void setPins(boolean[] pins) {
		Pins.pins = pins;
	}

private static boolean [] pins;

Pins ()
{
	pins = new  boolean [25];
}
}
