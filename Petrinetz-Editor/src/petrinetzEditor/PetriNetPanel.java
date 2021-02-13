package petrinetzEditor;

import interfaces.IScroller;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

/**
 * Diese Klasse ist die Zeichenfläche des Petri-Netzes. Hier finden alle
 * Zeichenoperationen statt.
 */
public final class PetriNetPanel extends JPanel implements ActionListener {
  /**
   * Dies ist eine Konstante, die angibt, welche Entfernung in Pixel zwischen
   * Mausposition und Kante maximal sein darf, um als vom Anwender angesprochene
   * Kante zu gelten.
   * 
   * Sie wird auf 3.0 Pixel festgelegt.
   */
  private final double MIN_DISTANCE_MOUSE_TO_ARROW = 3.0;

  /**
   * Diese Variable enthält die Breite der Elemente des Petri-Netzes in Pixel.
   * Zum Start der Anwendung ist sie auf 30 festgelegt.
   */
  private int widthOfFigures = 30;

  /**
   * Diese Variable enthält die Höhe der Elemente des Petri-Netzes in Pixel. Zum
   * Start der Anwendung ist sie auf 30 festgelegt.
   */
  private int heightOfFigures = 30;

  /**
   * Diese Variable enthält die Länge der Pfeilspitzen in Pixel. Zum Start der
   * Anwendung ist sie auf 10 (ein Drittel der Breite der Petrinetz-Elemente)
   * festgelegt.
   */
  private int arrowheadLength = widthOfFigures / 3;

  /**
   * Dies ist eine Referenz auf das Color-Objekt, das die Farbwerte für eine
   * aktivierte Transition enthält.
   */
  Color colorActivated = new Color(0, 176, 80);

  /**
   * Dies ist eine Referenz auf das CalculationsWithArrows-Objekt (Klasse dieser
   * Anwendung). Es ist das einzige Objekt dieser Klasse in dieser Anwendung.
   */
  private CalculationsWithArrows calculationsWithArrows = new CalculationsWithArrows();

  /**
   * Dies ist eine Referenz auf ein Kontextmenü, das als PopupMenu definiert
   * ist.
   */
  private PopupMenu popupMenu = new PopupMenu();

  /**
   * Dies ist eine Referenz auf das SizeFrame-Objekt (Klasse dieser Anwendung).
   * Es handelt sich dabei um das Fenster zur Veränderung der Größen der
   * Elemente und ist das einzige Objekt dieser Klasse in dieser Anwendung.
   */
  private SizeFrame sizeFrame = new SizeFrame(this, widthOfFigures,
      heightOfFigures);

  /**
   * Dies ist eine Referenz auf den aktuellen Status des PetriNetPanels. Sie ist
   * von zentraler Bedeutung für die Steuerung der Anwendung, da abhänging vom
   * Status die weiteren Aktionen (z. B. Was wird gezeichnet? Welches Fenster
   * öffnet sich?) erfolgen. Die möglichen Status sind in der Enum Statuses
   * aufgeführt.
   */
  private Statuses panelStatus;

  /**
   * Dies ist eine Referenz auf die zuletzt hinzugefügte/verschobene
   * PetriNetFigure. Sie wird zur Überprüfung, ob zu scrollen ist, verwendet.
   */
  private PetriNetFigure lastManipulatedFigure;

  /**
   * Dies ist eine Referenz auf eine neu gezeichnete Kante im PetriNetPanel.
   */
  private Kante newKante;

  /**
   * Diese Referenz beinhaltet alle Elemente des PetriNetPanels (Stellen und
   * Transitionen, nicht deren Kanten) in einer ArrayList.
   */
  private ArrayList<PetriNetFigure> figures = new ArrayList<PetriNetFigure>();

  /**
   * Diese Referenz beinhaltet alle Kanten des PetriNetPanels in einer
   * ArrayList.
   */
  private ArrayList<Kante> kanten = new ArrayList<Kante>();

  /**
   * Diese Variable beinhaltet die X-Koordinate der aktuellen Mausposition
   */
  private int currentMousePositionX;

  /**
   * Diese Variable beinhaltet die Y-Koordinate der aktuellen Mausposition
   */
  private int currentMousePositionY;

  /**
   * Diese Variable beinhaltet die maximale X-Koordinate der Elemente des
   * PetriNetPanels.
   */
  private int maxXCoordinate;

  /**
   * Diese Variable beinhaltet die maximale Y-Koordinate der Elemente des
   * PetriNetPanels.
   */
  private int maxYCoordinate;

  /**
   * Diese Variable beinhaltet die minimale X-Koordinate der Elemente des
   * PetriNetPanels.
   */
  private int minXCoordinate;

  /**
   * Diese Variable beinhaltet die minimale Y-Koordinate der Elemente des
   * PetriNetPanels.
   */
  private int minYCoordinate;

  /**
   * Dies ist eine Referenz auf das Petrinetz-Element, bei dem das Kontextmenü
   * angezeigt wird.
   */
  private PetriNetFigure popupAtFigure;

  /**
   * Dies ist eine Referenz auf die Kante, bei der das Kontextmenü angezeigt
   * wird.
   */
  private Kante popupAtArrow;

  /**
   * Dies ist eine Referenz auf die Kante, die der aktuellen Mausposition am
   * nächsten ist.
   */
  private Kante mouseIsNearArrow;

  /**
   * Dies ist eine Referenz auf den Mittelpunkt des beim Zeichnen einer Kante
   * zuerst angeklickten Elements.
   */
  private Point centerOfClickedShape1;

  /**
   * Dies ist eine Referenz auf den Mittelpunkt des beim Zeichnen einer Kante
   * zuletzt angeklickten Elements.
   */
  private Point centerOfClickedShape2;

  /**
   * Dies ist eine Referenz auf das Shape des beim Zeichnen einer Kante
   * angeklickten Elements.
   */
  private RectangularShape clickedShape;

  /**
   * Dies ist eine Referenz auf das Shape des beim Zeichnen einer Kante zuerst
   * angeklickten Elements (Startelement des Pfeils).
   */
  private RectangularShape clickedShape1;

  /**
   * Dies ist eine Referenz auf das Shape des beim Zeichnen einer Kante zuletzt
   * angeklickten Elements (Zielelement des Pfeils).
   */
  private RectangularShape clickedShape2;

  /**
   * Dies ist eine Referenz auf die PetriNetFigure des beim Zeichnen einer Kante
   * angeklickten Elements.
   */
  private PetriNetFigure clickedFigure;

  /**
   * Dies ist eine Referenz auf die PetriNetFigure des beim Zeichnen einer Kante
   * zuerst angeklickten Elements.
   */
  private PetriNetFigure clickedFigure1;
  /**
   * Dies ist eine Referenz auf die PetriNetFigure des beim Zeichnen einer Kante
   * zuletzt angeklickten Elements.
   */
  private PetriNetFigure clickedFigure2;

  /**
   * Dies ist eine Point2D.Double-Referenz auf den Schnittpunkt der Kante mit
   * der Startfigur des Pfeils.
   */
  Point2D.Double interceptionPointStart = new Point2D.Double();

  /**
   * Dies ist eine Point2D.Double-Referenz auf den Schnittpunkt der Kante mit
   * der Zielfigur des Pfeils.
   */
  Point2D.Double interceptionPointEnd = new Point2D.Double();

  /**
   * Dies ist eine Referenz auf die PetriNetFigure, die sich unter dem Maus-
   * zeiger befindet.
   */
  private PetriNetFigure currentFigureUnderMouse;

  /**
   * Dies ist eine Variable für die X-Koordinate des Mausklicks vor dem
   * Verschieben von Petrinetz-Elementen.
   */
  private int clickBeforeMovingX;

  /**
   * Dies ist eine Variable für die Y-Koordinate des Mausklicks vor dem
   * Verschieben von Petrinetz-Elementen.
   */
  private int clickBeforeMovingY;

  /**
   * Diese HashSet-Referenz beinhaltet die PetriNetFigures, die verschoben oder
   * gemeinsam gelöscht werden sollen.
   */
  private HashSet<PetriNetFigure> hashSetForFigures = new HashSet<PetriNetFigure>();

  /**
   * Diese HashSet-Referenz beinhaltet die Kanten, die durch Auswählen der
   * PetriNetFigure vom Verschiebe-Prozess betroffen sind. Es handelt sich also
   * um die Kanten, die von den ausgewählten PetriNetFigures abgehen oder zu
   * ihnen hinführen.
   */
  private HashSet<Kante> hashSetForMovingArrows = new HashSet<Kante>();

  /**
   * Diese HashSet-Referenz beinhaltet die Kanten, die durch explizites
   * Auswählen des Anwenders vom Verschiebe-Prozess betroffen sind. Nicht darin
   * enthalten sind Kanten, die von einer ausgewählten PetriNetFigure abgehen
   * oder zu ihr hinführen.
   */
  private HashSet<Kante> hashSetForExplicitelyClickedArrows = new HashSet<Kante>();

  /**
   * Diese Variable gibt an, ob die CTRL-/Strg-Taste gedrückt wird (true) oder
   * nicht (false)
   */
  public boolean ctrlPressed;

  /**
   * Diese Variable gibt an, ob die Shift-Taste gedrückt wird (true) oder nicht
   * (false)
   */
  public boolean shiftPressed;

  /**
   * Diese Variable gibt an, das unter lastManipulatedFigure gespeicherte
   * Element gerade umbenannt wurde (true) oder nicht (false).
   */
  public boolean isNamedJustNow;

  /**
   * Diese Variable gibt an, ob gerade eines oder mehrere Elemente verschoben
   * werden (true) oder nicht (false).
   */
  private boolean someElementsAreMoving;

  /**
   * Dies ist eine Referenz zum Interface IScroller. Diese Referenz wird durch
   * die Methode setScroller() in der Klasse MainFrame initialisiert.
   */
  private IScroller scroller;

  /**
   * Dies ist eine Referenz zum Interface IScroller. Diese Referenz wird durch
   * die Methode setScroller() initialisiert.
   */
  private boolean shouldBeSaved = false;

  /**
   * Der Konstruktor der Klasse PetriNetPanel erstellt ein neues Objekt der
   * Klasse PetriNetPanel.
   */
  PetriNetPanel() {
    // Anfangs-Status des Panels setzen
    this.panelStatus = Statuses.START;
    // keinen Layout-Manager verwenden um die Komponenten nach
    // Koordinaten zu platzieren.
    this.setLayout(null);

    // MouseListener für Maus-Klicks und -Bewegungen einrichten
    MouseListenerForPanel mouseAdapter = new MouseListenerForPanel(this);
    addMouseListener(mouseAdapter);
    addMouseMotionListener(mouseAdapter);

    // PopupListener für die Kanten einrichten
    PopupMenu popupMenuPanel = new PopupMenu();
    popupMenuPanel.buildAndAddPopupMenuForArrows(this);

    // KeyListener für die Überwachung des Anklickens von mehreren Elementen
    // einrichten
    this.addKeyListener(new PanelKeyListener(this));
  }

  /**
   * In der Methode paintComponent(g) wird das Aussehen des Petri-Netzes
   * bestimmt. Damit werden alle Elemente des Petrinetzes gezeichnet.
   * 
   * @param g
   *          Der Grafikkontext des PetriNetPanel (seine Zeichenfläche)
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    // Hintergrund zeichnen
    paintBackground(g2d);
    // Schönere Linien zeichnen, keine Pixelstruktur
    activateAntialiasingForSmootherLines(g2d);

    // Alle Elemente zeichnen
    paintAllElements(g2d);
    // Evtl. neuen Pfeil zeichnen
    maybePaintNewArrow(g2d);
    // Evtl. die Scrollbalken und die Panelbreite anpassen
    maybeAdjustScrollPanes(); 
  }

  /**
   * Diese Methode zeichnet über die gesamte Fläche des Panels ein gefülltes
   * weißes Rechteck als Hintergrund
   * 
   * @param g2d
   *          Das Graphics2D, der Graphics-Konext des Panels
   */
  private void paintBackground(Graphics2D g2d) {
    g2d.setColor(Color.white);
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
    g2d.setColor(Color.black);
  }

  /**
   * Diese Methode löscht alle vorhandenen Elemente des Petri-Netzes. Der Aufruf
   * dieser Methode erfolgt beim Laden einer neuen Datei.
   */
  public void clearData() {
    for (int i = figures.size(); i > 0; i--) {
      deletePetriNetFigureWithAllConnectedArrows(figures.get(i - 1));
    }

    this.setSize(new Dimension(0, 0));
    scroller.repaintScrollPane();
  }

