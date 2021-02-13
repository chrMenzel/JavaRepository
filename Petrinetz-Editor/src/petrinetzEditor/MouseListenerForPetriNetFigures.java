package petrinetzEditor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * Die Klasse MouseListenerForPetriNetFigures ist der MouseListener für die
 * Elemente des Petri-Netzes (Typ PetriNetFigure). Damit werden Maus-Aktivitäten
 * (Bewegung, Klicks) überwacht.
 */
public class MouseListenerForPetriNetFigures extends MouseAdapter {
  /**
   * Dies ist eine Referenz auf das PetriNetPanel, die Zeichenfläche dieser
   * Anwendung
   */
  private PetriNetPanel panel;

  /**
   * Dies ist eine Referenz auf die PetriNetFigure, die das Ereignis ausgelöst
   * hat.
   */
  private PetriNetFigure figure;

  /**
   * Dies ist der Konstruktor des MouseListenerForPetriNetFigures. Mit diesem
   * wird der MouseListener erzeugt. Dabei werden ihm das PetriNetPanel die die
   * PetriNetFigure, die den Listener erhalten soll, übergeben. Parameter
   * übergeb.
   * 
   * @param panel
   *          Das PetriNetPanel, auf dem sich die Petrinetz-Elemente befinden
   * 
   * @param figure
   *          Die PetriNetFigure, die den Listener erhält
   */
  MouseListenerForPetriNetFigures(PetriNetPanel panel, PetriNetFigure figure) {
    this.panel = panel;
    this.figure = figure;
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPetriNetFigures
   * einen Klick mit der Maus registriert (ausschließlich drücken der Taste,
   * ohne loslassen).
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mousePressed(MouseEvent e) {
    // Wenn das der Fall ist, die in der Component-Hierarchie unterste
    // Komponente
    // holen, die an dieser Mausposition ist
    Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(),
        e.getX(), e.getY());
    // und das PetriNetPanel darüber informieren.
    panel.mousePressedLeft((PetriNetFigure) c, e);
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPetriNetFigures
   * mit der Maus registriert (drücken und loslassen der Maus).
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    // Wenn es sich um die linke Maustaste gehandelt hat, ...
    if (SwingUtilities.isLeftMouseButton(e)) {
      // ... das PetriNetPanel darüber informieren
      panel.mouseClicked(figure, e);
    }
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPetriNetFigures
   * das Loslassen einer Maustaste registriert.
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    // Wenn das der Fall ist, die in der Component-Hierarchie unterste
    // Komponente
    // holen, die an dieser Mausposition ist
    Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(),
        e.getX(), e.getY());
    // und das PetriNetPanel darüber informieren.
    panel.mouseReleased((PetriNetFigure) c, e);
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPetriNetFigures
   * eine Bewegung der Maus mit gedrückter Maustaste registriert.
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    // Wenn es sich um die linke Maustaste gehandelt hat, ...
    if (SwingUtilities.isLeftMouseButton(e)) {
      // ... dem PetriNetPanel dieses Ereignis melden
      panel.mouseDragged(e);
    }
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPetriNetFigures
   * eine Bewegung des Mauszeigers über der PetriNetFigure registriert.
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    panel.mousePointerMovedOverComponent(figure, e.getX(), e.getY());
  }
}