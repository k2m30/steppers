package steppers;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class step {

	private static ArrayList<GElement> list = new ArrayList<GElement>();

	private static Properties properties = new Properties();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = null;

		readProperties(fileName); // читаем свойства из conf файла

		String svgFileName = getFilename(); // берем файл для рисования

		ArrayList<GElement> elements = parseSVG(svgFileName); // разбираем файл
																// на элементы
																// (линия,
																// прямоугольник
																// и т.д.)

		Point initialPoint = initialize(properties.initialXTicks,
				properties.initialYTicks); // инициализация, выставление в
											// начальную точку

		ArrayList<GElement> moreElements = addMoveTo(elements, initialPoint); // добавляем
		// переходы
		// между элементами
		listTrace(moreElements);

		ArrayList<Segment> segments = split(moreElements,
				properties.maxSegmentLength); // делим
		// элементы
		// на
		// небольшие
		// линейные
		// сегменты

		ArrayList<Segment> allLines = addPathToLines(segments); // добавляем
																// обходные
																// пути для
																// более
																// плавного
																// движения

		ArrayList<State> states = makeStates(initialPoint, allLines); // получение
																		// состояний
																		// длин
																		// ремней
																		// для
																		// рисования

		String outputFileName = "path";
		makeDrawFile(states, outputFileName); // запись состояний в файл
	}

	private static String getFilename() {

		// TODO Auto-generated method stub
		return "file:/Users/Mikhail/Documents/workspace/steppers/bin/Domik.svg";
	}

	private static void makeDrawFile(ArrayList<State> states,
			String outputFileName) {
		try {
			File f = new File(outputFileName
					+ new Date(System.currentTimeMillis()).toString() + ".ngc");
			f.createNewFile();
			FileWriter fw = new FileWriter(f);

			fw.append("%");
			fw.append('\n');
			// fw.append("G90 G40 G17 G21");
			// fw.append('\n');
			fw.append("G51Y-1");
			fw.append('\n');

			for (int i = 0; i < states.size(); i++) {

				if (!states.get(i).isMoveTo) { // прорисовка элементов
					fw.append("G01 X");
					fw.append(Double.toString(states.get(i).ll));
					fw.append(" Y");
					fw.append(Double.toString(states.get(i).lr));
					fw.append(" Z100");
					fw.append(" F");
					fw.append(Double.toString(properties.linearVelocity
							* states.get(i).rate));
					fw.append(" " + states.get(i).comment);
					fw.append('\n');
				} else { // быстрое перемещение между элементами
					fw.append("G00 X");
					fw.append(Double.toString(states.get(i).ll));
					fw.append(" Y");
					fw.append(Double.toString(states.get(i).lr));
					fw.append(" Z0");
					fw.append('\n');
				}
			}
			fw.append("M30");
			fw.append('\n');
			fw.append("%");
			fw.append('\n');
			fw.flush();
			fw.close();
		} catch (Exception ex) {
			p(ex.toString() + " makeDrawFile");
		}
	}

	/**
	 * makeStates
	 * 
	 * @param initialPoint
	 *            - начальная точка
	 * @param allLines
	 *            - список сегментов
	 * @return список состояний
	 */
	private static ArrayList<State> makeStates(Point initialPoint,
			ArrayList<Segment> allLines) {
		ArrayList<State> _states = new ArrayList<State>();
		Iterator<Segment> iterator;
		iterator = allLines.iterator();

		Segment s;

		while (iterator.hasNext()) {
			s = iterator.next();
			State st = new State(s, properties);

			if (s.isMoveToSegment)
				st.isMoveTo = true;
			st.comment = s.comment;
			_states.add(st);

		}

		return _states;
	}

	private static Point initialize(double initialXTicks, double initialYTicks) {
		// TODO Auto-generated method stub
		return new Point(500, 500);
	}

	private static ArrayList<Segment> addPathToLines(
			ArrayList<Segment> moreLines) {

		for (int i = 0; i < moreLines.size() - 1; i++) {
			// moreLines.get(i).
		}

		return moreLines;
	}

	private static ArrayList<GElement> addMoveTo(ArrayList<GElement> elements,
			Point initialPoint) {
		for (int i = 0; i < elements.size() - 1; i++) {
			switch (elements.get(i).type) {
			case bezier:
				break;
			case ellipse:
				break;
			case line:
				switch (elements.get(i + 1).type) {
				case bezier:
					break;
				case ellipse:
					break;
				case line:
					elements.add(i + 1, new GElement(EType.moveTo, elements
							.get(i).getP3(), elements.get(i).getP4(), elements
							.get(i + 1).getP1(), elements.get(i + 1).getP2()));
					break;
				case moveTo:
					break;
				case paintTo:
					break;
				case path:
					break;
				case rectangle:
					elements.add(i + 1, new GElement(EType.moveTo, elements
							.get(i).getP3(), elements.get(i).getP4(), elements
							.get(i + 1).getP1(), elements.get(i + 1).getP2()));
					break;
				default:
					break;

				}
				break;
			case moveTo:
				break;
			case paintTo:
				break;
			case path:
				break;
			case rectangle:
				switch (elements.get(i + 1).type) {
				case bezier:
					break;
				case ellipse:
					break;
				case line:
					elements.add(i + 1, new GElement(EType.moveTo, elements
							.get(i).getP1(), elements.get(i).getP2(), elements
							.get(i + 1).getP1(), elements.get(i + 1).getP2()));
					break;
				case moveTo:
					break;
				case paintTo:
					break;
				case path:
					break;
				case rectangle:
					elements.add(i + 1, new GElement(EType.moveTo, elements
							.get(i).getP1(), elements.get(i).getP2(), elements
							.get(i + 1).getP1(), elements.get(i + 1).getP2()));
					break;
				default:
					break;

				}
				break;
			default:
				break;

			}
		}

		elements.add(0, new GElement(EType.moveTo, initialPoint.x,
				initialPoint.y, elements.get(0).getP1(), elements.get(0)
						.getP2())); // добавление перехода в начало рисования

		for (int i = 0; i < elements.size(); i++) { // удаление нулевых
													// переходов (начало и конец
													// совпадают)
			if ((elements.get(i).type == EType.moveTo)
					&& (elements.get(i).getP1() == elements.get(i).getP3())
					&& (elements.get(i).getP2() == elements.get(i).getP4())) {
				elements.remove(i);
			}
		}
		return elements;
	}

	private static ArrayList<Segment> split(ArrayList<GElement> elements,
			double _maxSegmentLength) {
		ArrayList<Segment> s = new ArrayList<Segment>();

		GElement e;
		Iterator<GElement> eIt = elements.iterator();

		while (eIt.hasNext()) {
			e = eIt.next();

			switch (e.type) {
			case line:
				s.addAll(splitLine(e, _maxSegmentLength));
				break;
			case bezier:
				break;
			case ellipse:
				break;
			case moveTo:
				Segment moveToSegment = new Segment(e.getP1(), e.getP2(),
						e.getP3(), e.getP4());
				moveToSegment.isMoveToSegment = true;
				s.add(moveToSegment);
				break;
			case paintTo:
				break;
			case path:
				break;
			case rectangle:
				s.addAll(splitLine(
						new GElement(EType.line, e.getP(1), e.getP(2), e
								.getP(1) + e.getP(3), e.getP(2)),
						_maxSegmentLength));
				s.addAll(splitLine(
						new GElement(EType.line, e.getP(1) + e.getP(3), e
								.getP(2), e.getP(1) + e.getP(3), e.getP(2)
								+ e.getP(4)), _maxSegmentLength));
				s.addAll(splitLine(
						new GElement(EType.line, e.getP(1) + e.getP(3), e
								.getP(2) + e.getP(4), e.getP(1), e.getP(2)
								+ e.getP(4)), _maxSegmentLength));
				s.addAll(splitLine(
						new GElement(EType.line, e.getP(1), e.getP(2)
								+ e.getP(4), e.getP(1), e.getP(2)),
						_maxSegmentLength));
				break;
			default:
				break;
			}

		}

		// TODO Auto-generated method stub
		return s;
	}

	private static ArrayList<Segment> splitLine(GElement e,
			double _maxSegmentLength) {

		ArrayList<Segment> s = new ArrayList<Segment>();

		double dx = e.getP(3) - e.getP(1);
		double dy = e.getP(4) - e.getP(2);
		double l = Math.sqrt(dx * dx + dy * dy);
		int n = (int) Math.round(l / _maxSegmentLength) + 1;

		double x1 = e.getP(1);
		double x2;
		double y1 = e.getP(2);
		double y2;
		for (int i = 1; i <= n; i++) {
			x2 = x1 + dx / n;
			y2 = y1 + dy / n;
			Segment segment = new Segment(x1, y1, x2, y2);
			segment.comment = new String("(Line " + e.getP1() + " " + e.getP2()
					+ " " + e.getP3() + " " + e.getP4() + " " + "Segment #" + i
					+ ")");
			segment.segmentLength = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
			s.add(segment);
			x1 = x2;
			y1 = y2;
		}
		return s;

	}

	private static void readProperties(String fileName) {
		// TODO Auto-generated method stub
		properties.initialXTicks = 1000;
		properties.initialYTicks = 1000;
		properties.a = 10;
		properties.canvasSizeX = 846;
		properties.canvasSizeY = 1200;
		properties.linearVelocity = 15000;
		properties.maxV = 250;
		properties.radius = 15.9;
		properties.stepsPerRound = 200;
		properties.tickSize = 0.000250;
		properties.maxSegmentLength = 10;
		properties.calculate();

		return;
	}

	public static void listTrace(ArrayList<GElement> _list) {
		Iterator<GElement> iterator = _list.iterator();
		while (iterator.hasNext()) {
			GElement ge = (GElement) iterator.next();
			ge.trace();
		}
		return;
	}

	public static ArrayList<GElement> parseSVG(String svgFileName) {
		try {
			p(svgFileName);
			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			String uri = svgFileName;
			Document doc = f.createDocument(uri);

			Element svg = doc.getDocumentElement();
			// Remove the xml-stylesheet PI.
			for (Node n = svg.getPreviousSibling(); n != null; n = n
					.getPreviousSibling()) {
				if (n.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
					doc.removeChild(n);
					break;
				}
			}

			// Поиск элементов дерева

			for (Node n = svg.getFirstChild(); n != null; n = n
					.getNextSibling()) {

				if (n.getNodeType() == Node.ELEMENT_NODE) {

					if (n.getLocalName().equals("rect")) {
						Element e = (Element) n;

						GElement el = new GElement(EType.rectangle,
								e.getAttribute("x"), e.getAttribute("y"),
								e.getAttribute("width"),
								e.getAttribute("height"));

						list.add(el);
					}

					else if (n.getLocalName().equals("line")) {
						Element e = (Element) n;

						GElement el = new GElement(EType.line,
								e.getAttribute("x1"), e.getAttribute("y1"),
								e.getAttribute("x2"), e.getAttribute("y2"));

						list.add(el);
					} else if (n.getLocalName().equals("ellipse")) {
						Element e = (Element) n;

						GElement el = new GElement(EType.ellipse,
								e.getAttribute("cx"), e.getAttribute("cy"),
								e.getAttribute("rx"), e.getAttribute("ry"));

						list.add(el);
					}

				}

			}

		} catch (Exception ex) {
			p(ex.toString() + " Parse SVG");
		}
		return list;
	}

	public static void p(String str) {
		System.out.println(str);
	}

	public static void p(int str) {
		System.out.println(Integer.toString(str));
	}

}
