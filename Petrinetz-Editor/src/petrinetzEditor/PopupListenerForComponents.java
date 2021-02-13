package petrinetzEditor;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Die Klasse PopupListenerForPanel ist ein Listener für Klicks mit der rechten
 * Maustaste auf den Elementen (Klasse PetriNetFigure) des PetriNetPanel
 */
class PopupListenerForComponents extends MouseAdapter {
  /**
   * Dies ist eine Referenz auf das zu zeigende Kontextmenü
   */
  private JPopupMenu popup;

  /**
   * Dies ist eine Referenz auf das PetriNetPanel
   */
  private PetriNetPanel panel;

  /**
   * Diese Variable ist die X-Koordinate des Mausklicks
   */
  private int clickX;

  /**
   * Diese Variable ist die Y-Koordinate des Mausklicks
   */
  private int clickY;

  /**
   * Der Konstruktor erstellt einen neuen PopupListener
   * 
   * @param popupMenu
   *          Das zu zeigende PopupMenu
   * @param panel
   *          Das PetriNetPanel, auf dem die Mausklicks überwacht werden
   */
  PopupListenerForComponents(JPopupMenu popupMenu, PetriNetPanel panel) {
    this.popup = popupMenu;
    this.panel = panel;
  }

  /**
   * Die Methode mousePressed leitet die Überprüfung ein, ob das Popup-Menü
   * erscheinen soll
   */
  @Override
  public void mousePressed(MouseEvent e) {
    checkForPopup(e);
  }

  /**
   * Die Methode mouseReleased leitet die Überprüfung ein, ob das Popup-Menü
   * erscheinen soll
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    checkForPopup(e);
  }

  /**
   * Diese Methode ermittelt, ob das Kontextmenü erscheint
   * 
   * @param e
   *          Das auslösende Mausereignis
   */
  private void checkForPopup(MouseEvent e) {
    // Lt. Empfehlung in Newsgroup (17.12.2014, 13:08 Uhr) sollte
    // die Prüfung mit if (e.getButton() == MouseEvent.BUTTON3) erfolgen,
    // nicht mit if (e.isPopupTrigger()), da diese Variante offensichtlich
    // bei Apple-Rechnern nicht funktioniert

    // Wenn die rechte Maustaste betätigt wurde ...
    if (e.getButton() == MouseEvent.BUTTON3) {
      // Position des Mausklicks speichern
      clickX = e.getX();
      clickY = e.getY();

      // Ermitteln ob und wenn ja, welche PetriNetFigure (hier noch vom
      // Typ Component) sich an dieser Mausposition befindet
      Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(),
          clickX, clickY);

      // Wenn ein Pfeil ermittelt wurde
      if (c != null) {
        // das Kontextmenü anzeigen
        showPopup(e, c);
      }
    }
  }

  /**
   * Diese Methode zeigt das Kontextmenü auf der PetriNetFigure an
   * 
   * @param e
   *          Das Ereignis, das den Aufruf des Popup-Menüs ausgelöst hat
   * @param c
   *          Die PetriNetFigure (hier noch vom Typ Component) des
   *          PetriNetPanels, die der Anwender angeklickt hat
   */
  private void showPopup(MouseEvent e, Component c) {
    // Dem Panel mitteilen, bei welcher PetriNetFigure das Kontextmenü ist
    panel.setPopupAtComponent(c);
    // Kontextmenü anzeigen
    popup.show(c, clickX, clickY);
  }
}