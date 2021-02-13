package petrinetzEditor;

import java.awt.*;

import javax.swing.*;

import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.util.*;

/**
 * Die Klasse PetriNetFigure ist die Super-Klasse der Klassen Stelle und
 * Transition. Sie repräsentiert die gemeinsamen Eigenschaften der
 * Petrinetz-Elemente Stelle und Transition.
 */
public class PetriNetFigure extends JComponent {
  /**
   * Dies ist eine Referenz auf das PetriNetPanel, in dem die PetriNetFigures
   * dargestellt werden
   */
  private PetriNetPanel panel;

  /**
   * In der ArrayList werden die von der Figur abgehenden Kanten-Objekte
   * gespeichert.
   */
  ArrayList<Kante> arrowsFrom = new ArrayList<Kante>();

  /**
   * In der ArrayList werden die zu der Figur hinführenden Kanten-Objekte
   * gespeichert.
   */
  ArrayList<Kante> arrowsTo = new ArrayList<Kante>();

  /**
   * In der ArrayList werden die Petrinetz-Elemente gespeichert, von denen eine
   * Kante zu dieser Figur führt.
   */
  ArrayList<PetriNetFigure> connectionsFrom = new ArrayList<PetriNetFigure>();

  /**
   * In der ArrayList werden die Petrinetz-Elemente gespeichert, zu denen eine
   * Kante von dieser Figur führt.
   */
  ArrayList<PetriNetFigure> connectionsTo = new ArrayList<PetriNetFigure>();

  /**
   * Dies ist eine Referenz auf das RectangularShape aus java.awt.geom.
   */
  private RectangularShape rectangularShape;

  /**
   * Diese Variable enthält die Farbe der PetriNetFigure
   */
  private Color color;

  /**
   * Dies ist eine Variable, die den Text der Beschriftung der PetriNetFigure
   * enthält. Sie ist mit einem leeren String vorbelegt.
   */
  private String labelText = "";

  /**
   * Diese Variable enthält die X-Position der Beschriftung der PetriNetFigure
   */
  int labelPositionX;

  /**
   * Diese Variable enthält die Y-Position der Beschriftung der PetriNetFigure
   */
  int labelPositionY;

  /**
   * Der Konstruktor der Klasse PetriNetFigure erzeugt ein neues Objekt dieser
   * Klasse. Dieser Konstruktor ist nur zu verwenden, wenn die PetriNetFigure
   * tatsächlich in einem Petri-Netz (PetriNetPanel) dargestellt werden muss.
   * 
   * Für die Zeichnung in anderen Panels besteht ein gesonderter Konstruktor.
   * 
   * @param panel
   *          Das PetriNetPanel, auf dem die PetriNetFigure dargestellt wird
   * @param x
   *          Die X-Koordinate der PetriNetFigure
   * @param y
   *          Die Y-Koordinate der PetriNetFigure
   * @param width
   *          Die Breite der PetriNetFigure
   * @param height
   *          Die Höhe der PetriNetFigure
   * @param createdByUser
   *          Flag, ob die PetriNetFigure durch Zeichnen des Anwenders entstand
   *          oder z. B. durch Laden einer Datei. Diese Untescheidung ist für
   *          die korrekte Interpretation der Positions-Angaben erforderlich
   * 
   *          true = Die Positionsangaben beziehen sich auf den Mittelpunkt der
   *          Figur; false = Die Positionsangaben beziehen sich auf die linke
   *          obere Ecke
   */
  PetriNetFigure(PetriNetPanel panel, int x, int y, int width, int height,
      boolean createdByUser) {
    super.setSize(width, height);
    this.panel = panel;

    // Position der Figur festlegen
    setLocation(x, y, createdByUser);
    // Als RectangularShape zunächst den umgebenden Rahmen verwenden
    setRectangularShape(getBounds());

    // Die Koordinaten des Labels festlegen

    /*
     * Da die Namen der Figuren links neben der Figur stehen, muss für die
     * Feststellung der kleinsten X-Koordinate das kleinste X der Label-
     * positionen herangezogen werden.
     * 
     * Nachdem in der Grundstellung kein Labeltext existiert, erfolgt anfangs
     * die Voreinstellung dieser Position mit der aktuellen Position der Figur.
     */
    setLabelCoordinates(this.getX(), this.getY());

    // Listener hinzufügen (MouseListener und MouseMotionListener)
    addListenersToComponent();
  }

