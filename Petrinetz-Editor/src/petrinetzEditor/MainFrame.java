package petrinetzEditor;

import interfaces.IFileDialog;
import interfaces.IScroller;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import common.*;

/**
 * Die Klasse MainFrame ist eine von JFrame abgeleitete Klasse und repräsentiert
 * das Hauptfenster dieser Anwendung.
 */
final class MainFrame extends JFrame {
  /**
   * Dies ist eine Konstante, die die Start-Breite des Haupt-Fensters dieser
   * Anwendung definiert.
   */
  private static final int WINDOW_WIDTH = 550;

  /**
   * Dies ist eine Konstante, die die Start-Höhe des Haupt-Fensters dieser
   * Anwendung definiert.
   */
  private static final int WINDOW_HEIGHT = 450;

  /**
   * Eine Referenz auf die JToolBar dieses Fensters
   */
  private JToolBar toolBar;

  /**
   * Eine Referenz auf das PetriNetPanel (von JPanel abgeleitet), also die
   * Zeichenfläche des Fensters
   */
  private PetriNetPanel panel;

  /**
   * Eine Referenz auf den Datei-Dialog. Diese Referenz wird gleich
   * initialisiert, um nicht jedes Mal ein neues Fenster zu erstellen und vor
   * allem, dass die vorherigen Einstellungen erhalten bleiben.
   * 
   * Damit wird die in Punkt 4.2 der Aufgabenstellung genannte Anforderung "Bei
   * mehrmaliger Benutzung eines Dateiauswahldialogs während desselben
   * Programmlaufs soll dieser im zuletzt verwendeten Verzeichnis geöffnet
   * werden." automatisch erfüllt.
   */
  private IFileDialog fileDialog = new FileDialogWindow();

  /**
   * Eine Referenz auf den Scroller (gesonderte Klasse in dieser Anwendung), der
   * sich um den Scroll-Vorgang kümmert.
   */
  private IScroller scroller;

  /**
   * Eine Referenz auf den Rückgabewert Datei-Speichern-Dialogs
   */
  private int saveDialogReturnState;

  /**
   * Referenzen auf die Aktionen, die bei Klick auf einen Button der ToolBar
   * erfolgen sollen
   */
  private Action fileOpenAction = new ToolBarButtonListener(this);
  private Action fileSaveAction = new ToolBarButtonListener(this);
  private Action selectPlaceAction = new ToolBarButtonListener(this);
  private Action selectTransitionAction = new ToolBarButtonListener(this);
  private Action selectArrowAction = new ToolBarButtonListener(this);
  private Action selectTokenAction = new ToolBarButtonListener(this);
  private Action selectMovingAction = new ToolBarButtonListener(this);

  /**
   * Referenzen auf die JToggleButtons der Toolbar
   */
  private JToggleButton fileLoad;
  private JToggleButton fileSave;
  private JToggleButton place;
  private JToggleButton transition;
  private JToggleButton arrow;
  private JToggleButton token;
  private JToggleButton move;

  /**
   * Der Konstruktor der Klasse MainFrame erzeugt ein neues Objekt dieser
   * Klasse.
   * 
   * @param title
   *          Der Titel des Fensters
   * @param panel
   *          Das PetriNetPanel des Fensters (eine von JPanel abgeleitete Klasse
   *          dieser Anwendung)
   */
  MainFrame(String title, PetriNetPanel panel) {
    super(title);
    this.panel = panel;

    // Aussehen des Fensters festlegen und sichtbar machen
    setMainWindowAttributes();
    setVisible(true);

    // PreferredSize des Panels so einstellen, dass beim Start der Anwendung
    // keine Scrollleisten erscheinen
    panel.setActualSizeAsPreferredSize();
  }

