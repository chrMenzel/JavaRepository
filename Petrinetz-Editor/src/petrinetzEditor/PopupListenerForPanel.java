package petrinetzEditor;

import java.awt.event.*;

import javax.swing.JPopupMenu;

/**
 * Die Klasse PopupListenerForPanel ist ein Listener für Klicks mit der rechten
 * Maustaste auf dem PetriNetPanel
 */
public class PopupListenerForPanel extends MouseAdapter {
  /**
   * Dies ist eine Referenz auf das zu zeigende Kontextmenü
   */
  private JPopupMenu popup;

  /**
   * Dies ist eine Referenz auf das PetriNetPanel
   */
  private PetriNetPanel panel;

  /**
   * Dies ist eine Referenz auf eine Kante des PetriNetzes
   */
  private Kante arrowNearClick;

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
  PopupListenerForPanel(JPopupMenu popupMenu, PetriNetPanel panel) {
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
    // die Prüfung auf deie rechte Maustaste mit if
    // (e.getButton() == MouseEvent.BUTTON3) erfolgen,
    // nicht mit if (e.isPopupTrigger()), da diese Variante offensichtlich
    // bei Apple-Rechnern nicht funktioniert

    // Wenn die rechte Maustaste betätigt wurde ...
    if (e.getButton() == MouseEvent.BUTTON3) {
      // Position des Mausklicks speichern
      clickX = e.getX();
      clickY = e.getY();

      // Ermitteln, ob eine Kante in der Umgebung war und wenn ja,
      // welche Kante am nächsten am Mausklick ist
      arrowNearClick = panel.getArrowNearPosition(clickX, clickY);

      // Wenn eine Kante ermittelt wurde
      if (arrowNearClick != null) {
        // Das Kontextmenü anzeigen
        showPopup(arrowNearClick);
      }
    }
  }

  /**
   * Diese Methode zeigt das Kontextmenü auf dem Panel
   * 
   * @param k
   *          Die Kante des PetriNetPanels, die der Anwender angeklickt hat
   */
  private void showPopup(Kante k) {
    // Dem Panel mitteilen, bei welchem Pfeil das Kontextmenü ist
    panel.setPopupAtArrow(k);
    // Kontextmenü anzeigen
    popup.show(panel, clickX, clickY);
  }
}