  /**
   * Der Konstruktor der Klasse PetriNetFigure erzeugt ein neues Objekt dieser
   * Klasse. Dieser Konstruktur wurde das Größenänderungs-Fenster (Klasse
   * SizeFram) erstellt und wird nur dort verwendet. Hier erfolgt insbesondere
   * kein Hinzufügen von Listenern.
   * 
   * @param panel
   *          Das JPanel, auf dem die PetriNetFigure dargestellt wird
   * @param x
   *          Die X-Koordinate der PetriNetFigure
   * @param y
   *          Die Y-Koordinate der PetriNetFigure
   * @param width
   *          Die Breite der PetriNetFigure
   * @param height
   *          Die Höhe der PetriNetFigure
   */
  PetriNetFigure(JPanel panel, int x, int y, int width, int height) {
    super.setSize(width, height);
    super.setLocation(x, y);
  }

  /**
   * Diese Methode legt die Position der Beschriftung auf dem JPanel fest.
   * 
   * @param x
   *          Die X-Koordinate der Beschriftung
   * @param y
   *          Die Y-Koordinate der Beschriftung
   */
  private void setLabelCoordinates(int x, int y) {
    this.labelPositionX = x;
    this.labelPositionY = y;
  }

  /**
   * Diese Methode stattet die PetriNetFigure mit einem MouseListener und einem
   * MouseMotionListener aus.
   */
  private void addListenersToComponent() {
    addMouseListener(new MouseListenerForPetriNetFigures(panel, this));
    addMouseMotionListener(new MouseListenerForPetriNetFigures(panel, this));
  }

  /**
   * Die Methode paintComponent überschreibt die entsprechende Methode der
   * Superklasse JComponent. Die Methode wird aufgerufen, wenn die
   * PetriNetFigure auf dem Panel gezeichnet wird.
   */
  @Override
  protected void paintComponent(Graphics g2d) {
    // Neben dem Superklassen-Aufruf dieser Methode
    super.paintComponent(g2d);
    // als Zeichenfarbe die Farbe dieser PetriNetFigure verwenden
    g2d.setColor(color);
  }

  /**
   * Diese Methode schreibt die Bezeichnung der PetriNetFigure auf das JPanel.
   * 
   * @param g2d
   *          Der Grafikkontext, in dem der Text geschrieben wird
   */
  void writeLabelText(Graphics g2d) {
    if (labelText == null || labelText.isEmpty()) {
      // auch wenn kein Text vorhanden ist, muss für die Labelposition
      // eine Voreinstellung genommen werden, da diese für das Scrollen
      // nach rechts zur Feststellung des kleinsten X im Panel herangezogen wird
      labelPositionX = this.getX();
      labelPositionY = this.getY();
    } else {
      // Aktuellen Font sichern
      Font oldFont = g2d.getFont();
      // Rechteck, in das der Bezeichnungstext mit dem Font passt, holen
      Rectangle2D r = getRectangleAndSetFont(g2d, labelText, oldFont, false);

      // Text so positionieren, dass das Ende des Textes 5 Pixel links von
      // der Figur erscheint.
      labelPositionX = this.getX() - ((int) r.getWidth() + 5);
      /*
       * Die Y-Position bei Graphics.drawString() bezieht sich auf das untere
       * Ende des Textes (die baseline). Daher wird für die Y-Position folgende
       * Formel verwendet: Y-Position der PetriNetFigure + halbe Höhe (auf
       * diesem Y ist dann der Mittelpunkt der PetriNetFigure) + Hälfte der
       * halben Font-Höhe + 1, da die eine Fonthälfte über der Mittelline liegen
       * soll, ein Punkt auf der Mittellinie und vom Rest nur noch die Hälfte
       * addiert werden muss.
       * 
       * Experimente mit verschiedenen Größen haben jedenfalls zu passablen
       * Ergebnissen geführt.
       */
      labelPositionY = (int) (this.getY() + this.getHeight() / 2 + (g2d
          .getFont().getSize() / 2 + 1) / 2);

      // Als Textfarbe die Farbe der PetriNetFigure verwenden
      g2d.setColor(color);
      // Text schreiben
      g2d.drawString(labelText, labelPositionX, labelPositionY);
      // Alten Font wieder verwenden
      g2d.setFont(oldFont);
    }
  }

