package petrinetzEditor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JToggleButton;

/**
 * Die Klasse ToolBarButtonListener gibt dem Fenster, in dem sich der Button
 * befindet, eine Nachricht, wenn der Anwender den entsprechenden Button klickt.
 */
final class ToolBarButtonListener extends AbstractAction {
  /**
   * Eine Referenz auf das Hauptfenster dieser Anwendung, in dem sich die
   * Toolbar befindet
   */
  private MainFrame frame;

  /**
   * Der Konstruktor der Klasse erzeugt ein neues Objekt ToolBarButtonListener
   * 
   * @param frame
   *          Das Fenster, in dem sich die ToolBar befindet
   */
  ToolBarButtonListener(MainFrame frame) {
    this.frame = frame;
  }

  /**
   * Diese Methode meldet dem Fenster den JToggleButton, der geklickt wurde.
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    frame.toolBarButtonClicked((JToggleButton) event.getSource());
  }
}
