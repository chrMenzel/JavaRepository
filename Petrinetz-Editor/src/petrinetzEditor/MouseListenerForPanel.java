package petrinetzEditor;

import java.awt.event.*;

import javax.swing.SwingUtilities;

/**
 * Die Klasse MouseListenerForPanel ist der MouseListener für das PetriNetPanel.
 * Damit werden Maus-Aktivitäten (Bewegung, Klicks) überwacht.
 */
public class MouseListenerForPanel extends MouseAdapter {
  /**
   * Dies ist eine Referenz auf das PetriNetPanel, die Zeichenfläche dieser
   * Anwendung
   */
  private PetriNetPanel panel;

  /**
   * Dies ist der Konstruktor des MouseListenerForPanel. Mit diesem wird der
   * MouseListener erzeugt. Dabei wird ihm das PetriNetPanel als Parameter
   * übergeben.
   * 
   * @param panel
   *          Das bezüglich Mausaktivitäten zu überwachende PetriNetPanel
   */
  MouseListenerForPanel(PetriNetPanel panel) {
    this.panel = panel;
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPanel einen Klick
   * mit der Maus registriert (ausschließlich drücken der Taste, ohne
   * loslassen).
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mousePressed(MouseEvent e) {
    // Wenn das der Fall ist, das PetriNetPanel darüber informieren.
    // Der erste Parameter vom Typ PetriNetFigure ist hier null, da
    // das PetriNetPanel der Auslöser war und keine PetriNetFigure.
    panel.mousePressedLeft(null, e);
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPanel einen Klick
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
      // Der erste Parameter vom Typ PetriNetFigure ist hier null, da
      // das PetriNetPanel der Auslöser war und keine PetriNetFigure.
      panel.mouseClicked(null, e);
    }
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPanel eine
   * Bewegung des Mauszeigers über dem PetriNetPanel registriert.
   * 
   * @param e
   *          Das MouseEvent, das das Ereignis ausgelöst hat.
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    // Dem PetriNetPanel melden, dass sich der Mauszeiger bewegt hat.
    panel.mousePointerMoved(e.getX(), e.getY());
    // Das PetriNetPanel muss über diese Methode um den Eingabe-Focus anfragen,
    // sonst reagiert der KeyListener nicht mehr.
    panel.requestFocusInWindow();
  }

  /**
   * Diese Methode wird angesprochen, wenn der MouseListenerForPanel eine
   * Bewegung der Maus mit gedrückter Maustaste registriert.
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
}
