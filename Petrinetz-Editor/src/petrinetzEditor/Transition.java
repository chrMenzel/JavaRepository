package petrinetzEditor;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

/**
 * Die Klasse Transition repräsentiert die Transitionen eines Petri-Netzes
 */
public class Transition extends PetriNetFigure {
  /**
   * Dies ist eine Referenz auf die ID der Transition als Zahl.
   */
  static int id;

  /**
   * Dies ist eine Referenz auf den Menüpunkt im Kontextmenü, mit dem der
   * Anwender eine Simulation dieser Transition durchführen kann.
   */
  private String transitionID;

  /**
   * Dies ist eine Referenz auf den Menüpunkt zur Simulation einer Transition.
   */
  private JMenuItem menuSimulate;

  /**
   * Der Konstruktor der Klasse Transition erzeugt ein neues Objekt dieser
   * Klasse. Dieser Konstruktor wird für den Fall eingesetzt, dass die
   * Transition tatsächlich in ein Petri-Netz eingefügt wird. Dies ist beim
   * Größenänderungs-Fenster nicht der Fall, weswegen dafür ein weiterer
   * Konstruktor besteht.
   * 
   * @param panel
   *          Das PetriNetPanel, in dem die Transition gezeichnet wird
   * @param x
   *          Die X-Koordinate der Transition
   * @param y
   *          Die Y-Koordinate der Transition
   * @param width
   *          Die Breite der Transition
   * @param height
   *          Die Höhe der Transition
   * @param createdByUser
   *          Flag, ob die Transition vom Anwender aktiv gezeichnet wurde true,
   *          wenn die Transition vom Anwender gezeichnet wurde false, wenn die
   *          Transition durch Laden einer Datei erstellt wurde
   */
  Transition(PetriNetPanel panel, int x, int y, int width, int height,
      boolean createdByUser) {
    super(panel, x, y, width, height, createdByUser);
  }

  /**
   * Der Konstruktor der Klasse Transition erzeugt ein neues Objekt dieser
   * Klasse. Dieser weitere Konstruktor wird für den Fall eingesetzt, dass die
   * Transition nicht in das eigentliche Petri-Netz eingefügt wird, sondern
   * einem anderen Zweck (hier: Zur Veranschaulichung einer Größenänderung)
   * dient.
   * 
   * @param panel
   *          Das PetriNetPanel, in dem die Stelle gezeichnet wird
   * @param x
   *          Die X-Koordinate der Transition
   * @param y
   *          Die Y-Koordinate der Transition
   * @param width
   *          Die Breite der Transition
   * @param height
   *          Die Höhe der Transition
   */
  Transition(JPanel panel, int x, int y, int width, int height) {
    super(panel, x, y, width, height);
  }

  /**
   * Diese Methode dient dem Zeichnen der Transition. Sie überschreibt die
   * Methode paintComponent() der Superklasse PetriNetFigure.
   * 
   * @param g
   *          Der Grafikkontext, in dem die Transition gezeichnet wird
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
    RectangularShape rectShape = new Rectangle2D.Double(0, 0, getWidth() - 1,
        getHeight() - 1);
    g2d.draw(rectShape);
    // Das RectangularShape sichern, da es für die Berechnung der Pfeile
    // benötigt wird
    this.setRectangularShape(rectShape);
  }

  /**
   * Diese Methode gibt den Menüpunkt zurück, mit dem der Anwender die
   * Simulation der Transaktion durchführen kann.
   * 
   * @return Der Menüpunkt im Kontextmenü der Transaktion, mit dem die
   *         Simulation durchgeführt wird.
   */
  JMenuItem getMenuSimulate() {
    return menuSimulate;
  }

  /**
   * Diese Methode setzt fest, welcher Menüpunkt der Menüpunkt zum Simulieren
   * der Transaktion ist.
   * 
   * @param menuSimulate
   *          Der Menüpunkt, mit dem die Simulation durchgeführt werden kann
   */
  void setMenuSimulate(JMenuItem menuSimulate) {
    this.menuSimulate = menuSimulate;
  }

  /**
   * Diese Methode gibt zurück, ob die Transition aktiviert ist.
   * 
   * @return true, wenn die Transition aktiviert ist, ansonsten false
   */
  boolean isActivated() {
    // Eine Transition ist aktiviert, wenn jede Eingangsstelle
    // mindestens ein Token enthält.

    // Lokale Variable für eine Stelle deklarieren
    Stelle s;
    // Über alle PetriNetFigures der verbundenen Stellen, die zu dieser
    // Transition führen, iterieren
    for (PetriNetFigure f : this.connectionsFrom) {
      s = (Stelle) f;
      // Wenn eine Stelle keine Token enthält ...
      if (s.getNumberOfTokens() == 0) {
        // Das Menü zur Simulation der Transaktion deaktivieren
        menuSimulate.setEnabled(false);
        // Zurückgeben, dass die Transition nicht aktiviert ist
        return false;
      }
    }

    // Wenn der Code bis hierhin gekommen ist, ist die Transition aktiviert
    menuSimulate.setEnabled(true);
    return true;
  }

  /**
   * Diese Methode buildAndSetTransitionID ist eine überladene Methode. In
   * dieser Methode ohne Parameter wird die ID der Transition durch die
   * Klassenvariable id festgesetzt. Dies erfolgt dann, wenn der Anwender die
   * Transition gezeichnet hat. Beim Laden einer Datei wird die andere Methode
   * buildAndSetTransitionID verwendet.
   */
  void buildAndSetID() {
    // ID der Transition festsetzen und die Klassenvariable id um 1
    // inkrementieren
    setTransitionID(id++);
  }

  /**
   * Diese Methode buildAndSetTransitionID ist eine überladene Methode. In
   * dieser Methode mit Parameter wird die ID der Transition durch den
   * übergebenen Parameter festgesetzt. Dies erfolgt dann, eine Datei geladen
   * wird. Wenn der Anwender die Transition selbst zeichnet, wird die andere
   * Methode buildAndSetTransitionID verwendet.
   * 
   * @param idFromFile
   *          Die ID der Transition, die in der Datei gespeichert ist
   */
  void buildAndSetTransitionID(String idFromFile) {
    this.transitionID = idFromFile;
  }

  /**
   * Diese Methode wird von der Methode buildAndSetTransitionID() dieser Klasse
   * aufgerufen. Sie legt die ID der Transition nach dem Muster
   * "transition" + number fest.
   * 
   * @param number
   *          Die übergebene Zahl als Teil der ID
   */
  void setTransitionID(int number) {
    this.transitionID = "transition" + Integer.toString(number);
  }

  /**
   * Diese Methode gibt die transitionID dieser Transition zurück
   * 
   * @return Die transitionID dieser Transition
   */
  String getTransitionID() {
    return transitionID;
  }
}