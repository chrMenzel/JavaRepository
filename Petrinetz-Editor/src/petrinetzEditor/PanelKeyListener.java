package petrinetzEditor;

import java.awt.event.*;

/**
 * Die Klasse PanelKeyListener überwacht Tastaturaktivitäten auf dem
 * PetriNetPanel
 */
class PanelKeyListener extends KeyAdapter {
  /**
   * Dies ist eine Referenz auf das PetriNetPanel, das überwacht wird
   */
  private PetriNetPanel panel;

  /**
   * Der Konstruktor erstellt einen neuen PanelKeyListener für das übergebene
   * PentiNetPanel.
   * 
   * @param panel
   *          Das zu überwachende PetriNetPanel
   */
  PanelKeyListener(PetriNetPanel panel) {
    this.panel = panel;
  }

  /**
   * Die keyPressed-Methode führt Folgeaktivitäten aus, wenn eine Taste gedrückt
   * wurde.
   * 
   * @param e
   *          Das Tastaturereignis
   */
  @Override
  public void keyPressed(KeyEvent e) {
    // Aus dem Tastaturereignis den Schlüssel der Taste holen
    int keycode = e.getKeyCode();
    // Wenn es die Ctrl-Taste oder die Shift-Taste war, dies im PetriNetPanel
    // in einer Variable festhalten
    panel.ctrlPressed = (keycode == 17);
    panel.shiftPressed = (keycode == 16);
  }

  /**
   * Die keyReleased-Methode führt Folgeaktivitäten aus, wenn eine Taste
   * losgelassen wurde.
   * 
   * @param e
   *          Das Tastaturereignis
   */
  @Override
  public void keyReleased(KeyEvent e) {
    // Aus dem Tastaturereignis den Schlüssel der Taste holen
    int keycode = e.getKeyCode();

    // Wenn es die Ctrl-Taste oder die Shift-Taste war, dies im PetriNetPanel
    // in einer Variable festhalten
    if (keycode == 17) panel.ctrlPressed = false;
    if (keycode == 16) panel.shiftPressed = false;
  }
}