package petrinetzEditor;

import interfaces.IScroller;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JScrollPane;

/**
 * Die Klasse Scroller kümmert sich um den Vorgang des automatischen Scrollens,
 * wenn der Anwender Petri-Netz-Elemente über die aktuellen Panel-Grenzen hinaus
 * verschiebt.
 */
public class Scroller implements IScroller {
  /**
   * Dies ist eine Referenz auf das PetriNetPanel, dem die Scroll-Leisten
   * zugeordet sind.
   */
  private PetriNetPanel panel;

  /**
   * Dies ist eine Referenz auf die Scroll-Leisten des PetriNetPanels.
   */
  private JScrollPane scrollPane;

  /**
   * Der Konstruktor der Klasse Scroller erzeugt ein neues Objekt dieser Klasse.
   * 
   * @param panel
   *          Das PetriNetPanel, dem die Scroll-Leisten zugeordnet sind
   * @param scrollPane
   *          Die Scroll-Leisten des PetriNetPanels
   */
  Scroller(PetriNetPanel panel, JScrollPane scrollPane) {
    this.panel = panel;
    this.scrollPane = scrollPane;
  }

  /**
   * Diese Methode passt die Scroll-Leisten und das Panel an, wenn der Anwender
   * ein Petri-Netz-Element über den rechten Panel-Rand hinausschiebt
   * 
   * @param value
   *          Die aktuelle X-Koordinate des Petri-Elements
   * @param widthOfFigures
   *          Die Breite der Petrinetz-Elemente
   */
  @Override
  public void adjustScrollPanesRight(int value, int widthOfFigures) {
    // Der Mindest-Abstand zur rechten Grenze des Panels wird hier als
    // die Breite der PetriNetFigures festgelegt
    int borderCoordinate = widthOfFigures;

    // Die bevorzugte Größe des Panels anpassen
    // X = größte X-Koordinate + Mindest-Abstand zur rechten Grenze des Panels
    // Y bleibt gleich
    Dimension newDim = new Dimension(value + borderCoordinate, (int) panel
        .getPreferredSize().getHeight());
    // Ausrichtung der Scrolleisten neu festsetzen
    Rectangle r = adjustPanelSizeAndGetVisibleRectangle(newDim);
    // Ganz nach rechts scrollen, in Y-Richtung gar nicht scrollen
    r.translate(value + borderCoordinate, 0);
    panel.scrollRectToVisible(r);
    slowDown();
  }

  /**
   * Diese Methode passt die Scroll-Leisten und das Panel an, wenn der Anwender
   * ein Petri-Netz-Element über den unteren Panel-Rand hinausschiebt
   * 
   * @param value
   *          Die aktuelle Y-Koordinate des Petri-Elements
   * @param heightOfFigures
   *          Die Höhe der Petrinetz-Elemente
   */
  @Override
  public void adjustScrollPanesDown(int value, int heightOfFigures) {
    // Der Mindest-Abstand zur unteren Grenze des Panels wird hier als
    // die Höhe der PetriNetFigures festgelegt
    int borderCoordinate = heightOfFigures;
    // Wenn die größte Y-Koordinate größer als die Höhe des Panels minus dem
    // Mindest-Abstand, ...
    if (value > (panel.getHeight() - borderCoordinate)) {
      // Die bevorzugte Größe des Panels anpassen
      // Y = größte Y-Koordinate + Mindest-Abstand zur unteren Grenze des
      // Fensters
      // X bleibt gleich
      Dimension newDim = new Dimension((int) panel.getPreferredSize()
          .getWidth(), value + borderCoordinate);

      // Ausrichtung der Scrolleisten neu festsetzen
      Rectangle r = adjustPanelSizeAndGetVisibleRectangle(newDim);
      // Ganz nach unten scrollen, in X-Richtung gar nicht scrollen
      r.translate(0, value + borderCoordinate);
      panel.scrollRectToVisible(r);
      slowDown();
    }
  }

  /**
   * Diese Methode passt die Scroll-Leisten und das Panel an, wenn der Anwender
   * ein Petri-Netz-Element über den linken Panel-Rand hinausschiebt
   * 
   * @param value
   *          Die aktuelle X-Koordinate des Petri-Elements (sollte negativ sein)
   * @param maxValue
   *          Die aktuell größte X-Koordinate aller Petri-Netz-Elemente
   * @param includingMovingFigures
   *          Ein Flag zur Angabe, ob auch die angeklickten Elemente verschoben
   *          werden sollen true: Alle Elemente, auch die die zum Verschieben
   *          markiert sind, werden verschoben false: Nur die nicht markierten
   *          Elemente werden verschoben
   */
  @Override
  public void adjustScrollPanesLeft(int value, int maxValue,
      int widthOfFigures, boolean includingMovingFigures) {
    // Linke Grenze ist die X-Koordinate 0
    int borderCoordinate = 0;
    // Wenn die kleinste X-Koordinate kleiner als die linke Grenze ist, ...
    if (value < borderCoordinate) {
      // Alle Elemente so nach rechts verschieben, dass die kleinste
      // X-Koordinate an der linken Grenze ist
      int adjustPixels = Math.abs(borderCoordinate - value);
      if (includingMovingFigures) {
        panel.moveAllElementsXIncludingMovingElements(adjustPixels);
      } else {
        panel.moveAllElementsXExceptMovingElements(adjustPixels);
      }

      // Die größte X-Koordinate ist das größte X aller Figuren plus
      // die Breite der Figuren + die Anpassung der X-Koordinaten
      int maxXinFuture = maxValue + widthOfFigures + adjustPixels;
      // Wenn die größte X-Koordinate größer ist, als die Breite des Panels
      // minus dem
      // Mindest-Abstand
      if (maxXinFuture > (int) panel.getWidth() - borderCoordinate) {
        // Die bevorzugte Größe des Panels anpassen
        // X = größte X-Koordinate + Mindest-Abstand zur rechten Grenze des
        // Fensters
        // Y bleibt gleich
        Dimension newDim = new Dimension(maxXinFuture, (int) panel
            .getPreferredSize().getHeight());
        // Ausrichtung der Scrolleisten neu festsetzen
        Rectangle r = adjustPanelSizeAndGetVisibleRectangle(newDim);
        panel.scrollRectToVisible(r);
        slowDown();
      }
    }
  }

