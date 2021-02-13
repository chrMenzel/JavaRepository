package petrinetzEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Die Klasse SizeFrame repräsentiert ein Fenster, in dem der Anwender die Größe
 * der Petrinetz-Elemente verändern kann.
 */
class SizeFrame extends JFrame implements ChangeListener {
  /**
   * Die Breite des Fensters in Pixel als Konstante
   */
  private final int WINDOW_WIDTH = 300;

  /**
   * Die Höhe des Fensters in Pixel als Konstante
   */
  private final int WINDOW_HEIGHT = 300;

  /**
   * Eine Referenz auf das JPanel dieses Fensters
   */
  private JPanel thisPanel;

  /**
   * Dies ist eine Referenz auf das eigentliche Petri-Netz, in sich die zu
   * vergrößernden / zu verkleinernden Elemente befinden.
   */
  private PetriNetPanel petriNetPanel;

  /**
   * Dies ist eine Referenz auf den Schieberegler, mit dem der Anwender die
   * Größe der Elemente beeinflusst
   */
  private JSlider slider;

  /**
   * Dies ist eine Referenz auf die Stelle in diesem Fenster (zur Vorschau für
   * den Anwender)
   */
  private Stelle stelle;

  /**
   * Dies ist eine Referenz auf die Transition in diesem Fenster (zur Vorschau
   * für den Anwender)
   */
  private Transition transition;

  /**
   * Dies ist eine Referenz auf die Kante von der Stelle zur Transition in
   * diesem Fenster (zur Vorschau für den Anwender)
   */
  private Kante arrow;

  /**
   * Dies ist eine Referenz auf die Klasse CalculationsWithArrows, die zur
   * Berechnung der Kantenenden dient
   */
  private CalculationsWithArrows calculationsWithArrows = new CalculationsWithArrows();

  /**
   * Dies ist eine Referenz auf den JButton OK in diesem Fenster
   */
  private JButton buttonOK;

  /**
   * Dies ist eine Referenz auf den JButton Abbrechen in diesem Fenster
   */
  private JButton buttonCancel;

  /**
   * Diese Variable speichert die Breite der Petrinetz-Elemente.
   */
  private int widthOfFigures;

  /**
   * Diese Variable speichert die Höhe der Petrinetz-Elemente.
   */
  private int heightOfFigures;

  /**
   * Der Konstruktor der Klasse SizeFrame erstellt ein neues Objekt dieser
   * Klasse.
   * 
   * @param petriNetPanel
   *          Das PetriNetPanel, das die Petrinetz-Elemente enthält
   * @param widthOfFigures
   *          Die Breite der Elemente
   * @param heightOfFigures
   *          Die Höhe der Elemente
   */
  SizeFrame(PetriNetPanel petriNetPanel, int widthOfFigures, int heightOfFigures) {
    super("Größe einstellen");
    this.petriNetPanel = petriNetPanel;
    this.widthOfFigures = widthOfFigures;
    this.heightOfFigures = heightOfFigures;

    // Panel einrichten
    thisPanel = new JPanel(null) {
      @Override
      protected void paintComponent(Graphics g) {
        // Graphics in ein Graphics2D wandeln
        Graphics2D g2d = (Graphics2D) g;
        // Glatte Kreise und Linien malen, keine Pixelstruktur
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        // Der Text der Stelle und der Pfeil sind keine
        // JComponents und müssen explizit neu gezeichnet werden
        stelle.writeLabelText(g2d);
        arrow.paintFigure(g2d);
      }
    };

    // Fenstereigenschaften festlegen
    setWindowProperties();
    // Elemente des Fensters hinzufügen
    addContent();
  }

