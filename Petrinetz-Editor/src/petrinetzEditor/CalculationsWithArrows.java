package petrinetzEditor;

import java.awt.geom.Point2D;
import javax.swing.*;

/**
 * Die Klasse CalculationsWithArrows stellt Methoden zur Verfügung, um den
 * Start- und Endpunkt eines Pfeils zwischen zwei PetriNetFigures so zu
 * bestimmen, dass der Pfeil auf einer virtuellen Line zwischen den
 * Mittelpunkten der Figuren liegt.
 */
public class CalculationsWithArrows {
  /**
   * Diese Methode errechnet den Start- und Endpunkt eines Pfeils zur
   * Darstellung einer Kante. Die Pfeile beginnen und enden am Rand der
   * jeweiligen PetriNetFigure und liegen auf einer virtuellen Linie zwischen
   * den Mittel- punkten der PetriNetFiguren.
   * 
   * @param panel
   *          Das PetriNetPanel, auf dem gezeichnet wird
   * @param figure1
   *          Die PetriNetFigure, von der der Pfeil ausgeht
   * @param figure2
   *          Die PetriNetFigure, zu der der Pfeil hinführt
   * @param x1
   *          Die X-Koordinate des Mittelpunkts von figure1
   * @param y1
   *          Die Y-Koordinate des Mittelpunkts von figure1
   * @param x2
   *          Die X-Koordinate des Mittelpunkts von figure2
   * @param y2
   *          Die Y-Koordinate des Mittelpunkts von figure2
   */
  void calculateArrow(PetriNetPanel panel, PetriNetFigure figure1,
      PetriNetFigure figure2, double x1, double y1, double x2, double y2) {
    // Von Kreis zu Quadrat
    if (figure1 instanceof Stelle && figure2 instanceof Transition) {
      // interceptionPointStart und interceptionPointEnd werden innerhalb
      // dieser Routine bestimmt.
      double[] points = calculateStartAndEndOfAlineAtCircles(panel, figure1,
          figure2, x1, y1, x2, y2);

      // Hier ist nur der Start-Punkt interessant
      panel.interceptionPointStart = new Point2D.Double(points[0], points[1]);

      // Die Position der Pfeilspitze mit Hilfe der Methode für Rechtecke
      // ermitteln
      panel.interceptionPointEnd = calculateStartOfLineAtRectangle(panel,
          figure1, figure2, panel.interceptionPointStart.getX(),
          panel.interceptionPointStart.getY(), x2, y2, false);
    }

    // Von Quadrat zu Kreis
    if (figure1 instanceof Transition && figure2 instanceof Stelle) {
      // Endpunkt des Pfeils mit Hilfe der nachfolgenden Methode bestimmen
      double[] points = calculateStartAndEndOfAlineAtCircles(panel, figure1,
          figure2, x1, y1, x2, y2);

      panel.interceptionPointStart = new Point2D.Double(points[0], points[1]);
      panel.interceptionPointEnd = new Point2D.Double(points[2], points[3]);

      // interceptionPointEnd ist jetzt schon mal bestimmt
      // Jetzt den Startpunkt ermitteln (so tun, als würde der Endpunkt der
      // Maus folgen)
      panel.interceptionPointStart = calculateStartOfLineAtRectangle(panel,
          figure1, figure2, x1, y1, panel.interceptionPointEnd.getX(),
          panel.interceptionPointEnd.getY(), true);
    }

    // Von Kreis zu Kreis
    if (figure1 instanceof Stelle && figure2 instanceof Stelle) {
      panel.makeClickOnElementsUndone();
      // Wenn erlaubt, die Zeile darüber auskommentieren und bei der Zeile
      // darunter den Kommentar entfernen
      // calculateStartAndEndOfAlineAtCircles(x1, y1, x2, y2, 2);
      // interceptionPointStart und interceptionPointEnd werden innerhalb
      // dieser Methode bestimmt
    }

    // Von Quadrat zu Quadrat
    if (figure1 instanceof Transition && figure2 instanceof Transition) {
      panel.makeClickOnElementsUndone();
      // Wenn erlaubt, die Zeile darüber auskommentieren und bei den beiden
      // Zeilen darunter den Kommentar entfernen
      // interceptionPointEnd = calculateStartOfLineAtRectangle(x1, y1, x2, y2,
      // false);
      // interceptionPointStart = calculateStartOfLineAtRectangle(x1, y1,
      // interceptionPointEnd.getX(), interceptionPointEnd.getY(), true);
    }
  }

