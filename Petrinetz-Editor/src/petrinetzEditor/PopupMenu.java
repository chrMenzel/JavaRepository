package petrinetzEditor;

import java.awt.event.MouseListener;

import javax.swing.*;

/**
 * Die Klasse PopupMenu repräsentiert das Kontextmenü, das sich öffnet, wenn der
 * Anwender mit der rechten Maustaste klickt.
 * 
 * Der entsprechende MouseListener wird der übergebenen Komponente hinzugefügt.
 */
class PopupMenu {
  /**
   * Dies ist eine Referenz zu einem Menüpunkt. Sie wird innerhalb der Methoden
   * mit dem entsprechenden zu erstellenden Menüpunkt bestückt.
   */
  private JMenuItem menuItem;

  /**
   * Diese Methode fügt einer Transition das entsprechende Kontextmenü hinzu
   * 
   * @param panel
   *          Das PetriNetPanel, auf dem sich die Transition befindet
   * @param transition
   *          Die Transition, die das Kontextmenü erhalten soll
   * 
   * @return Der Menüpunkt "Schalten/Simulieren" dieser Transition
   */
  JMenuItem buildAndAddPopupMenuForTransition(PetriNetPanel panel,
      PetriNetFigure transition) {

    // Neues Popup-Menü erstellen
    JPopupMenu popup = new JPopupMenu();

    // Menüpunkt für das Schalten/Simulieren gesondert merken, da er
    // zurückgegeben wird

    // Neuen Menüpunkt erstellen
    JMenuItem menuItemSimulate = new JMenuItem("Schalten/Simulieren");
    // Listener hinzufügen
    menuItemSimulate.addActionListener(panel);
    // Diesen Menüpunkt standardmäßig auf disabled stellen
    menuItemSimulate.setEnabled(false);
    // Menüpunkt dem Popup-Menü hinzufügen
    popup.add(menuItemSimulate);

    // Die weiteren Menüpunkte entsprechend erstellen
    menuItem = new JMenuItem("Benennen/Name ändern");
    menuItem.addActionListener(panel);
    popup.add(menuItem);

    menuItem = new JMenuItem("Größe einstellen");
    menuItem.addActionListener(panel);
    popup.add(menuItem);

    menuItem = new JMenuItem("Löschen");
    menuItem.addActionListener(panel);
    popup.add(menuItem);

    // Das Popup-Menü der Transition hinzufügen
    transition.add(popup);

    // Neuen Mauslistener für das Popup-Menü erstellen und der Transition
    // hinzufügen
    MouseListener popupListener = new PopupListenerForComponents(popup, panel);
    transition.addMouseListener(popupListener);

    // Menüpunkt für das Schalten/Simulieren zurückgeben
    return menuItemSimulate;
  }

  /**
   * Diese Methode fügt einer Stelle das entsprechende Kontextmenü hinzu
   * 
   * @param panel
   *          Das PetriNetPanel, auf dem sich die Stelle befindet
   * @param stelle
   *          Die Stelle, die das Kontextmenü erhalten soll
   */
  void buildAndAddPopupMenuForPlace(PetriNetPanel panel, PetriNetFigure stelle) {

    // Neues Popup-Menü erstellen
    JPopupMenu popup = new JPopupMenu();

    // Neuen Menüpunkt erstellen
    menuItem = new JMenuItem("Anzahl Token eingeben");
    // Listener hinzufügen
    menuItem.addActionListener(panel);
    // Menüpunkt dem Popup-Menü hinzufügen
    popup.add(menuItem);

    // Die weiteren Menüpunkte entsprechend erstellen
    menuItem = new JMenuItem("Benennen/Name ändern");
    menuItem.addActionListener(panel);
    popup.add(menuItem);

    menuItem = new JMenuItem("Größe einstellen");
    menuItem.addActionListener(panel);
    popup.add(menuItem);

    menuItem = new JMenuItem("Löschen");
    menuItem.addActionListener(panel);
    popup.add(menuItem);

    // Das Popup-Menü der Stelle hinzufügen
    stelle.add(popup);

    // Neuen Mauslistener für das Popup-Menü erstellen und der Stelle
    // hinzufügen
    MouseListener popupListener = new PopupListenerForComponents(popup, panel);
    stelle.addMouseListener(popupListener);
  }

  /**
   * Diese Methode fügt einem PetriNetPanel ein Kontextmenü für die Kanten hinzu
   * 
   * @param panel
   *          Das PetriNetPanel, auf dem sich die Kanten befinden
   */
  void buildAndAddPopupMenuForArrows(PetriNetPanel panel) {
    // Neues Popup-Menü erstellen
    JPopupMenu popup = new JPopupMenu();

    // Neuen Menüpunkt erstellen
    menuItem = new JMenuItem("Löschen");
    // Listener hinzufügen
    menuItem.addActionListener(panel);
    // Menüpunkt dem Popup-Menü hinzufügen
    popup.add(menuItem);

    // Das Popup-Menü dem Panel hinzufügen
    panel.add(popup);

    // Neuen Mauslistener für das Popup-Menü erstellen und dem Panel
    // hinzufügen
    MouseListener popupListener = new PopupListenerForPanel(popup, panel);
    panel.addMouseListener(popupListener);
  }
}