  /**
   * Diese Methode legt grundlegende Eigenschaften des Fensters fest, wie Größe
   * und Reaktion auf den Schließen-Button
   */
  private void setWindowProperties() {
    // Größe des Fensters
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    // Das Panel enthält den Fensterinhalt
    setContentPane(thisPanel);
    // Schließen-Button: Nichts tun, es wird eine eigene Methode verwendet
    // (sh. nachfolgende Implementierung des WindowListeners)
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    // Listener für den Schließen-Button hinzufügen
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        // Wenn betätigt, eigene Methode zum Schließen der Anwendung aufrufen
        closeBecauseCancel();
      }
    });

    // Anwender soll die Fenstergröße nicht ändern
    setResizable(false);
    // Fenster in der Mitte des Bildschirms platzieren
    setLocationRelativeTo(null);
  }

  /**
   * Diese Methode fügt dem Fenster die erforderlichen Elemente (Schieberegler,
   * Buttons OK und Abbrechen, eine Stelle, eine Transition und eine Kante)
   * hinzu.
   */
  private void addContent() {
    // Schieberegler hinzufügen
    addSlider();

    // Buttons hinzufügen
    buttonOK = initButton("OK", 190, 200);
    buttonCancel = initButton("Abbrechen", 190, 235);
    addButton(buttonOK);
    addButton(buttonCancel);

    // Petrinetz-Elemente hinzufügen
    addPlace();
    addTransition();
    addArrow();
  }

  /**
   * Diese Methode fügt dem Panel einen Pfeil von der Stelle zur Transition
   * hinzu
   */
  private void addArrow() {
    // Start- und Endpunkt des Pfeils berechnen
    Point startPointArrow = calculateStartPointOfArrow();
    Point endPointArrow = calculateEndPointOfArrow();
    // Pfeil / Kante hinzufügen
    arrow = new Kante(stelle, transition, startPointArrow, endPointArrow,
        slider.getValue() / 3);
  }

  /**
   * Diese Methode berechnet den Startpunkt des Pfeils
   * 
   * @return Die Position des Pfeil-Startpunkts als Point
   */
  private Point calculateStartPointOfArrow() {
    // Zur Berechnung der Pfeilspitze wird die Methode
    // calculateStartAndEndOfAlineAtCircles der Klasse CalculationsWithArrows
    // herangezogen
    double[] points = calculationsWithArrows
        .calculateStartAndEndOfAlineAtCircles(thisPanel,
            (PetriNetFigure) stelle, (PetriNetFigure) transition, stelle
                .getRectangularShape().getCenterX(), stelle
                .getRectangularShape().getCenterY(), transition
                .getRectangularShape().getCenterX(), transition
                .getRectangularShape().getCenterY());

    return new Point((int) points[0], (int) points[1]);
  }

  /**
   * Diese Methode berechnet den Endpunkt (der Spitze) des Pfeils.
   * 
   * @return Die Position des Pfeil-Endpunkts als Point
   */
  private Point calculateEndPointOfArrow() {
    // Zur Berechnung der Pfeilspitze wird die Methode
    // calculateStartOfLineAtRectangle der Klasse CalculationsWithArrows
    // herangezogen
    Point2D.Double endPoint = calculationsWithArrows
        .calculateStartOfLineAtRectangle(thisPanel, (PetriNetFigure) stelle,
            (PetriNetFigure) transition, stelle.getRectangularShape()
                .getCenterX(), stelle.getRectangularShape().getCenterY(),
            transition.getRectangularShape().getCenterX(), transition
                .getRectangularShape().getCenterY(), false);

    // Punkt mit int-Werten zurückgeben
    return new Point((int) endPoint.getX() - 1, (int) endPoint.getY());
  }

  /**
   * Diese Methode fügt dem Panel eine Stelle des Petri-Netzes hinzu In der
   * Klasse Stelle besteht dafür ein eigener Konstruktor, damit keine Listener
   * hinzugefügt werden.
   */
  private void addPlace() {
    // Neue Stelle ertellen
    stelle = new Stelle(thisPanel, 200, 10, widthOfFigures, heightOfFigures);
    // Das RectangularShape existiert noch nicht, daher die Begrenzung
    // heranziehen
    // (wird für die Berechnung der Kante / des Pfeils benötigt)
    stelle.setRectangularShape(stelle.getBounds());
    stelle.setLabelText("Beispiel");
    // Anzahl Tokens auf 1 setzen, damit der Punkt erkennbar ist
    stelle.setNumberOfTokens(1);
    // Stelle dem Panel hinzufügen
    thisPanel.add(stelle);
  }

  /**
   * Diese Methode fügt dem Panel eine Transition des Petri-Netzes hinzu In der
   * Klasse Transition besteht dafür ein eigener Konstruktor, damit keine
   * Listener hinzugefügt werden.
   */
  private void addTransition() {
    // Neue Transition ertellen
    transition = new Transition(thisPanel, 80, 100, widthOfFigures,
        heightOfFigures);
    // Das RectangularShape existiert noch nicht, daher die Begrenzung
    // heranziehen
    // (wird für die Berechnung der Kante / des Pfeils benötigt)
    transition.setRectangularShape(transition.getBounds());
    // Transition in grün zeichnen, da sie aktiviert ist
    transition.setColor(petriNetPanel.colorActivated);
    // Transition dem Panel hinzufügen
    thisPanel.add(transition);
  }

  /**
   * Diese Methode fügt dem Panel einen Schieberegler hinzu
   */
  private void addSlider() {
    // Vertikalen Schieberegler mit den Werten von 20 bis 70 erstellen
    slider = new JSlider(JSlider.VERTICAL, 20, 70, widthOfFigures);
    // Position und Größe des Schiebereglers
    slider.setLocation(10, 10);
    slider.setSize(50, 250);
    // Alle 5 Werte größer darstellen, aber wirklich alle Werte zeichnen
    slider.setMajorTickSpacing(5);
    slider.setMinorTickSpacing(1);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);

    // Listener hinzufügen
    slider.addChangeListener(this);
    // JSlider auf Panel darstellen
    thisPanel.add(slider);
  }

  /**
   * Diese Methode reagiert auf die Veränderung des Werts auf dem JSlider.
   * 
   * @param e
   *          Das auslösende Event
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    // Wert des Schiebereglers sichern
    int currentSize = slider.getValue();
    // Die Stelle und Transition des Fensters entsprechend
    // vergrößern/verkleinern
    // und als RectangularShape den Rahmen des Elements nehmen
    // Das RectangularShape wird zur Berechnung des Pfeils verwendet
    stelle.setSize(currentSize, currentSize);
    stelle.setRectangularShape(stelle.getBounds());
    transition.setSize(currentSize, currentSize);
    transition.setRectangularShape(transition.getBounds());

    // Neuen Pfeil erstellen
    addArrow();

    // Den entsprechenden Bereich des Fensters neu zeichnen
    repaint(70, 0, 300, 180);
  }

  /**
   * Diese Methode erstellt einen neuen Button mit der Größe (100, 25) und setzt
   * ihn an die gewünschte Position. Ein ActionListener ist damit noch nicht
   * implementiert.
   * 
   * @param labelText
   *          Der Text des JButtons
   * @param posX
   *          Die X-Koordinate der linken oberen Ecke des JButtons
   * @param posY
   *          Die Y-Koordinate der linken oberen Ecke des JButtons
   * @return newButton Der neu erstellte JButton
   */
  private JButton initButton(String labelText, int posX, int posY) {
    // neuen Button mit dem übergebenen Text erstellen
    JButton newButton = new JButton(labelText);
    // Position und Größe festlegen
    newButton.setLocation(posX, posY);
    newButton.setSize(100, 25);
    // neuen Button zurückgeben
    return newButton;
  }

  /**
   * Diese Methode fügt den beiden Buttons OK und Abbrechen einen individuellen
   * ActionListener hinzu und fügt den Button in das panel ein.
   * 
   * @param button
   *          Der hinzufügende JButton (OK oder Abbrechen)
   */
  private void addButton(JButton button) {
    // Bei Klick auf den OK-Button
    if (button.getText().equals("OK")) {
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // wird der Wert des Schiebereglers gesichert
          int size = slider.getValue();
          // Die Werte auch in dieser Klasse speichern,
          // da das Fenster im Hintergrund immer noch vorhanden ist
          // und so beim erneuten Öffnen die aktuellen Werte vorhanden sind.
          widthOfFigures = size;
          heightOfFigures = size;
          // Das Fenster wird geschlossen
          SizeFrame.this.dispose();
          // und dem Petri-Netz die gewählte Größe mitgeteilt
          petriNetPanel.resizeFigures(size);
        }
      });
    } else {
      // Bei Klick auf den Abbrechen-Button
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // soll sich das Fenster schließen
          SizeFrame.this.closeBecauseCancel();
        }
      });
    }

    // Der Button wird dem Panel hinzugefügt
    thisPanel.add(button);
  }

  /**
   * Diese Methode schließt das Fenster, es ist jedoch im Hintergrund noch
   * vorhanden, so dass die Einstellungen erhalten bleiben.
   */
  private void closeBecauseCancel() {
    // Da der Anwender die Größe in dem Fenster verschoben haben könnte,
    // zur Sicherheit den Slider noch auf den alten Wert stellen.
    slider.setValue(widthOfFigures);
    // Fenster schließen
    this.dispose();
  }
}