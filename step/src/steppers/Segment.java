package steppers;

public class Segment {
public double xStart;
public double yStart;
public double xEnd;
public double yEnd;
public boolean isMoveToSegment = false;
public double segmentLength;
public String comment;

Segment (double _xStart, double _yStart, double _xEnd, double _yEnd)
{
	xStart = _xStart;
	yStart = _yStart;
	xEnd = _xEnd;
	yEnd = _yEnd;
	}
}