  /**
   * Diese Methode berechnet den Start des Pfeils zur Darstellung einer Kante an
   * einem Rechteck / Quadrat, also einer Transition
   * 
   * @param panel
   *          Das Panel, auf dem der Pfeil dargestellt wird
   * @param figure1
   *          Die PetriNetFigure, von der der Pfeil ausgeht
   * @param figure2
   *          Die PetriNetFigure, zu der der Pfeil hinführt
   * @param xStart
   *          Die X-Koordinate des Mittelpunkts von figure1
   * @param yStart
   *          Die Y-Koordinate des Mittelpunkts von figure1
   * @param xDestination
   *          Die X-Koordinate des Mittelpunkts von figure2
   * @param yDestination
   *          Die Y-Koordinate des Mittelpunkts von figure2
   * @param destinationFollowsMouse
   *          Boolean-Wert, der angibt, ob die Pfeilspitze beim Mauszeiger endet
   *          true, wenn die Pfeilspitze an der Position des Mauszeigers endet
   *          false, wenn die Pfeilspite an der Ziel-PetriNetFigure endet
   * 
   * @return Der Point2D.Double, der den Schnittpunkt des Pfeils mit der
   *         Transition auf einer virtuellen Mittellinie zwischen den
   *         Mittelpunkten von figure1 und figure1 repräsentiert.
   */
  Point2D.Double calculateStartOfLineAtRectangle(JPanel panel,
      PetriNetFigure figure1, PetriNetFigure figure2, double xStart,
      double yStart, double xDestination, double yDestination,
      boolean destinationFollowsMouse) {
    // Zunächst muss die Steigung m der Gerade aus den
    // beiden Punkten berechnet werden, denn davon ist abhängig,
    // wo der Punkt am Rechteck landet
    double m = (yDestination - yStart) / (xDestination - xStart);
    // Teilt man Position, von der die Linie zum Viereck führt,
    // in folgede 8 Bereiche ein, ergibt sich im Uhrzeigersinn diese Einteilung:
    // A: oben rechts => Xmaus > Xmitte; Ymaus > Ymitte; m < -1
    // B: rechts oben => Xmaus > Xmitte; Ymaus > Ymitte; m < 0; m > -1
    // C: rechts unten => Xmaus > Xmitte; Ymaus < Ymitte; m > 0; m < 1
    // D: unten rechts => Xmaus > Xmitte; Ymaus < Ymitte; m > 1
    // E: unten links => Xmaus < Xmitte; Ymaus < Ymitte; m < -1
    // F: links unten => Xmaus < Xmitte; Ymaus < Ymitte; m < 0; m > -1
    // G: links oben => Xmaus < Xmitte; Ymaus > Ymitte; m > 0; m < 1
    // H: oben links => Xmaus < Xmitte; Ymaus > Ymitte; m > 1

    // Die Bereiche können verkleinert werden, indem man sich ansieht,
    // aus welcher Richtung der Pfeil kommt. Je nachdem soll die Spitze
    // am oberen, unteren, rechten oder linken Rand des Rechtecks / Quadrats
    // sein
    // Z. B. würde "von rechts" die Bereiche B und C zusammenfassen
    // Es ergeben sich dann 4 endgültige Bereiche:
    // von bzw. nach rechts: Xmaus >= Xmitte; Ymaus nicht relevant; m >= -1; m <
    // 1
    // von bzw. nach unten: Xmaus nicht relevant; Ymaus < Ymitte; m < -1; m >= 1
    // von bzw. nach links: Xmaus < Xmitte; Ymaus nicht relevant; m >= -1; m < 1
    // von bzw. nach oben: Xmaus nicht relevant; Ymaus >= Ymitte; m < -1; m >= 1

    if ((m >= -1) && (m < 1)) {
      // Entweder von bzw. nach rechts oder links

      // Aus der allgemeinen Geradengleichung
      // y = m * x + b ist das b zu ermitteln
      // Da zwei Punkte (Mittelpunkt des Rechtecks / Quadrats) und die
      // Mausposition bekannt sind, wird die Punktsteigungsform verwendet,
      // um das b der Geradengleichung zu ermitteln
      // Durch Umformung ergibt sich: b = y - m * x
      // Für x und y werden die Koordinaten eines bekannten Punktes der
      // Gerade (hier: Mittelpunkt des angeklickten Elements) eingesetzt.
      double b = yStart - m * xStart;

      // Für die allgemeine Geradengleichung y = m * x + b
      // sind jetzt die Werte m und b bekannt

      if (xDestination >= xStart) {
        // Zielposition ist rechts vom Startobjekt, somit muss das Zielobjekt an
        // der linken Seite getroffen werden.
        // Für den Schnittpunkt mit der linken Seite des Rechtecks / Quadrats
        // gilt, dass x dem X-Wert der rechten Seite entspricht.
        // Dadurch lässt sich y ausrechnen

        if (destinationFollowsMouse) {
          // Wenn das Ziel der Mauszeiger ist, muss lediglich die Startposition
          // bestimmt werden
          // x-Wert von interceptionPointStart = linke Seite + Breite des
          // Rechtecks / Quadrats
          // y-Wert von interceptionPointStart = (m * x-Wert von
          // interceptionPointStart + b)
          double xOfInterceptionPointStart = figure1.getX()
              + figure1.getRectangularShape().getWidth() + 1;
          return new Point2D.Double(xOfInterceptionPointStart, (m
              * xOfInterceptionPointStart + b));
        } else {
          // Wenn das Ziel ein Quadrat oder ein Rechteck ist,
          // ist der Endpunkt der Line
          // die linke Seite des Quadrats / Rechtecks
          // x-Wert von interceptionPointEnd = linke Seite
          // y-Wert von interceptionPointEnd = (m * x-Wert von
          // interceptionPointEnd + b)
          double xOfInterceptionPointEnd = figure2.getX();
          return new Point2D.Double(xOfInterceptionPointEnd, (m
              * xOfInterceptionPointEnd + b));
        }
      } else {
        // Zielposition ist links vom Startobjekt, somit muss das Zielobjekt an
        // der rechten Seite getroffen werden.
        // Für den Schnittpunkt mit der rechten Seite des Rechtecks / Quadrats
        // gilt, dass x dem X-Wert der linken Seite + der Breite des Rechtecks /
        // Quadrats entspricht.
        // Dadurch lässt sich y ausrechnen

        if (destinationFollowsMouse) {
          // Wenn das Ziel der Mauszeiger ist, muss lediglich die Startposition
          // bestimmt werden
          // x-Wert von interceptionPointStart = linke Seite
          // y-Wert von interceptionPointStart = (m * x-Wert von
          // interceptionPointStart + b)
          double xOfInterceptionPointStart = figure1.getX();
          return new Point2D.Double(xOfInterceptionPointStart, (m
              * xOfInterceptionPointStart + b) + 1);
        } else {
          // Wenn das Ziel ein Rechteck / Quadrat ist, ist der Endpunkt der Line
          // die rechte Seite des Rechtecks / Quadrats
          // x-Wert von interceptionPointEnd = linke Seite + Breite des
          // Rechtecks / Quadrats
          // y-Wert von interceptionPointEnd = (m * x-Wert von
          // interceptionPointEnd + b)
          double xOfInterceptionPointEnd = figure2.getX()
              + figure2.getRectangularShape().getWidth() + 1;
          return new Point2D.Double(xOfInterceptionPointEnd, (m
              * xOfInterceptionPointEnd + b) + 1);
        }
      }
    } else {
      // Entweder von bzw. nach oben oder unten

      if (destinationFollowsMouse) {
        // Die Länge der Gegenkathete ist die halbe Höhe
        // Gesucht ist die Länge der Ankathete
        double laengeGegenkathete = (figure1.getRectangularShape().getHeight() + 1) / 2;
        double laengeAnkathete = laengeGegenkathete / m;
        if (yDestination >= yStart) {
          // unten
          return new Point2D.Double((xStart + laengeAnkathete),
              (yStart + laengeGegenkathete));
        } else {
          // oben
          return new Point2D.Double((xStart - laengeAnkathete),
              (yStart - laengeGegenkathete));
        }
      } else {
        // Die Länge der Gegenkathete ist die halbe Höhe
        // Gesucht ist die Länge der Ankathete
        double laengeGegenkathete = (figure2.getRectangularShape().getHeight() + 1) / 2;
        double laengeAnkathete = laengeGegenkathete / m;
        if (yDestination >= yStart) {
          // Zielposition ist unterhalb des Startobjekts, somit muss das
          // Zielobjekt an der oberen Seite getroffen werden.
          return new Point2D.Double((xDestination - laengeAnkathete),
              (yDestination - laengeGegenkathete));
        } else {
          // Zielposition ist oberhalb des Startobjekts, somit muss das
          // Zielobjekt an der unteren Seite getroffen werden.
          return new Point2D.Double((xDestination + laengeAnkathete),
              (yDestination + laengeGegenkathete) + 1);
        }
      }
    }
  }

