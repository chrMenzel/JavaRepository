package petrinetzEditor;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

/**
 * Die Klasse Stelle repräsentiert eine Stelle im Petrinetz-Editor
 */
public class Stelle extends PetriNetFigure {
  /**
   * Eine Referenz auf die ID der Stelle als Zahl
   */
  static int id;

  /**
   * Dies ist eine Referenz auf die tatsächliche ID der Stelle als String
   */
  private String placeID;

  /**
   * Dies ist eine Referenz auf die Anzahl der Markierungen (Token) in der
   * Stelle
   */
  private int numberOfTokens;

  /**
   * Der Konstruktor der Klasse Stelle erzeugt ein neues Objekt dieser Klasse.
   * Dieser Konstruktor wird für den Fall eingesetzt, dass die Stelle
   * tatsächlich in ein Petri-Netz eingefügt wird. Dies ist beim
   * Größenänderungs-Fenster nicht der Fall, weswegen dafür ein weiterer
   * Konstruktor besteht.
   * 
   * @param panel
   *          Das PetriNetPanel, in dem die Stelle gezeichnet wird
   * @param x
   *          Die X-Koordinate der Stelle
   * @param y
   *          Die Y-Koordinate der Stelle
   * @param width
   *          Die Breite der Stelle
   * @param height
   *          Die Höhe der Stelle
   * @param createdByUser
   *          Flag, ob die Stelle vom Anwender aktiv gezeichnet wurde true, wenn
   *          die Stelle vom Anwender gezeichnet wurde false, wenn die Stelle
   *          durch Laden einer Datei erstellt wurde
   */
  Stelle(PetriNetPanel panel, int x, int y, int width, int height,
      boolean createdByUser) {
    super(panel, x, y, width, height, createdByUser);
  }

  /**
   * Der Konstruktor der Klasse Stelle erzeugt ein neues Objekt dieser Klasse.
   * Dieser weitere Konstruktor wird für den Fall eingesetzt, dass die Stelle
   * nicht in das eigentliche Petri-Netz eingefügt wird, sondern einem anderen
   * Zweck (hier: Zur Veranschaulichung einer Größenänderung) dient.
   * 
   * @param panel
   *          Das PetriNetPanel, in dem die Stelle gezeichnet wird
   * @param x
   *          Die X-Koordinate der Stelle
   * @param y
   *          Die Y-Koordinate der Stelle
   * @param width
   *          Die Breite der Stelle
   * @param height
   *          Die Höhe der Stelle
   */
  Stelle(JPanel panel, int x, int y, int width, int height) {
    super(panel, x, y, width, height);
  }

  /**
   * Diese Methode dient dem Zeichnen der Stelle. Sie überschreibt die Methode
   * paintComponent() der Superklasse PetriNetFigure.
   * 
   * @param g
   *          Der Grafikkontext, in dem die Stelle gezeichnet wird
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    // x und y haben beim Zeichnen die Koordinaten (0, 0), da es sich
    // um eine JComponent handelt, die schon plaziert wurde. Das Graphics,
    // in das die Ellipse2D.Double gezeichnet wird, gehört zur JComponent,
    // nicht zum panel.
    // Da getWidth() und getHeight() einer Ellipse2D.Double die Breite und Höhe
    // des umgebenden Rechtecks ausgibt, sind diese Werte um 1 größer als die
    // JComponent auf der gezeichnet wird. Daher werden für die Zeichnung die
    // Werte
    // von Breite und Höhe um 1 reduziert.
    // Sh. auch: http://dictionary.babylon.com/getwidth/
    RectangularShape rectShape = new Ellipse2D.Double(0, 0, getWidth() - 1,
        getHeight() - 1);
    g2d.draw(rectShape);
    // Das RectangularShape sichern, da es für die Berechnung der Pfeile
    // benötigt wird
    this.setRectangularShape(rectShape);

    // Eventuelle Token der Stelle hinzufügen
    paintToken(g2d);
  }

  /**
   * Diese Methode dient dem Zeichnen der Markierungen (Token) innterhalb der
   * Stelle. Sofern die Anzahl 1 ist, soll die Markierung als dicker Punkt in
   * der Mitte dargestellt werden, ansonsten als Zahl.
   * 
   * @param g2d
   *          Der Grafikkontext, in dem die Markierung der Stelle gezeichnet
   *          wird
   */
  private void paintToken(Graphics2D g2d) {
    // Wenn die Anzahl genau 1 ist, dann einen Punkt zeichnen, der
    // 40 % der Größe der Stelle hat.
    if (numberOfTokens == 1) {
      double tokenWidth = getWidth() * 0.4;
      double tokenHeight = getHeight() * 0.4;
      // Kreis in der Mitte der Stelle zeichnen
      Ellipse2D.Double token = new Ellipse2D.Double(getWidth() / 2 - tokenWidth
          / 2, getHeight() / 2 - tokenHeight / 2, tokenWidth, tokenHeight);
      // und ausfüllen
      g2d.fill(token);

      // Wenn die Anzahl größer 1 (bis zum höchsten Integer-Wert 2.147.483.647)
      // ist
    } else if (numberOfTokens > 1 && numberOfTokens <= Integer.MAX_VALUE) {
      // Den aktuellen Font sichern und die Anzahl in Text wandeln
      Font oldFont = g2d.getFont();
      String text = Integer.toString(numberOfTokens);

      // Das Rechteck, in das der Token-Text passt, ermitteln
      // Methode getRectangleAndSetFont ist in der Superklasse PetriNetFigure
      Rectangle2D rectangleForAlignedText = getRectangleAndSetFont(g2d, text,
          oldFont, true);
      // Den Text (die Anzahl Token als Zahl) in die Stellen-Komponente
      // schreiben
      writeTextOnComponent(g2d, oldFont, text, rectangleForAlignedText);

      // Sicherheitsmaßnahme, falls der Wert Integer.MAX_VALUE überschritten
      // wird
      // Das kann regulär nur vorkommen, wenn die Anzahl Tokens 2147483647
      // (= Integer.MAX_VALUE) war und mit dem Button "Stelle hinzufügen"
      // eine weitere Stelle hinzugefügt wird. In dem Fall würde bei Addition
      // von
      // 1 der Wert -2147483648 ermittelt.
    } else if (numberOfTokens < 0) {
      // Daher den Wert auf dem maximalen Integer-Wert begrenzen
      // (in der Praxis extrem unwahrscheinlich, daher auf eine Benachrichtigung
      // des Benutzers verzichtet)
      numberOfTokens = Integer.MAX_VALUE;
    }
  }

