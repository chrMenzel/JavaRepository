package petrinetzEditor;

import java.awt.*;
import java.awt.geom.*;

/**
 * Die Klasse Kante repräsentiert eine Kante im Petrinetz-Editor
 */
public class Kante {
  /**
   * Dies ist eine Referenz auf die ID der Kante als Zahl
   */
  static int id;

  /**
   * Dies ist eine Referenz auf die tatsächliche ID der Kante als String
   */
  private String arrowID;

  /**
   * Dies ist eine Variable für die Linie der Kante
   */
  Line2D.Double arrowLine;

  /**
   * Dies ist eine Variable für die Farbe der Kante
   */
  Color color;

  /**
   * Dies ist eine Referenz auf den Startpunkt der Kante
   */
  Point start;

  /**
   * Dies ist eine Referenz auf den Zielpunkt der Kante
   */
  Point destination;

  /**
   * Diese Referenz ist ein Zwischenwert für die Berechnung des Drehwinkels. Es
   * handelt sich dabei um die Differenz der X- und Y-Werte von Zielpunkt der
   * Kante minus Startpunkt der Kante.
   */
  private Point differenceDestinationStart = new Point();

  /**
   * Diese Referenz ist der rechte Eckpunkt der Pfeilspitze
   */
  private Point cornerRight = new Point();

  /**
   * Diese Referenz ist der linke Eckpunkt der Pfeilspitze
   */
  private Point cornerLeft = new Point();

  /**
   * Diese Variable ist die Länge der Pfeilspitze in Pixel
   */
  private int arrowheadLength;

  /**
   * Diese Referenz verweist auf die PetriNetFigure, von der die Kante ausgeht
   */
  private PetriNetFigure figureFrom;

  /**
   * Diese Referenz verweist auf die PetriNetFigure, zu der die Kante hinführt
   */
  private PetriNetFigure figureTo;

  /**
   * Dies ist eine Referenz auf ein AffineTransform, das für Drehungen,
   * Spiegelungen usw. eingesetzt werden kann. Die Referenz wird gleich
   * definiert, so dass wärend dem Zeichnen des Pfeils immer dasselbe
   * AffineTransform verwendet wird.
   */
  private AffineTransform transformer = new AffineTransform();

  /**
   * Dies ist eine Referenz auf ein weiteres AffineTransform, nämlich dasjenige,
   * welches vor dem Zeichnen des Pfeils eingesetzt wird.
   */
  private AffineTransform originalTransform;

  /**
   * Der Konstruktor der Klasse Kante erzeugt ein neues Objekt dieser Klasse.
   * Dieser Konstruktor wird für den Fall eingesetzt, dass die Parameter, um die
   * Kante tatsächlich schon im Petrinetz zu zeichnen, bekannt sind.
   * 
   * Dies ist beim Laden einer Datei noch nicht der Fall, bzw. die Parameter
   * werden später ermittelt. Für diesen Fall besteht noch ein weiterer
   * Konstruktor.
   * 
   * @param figureFrom
   *          Die PetriNetFigure, von der die Kante ausgeht
   * @param figureTo
   *          Die PetriNetFigure, zu der die Kante hinführt
   * @param start
   *          Der Startpunkt der Kante als Point
   * @param destination
   *          Der Zielpunkt der Kante als Point
   * @param arrowheadLength
   *          Die Länge der Pfeilspitze in Pixel
   */
  public Kante(PetriNetFigure figureFrom, PetriNetFigure figureTo, Point start,
      Point destination, int arrowheadLength) {
    this.setFigureFrom(figureFrom);
    this.figureTo = figureTo;
    // Start- und Endpunkt des Pfeils auf dem PetriNetPanel setzen
    this.start = start;
    this.destination = destination;
    // Pfeilspitzenlänge setzen
    this.arrowheadLength = arrowheadLength;
    this.color = Color.red;
  }

  /**
   * Der Konstruktor der Klasse Kante erzeugt ein neues Objekt dieser Klasse.
   * Dieser Konstruktor wird für den Fall eingesetzt, dass die Parameter, um die
   * Kante tatsächlich schon im Petrinetz zu zeichnen, noch nicht bekannt sind.
   * 
   * Dies ist insbesondere beim Laden einer Datei der Fall, denn die Parameter
   * der Kante werden später ermittelt. Sind alle Parameter (insbesondere Start-
   * und Zielpunkt der Kante auf dem PetriNetPanel) bekannt, sollte der andere
   * Konstruktor verwendet werden.
   * 
   * @param arrowheadLength
   *          Die Länge der Pfeilspitze in Pixel
   */
  public Kante(int arrowheadLength) {
    // Die Werte für Start- und Zielpunkt der Kante sind nur eine Voreinstellung
    this.start = new Point(0, 0);
    this.destination = new Point(0, 0);
    // Die Länge der Pfeilspitze wurde übergeben
    this.arrowheadLength = arrowheadLength;
  }