  /**
   * Diese Methode passt die Scroll-Leisten und das Panel an, wenn der Anwender
   * ein Petri-Netz-Element über den oberen Panel-Rand hinausschiebt
   * 
   * @param value
   *          Die aktuelle Y-Koordinate des Petri-Elements (sollte negativ sein)
   * @param maxValue
   *          Die aktuell größte Y-Koordinate aller Petri-Netz-Elemente
   * @param heightOfFigures
   *          Die Höhe der Petrinetz-Elemente
   * @param includingMovingFigures
   *          Ein Flag zur Angabe, ob auch die angeklickten Elemente verschoben
   *          werden sollen true: Alle Elemente, auch die die zum Verschieben
   *          markiert sind, werden verschoben false: Nur die nicht markierten
   *          Elemente werden verschoben
   */
  @Override
  public void adjustScrollPanesUp(int value, int maxValue, int heightOfFigures,
      boolean includingMovingFigures) {
    // Obere Grenze ist die Y-Koordinate 0
    int borderCoordinate = 0;
    // Wenn die kleinste Y-Koordinate kleiner als die obere Grenze ist, ...
    if (value < borderCoordinate) {
      // Alle Elemente so nach unten verschieben, dass die kleinste
      // Y-Koordinate die Grenze ist
      int adjustPixels = Math.abs(borderCoordinate - value);
      if (includingMovingFigures) {
        panel.moveAllElementsYIncludingMovingElements(adjustPixels);
      } else {
        panel.moveAllElementsYExceptMovingElements(adjustPixels);
      }

      // Das neue maximale Y bestimmen:
      // Bisheriges Maximum + Höhe der Elemente + Anzahl Pixels im negativen
      // Bereich
      int maxYinFuture = maxValue + heightOfFigures + adjustPixels;

      // Die bevorzugte Größe des Panels anpassen
      if ((int) panel.getHeight() < maxYinFuture) {
        Dimension newDim = new Dimension((int) panel.getPreferredSize()
            .getWidth(), maxYinFuture);
        // Ausrichtung der Scrolleisten neu festsetzen
        Rectangle r = adjustPanelSizeAndGetVisibleRectangle(newDim);
        panel.scrollRectToVisible(r);
        slowDown();
      }
    }
  }

  /**
   * Diese Methode passt die Größe des Panels auf die übergebene dimension an
   * und gibt das aktuell sichtbare Rechteck zurück.
   * 
   * @param dimension
   *          Die Größe, die das Panel erreichen soll
   * @return Das aktuell sichtbare Rechteck des Panels
   */
  Rectangle adjustPanelSizeAndGetVisibleRectangle(Dimension dimension) {
    // Panelgröße neu setzen und Panel zum Neuzeichnen anmelden
    panel.setPreferredSize(dimension);
    panel.revalidate();

    // Die Scroll-Leiste neu zeichnen
    repaintScrollPane();

    // Das aktuelle Rechteck zurückgeben
    return panel.getVisibleRect();
  }

  /**
   * Diese Methode zeichnet die Scroll-Leisten neu
   */
  @Override
  public void repaintScrollPane() {
    scrollPane.revalidate();
    scrollPane.repaint();
  }

  /**
   * Diese Methode sorgt dafür, dass der Scroll-Vorgang beim Verschieben von
   * Elementen etwas langsamer vollzogen wird.
   */
  @Override
  public void slowDown() {
    // Damit das Scrollen nicht so rasend-schnell vollzogen wird, den Thread
    // kurz stoppen
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      // Nichts machen, wenn eine Exception auftritt
    }
  }

  /**
   * Diese Methode stellt eine Verbinung des Scrollers zum PetriNetPanel her.
   */
  @Override
  public void setPanel(PetriNetPanel panel) {
    this.panel = panel;
  }

  /**
   * Diese Methode stellt eine Verbinung des Scrollers zu den Scroll-Leisten
   * her.
   */
  @Override
  public void setScrollPane(JScrollPane scrollPane) {
    this.scrollPane = scrollPane;
  }
}