  /**
   * Diese Methode schreibt die Anzahl der Token auf die PetriNetFigure
   * 
   * @param g2d
   *          Der Grafikkontext, in dem die Markierung der Stelle gezeichnet
   *          wird
   * @param oldFont
   *          Der Font, auf den nach dem Schreiben zurückgestellt wird
   * @param text
   *          Der zu schreibende Text
   * @param rectangleForAlignedText
   *          Das Rechteck, in das der Text gerade noch reinpasst
   */
  private void writeTextOnComponent(Graphics2D g2d, Font oldFont, String text,
      Rectangle2D rectangleForAlignedText) {

    // Die X-Koordinate ist relativ einfach zu ermitteln:
    // Hälfte der Breite des Elements minus die Hälfte der Breite des Textes
    int tempX = this.getWidth() / 2 - (int) rectangleForAlignedText.getWidth()
        / 2;
    // Für die Y-Koordinate ist es nicht so leicht, da sich das Y bei der
    // Methode
    // drawString von Graphics bzw. Graphics2D auf die baseline bezieht, auf der
    // der Text steht. Daher wird für den Y-Wert folgendes angenommen:
    // Der Y-Wert des Mittelpunkts des Elements
    // + Die Hälfte von fünf Sechsteln der Fontgröße
    // Verschiedene Experimente haben ganz passable Ergebnisse erzielt.
    int tempY = (int) (this.getRectangularShape().getCenterY() + (g2d.getFont()
        .getSize() * 5 / 6) / 2);
    // Den Token-Wert in das Element schreiben
    g2d.drawString(text, tempX, tempY);
    // den bisherigen Font wieder herstellen
    g2d.setFont(oldFont);
  }

  /**
   * Diese Methode gibt die Anzahl von Token dieser Stelle zurück
   * 
   * @return Die Anzahl von Token der Stelle
   */
  public int getNumberOfTokens() {
    return numberOfTokens;
  }

  /**
   * Diese Methode legt die Anzahl von Token dieser Stelle fest
   * 
   * @param numberOfTokens
   *          Die Anzahl von Token der Stelle
   */
  void setNumberOfTokens(int numberOfTokens) {
    this.numberOfTokens = numberOfTokens;
  }

  /**
   * Die Methode buildAndSetPlaceID ist eine überladene Methode. In dieser
   * Methode ohne Parameter wird die ID der Stelle durch die Klassenvariable id
   * festgesetzt. Dies erfolgt dann, wenn der Anwender die Stelle gezeichnet
   * hat. Beim Laden einer Datei wird die andere Methode buildAndSetPlaceID
   * verwendet.
   */
  void buildAndSetPlaceID() {
    // ID der Stelle festsetzen und die Klassenvariable id um 1 inkrementieren
    setPlaceID(id++);
  }

  /**
   * Die Methode buildAndSetPlaceID ist eine überladene Methode. In dieser
   * Methode mit Parameter wird die ID der Stelle durch den übergebenen
   * Parameter festgesetzt. Dies erfolgt dann, eine Datei geladen wird. Wenn der
   * Anwender die Stelle selbst zeichnet, wird die andere Methode
   * buildAndSetPlaceID verwendet.
   * 
   * @param idFromFile
   *          Die ID der Stelle, die in der Datei gespeichert ist
   */
  void buildAndSetPlaceID(String idFromFile) {
    this.placeID = idFromFile;
  }

  /**
   * Diese Methode wird von der Methode buildAndSetPlaceID() dieser Klasse
   * aufgerufen. Sie legt die ID der Stelle nach dem Muster "place" + number
   * fest.
   * 
   * @param number
   *          Die übergebene Zahl als Teil der ID
   */
  private void setPlaceID(int number) {
    this.placeID = "place" + Integer.toString(number);
  }

  /**
   * Diese Methode gibt die placeID dieser Stelle zurück
   * 
   * @return Die placeID dieser Stelle
   */
  String getPlaceID() {
    return placeID;
  }
}