  /**
   * Diese Methode zeichnet die Kante in das Petrinetz
   * 
   * @param g2d
   *          Der Grafikkontext, in dem die Kante gezeichnet wird
   */
  void paintFigure(Graphics2D g2d) {
    // Zeichenfarbe auf die Farbe der Kante setzen
    g2d.setColor(this.color);

    // Aktuelle Transformation sichern
    originalTransform = g2d.getTransform();

    /* Drehwinkel der Pfeilspitze berechnen */

    // Differenzen der X-/Y-Werte zwischen Ziel- und Startpunkt des
    // Pfeils bestimmen (kartesische Ausgangswerte), um den Drehwinkel
    // als Polarwert bestimmen zu können
    differenceDestinationStart.x = destination.x - start.x;
    differenceDestinationStart.y = destination.y - start.y;

    // Drehwinkel als Polarwert bestimmen
    double winkel = Math.atan2(differenceDestinationStart.y,
        differenceDestinationStart.x);

    /* Pfeilspitze bestimmen */

    // Länge des Pfeils
    double arrowLength = start.distance(destination);
    // X-Koordinate der Enden der Pfeilspitze ist die Pfeillänge minus
    // der Länge der Pfeilspitze
    cornerRight.x = (int) (arrowLength - arrowheadLength);
    cornerLeft.x = cornerRight.x;
    // Y-Koordinate der Enden der Pfeilspitze sind jeweils 60 % (hier
    // festgelegt)
    // der Pfeilspitzenlänge, einmal nach oben und nach unten
    cornerRight.y = (int) (arrowheadLength * 0.6);
    cornerLeft.y = (int) (arrowheadLength * -0.6);

    // Transformation der Koordinaten anhand des Drehwinkels vornehmen
    transformer.setToIdentity();
    transformer.translate(start.x, start.y);
    transformer.rotate(winkel);
    g2d.transform(transformer);

    // Dreieck der Pfeilspitze zeichnen
    Polygon arrowHead = new Polygon();
    arrowHead.addPoint(cornerRight.x, cornerRight.y);
    arrowHead.addPoint(cornerLeft.x, cornerLeft.y);
    arrowHead.addPoint((int) (arrowLength), 0);

    // Linie des Pfeils als Line2D festlegen
    arrowLine = new Line2D.Double(0, 0, (int) (arrowLength), 0);

    // Dreieck und Linie als zusammenhängenden Pfad zeichnen
    Path2D arrowPath = new Path2D.Double();
    arrowPath.append(arrowHead, true);
    arrowPath.append(arrowLine, false);

    // Im kartesischen Koordinatensystem ist dies die Linie des Pfeils
    // Diese im Kanten-Objekt speichern, damit es später auf die Nähe der
    // Maus reagieren kann
    arrowLine = new Line2D.Double(start, destination);

    // Dreieck zeichnen
    g2d.fill(arrowPath);
    // Linie zeichnen
    g2d.draw(arrowPath);

    // Unsprüngliche Transformation zurücksetzen
    g2d.setTransform(originalTransform);
  }

  /**
   * Die Methode setArrowID ist eine überladene Methode. In dieser Methode mit
   * Parameter wird die ID der Kante durch den übergebenen Parameter
   * festgesetzt. Dies erfolgt dann, eine Datei geladen wird. Wenn der Anwender
   * die Kante selbst zeichnet, wird die andere Methode setArrowID verwendet.
   * 
   * @param idFromFile
   *          Die ID, die in der Datei gespeichert wurde.
   */
  public void setArrowID(String idFromFile) {
    this.arrowID = idFromFile;
  }

  /**
   * Die Methode setArrowID ist eine überladene Methode. In dieser Methode ohne
   * Parameter wird die ID der Kante durch die Klassenvariable id festgesetzt.
   * Dies erfolgt dann, wenn der Anwender die Kante gezeichnet hat. Beim Laden
   * einer Datei wird die andere Methode setArrowID verwendet.
   */
  public void setArrowID() {
    this.arrowID = "arc" + Integer.toString(id++);
  }

  /**
   * Diese Methode gibt die ID der Kante zurück.
   * 
   * @return Die ID der Kante
   */
  public String getArrowID() {
    return arrowID;
  }

  /**
   * Diese Methode gibt die PetriNetFigure zurück, von der die Kante ausgeht.
   * 
   * @return Die PetriNetFigure, von der die Kante ausgeht
   */
  public PetriNetFigure getFigureFrom() {
    return figureFrom;
  }

  /**
   * Diese Methode setzt die PetriNetFigure, von der die Kante ausgeht.
   * 
   * @param figureFrom
   *          Die PetriNetFigure, von der die Kante ausgeht
   */
  public void setFigureFrom(PetriNetFigure figureFrom) {
    this.figureFrom = figureFrom;
  }

  /**
   * Diese Methode gibt die PetriNetFigure zurück, zu der die Kante hinführt.
   * 
   * @return Die PetriNetFigure, zu der die Kante hinführt
   */
  public PetriNetFigure getFigureTo() {
    return figureTo;
  }

  /**
   * Diese Methode setzt die PetriNetFigure, zu der die Kante hinführt.
   * 
   * @param figureTo
   *          Die PetriNetFigure, zu der die Kante hinführt
   */
  void setFigureTo(PetriNetFigure figureTo) {
    this.figureTo = figureTo;
  }

  /**
   * Diese Methode gibt die Länge der Pfeilspitze zurück
   * 
   * @return Die Länge der Pfeilspitze
   */
  int getArrowheadLength() {
    return arrowheadLength;
  }

  /**
   * Diese Methode setzt die Länge der Pfeilspitze
   * 
   * @param arrowHeadLength
   *          Die Länge der Pfeilspitze
   */
  public void setArrowHeadLength(int arrowHeadLength) {
    this.arrowheadLength = arrowHeadLength;
  }
}