  /**
   * Diese Methode ermittelt das Rechteck, in das der übergebene Text passt und
   * gibt dieses zurück.
   * 
   * @param g2d
   *          Der Grafikkontext, in dem der Text geschrieben wird
   * @param text
   *          Der zu schreibende Text
   * @param oldFont
   *          Der aktuell verwendete Font
   * @param bold
   *          Angabe, ob im Fettdruck geschrieben werden soll (true) oder nicht
   *          (false)
   * 
   * @return Das Rechteck in der übergebene Text genau reinpasst
   */
  Rectangle2D getRectangleAndSetFont(Graphics g2d, String text, Font oldFont,
      boolean bold) {

    // Temporären Font erstellen; Größe = 40 % der Höhe der Stelle (wie der
    // Punkt bei einem Token)
    Font tempFont = oldFont.deriveFont((float) (this.getHeight() * 0.4));
    // Wenn auch Fettdruck gewünscht ist, ...
    if (bold) tempFont.deriveFont(Font.BOLD);
    // Temporären Font verwenden
    g2d.setFont(tempFont);
    // Textausmessungen holen und damit das das Rechteck, in dem der Text stehen
    // würde ...
    FontRenderContext fontRenderContext = g2d.getFontMetrics()
        .getFontRenderContext();
    // ... an Hand des übergebenen Textes ermitteln und zurückgeben
    return tempFont.getStringBounds(text, fontRenderContext);
  }

  /**
   * Diese Methode setzt die Koordinaten der PetriNetFigure, in Abhängigkeit
   * davon, ob die Figur durch den Anwender gezeichnet wurde oder durch
   * Hochladen einer Datei entstanden ist.
   * 
   * @param x
   *          Die übermittelte X-Koordinate
   * @param y
   *          Die übermittelte Y-Koordinate
   * @param createdByUser
   *          Flag, ob die PetriNetFigure durch Zeichnen des Anwenders entstand
   *          oder z. B. durch Laden einer Datei. Diese Untescheidung ist für
   *          die korrekte Interpretation der Positions-Angaben erforderlich
   * 
   *          true = Die Positionsangaben beziehen sich auf den Mittelpunkt der
   *          Figur; false = Die Positionsangaben beziehen sich auf die linke
   *          obere Ecke
   */
  private void setLocation(int x, int y, boolean createdByUser) {
    // Wenn durch Anwender gezeichnet
    if (createdByUser) {
      // Die tatsächliche Position aus der Breite und Höhe ermitteln:
      // (Mittelpunkt minus halbe Breite der Figure ist die tatsächliche
      // Position
      super.setLocation(x - this.getWidth() / 2, y - this.getHeight() / 2);
    } else {
      // Ansonsten können die Angaben einfach übernommen werden
      super.setLocation(x, y);
    }
  }

  /**
   * Diese Methode gibt den LabelText der PetriNetFigure zurück.
   * 
   * @return Der LabelText der PetriNetFigure
   */
  public String getLabelText() {
    return labelText;
  }

  /**
   * Diese Methode legt den LabelText der PetriNetFigure fest.
   * 
   * @param labelText
   *          Der LabelText der PetriNetFigure
   */
  public void setLabelText(String labelText) {
    this.labelText = labelText;
  }

  /**
   * Diese Methode legt die Farbe der PetriNetFigure fest.
   * 
   * @param color
   *          Die Farbe der PetriNetFigure
   */
  void setColor(Color color) {
    this.color = color;
  }

  /**
   * Diese Methode gibt das RectangularShape der PetriNetFigure zurück.
   * 
   * @return Das RectangularShape der PetriNetFigure
   */
  RectangularShape getRectangularShape() {
    return this.rectangularShape;
  }

  /**
   * Diese Methode legt das RectangularShape der PetriNetFigure fest.
   * 
   * @param rectangularShape
   *          Das RectangularShape der PertiNetFigure
   */
  void setRectangularShape(RectangularShape rectangularShape) {
    this.rectangularShape = rectangularShape;
  }

  /**
   * Diese Methode gibt die ID der PetriNetFigure zurück.
   * 
   * @return Die ID der PetriNetFigure
   */
  public String getID() {
    return this instanceof Stelle ? ((Stelle) this).getPlaceID()
        : ((Transition) this).getTransitionID();
  }
}