  /**
   * Diese Methode errechnet den Start- und Endpunkt eines Pfeils an einem Kreis
   * auf einer virtuellen Mittellinie zwischen den beiden Punkten. Die
   * Schnittpunkte werden in einem double-Datenfeld von 4 Werten zurück-
   * gegeben, die die X-/Y-Koordinaten der beiden Punkte repräsentieren.
   * 
   * @param panel
   *          Das Panel, auf dem der Pfeil dargestellt wird
   * @param figure1
   *          Die PetriNetFigure, von der der Pfeil ausgeht
   * @param figure2
   *          Die PetriNetFigure, zu der der Pfeil hinführt
   * @param xCenterOfStart
   *          Die X-Koordinate des Mittelpunkts von figure1
   * @param yCenterOfStart
   *          Die Y-Koordinate des Mittelpunkts von figure1
   * @param xCenterOfEnd
   *          Die X-Koordinate des Mittelpunkts von figure2
   * @param yCenterOfEnd
   *          Die Y-Koordinate des Mittelpunkts von figure2
   * 
   * @return Ein Feld mit 4 double-Werten, die die X-/Y-Koordinaten von Start-
   *         und Endpunkt repräsentieren
   */
  double[] calculateStartAndEndOfAlineAtCircles(JPanel panel,
      PetriNetFigure figure1, PetriNetFigure figure2, double xCenterOfStart,
      double yCenterOfStart, double xCenterOfEnd, double yCenterOfEnd) {
    // Entfernung zwischen den beiden Punkten ermitteln
    double d = Point2D.distance(xCenterOfStart, yCenterOfStart, xCenterOfEnd,
        yCenterOfEnd);

    // Radius der Stelle(n): Durchmesser durch 2
    double r = (figure1.getRectangularShape().getWidth()) / 2.0;
    // Schnittpunkte mit dem Kreisrand bestimmen
    // (xMaus - xMitte) ist die Differenz der X-Koordinaten (y entsprechend Y)
    double values[] = { 0, 0, 0, 0 };
    values[0] = xCenterOfStart + r / d * (xCenterOfEnd - xCenterOfStart);
    values[1] = yCenterOfStart + r / d * (yCenterOfEnd - yCenterOfStart);
    values[2] = xCenterOfEnd + r / d * (xCenterOfStart - xCenterOfEnd);
    values[3] = yCenterOfEnd + r / d * (yCenterOfStart - yCenterOfEnd);

    return values;
  }
}