  /**
   * Diese Methode sorgt dafür, dass insbesondere die Stellen (Kreise) eine
   * schöne runde Form und keine sichtbare Pixel-Struktur aufweisen.
   * 
   * @param g2d
   *          Das Graphics2D, der Graphics-Konext des Panels
   */
  private void activateAntialiasingForSmootherLines(Graphics2D g2d) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
  }

  /**
   * Mit dieser Methode werden alle Stellen, Transitionen und Kanten des
   * Petri-Netzes neu gezeichnet. Sie wird von PaintComponents() aufgerufen.
   * 
   * @param g2d
   *          Das Graphics2D, der Graphics-Konext des Panels
   */
  private void paintAllElements(Graphics2D g2d) {
    // Alle Elemente neu zeichnen
    for (PetriNetFigure f : figures) {
      // Größe des Elements setzen
      f.setSize(widthOfFigures, heightOfFigures);

      // Wenn das Element noch nicht gezeichnet wurde, hat es noch kein
      // RectangularShape. Dieses wird jedoch benötigt, um zu bestimmen,
      // ob sich der Mauszeiger über einem Element befindet oder nicht.
      // (je nachdem wird das Element in einer anderen Farbe gezeichnet).
      if (f.getRectangularShape() == null) {
        // Ohne RectangularShape Element schwarz zeichnen
        f.setColor(Color.black);
      } else {
        // Mit RectangularShape, das Element wurde also schon gezeichnet
        // Hier sind verschiedene Konstellationen zu unterscheiden, die
        // nachfolgend durchlaufen werden.

        // Wenn die Maus über der aktuell betrachteten PetriNetFigure ist ...
        if (f == currentFigureUnderMouse) {
          // ... und die Maus-Position in dem Symbol der PetriNetFigure liegt
          if (f.getRectangularShape().contains(currentMousePositionX,
              currentMousePositionY)) {
            // Mauszeiger ist über dem Element
            if (panelStatus == Statuses.KANTE) {
              // Status Pfeil wird gezeichnet
              if (clickedFigure1 == null && clickedFigure2 == null) {
                // Es gibt noch keine relevante Figur für den Pfeil
                f.setColor(Color.gray);
              }
              // Die Ausgangs-Figur des Pfeils steht fest (wurde angeklickt)
              if (clickedFigure1 != null && clickedFigure2 == null) {
                // Wenn die Verbindung zulässig ist, Figur blau zeichnen, sonst
                // schwarz
                f.setColor(isAllowedConnection(clickedFigure1, f) ? Color.blue
                    : Color.black);
                // Die Ausgangsfigur des Pfeils auf jeden Fall blau zeichen
                clickedFigure1.setColor(Color.blue);
              }

              // Wenn ein einziges Token eingefügt werden soll, die Figur
              // orange zeichnen
            } else if (panelStatus == Statuses.TOKEN) {
              if (f instanceof Stelle) {
                f.setColor(Color.orange);
              }

              // Wenn das Element verschoben werden soll, die Figur in magenta
              // zeichnen
            } else if (panelStatus == Statuses.MOVINGFIGURES) {
              f.setColor(Color.magenta);

            } else {
              // kein relevanter Status, also schwarz zeichnen
              f.setColor(Color.black);
            }
          } else {
            // Mauszeiger ist über Component, aber nicht innerhalb der Figur
            if (hashSetForFigures.contains(f)) {
              f.setColor(Color.magenta);
            } else {
              // Sofern keine aktivierte Transition
              if ((f instanceof Transition) && ((Transition) f).isActivated()) {
                f.setColor(colorActivated);
              } else {
                f.setColor(Color.black);
              }
            }
            if (clickedFigure1 != null) clickedFigure1.setColor(Color.blue);
          }

          // Mauszeiger ist nicht über dem aktuell betrachteten Element
          // Beim Pfeilzeichnen wurde aber eine Figur angeklickt
        } else if (panelStatus == Statuses.KANTE && clickedFigure1 != null
            && clickedFigure2 == null) {
          f.setColor(Color.black);
          clickedFigure1.setColor(Color.blue);

          // Beim Markieren wurde die Figur angeklickt
        } else if (hashSetForFigures.contains(f)) {
          f.setColor(Color.magenta);
          // Es ist eine aktivierte Transition
        } else {
          f.setColor(Color.black);
        }
      }
      // Eine aktivierte Transition grün zeichnen
      if (f instanceof Transition) {
        if (((Transition) f).isActivated()) {
          // Aber auch wenn aktiviert, nur grün zeichnen, wenn
          // - der Status entweder das Zeichnen von Stellen/Transitionen ist
          // In allen anderen Status:
          // - die Transition nicht der Beginn des Pfeils ist
          // - die Transition nicht die Figur unter der Maus ist
          // - die Transition nicht zum Verschieben ausgewählt wurde
          if (panelStatus == Statuses.STELLE
              || panelStatus == Statuses.TRANSITION
              || (f != clickedFigure1 && f != currentFigureUnderMouse && !hashSetForFigures
                  .contains(f))) {
            f.setColor(colorActivated);
          }
        }
      }
      // Beschriftung der PetriNetFigure schreiben
      f.writeLabelText(g2d);
    }

    // Pfeile neu berechen (wichtig z. B. wenn eine Größenänderung erfolgte)
    calculateAllArrowsNew();

    // Pfeile färben
    for (Kante k : this.kanten) {
      if (hashSetForExplicitelyClickedArrows.contains(k)
          || (panelStatus == Statuses.MOVINGFIGURES && k
              .equals(mouseIsNearArrow))) {
        k.color = Color.magenta;
      } else {
        k.color = Color.red;
      }

      // und neu zeichnen
      k.paintFigure(g2d);
    }
  }

  /**
   * Wenn der Anwender eine neue Kante (Pfeil) zeichnet, wird das über diese
   * Methode durchgeführt. Solange nur das erste Element angeklickt wurde, folgt
   * der Pfeil der Maus.
   * 
   * Wenn das zweite Element angeklickt wird, wird der Pfeil - sofern die
   * Verbindung zulässig ist - auf einer virtuellen Mittellinie der beiden
   * Elemente gezeichnet.
   * 
   * @param g2d
   *          Das Graphics2D, der Graphics-Konext des Panels
   */
  private void maybePaintNewArrow(Graphics2D g2d) {
    if ((panelStatus == Statuses.KANTE) && (clickedShape != null)) {
      // Die Koordinaten des Mittelpunkts des angeklickten Elements
      // sowie die aktuelle Mausposition in Variablen speichern,
      // damit diese Werte bei den nachfolgenden Prüfungen nicht
      // immer wieder neu geholt werden müssen
      double x1 = centerOfClickedShape1.getX();
      double y1 = centerOfClickedShape1.getY();
      double x2; // lokale Variablen für die Mausposition
      double y2;

      // Wenn das erste Element für den Pfeil angeklickt wurde,
      if (clickedShape2 == null) {
        calculateStartPointOfArrow(x1, y1);
      } else {
        // Das zweite Element wurde angeklickt
        // Um den Zielpunkt des Pfeils zu berechnen, daher in
        // x2 und y2 die Koordinaten des Mittelpunkts dieses Elements übergeben
        x2 = centerOfClickedShape2.getX();
        y2 = centerOfClickedShape2.getY();
        calculationsWithArrows.calculateArrow(this, clickedFigure1,
            clickedFigure2, x1, y1, x2, y2);
      }

      // Wenn ein neues Pfeil-Objekt erstellt wurde, ...
      if (paintArrow(clickedFigure1, clickedFigure2, clickedShape1,
          clickedShape2, "")) {
        // ... den Pfeil tatsächlich zeichnen
        newKante.paintFigure(g2d);
      }
    }
  }

  /**
   * Eine Kante (Pfeil) darf nur gezeichnet werden, wenn es keine gleichartigen
   * PetriNetFigure-Objekte sind (Transition --- Transition; Stelle --- Stelle)
   * Es muss sich also entweder um eine Verbindung von einer Stelle zu einer
   * Transition oder umgekehrt handeln.
   * 
   * Zusätzlich sind keine Mehrfachkanten erlaubt (Newsgroup vom 26.10.2014
   * 19:56 Uhr)
   * 
   * @param figure1
   *          PetriNetFigure, von der der Pfeil startet
   * @param figure2
   *          PetriNetFigure, zu der der Pfeil hinführt
   * 
   * @return Boolean-Wert, der angibt, ob es sich bei der Verbindung um eine
   *         erlaubte Verbindung handelt (true) oder nicht (false).
   */
  private boolean isAllowedConnection(PetriNetFigure figure1,
      PetriNetFigure figure2) {
    return (
    // nicht die gleiche Figur
    (figure1 != figure2) &&
    // von Stelle zu Transition
        ((figure1 instanceof Stelle) && (figure2 instanceof Transition) ||
        // oder von Transition zu Stelle
        (figure1 instanceof Transition) && (figure2 instanceof Stelle)) &&
    // und nicht schon in dieser Richtung verbunden
    !figure1.connectionsTo.contains(figure2));
  }

  /**
   * In dieser Methode wird der Startpunkt für den Pfeil berechnet
   * 
   * @param x1
   *          Die X-Koordinate des Mittelpunkts des angeklickten Startelements
   * @param y1
   *          Die Y-Koordinate des Mittelpunkts des angeklickten Startelements
   */
  private void calculateStartPointOfArrow(double x1, double y1) {
    double x2;
    double y2;
    // x2 und y2 mit der aktuellen Mausposition bestücken
    // Hier ist zu unterscheiden, ob sich die Maus gerade über einer
    // Component oder einer freien Fläche befindet
    if (currentFigureUnderMouse == null) {
      // Mauszeiger ist über der freien Fläche
      x2 = currentMousePositionX;
      y2 = currentMousePositionY;
    } else if (currentFigureUnderMouse.getRectangularShape().contains(
        currentMousePositionX, currentMousePositionY)) {
      // Mauszeiger ist über einer Component und innerhalb der
      // gezeichneten Figur
      x2 = x1;
      y2 = y1;
    } else {
      // Mauszeiger ist über einer Component, aber außerhalb der
      // gezeichneten Figur
      x2 = currentMousePositionX + currentFigureUnderMouse.getX();
      y2 = currentMousePositionY + currentFigureUnderMouse.getY();
    }

    // Je nachdem, welches Element angeklickt wurde, wird der
    // Start des Pfeils auf andere Weise berechnet.

    // Wenn es eine Stelle ist ...
    if (clickedFigure1 instanceof Stelle) {
      // ... den Start der zu zeichnenden Linie an der Stelle bestimmen

      // Zur Berechnung des Pfeilanfangs wird die Methode
      // calculateStartAndEndOfAlineAtCircles der Klasse CalculationsWithArrows
      // herangezogen
      double[] points = calculationsWithArrows
          .calculateStartAndEndOfAlineAtCircles((JPanel) this, clickedFigure1,
              null, x1, y1, x2, y2);

      // Zu diesem Zeitpunkt ist die Methode aber nur für den Startpunkt
      // interessant
      interceptionPointStart = new Point2D.Double(points[0], points[1]);

    } else {
      // In allen anderen Fällen ist es eine Transition
      // Den Start der zu zeichnenden Linie an der Transition bestimmen
      interceptionPointStart = calculationsWithArrows
          .calculateStartOfLineAtRectangle(this, clickedFigure1, null, x1, y1,
              x2, y2, true);
    }

    // Nachdem nur das erste Element angeklickt wurde, den zweiten
    // Punkt mit den Mauskoordinaten gleichsetzen, damit der Pfeil
    // beim Mauszeiger endet

    if (currentFigureUnderMouse != null
        && isAllowedConnection(clickedFigure1, currentFigureUnderMouse)) {
      // Der Mauszeiger befindet sich über einem potenziellen zweiten Element
      // Um den Zielpunkt des Pfeils zu berechnen, daher in
      // x2 und y2 die Koordinaten des Mittelpunkts dieses Elements auf
      // dem Panel übergeben
      RectangularShape shape = getRectangularShapeOfElementUnderMouse();
      x2 = (int) (shape.getCenterX() + currentFigureUnderMouse.getX());
      y2 = (int) (shape.getCenterY() + currentFigureUnderMouse.getY());

      // Punkte des Pfeils berechnen
      calculationsWithArrows.calculateArrow(this, clickedFigure1,
          currentFigureUnderMouse, x1, y1, x2, y2);
    } else {
      interceptionPointEnd.x = x2;
      interceptionPointEnd.y = y2;
    }
  }

  /**
   * Mit dieser Methode wird - sofern alle Voraussetzungen erfüllt sind - der
   * Pfeil dem Petri-Netz hinzugefügt.
   * 
   * @param figure1
   *          Die PetriNetFigure, an der der Pfeil beginnt
   * @param figure2
   *          Die PetriNetFigure, an der der Pfeil endet
   * @param shape1
   *          Das RectangularShape von figure1
   * @param shape2
   *          Das RectangularShape von figure2
   * @param idFromFile
   *          true, wenn die ID aus einer gerade geladenen Datei kommt;
   *          ansonsten false
   * 
   * @return true, wenn der Pfeil auf dem dem PetriNetPanel gezeichnet wurde
   *         false, wenn kein Pfeil gezeichnet wurde
   */
  public boolean paintArrow(PetriNetFigure figure1, PetriNetFigure figure2,
      RectangularShape shape1, RectangularShape shape2, String idFromFile) {
    // Sofern in ein zulässiges Element geklickt wurde, ...
    if (shape1 != null) {
      // Solange der Mauszeiger noch im Start-Element ist, gibt es keinen
      // interceptionPointStart, so das die Methode getX() einen Not a Number-
      // Wert ausgibt
      if (!Double.isNaN(interceptionPointStart.getX())) {
        // ... den Start- und Endpunkt des Pfeils neu bestimmen
        Point startPunkt = new Point((int) interceptionPointStart.getX(),
            (int) interceptionPointStart.getY());
        Point endPunkt = new Point((int) interceptionPointEnd.getX(),
            (int) interceptionPointEnd.getY());

        // Pfeil zeichnen
        newKante = new Kante(figure1, figure2, startPunkt, endPunkt,
            getArrowHeadLength());

        // Wenn die Ziel-Figur des Pfeils bekannt ist
        if (shape2 != null) {
          // ID vergeben
          if (idFromFile.isEmpty()) {
            newKante.setArrowID();
          } else {
            newKante.setArrowID(idFromFile);
          }

          // Kante dem PetriNetPanel hinzufügen
          kanten.add(newKante);
          // Flag zum Speichern setzen
          shouldBeSaved = true;

          // Und die Kante bei den betroffenen PetriNetFigures in die
          // entsprechenden Attribute einpflegen
          figure1.arrowsTo.add(newKante);
          figure1.connectionsTo.add(figure2);
          figure2.arrowsFrom.add(newKante);
          figure2.connectionsFrom.add(figure1);
          makeClickOnElementsUndone();
        }
      }
    }
    // Zurückgeben, ob tatsächlich ein neuer Pfeil gezeichnet wurde
    return !(newKante == null);
  }

  /**
   * Mit dieser Methode wird das automatische Scrollen verwaltet: Je nachdem, ob
   * eine neue PetriNetFigure gezeichnet wurde, die Figur neu benannt wurde,
   * oder die Figuren vom Anwender verschoben werden, wird der sichtbare Bereich
   * des Panels zur Verfügung gestellt.
   */
  private void maybeAdjustScrollPanes() {
    // Eine neue Figur wurde gezeichnet
    if ((panelStatus == Statuses.STELLE || panelStatus == Statuses.TRANSITION)
        && lastManipulatedFigure != null) {
      checkWhereToAdjustPositionAndScroll(false, lastManipulatedFigure);
    }

    // Möglicherweise werden Elemente verschoben
    if (panelStatus == Statuses.MOVINGFIGURES) {
      // Wenn es genau ein Element ist
      if (hashSetForFigures.size() == 1) {
        // Dann ist es das Element, das gerade unter dem Mauszeiger ist
        lastManipulatedFigure = hashSetForFigures.iterator().next();
        // Prüfen, ob die Scroll-Leisten anzupassen sind
        // (true heißt, dass gerade eine Figur bewegt wird)
        checkWhereToAdjustPositionAndScroll(true, lastManipulatedFigure);

      } else if (hashSetForFigures.isEmpty()) {
        // Das Verschieben von einem Element wurde gerade beendet
        // Wenn das Element über eine Grenze hinausragt, ...
        if (minXCoordinate < 0 || maxXCoordinate > this.getWidth()
            || minYCoordinate < 0 || maxYCoordinate > this.getHeight()) {
          // ... die Scroll-Leisten anpassen (false heißt, dass gerade keine
          // Figur bewegt wird).
          checkWhereToAdjustPositionAndScroll(false, lastManipulatedFigure);
        }
      } else if (hashSetForFigures.size() > 1) {
        // Mehrere Elemente werden oder wurden zugleich verschoben
        for (PetriNetFigure f : hashSetForFigures) {
          // Bei allen verschobenen Elementen, prüfen, ob deswegen die
          // Panel-Fläche vergrößert werden muss
          checkWhereToAdjustPositionAndScroll(someElementsAreMoving, f);
        }
      }
    }

    // Zur Sicherheit nochmal prüfen, wenn der Scroll-Vorgang beendet ist,
    // da bei zu schnellem Ruckeln der Maus ohne weiteres Elemente außerhalb
    // des sichbaren Bereichts liegen können!
    if (!someElementsAreMoving) {
      for (PetriNetFigure f : figures) {
        if (f.labelPositionX < 0) scroller.adjustScrollPanesLeft(
            f.labelPositionX, maxXCoordinate, widthOfFigures, true);
        if (f.getY() < 0) scroller.adjustScrollPanesUp(f.getY(),
            maxYCoordinate, widthOfFigures, true);
        if (f.getY() + heightOfFigures > this.getHeight()) scroller
            .adjustScrollPanesDown(f.getY(), heightOfFigures);
        if (f.getX() + widthOfFigures > this.getWidth()) scroller
            .adjustScrollPanesRight(f.getX(), widthOfFigures);
      }
    }
  }

  /**
   * Vorbereitende Methode für das Scrollen: Hier wird zum einen während dem
   * Verschiebe-Vorgang des Anwenders (bolMoving ist true), zum anderen aber
   * auch nach dieser Aktion (bolMoving ist false) geprüft, ob die in figure
   * übergebene PetriNetFigure außerhalb der Fenstergrenzen liegt.
   * 
   * Ist dies der Fall, wird je nach Grenze, die überschritten wurde, eine
   * andere Methode der Klasse Scroller aufgerufen. Dort werden die nicht von
   * der Verschiebung betroffenen Elemente verschoben und der Balken der
   * jeweiligen Scroll-Leiste eingestellt.
   * 
   * @param bolMoving
   *          true, wenn der Anwender die PetriNetFigure gerade verschiebt
   *          false, wenn der Verschiebe-Vorgang des Anwenders beendet ist
   * 
   * @param figure
   *          Die PetriNetFigure, deren Position gerade betrachtet wird
   */
  private void checkWhereToAdjustPositionAndScroll(boolean bolMoving,
      PetriNetFigure figure) {
    // Vergleichs-Werte aus der PetriNetFigure holen, damit
    // sie nicht ständig neu ermittelt werden müssen
    int xFigure = figure.getX();
    int xMaxFigure = xFigure + widthOfFigures;
    int xMinFigure = figure.labelPositionX;
    int yFigure = figure.getY();
    int yMaxFigure = yFigure + heightOfFigures;
    Rectangle visibleRect = this.getVisibleRect();
    boolean visibleRectChanged = false;

    // Wenn ein Element verschoben wird ...
    if (bolMoving) {
      // ... und das Element über den sichtbaren Bereich hinausragt,
      // ist der sichtbare Bereich anzupassen

      // Test für die rechte/linke Fenstergrenze
      if (xMaxFigure > visibleRect.getMaxX()) {
        // rechts wird überschritten
        visibleRect.setLocation(
            (int) (visibleRect.getX() + (xMaxFigure - visibleRect.getMaxX())),
            (int) visibleRect.getY());
        visibleRectChanged = true;
      } else if (xMinFigure < visibleRect.getX()) {
        // links wird überschritten
        visibleRect.setLocation(
            (int) (visibleRect.getX() - (visibleRect.getX() - xMinFigure)),
            (int) visibleRect.getY());
        visibleRectChanged = true;
      }

      // Test für die untere/obere Fenstergrenze
      if (yMaxFigure > visibleRect.getMaxY()) {
        // unten wird überschritten
        visibleRect.setLocation((int) visibleRect.getX(),
            (int) (visibleRect.getY() + (yMaxFigure - visibleRect.getMaxY())));
        visibleRectChanged = true;
      } else if (yFigure < visibleRect.getY()) {
        // oben wird überschritten
        visibleRect.setLocation((int) visibleRect.getX(),
            (int) (visibleRect.getY() - (visibleRect.getY() - yFigure)));
        visibleRectChanged = true;
      }

      if (visibleRectChanged) {
        this.scrollRectToVisible(visibleRect);
        scroller.slowDown();
      }

      // ... und das Element über die Breite des Panels hinausreicht, ...
      if (xMaxFigure >= this.getWidth()) {
        // ... ist die horizontale Scroll-Leiste anzupassen
        scroller.adjustScrollPanesRight(xFigure, widthOfFigures);
        // ... und die größte X-Koordinate neu zu bestimmen
        maxXCoordinate = Math.max(maxXCoordinate, xMaxFigure);
        // ... oder das Element am weitesten links ist, ...
      } else if (xMinFigure < 0) {
        // ggf. die horizontale Scroll-Leiste anpassen
        scroller.adjustScrollPanesLeft(xMinFigure, maxXCoordinate,
            widthOfFigures, false);
        // die kleinste X-Koordinate neu bestimmen
        minXCoordinate = Math.min(minXCoordinate, xMinFigure);
      }

      // ... oder das Element am weitesten unten ist, ...
      if (yMaxFigure > maxYCoordinate) {
        // ggf. die vertikale Scroll-Leiste anpassen
        scroller.adjustScrollPanesDown(yFigure, heightOfFigures);
        // die größte Y-Koordinate neu bestimmen
        maxYCoordinate = Math.max(maxYCoordinate, yMaxFigure);
        // ... oder das Element am weitesten oben ist, ...
      } else if (yFigure < 0) {
        // ggf. die vertikale Scroll-Leiste anpassen
        scroller.adjustScrollPanesUp(yFigure, maxYCoordinate, heightOfFigures,
            false);
        // die kleinste Y-Koordinate neu bestimmen
        minYCoordinate = Math.min(minYCoordinate, yFigure);
      }

      // Wenn eine neue Figur hinzukam oder das Verschieben
      // eines oder mehrerer Elemente gerade beendet wurde ...
    } else {
      if (xFigure == maxXCoordinate) {
        scroller.adjustScrollPanesRight(maxXCoordinate, widthOfFigures);
      } else if (xMinFigure < 0) {
        scroller.adjustScrollPanesLeft(xMinFigure, maxXCoordinate,
            widthOfFigures, true);
      }

      if (yFigure == maxYCoordinate) {
        scroller.adjustScrollPanesDown(maxYCoordinate, heightOfFigures);
      } else if (yFigure == minYCoordinate) {
        scroller.adjustScrollPanesUp(minYCoordinate, maxYCoordinate,
            heightOfFigures, true);
      }
    }
    updateView();
  }

  /**
   * Wenn Elemente über den linken Rand hinaus verschoben werden, werden die
   * übrigen Elemente nach rechts verschoben. Damit bleibt die kleinte
   * X-Koordinate 0. Damit ist die Panel-Fläche auch nach links beliebig
   * erweiterbar, die Verwaltung der Elemente kann aber immer noch über die
   * Koordinate (0, 0) in der linken oberen Ecke des Panels erfolgen.
   * 
   * Diese Methode verschiebt nur die Elemente, die nicht markiert sind, also
   * nicht die Elemente, die vom Anwender verschoben werden.
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Kanten in X-Richtung verschoben
   *          werden
   */
  void moveAllElementsXExceptMovingElements(int pixels) {
    // Zuerst die Elemente des Petri-Netzes verschieben
    for (PetriNetFigure f : figures) {
      // Aber nur, wenn es nicht zu denen gehört, die gerade mit der Maus bewegt
      // werden
      if (!hashSetForFigures.contains(f)) {
        // Position um die Anzahl Pixel nach rechts/links verschieben
        f.setLocation(f.getX() + pixels, f.getY());
        f.labelPositionX += pixels;
      }
    }

    // Anschließend die Pfeile verschieben und Maximum und Minimum anpassen
    calculateAllArrowsNew();
  }

  /**
   * Wenn Elemente über den linken Rand hinaus verschoben werden, werden die
   * übrigen Elemente nach rechts verschoben. Damit bleibt die kleinste
   * X-Koordinate 0. Damit ist die Panel-Fläche auch nach links beliebig
   * erweiterbar, die Verwaltung der Elemente kann aber immer noch über die
   * Koordinate (0, 0) in der linken oberen Ecke des Panels erfolgen.
   * 
   * Diese Methode verschiebt auch die Elemente, die markiert sind, also vom
   * Anwender verschoben wurden.
   * 
   * Hintergrund: Bei Verschiebung von mehreren Elementen kann ein Element beim
   * Loslassen der Maus ohne weiteres im nicht sichtbaren Bereich liegen.
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Kanten in X-Richtung verschoben
   *          werden
   */
  void moveAllElementsXIncludingMovingElements(int pixels) {
    // Zuerst die Elemente des Petri-Netzes verschieben
    for (PetriNetFigure f : figures) {
      // Position um die Anzahl Pixel nach rechts/links verschieben
      f.setLocation(f.getX() + pixels, f.getY());
      f.labelPositionX += pixels;
    }

    // Anschließend die Pfeile verschieben und Maximum und Minimum anpassen
    moveArrowsAndAdjustMinAndMaxX(pixels);
  }

  /**
   * In dieser Methode werden die Pfeile in X-Richtung verschoben und auch die
   * größte und kleinste X-Koordinate der Elemente angepasst
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Kanten in X-Richtung verschoben
   *          werden
   */
  private void moveArrowsAndAdjustMinAndMaxX(int pixels) {
    // Alle Pfeile verschieben
    moveArrowsX(pixels);
    // Das Minimum und Maximum anpassen
    adjustMinAndMaxX(pixels);
  }

  /**
   * Die X-Koordinaten des Start- und Endepunkts der Kanten werden um die
   * übergebene Anzahl pixels erhöht
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Kanten in X-Richtung verschoben
   *          werden
   */
  private void moveArrowsX(int pixels) {
    for (Kante k : kanten) {
      // Nur verschieben, wenn der Pfeil nicht zu denen gehört,
      // die gerade mit der Maus bewegt werden
      if (!hashSetForMovingArrows.contains(k)) {
        k.start.x += pixels;
        k.destination.x += pixels;
      }
    }
  }

  /**
   * Nach dem Verschieben von Figuren haben sich die maximalen und minimalen
   * X-Koordinaten der PetriNetFigures ebenfalls verschoben
   * 
   * Die beiden Variablen minXCoordinate und maxXCoordinate werden hier
   * angepasst.
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die in X-Richtung verschoben wurde
   */
  private void adjustMinAndMaxX(int pixels) {
    // Minimum und Maximum der X-Koordinate anpassen
    if (pixels > 0) {
      maxXCoordinate += pixels;
      minXCoordinate += pixels;
    }
  }

  /**
   * Wenn Elemente über den oberen Rand hinaus verschoben werden, werden die
   * übrigen Elemente nach rechts verschoben. Damit bleibt die kleinste
   * Y-Koordinate 0. Damit ist die Panel-Fläche auch nach oben beliebig
   * erweiterbar, die Verwaltung der Elemente kann aber immer noch über die
   * Koordinate (0, 0) in der linken oberen Ecke des Panels erfolgen.
   * 
   * Diese Methode verschiebt nur die Elemente, die nicht markiert sind, also
   * nicht die Elemente, die vom Anwender verschoben werden.
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Figuren in Y-Richtung verschoben
   *          werden
   */
  void moveAllElementsYExceptMovingElements(int pixels) {
    // Zuerst die Elemente des Petri-Netzes verschieben
    for (PetriNetFigure f : figures) {
      // Aber nur, wenn es nicht zu denen gehört, die gerade mit der Maus bewegt
      // werden
      if (!hashSetForFigures.contains(f)) {
        // Position um die Anzahl Pixel nach rechts/links verschieben
        f.setLocation(f.getX(), f.getY() + pixels);
        f.labelPositionY += pixels;
      }
    }
    // Anschließend die Pfeile verschieben und Maximum und Minimum anpassen
    moveArrowsAndAdjustMinAndMaxY(pixels);
  }

  /**
   * Wenn Elemente über den oberen Rand hinaus verschoben werden, werden die
   * übrigen Elemente nach unten verschoben. Damit bleibt die kleinste
   * Y-Koordinate 0. Damit ist die Panel-Fläche auch nach oben beliebig
   * erweiterbar, die Verwaltung der Elemente kann aber immer noch über die
   * Koordinate (0, 0) in der linken oberen Ecke des Panels erfolgen.
   * 
   * Diese Methode verschiebt nur die Elemente, die nicht markiert sind, also
   * nicht die Elemente, die vom Anwender verschoben werden.
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Figuren in Y-Richtung verschoben
   *          werden
   */
  void moveAllElementsYIncludingMovingElements(int pixels) {
    // Zuerst die Elemente des Petri-Netzes verschieben
    for (PetriNetFigure f : figures) {
      // Position um die Anzahl Pixel nach unten/oben verschieben
      f.setLocation(f.getX(), f.getY() + pixels);
      f.labelPositionY += pixels;
    }
    // Anschließend die Pfeile verschieben und Maximum und Minimum anpassen
    moveArrowsAndAdjustMinAndMaxY(pixels);
  }

  /**
   * Diese Methode verschiebt alle Kanten in Y-Richtung und ändert die Attribute
   * minYCoordinate und maxYCoordinate
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Kanten in Y-Richtung verschoben
   *          werden
   */
  private void moveArrowsAndAdjustMinAndMaxY(int pixels) {
    // Alle Pfeile verschieben
    moveArrowsY(pixels);
    // Das Minimum und Maximum anpassen
    adjustMinAndMaxY(pixels);
  }

  /**
   * In dieser Methode werden die Y-Koordinaten des Start- und Endepunkts der
   * Kanten um die übergebene Anzahl pixels erhöht
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die die Kanten in Y-Richtung verschoben
   *          werden
   */
  private void moveArrowsY(int pixels) {
    for (Kante k : kanten) {
      // Nur verschieben, wenn der Pfeil nicht zu denen gehört,
      // die gerade mit der Maus bewegt werden
      if (!hashSetForMovingArrows.contains(k)) {
        k.start.y += pixels;
        k.destination.y += pixels;
      }
    }
  }

  /**
   * Nach dem Verschieben von Figuren haben sich die maximalen und minimalen
   * X-Koordinaten der PetriNetFigures ebenfalls verschoben
   * 
   * Mit dieser Methode werden die beiden Variablen maxYCoordinate und
   * minYCoordinate angepasst.
   * 
   * @param pixels
   *          Die Anzahl Pixel, um die in Y-Richtung verschoben wurde
   */
  private void adjustMinAndMaxY(int pixels) {
    // Minimum und Maximum der Y-Koordinate anpassen
    if (pixels > 0) {
      maxYCoordinate += pixels;
      minYCoordinate += pixels;
    }
  }

  /**
   * Diese Methode setzt die Panel-Größe (Breite und Höhe) auf die bevorzugte
   * Größe. Dadurch erscheinen keine Scroll-Leisten.
   */
  void setActualSizeAsPreferredSize() {
    this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
  }

  /**
   * Diese Methode speichert, an welchem Petrinetz-Element das Kontextmenü
   * aufgeblendet ist.
   * 
   * @param popupAtComponent
   *          Das Petrinetz-Element, bei dem das Kontextmenü erscheint
   */
  void setPopupAtComponent(Component popupAtComponent) {
    // Sichern, bei welchem Petrinetz-Element das Kontextmenü ist
    this.popupAtFigure = (PetriNetFigure) popupAtComponent;
    // Evtl. bestehende Sicherung bei einer Kante entfernen
    // (passiert, wenn das Popup-Menü hochkam, aber kein Menüpunkt gewählt
    // wurde)
    this.popupAtArrow = null;
  }

  /**
   * Diese Methode speichert, an welcher Kante des Petrinetzes das Kontextmenü
   * aufgeblendet ist.
   * 
   * @param popupAtArrow
   *          Die Kante des Petrinetzes, bei der das Kontextmenü erscheint
   */
  void setPopupAtArrow(Kante popupAtArrow) {
    // Sichern, bei welcher Kante das Kontextmenü ist
    this.popupAtArrow = popupAtArrow;
    // Evtl. bestehende Sicherung bei einer PetriNetFigure entfernen
    // (passiert, wenn das Popup-Menü hochkam, aber kein Menüpunkt gewählt
    // wurde)
    this.popupAtFigure = null;
  }

  /**
   * Diese Methode wird nach der Auswahl eines Menüpunkts im Kontextmenü
   * aufgerufen. Sie kümmert sich um die Durchführung der entsprechenden
   * Aktionen.
   * 
   * @param e
   *          Das ActionEvent, welches das Event ausgelöst hat
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    // Ein Menüpunkt aus dem PopupMenü bei einer Stelle oder Transition wurde
    // gewählt, wenn popupAtFigure nicht null ist.
    if (popupAtFigure != null) {
      String command = e.getActionCommand();
      // PetriNetFigure zwischenspeichern
      PetriNetFigure f = (PetriNetFigure) popupAtFigure;

      switch (command) {
      case "Benennen/Name ändern":
        // Eingabefenster zur Erfassung des neuen Namens anzeigen
        String newLabelText = JOptionPane.showInputDialog(this,
            "Neue Bezeichnung:", f.getLabelText());

        // Wenn nicht abgebrochen wurde
        if (newLabelText != null) {
          // Die Figur mit dem erfassten Text versehen
          f.setLabelText(newLabelText);
          // Für den Scroll-Vorgang die letzte beeinflusste Figur setzen
          lastManipulatedFigure = f;
          // Flag, dass gerade eine Figur gezeichnet wurde setzen
          // um die Scroll-Leisten ggf. neu zu positionieren
          isNamedJustNow = true;
          // Flag für Abfrage zum Speichern setzen
          shouldBeSaved = true;
        }
        break;

      case "Anzahl Token eingeben":
        // Sichern, bei welcher Stelle das Kontextmenü ist
        Stelle place = (Stelle) f;
        // und die aktuelle Anzahl der Token holen.
        int numberOfTokens = place.getNumberOfTokens();

        // Eingabe-Fenster öffnen und erfassten String auswerten
        String enteredString = JOptionPane.showInputDialog(this,
            "Anzahl Token", numberOfTokens);

        // Wenn nicht abgebrochen wurde
        if (enteredString != null) {
          try {
            // String in einen Integer-Wert umwandeln
            numberOfTokens = Integer.parseInt(enteredString);
          } catch (NumberFormatException exception) {
            // Wenn der Wert
            // - höchstens ein Mal mit - oder + beginnt
            // - Und im folgenden höchstens ein Mal
            // - beliebig viele Ziffern von 0 bis 9 kommen
            // - ein Punkt
            // - beliebig vielen anschließende Ziffern von 0 bis 9
            // handelt es sich um eine Kommazahl
            if (enteredString.matches("(-|\\+)?[0-9]+(\\.[0-9]+)?")) {
              JOptionPane
                  .showMessageDialog(
                      this,
                      "<html>"
                          + enteredString
                          + " ist keine erlaubte Zahl!<br>Es sind nur ganze Zahlen von 0 bis maximal "
                          + Integer.MAX_VALUE + " zulässig.</html>");

              // Ansonsten enthält der erfasste Text unzulässige Zeichen
            } else {
              JOptionPane
                  .showMessageDialog(
                      this,
                      "<html>"
                          + enteredString
                          + " enthält unerlaubte Zeichen und ist damit keine Zahl!<br>Es sind nur ganze Zahlen von 0 bis maximal "
                          + Integer.MAX_VALUE + " zulässig.</html>");
            }
          }
          // Wenn die Anzahl ganzzahlig aber negativ ist
          if (numberOfTokens < 0) {
            // Die bisherige Anzahl Token merken
            numberOfTokens = place.getNumberOfTokens();
            // Nachrichtenfenster anzeigen
            JOptionPane.showMessageDialog(this,
                "Die Anzahl Token muss mindestens 0 sein!");
          }
        }
        // Anzahl Token in der Stelle setzen
        place.setNumberOfTokens(numberOfTokens);
        // Flag für Abfrage zum Speichern setzen
        shouldBeSaved = true;
        break;

      case "Schalten/Simulieren":
        // Sichern, bei welcher Transition das Kontextmenü ist
        Transition t = (Transition) popupAtFigure;
        Stelle s;

        // Die mit der Transition verbundenen Stellen betrachten

        // Von den Eingangs-Stellen ein Token abziehen
        for (PetriNetFigure figure : t.connectionsFrom) {
          s = (Stelle) figure;
          s.setNumberOfTokens(s.getNumberOfTokens() - 1);
        }

        // Bei den Ausgangs-Stellen ein Token addieren
        for (PetriNetFigure figure : t.connectionsTo) {
          s = (Stelle) figure;
          s.setNumberOfTokens(s.getNumberOfTokens() + 1);
        }

        // Flag für Abfrage zum Speichern setzen
        shouldBeSaved = true;
        break;

      case "Größe einstellen":
        // Größenänderungsfenster (wieder) sichtbar machen
        sizeFrame.setVisible(true);
        break;

      case "Löschen":
        executeDelete();
        break;

      default:
        // Es gibt keine Default-Auswahl
      }
      // PopupAtComponent ist null, also war das Popup-Menü an einer Kante
      // Dort gibt es nur den Menüpunkt "Löschen"
    } else {
      // Diese Kante dem HashSet hinzufügen hinzufügen
      hashSetForExplicitelyClickedArrows.add(popupAtArrow);
      executeDelete();
    }
    updateView();

    // Evtl. bestehende Sicherung, dass das Kontextmenü erschienen ist,
    // sowohl bei den PetriNetFigures als auch bei den Kanten entfernen.
    // (passiert, wenn das Popup-Menü hochkam, aber kein Menüpunkt gewählt
    // wurde)
    popupAtFigure = null;
    popupAtArrow = null;
  }

  /**
   * Diese Methode löscht vom Anwender ausgewählte PetriNetFigures und deren
   * Kanten sowie davon unabhängig vom Anwender gesondert ausgewählte Kanten
   */
  private void executeDelete() {
    // Evtl. vorher angeklickte Figuren mit ihren Kanten löschen
    deletePetriNetFiguresAndTheirArrows();
    // die explizit ausgewählten Kanten löschen
    deleteExplicitelyClickedArrows();
    // Flag für Abfrage zum Speichern setzen
    shouldBeSaved = true;
  }

  /**
   * Diese Methode führt ein Neuzeichnen des PetriNetPanels durch
   */
  private void updateView() {
    this.revalidate();
    this.repaint();
  }

  /**
   * Diese Methode löscht alle in dem HashSet hashSetForFigures enthaltenen
   * PetriNetFigures.
   */
  private void deletePetriNetFiguresAndTheirArrows() {
    // Wenn in dem HashSet Elemente enthalten sind
    if (hashSetForFigures.size() > 0) {
      // ... über diese iteriereen
      for (PetriNetFigure f : hashSetForFigures) {
        // und das Element mit allen verbundenen Kanten löschen
        deletePetriNetFigureWithAllConnectedArrows(f);
      }
      // Wenn das HashSet keine Elemente enthält
    } else {
      // Aber ein Popup-Menü bei einem Element offen war
      if (popupAtFigure != null) {
        // Wurde der Menüpunkt "Löschen" ohne vorheriges Anklicken mehrerer
        // Elemente gewählt und bezieht sich nur auf diese Figur. Daher
        // diese löschen.
        deletePetriNetFigureWithAllConnectedArrows((PetriNetFigure) popupAtFigure);
      }
    }
  }

  /**
   * Diese Methode entfernt die übergebene PetriNetFigure und deren ankommenden
   * bzw. abgehenden Kanten.
   * 
   * @param f
   *          Zu löschende PetriNetFigure
   */
  private void deletePetriNetFigureWithAllConnectedArrows(PetriNetFigure f) {
    int i;

    // Zuerst die von der Figur abgehenden Kanten löschen
    for (i = f.arrowsFrom.size(); i > 0; i--) {
      deleteArrowAndAdditionallyFromOtherHashMapToo(f.arrowsFrom.get(i - 1));
    }

    // Jetzt die zu der Figur hinführenden Kanten löschen
    for (i = f.arrowsTo.size(); i > 0; i--) {
      deleteArrowAndAdditionallyFromOtherHashMapToo(f.arrowsTo.get(i - 1));
    }

    // eigentliches Löschen der Figur
    this.remove(f);
    figures.remove(f);
  }

  /**
   * Diese Methode löscht alle explizit vom Anwender angeklickten Pfeile
   */
  private void deleteExplicitelyClickedArrows() {
    // Über alle in dem HashSet eingetragenen Kanten iterieren
    for (Kante k : hashSetForExplicitelyClickedArrows) {
      // und diese von den Attributen der verbundenen Figuren entfernen
      deleteArrowFromFigureAttributes(k);
    }
  }

  /**
   * Diese Methode löscht eine Kante aus den Attributen arrowsTo, arrowsFrom,
   * connectionsTo und connectionsFrom der mit der Kante verbundenen
   * PetriNetFigures.
   * 
   * @param k
   *          Zu entfernende Kante
   */
  private void deleteArrowFromFigureAttributes(Kante k) {
    // Temporäre Variablen f1 und f2 für die betroffenen
    // PetriNetFigures deklarieren und initialisieren
    PetriNetFigure f1 = k.getFigureFrom();
    PetriNetFigure f2 = k.getFigureTo();

    // Von der den Pfeil abgebenden Figur die Verbindungsinformation zur
    // den Pfeil empfangenden Figur entfernen
    f1.arrowsTo.remove(k);
    f1.connectionsTo.remove(f2);

    // Von der den Pfeil empfangenden Figur die Verbindungsinformation zur
    // den Pfeil abgebenden Figur entfernen
    f2.arrowsFrom.remove(k);
    f2.connectionsFrom.remove(f1);

    // Eigentliches Löschen des Pfeils
    kanten.remove(k);
  }

  /**
   * Diese Methode löscht eine Kante aus der Kanten-ArrayList und entfernt die
   * Kante auch aus dem HashSet der explizit angeklickten Kanten.
   * 
   * @param k
   *          Die zu löschende/zu entfernende Kante
   */
  private void deleteArrowAndAdditionallyFromOtherHashMapToo(Kante k) {
    if (hashSetForExplicitelyClickedArrows.contains(k)) {
      hashSetForExplicitelyClickedArrows.remove(k);
    }

    deleteArrowFromFigureAttributes(k);
  }

  /**
   * Diese Methode entfernt die Kanten einer PetriNetFigure aus dem
   * Kanten-HashSet.
   * 
   * @param figure
   *          PetriNetFigure, deren Kanten aus dem HashSet entfernt werden
   *          sollen
   */
  private void deleteArrowsFromHashSet(PetriNetFigure figure) {
    // Erst die zur Figur ankommenden Pfeile entfernen ...
    for (Kante k : figure.arrowsFrom) {
      hashSetForMovingArrows.remove(k);
    }

    // ... dann die von der Figur abgehenden Pfeile
    for (Kante k : figure.arrowsTo) {
      hashSetForMovingArrows.remove(k);
    }
  }

  /**
   * Diese Methode gibt das RectangularShape der PetriNetFigure zurück, die sich
   * unter dem Mauszeiger befindet.
   * 
   * @return Das RectangularShape der PetriNetFigure, die sich unter dem
   *         Mauszeiger befindet
   */
  private RectangularShape getRectangularShapeOfElementUnderMouse() {
    return currentFigureUnderMouse == null ? null : currentFigureUnderMouse
        .getRectangularShape();
  }

  /**
   * Diese Methode wird aufgerufen, wenn der Anwender im beim Zeichnen einer
   * Kante eine PetriNetFigure angeklickt hat. Die PetriNetFigure wird in der
   * Referenz ClickedFigure zwischengespeichert.
   * 
   * @param figure
   *          Die PetriNetFigure, die angeklickt wurde
   */
  private void setClickedFigure(PetriNetFigure figure) {
    this.clickedFigure = figure;
  }

  /**
   * Diese Methode wird aufgerufen, wenn der Anwender im beim Zeichnen einer
   * Kante eine PetriNetFigure angeklickt hat. Von dem Element wird das
   * RectangularShape in der Referenz clickedShape zwischengespeichert.
   */
  private void setClickedShape() {
    this.clickedShape = getRectangularShapeOfElementUnderMouse();
  }

  /**
   * Diese Methode setzt alle im Einfügen einer Kante stehenden Attribute des
   * PetriNetPanels auf null. Dies ist notwendig, um das Zeichnen einer Kante
   * abzuschließen.
   */
  void makeClickOnElementsUndone() {
    clickedShape = null;
    clickedShape1 = null;
    clickedShape2 = null;
    clickedFigure = null;
    clickedFigure1 = null;
    clickedFigure2 = null;
    centerOfClickedShape1 = null;
    centerOfClickedShape2 = null;
    // newKante = null;
    // Neuzeichnen notwendig, da sonst z. B. ein neu gezeichneter Pfeil
    // verschwinden würde und erst bei Mausbewegung erscheinen würde.
    updateView();
  }

  /**
   * Diese Methode gibt den aktuellen Status des PetriNetPanels zurück.
   * 
   * @return Der aktuelle Status des PetriNetPanels
   */
  Statuses getPanelStatus() {
    return panelStatus;
  }

  /**
   * Mit dieser Methode wird dem PetriNetPanel bekannt gemacht, welche Figur der
   * Anwender zeichnen möchte bzw. welche Aktion er durchführen möchte.
   * 
   * In Abhängigkeit von der in der Toolbar gewählten Figu/Aktion muss das
   * PetriNetPanel einen entsprechenden Zustand (Status) einnehmen.
   * 
   * Dieser Zustand wird insbesondere beim Neuzeichnen des Petri-Netzes
   * herangezogen.
   * 
   * @param panelStatus
   *          Der Status des Panels
   */
  void setPanelStatus(Statuses panelStatus) {
    this.panelStatus = panelStatus;

    // Wenn der Status nicht das Verschieben von Figuren ist
    if (panelStatus != Statuses.MOVINGFIGURES) {
      // Alle HashSets im Zusammenhang mit dem Verschieben löschen
      clearAllHashSetsForMoving();

      // Wenn der Status auch nicht das Zeichnen eines Pfeils ist
      if (panelStatus != Statuses.KANTE) {
        // Die Variablen zum Zeichnen eines Pfeils leeren
        makeClickOnElementsUndone();
      }
    }
  }

  /**
   * Diese Methode gibt den Mittelpunkt einer PetriNetFigure zurück.
   * 
   * @param figure
   *          Die PetriNetFigure, von der Mittelpunkt zurückgegeben wird.
   * 
   * @return Der Mittelpunkt der PetriNetFigure als Point
   */
  private Point getCenterOfShapeInFigure(PetriNetFigure figure) {
    RectangularShape shape = figure.getRectangularShape();

    // Eigentlich wäre es ganz einfach: Der Mittelpunkt ergibt sich aus dem
    // RectangularShape.
    // Dies funktioniert allerdings nicht immer, denn aus mir unerfindlichen
    // Gründen hat das shape auf ein Mal die Koordinaten (0, 0), so dass
    // dann ein falscher Mittelpunkt ermittelt wird.

    // Deshalb wird geprüft, ob X oder Y der Figur vom RectangularShape
    // abweichen.
    // Wenn ja, wird der Mittelpunkt mit Hilfe der Figur und des
    // RectangularShape errechnet.
    // Ansonsten reicht es aus, nur das Shape zu nutzen. Versuche, immer nur auf
    // die Hilfe von Figur und Shape zurückzugreifen brachten aber auch
    // keinen Erfolg, dann blieben beim Laden einer Datei
    // Pfeile mitten im Panel stehen.
    if (shape.getX() != figure.getX() || shape.getY() != figure.getY()) {
      return new Point((int) (figure.getX() + shape.getCenterX()),
          (int) (figure.getY() + shape.getCenterY()));
    } else {
      return new Point((int) (shape.getCenterX()), (int) (shape.getCenterY()));
    }
  }

  /**
   * Diese Methode errechnet von allen Kanten die Schnittpunkte mit den
   * Stellen/Transitionnen neu
   */
  private void calculateAllArrowsNew() {
    // Um nicht bei jedem Pfeil neue Objekte zu bilden, diese
    // vor der For-Schleife deklarieren
    PetriNetFigure f1;
    PetriNetFigure f2;
    Point center1;
    Point center2;

    // Die neu zu berechnenden Pfeile durchlaufen
    for (Kante k : this.getKanten()) {
      // Pfeilspitzenlänge einstellen
      k.setArrowHeadLength(this.getArrowHeadLength());

      // f1 und f2 sind die PetriNetFigures, an denen die Pfeile hängen
      f1 = k.getFigureFrom();
      f2 = k.getFigureTo();

      // Den Mittelpunkt dieser beiden Figuren berechnen
      center1 = getCenterOfShapeInFigure(f1);
      center2 = getCenterOfShapeInFigure(f2);

      // Start- und Endpunkt des Pfeils ermitteln
      calculationsWithArrows.calculateArrow(this, f1, f2, center1.getX(),
          center1.getY(), center2.getX(), center2.getY());

      // Die Werte von Start- und Endpunkt in dem Pfeil-Objekt speichern
      // Dies muss für X und Y getrennt erfolgen, da die interception-Points vom
      // Typ
      // Point2D.Double sind, Start- und Endpunkt des Pfeils aber vom Typ Point.

      if (center1.equals(center2)) {
        // Wenn beide Elemente exakt übereinander liegen, sind die Schnittpunkte
        // etwas kurios (der Pfeil würde dann ganz links ins Fenster gelegt
        // werden)
        // Damit wenigstens erkennbar ist, dass eine Verbindung existiert,
        // wird als Beginn und Ende die linke obere Ecke der beiden Figuren
        // verwendet.
        k.start.x = (int) f1.getX();
        k.start.y = (int) f1.getY();
        k.destination.x = (int) k.start.x;
        k.destination.y = (int) k.destination.y;
      } else {
        k.start.x = (int) interceptionPointStart.x;
        k.start.y = (int) interceptionPointStart.y;
        k.destination.x = (int) interceptionPointEnd.x;
        k.destination.y = (int) interceptionPointEnd.y;
      }
    }
  }

  /**
   * Diese Methode gibt die Kante zurück, die sich am nächsten an der aktuellen
   * Position des Mauszeigers befindet.
   * 
   * @param positionX
   *          Die X-Koordinate des Punkts, für den die kürzeste Entfernung zur
   *          zurückzugebenden Kante (Pfeil) ermittelt wird
   * @param positionY
   *          Die entsprechende Y-Koordinate des Punkts
   * 
   * @return Die Kante (Pfeil) auf dem Panel, die der übergebenen Position am
   *         nächsten ist.
   */
  Kante getArrowNearPosition(int positionX, int positionY) {
    Kante nearestArrow = null;
    double smallestDistance = MIN_DISTANCE_MOUSE_TO_ARROW;
    double distance;

    // Möglichen angeklickten Pfeil bestimmen
    for (Kante k : this.getKanten()) {
      // kürzeste Entfernung des Klicks zum Pfeil ermitteln

      // Es kann vorkommen, dass zwar eine Kante existiert, aber
      // die Linie noch nicht (z. B., wenn eine neue Datei geladen und die Maus
      // bewegt wird).
      if (k.arrowLine != null) {
        distance = k.arrowLine.ptSegDist(positionX, positionY);

        // Wenn die jetzt kürzeste Entfernung kleiner ist
        // als die bisher kürzeste Entfernung ...
        if (distance <= smallestDistance) {
          // ... ist das die kürzeste Entfernung
          smallestDistance = distance;
          // ... und k der vom Anwender angeklickte Pfeil
          nearestArrow = k;
        }
      }
    }
    return nearestArrow;
  }

  /**
   * Der Maus-Klick wird in seine X- und Y-Koordinate getrennt und den Variablen
   * clickBeforeMovingX und clickBeforeMovingY zugewiesen.
   * 
   * In den weiteren Methoden wird mit diesen Koordinaten gerechnet, damit keine
   * getX()-Methoden und getY()-Methoden aufgerufen werden müssen. Dies sollte
   * der Performance-Steigerung dienen.
   * 
   * @param e
   *          Das MouseEvent, das diese Methode aufgerufen hat.
   */
  private void setCoordinatesForClickBeforeMoving(MouseEvent e) {
    clickBeforeMovingX = e.getX();
    clickBeforeMovingY = e.getY();
  }

  /**
   * Bestückt alle für das Verschieben von Elementen erforderlichen HashSets
   * 
   * @param figure
   *          Die den HashSets hinzuzufügende PetriNetFigure
   */
  private void setAllHashSetsForMoving(PetriNetFigure figure) {
    addFigureToHashSet(figure);
    addArrowsToHashSet(figure);
  }

  /**
   * Leert alle für das Verschieben von Elementen erforderlichen HashSets
   */
  void clearAllHashSetsForMoving() {
    clearFiguresToMoveHashSet();
    clearArrowHashSet();
    clearExplicitelyClickedArrowHashSet();
  }

  /**
   * Fügt eine PetriNetFigure zum für das Verschieben von Elementen
   * erforderlichen HashSet hinzu
   * 
   * @param figure
   *          Die dem HashSet hinzuzufügende PetriNetFigure
   */
  private void addFigureToHashSet(PetriNetFigure figure) {
    hashSetForFigures.add(figure);
  }

  /**
   * Entfernt eine PetriNetFigure zum für das Verschieben von Elementen
   * erforderlichen HashSet
   * 
   * @param figure
   *          Die aus dem HashSet zu entfernende PetriNetFigure
   */
  private void removeFigureFromHashSet(PetriNetFigure figure) {
    hashSetForFigures.remove(figure);
  }

  /**
   * Um die mitwandernden Pfeile zu ermitteln, werden diese in ein HashSet
   * eingefügt. Um dies nicht bei jedem mouseDragged-Ereignis vornehmen zu
   * müssen, wird diese Methode bereits beim mousePressed-Ereignis aufgerufen.
   * 
   * HashSet deswegen, weil dann jeder Pfeil nur ein Mal reinkommt, also
   * unabhängig, ob er von dem Element abgeht oder dort ankommt.
   * 
   * @param figure
   *          Die PetriNetFigure, deren Pfeile zum HashSet hinzugefügt werden
   *          sollen
   */
  private void addArrowsToHashSet(PetriNetFigure figure) {
    // Alle betoffenen Kanten der übergebenen Figur in das HashSet einfügen

    // erst die ankommenden Pfeile
    for (Kante k : figure.arrowsFrom) {
      hashSetForMovingArrows.add(k);
    }

    // dann die abgehenden Pfeile
    for (Kante k : figure.arrowsTo) {
      hashSetForMovingArrows.add(k);
    }
  }

  /**
   * Diese Methode löscht die Auflistung der zu verschiebenden Figuren
   */
  private void clearFiguresToMoveHashSet() {
    hashSetForFigures.clear();
  }

  /**
   * Diese Methode löscht die Auflistung der zu verschiebenden Pfeile
   */
  private void clearArrowHashSet() {
    hashSetForMovingArrows.clear();
  }

  /**
   * Diese Methode löscht die Auflistung der explizit angeklickten Pfeile
   */
  private void clearExplicitelyClickedArrowHashSet() {
    hashSetForExplicitelyClickedArrows.clear();
  }

  /**
   * Die linke Maustaste wurde betätigt.
   * 
   * Diese Methode wird entweder vom MouseListenerForPanel (component = null)
   * oder vom MouseListenerForPetriNetFigures aufgerufen. Zur weiteren
   * Verarbeitung des Mausklicks wird in weitere Methoden verzweigt.
   * 
   * @param figure
   *          Die angeklickte PetriNetFigure oder null
   * @param e
   *          Das Maus-Event, das den Klick gemeldet hat
   */
  void mousePressedLeft(PetriNetFigure figure, MouseEvent e) {
    if (getPanelStatus() == Statuses.MOVINGFIGURES) {
      // Koordinaten des Mausklicks in den Klassenvariablen
      // clickBeforeMovingX und clickBeforeMovingY speichern
      setCoordinatesForClickBeforeMoving(e);

      // Wenn kein Element angeklickt wurde oder der Klick ins Element
      // außerhalb der gezeichneten Figur ist ...
      if (figure == null
          || !figure.getRectangularShape().contains(clickBeforeMovingX,
              clickBeforeMovingY)) {
        // ... prüfen, ob ein Pfeil gemeint sein könnte. Wenn ja,
        // wird die Behandlung des Klicks in weiteren Methoden vorgenommen.
        checkClickForArrow();
      } else {
        // Ansonsten war es ein Klick in eine Figur. Die Behandlung dieses
        // Ereignisses erfolgt in einer eigenen Methode.
        treatClickOnFigure(figure);
      }
      updateView();
    }
  }

  /**
   * Es steht fest, dass der Anwender eine Figur angeklickt hat. Hier wird die
   * Behandlung des Figur verarbeitet.
   * 
   * @param figure
   *          Die angeklickte PetriNetFigure
   */
  private void treatClickOnFigure(PetriNetFigure figure) {
    // Mausklick ist innerhalb einer Figur
    if (ctrlPressed) {
      // Wenn die Ctrl-Taste gedrückt ist
      if (hashSetForFigures.contains(figure)) {
        // und die Figur schon im HashSet enthalten ist, wird sie entfernt
        // und wieder hinzugefügt. Dadurch steht die gerade angeklickte
        // Figur an letzter Position im HashSet
        // Das ist wichtig, damit sich die Elemente beim Verschieben von
        // mehreren Elementen zugleich an der momentanen Mausposition
        // orientieren
        removeFigureFromHashSet(figure);
        addFigureToHashSet(figure);
      } else {
        // Ansonsten wird die Figur dem HashSet hinzugefügt
        setAllHashSetsForMoving(figure);
      }
    } else if (shiftPressed) {
      // Wenn die Shift-Taste gedrückt und das HashSet nicht leer ist,
      if (!hashSetForFigures.isEmpty()) {
        // die Figur aus allen HashSets entfernen
        removeFigureFromHashSet(figure);
        deleteArrowsFromHashSet(figure);
      } // wenn im Figuren-HashSet nichts ist, braucht nichts weiter
        // gemacht zu werden.
    } else {
      // Weder die Ctrl-Taste noch die Shift-Taste wurden gedrückt
      // Wenn alle HashSets leer sind, dieses Element als einziges
      // den HashSets hinzufügen
      if (hashSetForFigures.isEmpty()
          && hashSetForExplicitelyClickedArrows.isEmpty()) {
        setAllHashSetsForMoving(figure);
      } else {
        // Wenn das Element im HashSet nicht enthalten ist, alle HashSets
        // löschen
        // und das geklickte Element als einziges dem HashSet hinzufügen
        if (!hashSetForFigures.contains(figure)) {
          clearAllHashSetsForMoving();
          setAllHashSetsForMoving(figure);
        }
        // Ansonsten nichts machen.
        // Der Anwender kann mehrere Elemente verschieben, unabhängig,
        // von welchem der angeklickten Elemente (nicht Pfeile) er
        // dies tut.
      }
    }
  }

  /**
   * An Hand der Klassenvariablen clickBeforeMovingX und clickBeforeMovingY wird
   * geprüft ob ein Pfeil angeklickt wurde.
   * 
   * Ist dies der Fall, wird in einer weiteren Methode der Klick verarbeitet,
   * ansonsten wird die Behandlung des Klicks abgebrochen
   */
  private void checkClickForArrow() {
    // Evtl. wurde eine Kante (Pfeil) angeklickt
    Kante nearestArrow = getArrowNearPosition(clickBeforeMovingX,
        clickBeforeMovingY);

    // Anwender hat keine Kante angeklickt
    if (nearestArrow == null) {
      // Alle HashSets löschen
      clearAllHashSetsForMoving();
    } else {
      // Wenn doch, dann diesen Klick weiter verarbeiten
      treatClickOnArrow(nearestArrow);
    }
  }

  /**
   * Es steht fest, dass der Anwender eine Kante angeklickt hat. Hier wird die
   * Behandlung des Pfeils verarbeitet.
   * 
   * @param arrow
   *          Die Kante (Pfeil), auf die geklickt wurde
   */
  private void treatClickOnArrow(Kante arrow) {
    // angeklickten Pfeil hinzufügen, wenn die Ctrl-Taste gedrückt wurde
    if (ctrlPressed) {
      hashSetForExplicitelyClickedArrows.add(arrow);
    } else if (shiftPressed) {
      // angeklickten Pfeil entfernen, wenn die Shift-Taste gedrückt wurde
      hashSetForExplicitelyClickedArrows.remove(arrow);
    } else {
      // Weder Ctrl- noch Shift-Taste wurden gedrückt
      // Alle HashSets löschen, wenn der Pfeil nicht im Hashset enthalten ist
      if (!hashSetForExplicitelyClickedArrows.contains(arrow)) {
        clearAllHashSetsForMoving();
      }
    }
  }

  /**
   * Die linke Maustaste wurde geklickt und wieder losgelassen.
   * 
   * Diese Methode wird entweder vom MouseListenerForPanel (component = null)
   * oder vom MouseListenerForPetriNetFigures aufgerufen. Zur weiteren
   * Verarbeitung des Mausklicks wird in weitere Methoden verzweigt.
   * 
   * Die Methode leitet den Zeichenvorgang für die einzelnen Elemente des
   * Petri-Netzes ein.
   * 
   * @param figure
   *          Die PetriNetFigure, die angeklickt wurde (oder null)
   * @param e
   *          Das Maus-Event, das den Klick gemeldet hat
   */
  void mouseClicked(PetriNetFigure figure, MouseEvent e) {
    // Koordinaten des Mausklicks in Variablen speichern
    int xClick = e.getX();
    int yClick = e.getY();

    // Wenn auf eine PetriNetFigure geklickt wurde, enthalten xClick und
    // yClick die Koordinaten bezogen auf diese Figur. Um die absoluten
    // Koordinaten
    // zu erhalten, müssen noch die Koordinaten der Figur addiert werden.
    if (figure != null) {
      xClick += figure.getX();
      yClick += figure.getY();
    }

    // Ermitteln, welche Figur hinzuzufügen ist
    Statuses figureToAdd = getPanelStatus();

    // Dass keine Figur erstellt werden soll, kann nur beim
    // Programmstart der Fall sein.
    if (figureToAdd != null) {
      switch (figureToAdd) {
      case STELLE:
        // Neue Stelle bilden - das true steht dafür, dass die Stelle
        // vom Anwender erstellt wird, nicht durch Laden einer Datei
        Stelle stelle = newPlace(xClick, yClick, true);
        // ID vergeben und Stelle zeichnen
        stelle.buildAndSetPlaceID();
        paintFigure(stelle, xClick - widthOfFigures / 2, yClick
            - heightOfFigures / 2);
        shouldBeSaved = true;
        break;

      case TRANSITION:
        // Neue Transition bilden - das true steht dafür, dass die Transition
        // vom Anwender erstellt wird, nicht durch Laden einer Datei
        Transition transition = newTransition(xClick, yClick, true);
        // ID vergeben und Transition zeichnen
        transition.buildAndSetID();
        paintFigure(transition, xClick - widthOfFigures / 2, yClick
            - heightOfFigures / 2);
        shouldBeSaved = true;
        break;

      case KANTE:
        // Prüfen, ob ein Element angeklickt wurde und wenn ja,
        // das Element der Variable clickedShape1 oder clickedShape2 zuweisen
        // und den Mittelpunkt des angeklickten Elements holen
        clickedShape = getRectangularShapeOfElementUnderMouse();

        if (clickedShape == null) {
          // Mausklick in die freie Fläche
          makeClickOnElementsUndone();
        } else {
          if (clickedShape.contains(currentMousePositionX,
              currentMousePositionY)) {
            // Ein Element wurde angeklickt, die entsprechende PetriNetFigure
            // zuweisen
            setClickedFigure(currentFigureUnderMouse);

            // Wenn das Start-Element für den Pfeil noch nicht existiert, ...
            if (clickedShape1 == null) {
              // ... die Variablen für das Start-Element zuweisen
              clickedShape1 = clickedShape;
              clickedFigure1 = clickedFigure;
              centerOfClickedShape1 = getCenterOfShapeInFigure(clickedFigure1);

              // Das Start-Element existiert, aber ist die Verbindung auch
              // zulässig?
            } else if (isAllowedConnection(clickedFigure1, clickedFigure)) {
              // Die Verbindung ist zulässig, daher die Variablen dem
              // Element, bei dem der Pfeil endet, zuweisen
              clickedShape2 = clickedShape;
              clickedFigure2 = clickedFigure;
              centerOfClickedShape2 = getCenterOfShapeInFigure(clickedFigure2);

              // Die Verbindung ist unzulässig, daher Erstellung des Pfeils
              // abbrechen
            } else {
              makeClickOnElementsUndone();
            }
          } else {
            // Mausklick zwar über Element, aber nicht im Symbol
            // Erstellung des Pfeils abbrechen
            makeClickOnElementsUndone();
          }
        }
        break;

      case TOKEN:
        // Prüfen, ob ein Element angeklickt wurde und wenn ja,
        // prüfen, ob es eine Stelle (Kreis) ist, denn nur dort dürfen
        // Token eingefügt werden
        setClickedShape();

        if (clickedShape != null) {
          if (clickedShape.contains(currentMousePositionX,
              currentMousePositionY)) {
            // Ein Element wurde angeklickt, die entsprechende PetriNetFigure
            // zuweisen
            setClickedFigure(currentFigureUnderMouse);

            if (clickedFigure instanceof Stelle) {
              // Nur wenn es eine Stelle ist, darf was passieren ...

              // Angeklicktes Element ist eine Stelle, diese einer entsprecheden
              // lokalen Variablen zuweisen.
              Stelle clickedPlace = (Stelle) clickedFigure;
              // Aktuelle Anzahl von Token um 1 erhöhen
              clickedPlace
                  .setNumberOfTokens(clickedPlace.getNumberOfTokens() + 1);
              shouldBeSaved = true;
            }
          }
        }

        // Die im Zusammenhang mit geklickten Objekten stehenden Variablen
        // leeren
        makeClickOnElementsUndone();
        break;

      default:
        makeClickOnElementsUndone();
        break;
      }
      updateView();
    }
  }

  /**
   * Diese Methode erzeugt eine neue Stelle als Teil des Petri-Netzes
   * 
   * @param x
   *          X-Koordinate linke obere Ecke, wenn createdByUser = false,
   *          Mittelpunkt, wenn createdByUser = true
   * @param y
   *          Y-Koordinate (Bezug sh. X-Koordinate)
   * @param createdByUser
   *          true, wenn die Stelle durch Zeichnen des Anwenders erstellt wurde
   *          false, wenndie Stelle durch Laden einer Datei erstellt wurde
   * 
   * @return Ein neues Objekt der Klasse Stelle
   */
  private Stelle newPlace(int x, int y, boolean createdByUser) {
    return new Stelle(this, x, y, widthOfFigures, heightOfFigures,
        createdByUser);
  }

  /**
   * Diese Methode erzeugt eine neue Transition als Teil des Petri-Netzes
   * 
   * @param x
   *          X-Koordinate linke obere Ecke, wenn createdByUser = false,
   *          Mittelpunkt, wenn createdByUser = true
   * @param y
   *          Y-Koordinate (Bezug sh. X-Koordinate)
   * @param createdByUser
   *          true, wenn die Stelle durch Zeichnen des Anwenders erstellt wurde
   *          false, wenndie Stelle durch Laden einer Datei erstellt wurde
   * 
   * @return Ein neues Objekt der Klasse Transition
   */
  private Transition newTransition(int x, int y, boolean createdByUser) {
    return new Transition(this, x, y, widthOfFigures, heightOfFigures,
        createdByUser);
  }

  /**
   * Diese Methode erzeugt eine neue Kante als Teil des Petri-Netzes
   * 
   * @return Ein neues Objekt der Klasse Kante
   */
  private Kante newArrow() {
    return new Kante(arrowheadLength);
  }

  /**
   * Beim Laden einer Datei wurde festgestellt, dass bereits IDs mit der
   * Bezeichnung "place" + id existieren. Damit dadurch nicht beim Zeichnen
   * einer neuen Stelle die gleiche ID vergeben wird, wird die nächste ID hier
   * festgesetzt
   * 
   * @param number
   *          Die nächste Zahl der ID
   */
  public void setNumberOfPlaceID(int number) {
    Stelle.id = number;
  }

  /**
   * Beim Laden einer Datei wurde festgestellt, dass bereits IDs mit der
   * Bezeichnung "transition" + id existieren. Damit dadurch nicht beim Zeichnen
   * einer neuen Transition die gleiche ID vergeben wird, wird die nächste ID
   * hier festgesetzt
   * 
   * @param number
   *          Die nächste Zahl der ID
   */
  public void setNumberOfTransitionID(int number) {
    Transition.id = number;
  }

  /**
   * Diese Methode fügt dem PetriNetPanel eine neue Stelle hinzu, die in einer
   * zu ladenden Datei gefunden wurde.
   * 
   * @param id
   *          Die ID der hinzuzufügenden Transition (als String)
   * @param xPos
   *          Die X-Koordinate der linken oberen Ecke dieser PetriNetFigure
   * @param yPos
   *          Die Y-Koordinate der linken oberen Ecke dieser PetriNetFigure
   * @param label
   *          Die Bezeichnung der neuen Transition
   * @param numberOfTokens
   *          Die Anzahl Token in der Stelle
   * 
   * @return Stelle Ein neues Objekt der Klasse Stelle
   */
  public Stelle newPlaceFromFile(String id, int xPos, int yPos, String label,
      int numberOfTokens) {
    // Neues Stelle-Objekt erstellen
    Stelle stelle = newPlace(xPos, yPos, false);
    // ID der Stelle im Objekt speichern
    stelle.buildAndSetPlaceID(id);
    // Labeltext ebenfalls speichern
    stelle.setLabelText(label);
    // auch die Token-Anzahl speichern
    stelle.setNumberOfTokens(numberOfTokens);
    // Figur dem PetriNetPanel hinzufügen und letztlich zeichnen
    paintFigure(stelle, xPos, yPos);
    // Stelle-Objekt zurückgeben
    return stelle;
  }

  /**
   * Diese Methode fügt dem PetriNetPanel eine neue Transition hinzu, die in
   * einer zu ladenden Datei gefunden wurde.
   * 
   * @param id
   *          Die ID der hinzuzufügenden Transition (als String)
   * @param xPos
   *          Die X-Koordinate der linken oberen Ecke dieser PetriNetFigure
   * @param yPos
   *          Die Y-Koordinate der linken oberen Ecke dieser PetriNetFigure
   * @param label
   *          Die Bezeichnung der neuen Transition
   * 
   * @return Ein neues Objekt der Klasse Transition
   */
  public Transition newTransitionFromFile(String id, int xPos, int yPos,
      String label) {
    // Neues Transitions-Objekt erstellen
    Transition transition = newTransition(xPos, yPos, false);
    // ID der Transition im Objekt speichern
    transition.buildAndSetTransitionID(id);
    // Labeltext ebenfalls speichern
    transition.setLabelText(label);
    // Figur dem PetriNetPanel hinzufügen und letztlich zeichnen
    paintFigure(transition, xPos, yPos);
    // Transitions-Objekt zurückgeben
    return transition;
  }

  /**
   * Diese Methode fügt dem PetriNetPanel eine neue Kante hinzu, die in einer zu
   * ladenden Datei gefunden wurde.
   * 
   * @param arrowID
   *          Die ID der hinzuzufügenden Kante (als String)
   * @param source
   *          Die ID der PetriNetFigure, von der die Kante ausgeht
   * @param target
   *          Die ID der PetriNetFigure, zu der die Kante hinführt
   * 
   * @return Die Kante, deren Attribute nun gepflegt wurden
   */
  public Kante newArrowFromFile(String arrowID, String source, String target) {
    // Neues Arrow-Objekt erstellen
    Kante kante = newArrow();
    // Die übergebene ID im entsprechenden Arrow-Attribut sichern
    kante.setArrowID(arrowID);

    // Lokale Variable für die ID der PetriNetFigures, die die Kante verbindet,
    // deklarieren, damit nicht bei jedem Durchlauf der For-Schleife ein neues
    // String-Objekt erstellt werden muss.
    String figureID;
    // Über die bestehenden PetriNetFigures iterieren
    // Beim Laden der Datei wird darauf geachtet, dass zuerst die
    // Stellen/Transitionen hinzugefügt werden, erst dann die Kanten. Somit
    // ist gewährleistet, dass die Quell- und Ziel-Figur vorhanden sind.
    for (PetriNetFigure f : figures) {
      // Je nachdem, ob es sich um eine Stelle oder eine Transition handelt,
      // die ID auf individuelle Weise holen
      figureID = (f instanceof Stelle) ? ((Stelle) f).getPlaceID()
          : ((Transition) f).getTransitionID();

      // Wenn es sich um die Quell-ID handelt,
      if (figureID.equals(source)) {
        // diese Figur in dem entsprechenden Kante-Attribut sichern
        kante.setFigureFrom(f);
        // und widerum die Kante in dem entsprechenden Figur-Attribut sichern
        f.arrowsTo.add(kante);
      }

      // Wenn es sich um die Ziel-ID handelt,
      if (figureID.equals(target)) {
        // diese Figur in dem entsprechenden Kante-Attribut sichern
        kante.setFigureTo(f);
        // und widerum die Kante in dem entsprechenden Figur-Attribut sichern
        f.arrowsFrom.add(kante);
      }

      // Wenn von dieser Kante sowohl die Quell- als auch die Ziel-Figur
      // bekannt sind, ...
      if (kante.getFigureFrom() != null && kante.getFigureTo() != null) {
        // Im connectionsTo- bzw. connectionsFrom-Array der betroffenen
        // Figuren die Kante entsprechend hinzufügen
        kante.getFigureFrom().connectionsTo.add(kante.getFigureTo());
        kante.getFigureTo().connectionsFrom.add(kante.getFigureFrom());
        // Abbruch der For-Schleife
        break;
      }
    }

    // Kante der Kanten-ArryList hinzufügen
    this.kanten.add(kante);
    // Die mit gefüllten Attributen bestückte Kante zurückgeben
    return kante;
  }

  /**
   * Diese Methode fügt dem PetriNetPanel ein neues Element hinzu. Das Element
   * wird in der Folge auf das Panel gezeichnet.
   * 
   * @param figure
   *          Die hinzuzufügende / neu zu zeichnende PetriNetFigure
   * @param xPos
   *          Die X-Koordinate der linken oberen Ecke dieser PetriNetFigure
   * @param yPos
   *          Die Y-Koordinate der linken oberen Ecke dieser PetriNetFigure
   */
  public void paintFigure(PetriNetFigure figure, int xPos, int yPos) {
    figure.setSize(widthOfFigures, heightOfFigures);
    figure.setLocation(xPos, yPos);

    // Wenn eine Datei geladen wird und ein Element nicht im sichtbaren Bereich
    // plaziert wird, hat es noch kein RectangularShape und die Anwendung
    // wirft beim Berechnen der Pfeile eine Exception. Daher wird in diesem
    // Fall vorher der sichtbare Bereich angepasst, indem gescrollt wird.
    // Ggf. die Größe des Panels über Anpassung der Scroll-Leisten ändern

    // Die größte und kleinste X- und Y-Koordinate ermitteln
    maxXCoordinate = Math.max(figure.getX(), maxXCoordinate);
    maxYCoordinate = Math.max(figure.getY(), maxYCoordinate);

    // Wenn es die erste Figur ist, sind dessen Koordinaten auch die
    // Minimum-Koordinaten
    if (figures.isEmpty()) {
      minXCoordinate = figure.labelPositionX;
      minYCoordinate = figure.getY();
    } else {
      // Ansonsten müssen die Minimum-Koordinaten ermittelt werden
      minXCoordinate = Math.min(figure.labelPositionX, minXCoordinate);
      minYCoordinate = Math.min(figure.getY(), minYCoordinate);
    }

    if (figure instanceof Stelle) {
      popupMenu.buildAndAddPopupMenuForPlace(this, figure);
    } else {
      ((Transition) figure).setMenuSimulate(popupMenu
          .buildAndAddPopupMenuForTransition(this, figure));
    }

    // Figur der ArrayList hinzufügen
    figures.add(figure);
    // Figur dem Panel hinzufügen
    this.add(figure);
    // Letzte gesetzte Figur setzen, das wird ggf. fürs Scrollen noch gebraucht.
    this.lastManipulatedFigure = figure;

    // Beim Laden einer Datei kann es vorkommen, dass die Koordinaten der
    // Elemente
    // weiter rechts oder weiter unten liegen, als das Fenster groß ist.
    // Daher muss geprüft werden, ob die Scroll-Leisten anzubringen sind und
    // wohin gescrollt werden muss
    if (this.getWidth() < maxXCoordinate + widthOfFigures) {
      // Nach rechts
      scroller.adjustScrollPanesRight(maxXCoordinate, widthOfFigures);
    }

    if (this.getHeight() < maxYCoordinate + heightOfFigures) {
      // Nach unten
      scroller.adjustScrollPanesDown(maxYCoordinate, heightOfFigures);
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn die Maus bei gedrückter linker
   * Maustaste verschoben wird. Standardmäßig ist dies nach einem
   * Verschiebevorgang der Fall. Die Methode verarbeitet dieses Ereignis weiter.
   * 
   * Der Aufruf dieser Methode erfolgt durch die Listener
   * MouseListenerForPetriNetFigures und MousListenerForPanel
   * 
   * @param e
   *          Das Mausevent, das dieses Ereignis ausgelöst hat
   */
  void mouseDragged(MouseEvent e) {
    // Nur aktiv werden, wenn im Verschiebe-Modus und das
    // HashSet für die Figuren nicht leer ist
    if (panelStatus == Statuses.MOVINGFIGURES && !hashSetForFigures.isEmpty()) {

      // addX und addY sind die Vektoren, die beim Verschieben der
      // Elemente zu der Koordinate des jeweiligen Elements addiert werden
      // müssen.
      // Es handelt sich dabei um die Differenz zwischen aktueller Mausposition
      // und Position beim Anklicken des Elements vor dem Verschieben.
      // Dadurch wandern die Elemente mit der gedrückten Maus mit.

      // clickBeforeMoving wurde bereits in der
      // Methode setCoordinatesForClickBeforeMoving()
      // bzw. setCoordinatesForClickBeforeMoving() mit einem Wert versehen
      int addX = e.getX() - clickBeforeMovingX;
      int addY = e.getY() - clickBeforeMovingY;

      // Über alle betroffenen Elemente iterieren, damit
      for (PetriNetFigure f : hashSetForFigures) {
        // die Stellen/Transitionen mit dem Mauszeiger mitlaufen
        f.setLocation(f.getX() + addX, f.getY() + addY);
      }

      // Flag für Prüfung, ob zu Scrollen ist, setzen
      someElementsAreMoving = true;
      // Flag für Aufforderung zum Speichern setzen
      setShouldBeSaved(true);

      // Panel neu zeichnen
      // Dadurch werden auch die Pfeile neu gezeichnet
      updateView();
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn die linke Maustaste lowgelassen wird.
   * Standardmäßig ist dies nach einem Verschiebevorgang der Fall. Die Methode
   * verarbeitet dieses Ereignis weiter.
   * 
   * Hintergrund: Wurde lediglich ein Element verschoben, sollte es nach dem
   * Loslassen trotzdem nicht mehr markiert sein.
   * 
   * Der Aufruf dieser Methode erfolgt durch den Listener
   * MouseListenerForPetriNetFigures
   * 
   * @param figure
   *          Das Petrinetz-Element, über dem sich die Maus befindet
   * @param e
   *          Das Ereignis, das den Aufruf ausgelöst hat
   */
  void mouseReleased(PetriNetFigure figure, MouseEvent e) {
    // Nur im Status des Verschiebens von Elementen was machen
    if (getPanelStatus() == Statuses.MOVINGFIGURES) {
      // Das Flag für Prüfung, ob zu Scrollen ist, verneinen
      someElementsAreMoving = false;

      // Wenn nicht CTRL gedrückt wurde oder der Klick ins Leere ging,
      // die Auflistung der HashSets (Figuren und Kanten je eins) leeren, wenn
      if (!ctrlPressed
          || (figure == null && getArrowNearPosition(e.getX(), e.getY()) == null)) {
        // das Figuren-HashSet nur ein Element enthält und das Pfeil-HashSet
        // leer ist.
        if ((hashSetForFigures.size() == 1)
            && (hashSetForExplicitelyClickedArrows.isEmpty())) {
          clearAllHashSetsForMoving();
        }
      }
      updateView();
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn sich die Maus über dem Petrinetz
   * befindet. Sie setzt diverse Parameter, die für die Verarbeitung dieses
   * Ereignisses im PetriNetPanel von Bedeutung sind.
   * 
   * Der Aufruf erfolgt durch den Listener MouseListenerForPanel
   * 
   * @param x
   *          Die X-Koordinate der Mausposition auf dem Panel
   * @param y
   *          Die Y-Koordinate der Mausposition auf dem Panel
   */
  void mousePointerMoved(int x, int y) {
    currentFigureUnderMouse = null;
    if (getPanelStatus() == Statuses.MOVINGFIGURES) {
      mouseIsNearArrow = getArrowNearPosition(x, y);
    }
    setCurrentMousePosition(x, y);
  }

  /**
   * Diese Methode wird aufgerufen, wenn sich die Maus über einem Element des
   * Petrinetzes befindet. Sie setzt diverse Parameter, die für die Verarbeitung
   * dieses Ereignisses im PetriNetPanel von Bedeutung sind.
   * 
   * Der Aufruf erfolgt durch den Listener MouseListenerForPetriNetFigures
   * 
   * @param figure
   *          Das Petrinetz-Element, über dem sich die Maus befindet
   * @param x
   *          Die X-Koordinate der Mausposition auf dem Element (nicht auf dem
   *          Panel)
   * @param y
   *          Die Y-Koordinate der Mausposition auf dem Element (nicht auf dem
   *          Panel)
   */
  void mousePointerMovedOverComponent(PetriNetFigure figure, int x, int y) {
    currentFigureUnderMouse = figure;
    mouseIsNearArrow = null;
    setCurrentMousePosition(x, y);
  }

  /**
   * Diese Methode hält die aktuelle Mausposition (Basis: Dieses PetriNetPanel)
   * fest. Diese Information ist für verschiedene Operationen von Bedeutung, z.
   * B. für die Darstellung der Elemente in unterschiedlichen Farben. Der Aufruf
   * erfolgt durch die Methoden mousePointerMoved() und
   * mousePointerMovedOverComponent()
   * 
   * @param x
   *          Die X-Koordinate der Mausposition auf dem Panel
   * @param y
   *          Die Y-Koordinate der Mausposition auf dem Panel
   */
  private void setCurrentMousePosition(int x, int y) {
    currentMousePositionX = x;
    currentMousePositionY = y;
    updateView();
  }

  /**
   * Getter für die Auflistung der Elemente (nur Stellen und Transitionen, keine
   * Kanten) des Petri-Netzes
   * 
   * @return Die ArrayList der Petrinetz-Elemente
   */
  public ArrayList<PetriNetFigure> getFigures() {
    return figures;
  }

  /**
   * Getter für die Auflistung der Kanten des Petri-Netzes
   * 
   * @return Die ArrayList der Kanten des Petri-Netzes
   */
  public ArrayList<Kante> getKanten() {
    return kanten;
  }

  /**
   * Diese Methode stellt eine Verbindung dieser Klasse (PetriNetPanel) zur
   * Klasse Scroller her. Die Verbindung erfolgt über das Interface IScroller.
   * Der Aufruf der Methode erfolgt in der Klasse MainFrame.
   * 
   * @param scroller
   *          Das Interface, das der Scroller implementiert hat
   */
  void setScroller(IScroller scroller) {
    this.scroller = scroller;
  }

  /**
   * Diese Methode wird bei Klick auf den OK-Button im
   * Größenveränderungs-Fenster (Klasse SizeFrame) aufgerufen. Sie verändert die
   * Größen der Petrinetz-Elemente
   * 
   * @param size
   *          Die gewünschte Größe der Elemente
   */
  void resizeFigures(int size) {
    // Breite und Höhe einstellen
    setWidthOfFigures(size);
    setHeightOfFigures(size);
    // Größe der Pfeilspitze: Ein Drittel der Breite / Höhe
    setArrowHeadLength(size / 3);
    // Durch die Größenänderung können sich die Scroll-Leisten verschieben
    scroller.repaintScrollPane();
    // Alles neu zeichnen
    updateView();
  }

  /**
   * Diese Methode wird von resizeFigures() aufgerufen. Sie verändert die Breite
   * der Petrinetz-Elemente auf den übergebenen Wert
   * 
   * @param size
   *          Die gewünschte Breite der Elemente
   */
  private void setWidthOfFigures(int size) {
    this.widthOfFigures = size;
  }

  /**
   * Diese Methode wird von resizeFigures() aufgerufen. Sie verändert die Höhe
   * der Petrinetz-Elemente auf den übergebenen Wert
   * 
   * @param size
   *          Die gewünschte Höhe der Elemente
   */
  private void setHeightOfFigures(int size) {
    this.heightOfFigures = size;
  }

  /**
   * Diese Methode wird von resizeFigures() aufgerufen. Sie verändert die Länge
   * der Pfeilspitze einer Kante des Petrinetzes auf den übergebenen Wert
   * 
   * @param size
   *          Die gewünschte Länge der Pfeilspitze einer Kante
   */
  private void setArrowHeadLength(int size) {
    arrowheadLength = size;
  }

  int getArrowHeadLength() {
    return arrowheadLength;
  }

  /**
   * Diese Methode gibt zurück, ob das PetriNetPanel ungespeicherte Änderungen
   * enthält (true) oder nicht (false).
   * 
   * @return Angabe, ob das PetriNetPanel ungespeicherte Änderungen enthält
   *         true: Das PetriNetPanel hat ungespeicherte Änderungen false: Das
   *         PetriNetPanel hat keine ungespeicherte Änderungen
   */
  public boolean getShouldBeSaved() {
    return shouldBeSaved;
  }

  /**
   * Diese Methode setzt das Kennzeichen, ob das PetriNetPanel ungespeicherte
   * Änderungen enthält.
   * 
   * @param shouldBeSaved
   *          Angabe, ob das PetriNetPanel ungespeicherte Änderungen enthält
   *          true: Das PetriNetPanel hat ungespeicherte Änderungen false: Das
   *          PetriNetPanel hat keine ungespeicherte Änderungen
   */
  public void setShouldBeSaved(boolean shouldBeSaved) {
    this.shouldBeSaved = shouldBeSaved;
  }
}