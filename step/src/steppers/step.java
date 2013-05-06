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
	private static ArrayList<GElement> segmentList = new ArrayList<GElement>();
	private static double _maxSegmentLength;
	private static Properties properties = new Properties();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = null;

		readProperties(fileName); // читаем свойства из ini файла

		String svgFileName = getFilename(); // берем файл для рисования

		ArrayList<GElement> elements = parseSVG(svgFileName); // разбираем файл
																// на элементы
																// (линия,
																// прямоугольник
																// и т.д.)
		listTrace(elements);

		ArrayList<Segment> lines = split(elements, _maxSegmentLength); // делим
																		// элементы
																		// на
																		// небольшие
																		// линейные
																		// сегменты

		ArrayList<Segment> moreLines = addMoveTo(lines); // добавляем переходы
															// между элементами
		ArrayList<Segment> allLines = addPathToLines(moreLines); // добавляем
																	// обходные
																	// пути для
																	// более
																	// плавного
																	// движения

		Point initialPoint = new Point();
		initialPoint = initialize(properties.initialXTicks,
				properties.initialYTicks); // инициализация, выставление в центр

		initialPoint.x = 500;
		initialPoint.y = 500;
		allLines.add(new Segment(initialPoint.x, initialPoint.y,
				initialPoint.x - 400, initialPoint.y));
		ArrayList<State> states = makeStates(initialPoint, allLines); // получение
																		// состояний
																		// пинов
																		// порта
																		// для
																		// рисования

		String outputFileName = null;
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
					+ new Date(System.currentTimeMillis()).toString());
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			for (int i = 0; i < states.size(); i++) {
				fw.append(Double.toString(states.get(i).ll));
				fw.append(' ');
				fw.append(Double.toString(states.get(i).lr));
				fw.append('\n');
				fw.flush();
			}
			fw.close();
		} catch (Exception ex) {
			p(ex.toString() + " makeDrawFile");
		}
	}

	private static ArrayList<State> makeStates(Point initialPoint,
			ArrayList<Segment> allLines) {
		ArrayList<State> _states = new ArrayList<State>();
		Iterator<Segment> iterator;
		iterator = allLines.iterator();
		// first segment from initial point
		_states.add(new State(initialPoint.x, initialPoint.y, properties));
		//
		Segment s = iterator.next();

		while (iterator.hasNext()) {

			_states.add(new State(s.xEnd, s.yEnd, properties));
			s = iterator.next();
		}

		return _states;
	}

	private static Point initialize(double initialXTicks, double initialYTicks) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ArrayList<Segment> addPathToLines(
			ArrayList<Segment> moreLines) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ArrayList<Segment> addMoveTo(ArrayList<Segment> lines) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ArrayList<Segment> split(ArrayList<GElement> elements,
			double _maxSegmentLength2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void readProperties(String fileName) {
		// TODO Auto-generated method stub
		properties.initialXTicks = 1000;
		properties.initialYTicks = 1000;
		properties.a = 10;
		properties.canvasSizeX = 846;
		properties.canvasSizeY = 1200;
		properties.linearVelocity = 200;
		properties.maxV = 250;
		properties.radius = 15.9;
		properties.stepsPerRound = 200;
		properties.tickSize = 0.000250;

		properties.calculate();

		return;
	}

	private static ArrayList<State> moveTo(double xto, double yto) {
		ArrayList<State> states = new ArrayList<State>();

		return states;

	}

	private static ArrayList<GElement> polyline(ArrayList<GElement> _list,
			double _maxSegmentLength) {
		Iterator<GElement> iterator = _list.iterator();
		while (iterator.hasNext()) {
			GElement ge = (GElement) iterator.next();

			switch (ge.type) {
			case line:
			case rectangle:
			case ellipse:
			default:
				break;

			}
		}

		return _list;
	}

	private static ArrayList<GElement> lineToPolyline(GElement ge) {
		ArrayList<GElement> segments = new ArrayList<GElement>();

		return segments;
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
			p("file:/Users/Mikhail/Documents/workspace/steppers/bin/Domik.svg");
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