  /**
   * Diese Methode legt die Bestandteile des Fensters wie Look and Feel, ToolBar
   * und Zeichenfläche fest.
   */
  private void setMainWindowAttributes() {
    // Zunächst Look and Feel festlegen
    try {
      // Sieht am besten aus!
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch (Exception e1) {
      // Wenn das Look and Feel nicht klappen sollte, ist es auch kein Problem
    }

    // bevorzugte Fenstergröße setzen
    this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

    // Toolbar hinzufügen (ganz oben im Fenster)
    addToolBar();

    // Panel einrichten (direkt unter der ToolBar)
    panel.setBackground(Color.white);
    this.add(panel, BorderLayout.CENTER);

    // Panel dem FileDialog bekannt machen
    fileDialog.setPanel(panel);

    // Scroll-Leisten hinzufügen bzw. vorsehen
    // Die Scroll-Leisten würden das Panel überdecken
    addScrollPane();

    // Alle Komponenten des Fensters kompakt darstellen
    pack();
    // Standardoperation für den Schließen-Button: Nichts tun, das
    // Schließen der Anwendung wird über den WindowAdapter gesteuert.
    // (sh. nachfolgende Implementierung des WindowAdapters)
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    // Listener für den Schließen-Button hinzufügen
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        // Wenn betätigt, eigene Methode zum Schließen der Anwendung aufrufen
        quitApplication();
      }
    });

    // Das Fenster soll beliebig vergrößerbar sein
    setResizable(true);
    // Positionierung des Fensters in der Mitte des Bildschirms
    setLocationRelativeTo(null);
  }

  /**
   * Diese Methode ist die Reaktion auf das Betätigen des Schließen-Buttons des
   * Fensters.
   */
  private void quitApplication() {
    // Wenn es Änderungen gibt, die noch nicht gespeichert wurden,
    // dem Anwender die entsprechende Frage stellen.
    if (panel.getShouldBeSaved()) {
      int saveData = askForSaving();
      // saveMe = 0 => Ja; = 1 => Nein; = 2 => Abbrechen
      if (saveData == 0) {
        // Wenn gespeichert werden soll, Dialog zum Speichern aufrufen
        fileSave.doClick();
        if (saveDialogReturnState == 0) {
          // Rückgabewert 0 => Speichern
          // 1 => Abbruch
          // Wenn tatsächlich gespeichert wurde, Anwendung beenden
          System.exit(0);
          // Bei Abbruch oder Klick auf den Schließen-Button nichts machen
        }
      } else if (saveData == 1) {
        // Wenn nicht gespeichert werden soll, Anwendung beenden
        System.exit(0);
      } else {
        // Wenn Abbrechen, nichts machen
      }
    } else {
      // Wenn nichts gespeichert werden braucht, Anwendung beenden
      System.exit(0);
    }
  }

  /**
   * Diese Methode öffnet einen Frage-Dialog, in dem der Anwender gefragt wird,
   * ob die Änderungen gespeichert werden sollen.
   * 
   * @return
   *      Der Rückgabewert des Anwenders 0 = Ja; 1 = Nein; 2 = Abbrechen
   */
  private int askForSaving() {
    int saveMe = JOptionPane.showOptionDialog(this,
        "Sollen Ihre Änderungen gespeichert werden?",
        "Änderungen nicht gespeichert", JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, new String[] { "Ja", "Nein",
            "Abbrechen" }, "Ja");

    return saveMe;
  }

  /**
   * Diese Methode fügt die ToolBar dem Fenster hinzu
   */
  private void addToolBar() {
    // JToggleButtons der ToolBar erstellen
    fileLoad = createAndConfigureSingleToolBarButton(fileOpenAction,
        "DateiOeffnen.jpg", "1 Laden", "Datei öffnen", new Integer(
            KeyEvent.VK_1));
    fileSave = createAndConfigureSingleToolBarButton(fileSaveAction,
        "DateiSpeichern.jpg", "2 Speichern", "Datei speichern", new Integer(
            KeyEvent.VK_2));
    place = createAndConfigureSingleToolBarButton(selectPlaceAction,
        "Stelle.jpg", "3 Stelle", "Stelle einfügen", new Integer(KeyEvent.VK_3));
    transition = createAndConfigureSingleToolBarButton(selectTransitionAction,
        "Transition.jpg", "4 Transition", "Transition einfügen", new Integer(
            KeyEvent.VK_4));
    arrow = createAndConfigureSingleToolBarButton(selectArrowAction,
        "Kante.jpg", "5 Kante", "Kante einfügen", new Integer(KeyEvent.VK_5));
    token = createAndConfigureSingleToolBarButton(selectTokenAction,
        "Token.jpg", "6 Token", "Token einfügen", new Integer(KeyEvent.VK_6));
    move = createAndConfigureSingleToolBarButton(selectMovingAction,
        "Verschieben.jpg", "7 Verschieben", "Stelle/Transition verschieben",
        new Integer(KeyEvent.VK_7));

    // Buttons der Toolbar hinzufügen
    configureToolBar();

    // ToolBar dem Fenster hinzufügen
    this.add(toolBar, BorderLayout.NORTH);
  }

  /**
   * Diese Methode konfiguriert die ToolBar des Fensters und fügt die
   * JToggleButtons hinzu.
   */
  private void configureToolBar() {
    // Neue JToolBar erstellen
    toolBar = new JToolBar();

    // Anwender darf die ToolBar nicht verschieben
    toolBar.setFloatable(false);

    // Buttons zum Laden und Speichern hinzufügen
    toolBar.add(fileLoad);
    toolBar.add(fileSave);

    // Da sich die Zeichen-Buttons der ToolBar gegenseitig ausschließen,
    // werden sie einer einzigen ButtonGroup zugeordnet.
    ButtonGroup buttonGroup = new ButtonGroup();
    addButtonToToolBarAndGroup(place, buttonGroup);
    addButtonToToolBarAndGroup(transition, buttonGroup);
    addButtonToToolBarAndGroup(arrow, buttonGroup);
    addButtonToToolBarAndGroup(token, buttonGroup);
    addButtonToToolBarAndGroup(move, buttonGroup);
  }

  /**
   * Diese Methode fügt einen JToggleButton zu der ToolBar und einer ButtonGroup
   * hinzu
   * 
   * @param button
   *          Der hinzuzufügende JToggleButton
   * @param buttonGroup
   *          Die ButtonGroup, zu der der Button hinzugefügt wird
   */
  private void addButtonToToolBarAndGroup(JToggleButton button,
      ButtonGroup buttonGroup) {
    toolBar.add(button);
    buttonGroup.add(button);
  }

  /**
   * Einen einzelnen Button der ToolBar mit dem Bild, Tooltip-Text und
   * ActionCommand konfigurieren
   */

  /**
   * Diese Methode erstellt und konfiguriert einen JToggleButton. Sie versieht
   * ihn mit einem Bild, Tooltip-Text, Tastaturkürzel und einem ActionCommand.
   * Falls die Bild-Datei nicht gefunden/geöffnet werden kann, wird der
   * alternative Text angezeigt.
   * 
   * @param action
   *          Die Aktion, die bei Klick des Buttons ausgeführt werden soll
   * @param fileName
   *          Der Dateiname für das Bild im Verzeichnis images
   * @param alternativeText
   *          Der alternative Text, falls das Bild nicht geöffnet werden kann
   * @param toolTip
   *          Der Tooltip-Text, wenn der Anwender mit der Maus über den Button
   *          kommt
   * @param mnemonic
   *          Das Tastaturkürzel ALT + Taste, mit dem der Button auch gewählt
   *          werden kann.
   * @return Der neue JToggleButton
   */
  private JToggleButton createAndConfigureSingleToolBarButton(Action action,
      String fileName, String alternativeText, String toolTip, Integer mnemonic) {

    // Neuen JToggleButton erzeugen
    JToggleButton toggleButton = new JToggleButton();

    // Icon aus dem Image-Verzeichnis einsetzen. Wenn das aus irgendwelchen
    // Gründen nicht klappt, alternativ nur Text darstellen.
    try {
      java.net.URL imagePath = getClass().getResource("/images/" + fileName);
      toggleButton.setIcon(new ImageIcon(imagePath));
    } catch (Exception e) {
      toggleButton.setText(alternativeText);
    }

    // Den JToggleButton mit den erforderlichen Eigenschaften ausstatten
    toggleButton.addActionListener(action);
    toggleButton.setToolTipText(toolTip);
    toggleButton.setActionCommand(toolTip);
    // Das automatische Unterstreichen des Buchstabens ist nur für Labels,
    // ein JToggleButton hat aber nur den Tooltipp
    toggleButton.setMnemonic(mnemonic);

    // Rückgabe des JToggleButtons
    return toggleButton;
  }

  /**
   * Diese Methode fügt dem Fenster Scroll-Leisten hinzu
   */
  private void addScrollPane() {
    // neue Scroll-Leisten erstellen
    JScrollPane scrollPane = new JScrollPane(panel);

    // Die preferredSize des Panels ist bei Programmstart noch (0, 0), daher
    // werden die Scrolleisten nicht angezeigt, obwohl sie jetzt hinzugefügt
    // werden. Dieser Effekt ist aber erwünscht.
    this.add(scrollPane, BorderLayout.CENTER);
    // Neuen Scroller erstellen
    this.scroller = new Scroller(panel, scrollPane);
    // Dem panel den Scroller bekannt machen
    panel.setScroller(scroller);
  }

  /**
   * Diese Methode kümmert sich um die Folgeaktivitäten, die nach dem Anklicken
   * eines Buttons in der ToolBar erforderlich sind.
   * 
   * @param pressedButton
   *          Der JToggleButton der ToolBar, den der Anwender ausgewählt hat
   */
  void toolBarButtonClicked(JToggleButton pressedButton) {
    // Öffnen einer Datei
    if (pressedButton.equals(fileLoad)) {
      fileLoadAction();
    }

    // Speichern einer Datei
    if (pressedButton.equals(fileSave)) {
      fileSaveAction();
    }

    // Bei den restlichen Buttons ist nur die aktuelle Figur im Panel
    // zu setzen. Die weiteren Aktionen sind vom Anwender abhängig.

    // Button Stelle einfügen
    if (pressedButton.equals(place)) {
      panel.setPanelStatus(Statuses.STELLE);
    }

    // Button Transition einfügen
    if (pressedButton.equals(transition)) {
      panel.setPanelStatus(Statuses.TRANSITION);
    }

    // Button Kante einfügen
    if (pressedButton.equals(arrow)) {
      panel.setPanelStatus(Statuses.KANTE);
    }

    // Button Token einfügen
    if (pressedButton.equals(token)) {
      panel.setPanelStatus(Statuses.TOKEN);
    }

    // Button Verschieben von Elementen
    if (pressedButton.equals(move)) {
      panel.setPanelStatus(Statuses.MOVINGFIGURES);
    }
  }

  /**
   * Diese Methode Öffnet den Dateidialog zum Öffnen einer Datei.
   */
  private void fileLoadAction() {
    // Bisher gewählte Figur sichern
    Statuses oldFigure = panel.getPanelStatus();
    // Als jetzige Figur den Dateiumgang setzen
    panel.setPanelStatus(Statuses.FILEHANDLING);
    // Flag, ob der Dateidialog zum Laden erstellt werden kann, bejahen
    boolean canOpenLoadFileDialog = true;

    // Wenn der Anwender noch nicht gespeicherte Änderungen hat
    if (panel.getShouldBeSaved()) {
      // Anwender fragen, ob gespeichert werden soll
      int saveData = askForSaving();
      /*
       * saveData = 0 = Ja; 1 = Nein; 2 = Abbrechen
       */
      if (saveData == 0) {
        // Wenn gespeichert werden soll, Dialog zum Speichern aufrufen
        fileSave.doClick();
        canOpenLoadFileDialog = saveDialogReturnState == 0 ? true : false;
        // Rückgabewert 0 => Speichern
        // 1 => Abbruch
        // Wenn tatsächlich gespeichert wurde, Abfrage beenden

      } else if (saveData == 1) {
        // Wenn nicht gespeichert werden soll, bleibt canOpenFileDialog auf true
        // Es ist keine Aktion erforderlich
      } else {
        // Bei Abbruch des Speichern-Dialogs sollte auch keine Datei geladen
        // werden
        canOpenLoadFileDialog = false;
      }
    }

    // Wenn Flag, ob der Dateidialog geöffnet werden kann, bejaht wird,
    // diesen öffen
    if (canOpenLoadFileDialog) {
      fileDialog.openFile();
    }

    // Wenn der Anwender den Dialog geschlossen hat, wieder den vorherigen
    // Zustand der Buttons herstellen und im Panel die entsprechende Figur
    // setzen
    fileLoad.setSelected(false);
    panel.setPanelStatus(oldFigure);
  }

  /**
   * Diese Methode Öffnet den Dateidialog zum Speichern einer Datei.
   */
  private void fileSaveAction() {
    // Bisher gewählte Figur sichern
    Statuses oldFigure = panel.getPanelStatus();
    // Als jetzige Figur den Dateiumgang setzen
    panel.setPanelStatus(Statuses.FILEHANDLING);

    // Dialog zum Datei speichern öffnen und abwarten, wie der Anwender
    // reagiert
    saveDialogReturnState = (fileDialog.saveFile() == true) ? 0 : 1;
    // Rückgabewert 0 => Speichern
    // 1 => Abbruch
    // Ist nur relevant, wenn die Anwendung beendet wird

    // Jetzt wieder den vorherigen Zustand der Buttons herstellen
    // und im Panel die entsprechende Figur setzen
    fileSave.setSelected(false);
    panel.setPanelStatus(oldFigure);
  }
}
