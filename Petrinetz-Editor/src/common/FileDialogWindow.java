package common;

import interfaces.IFileDialog;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.*;

import petrinetzEditor.*;
import pnmlTools.PNMLParser;
import pnmlTools.PNMLWriter;

/**
 * Die Klasse FileDialogWindow stellt die Methoden im Zusammenhang mit dem
 * Speichern und Laden einer Datei zur Verfügung. Mit dieser Klasse wird
 * zunächst lediglich das Fenster des Dateieditors gesteuert.
 * 
 * Nach der Bestätigung des Anwenders, dass diese Datei geladen oder gespeichert
 * werden soll, werden der entsprechende Einlese-/Abspeicherprozess der
 * Petrinetz-Elemente an die PNML-Klassen abgegeben.
 */
public final class FileDialogWindow implements IFileDialog {
  /**
   * Dies ist eine Referenz auf den Dateiauswahl-Dialog. Es handelt sich dabei
   * um den JFileChooser von SWING
   */
  private JFileChooser fileChooser = new JFileChooser();

  /**
   * Dies ist eine Referenz auf die File-Klasse. Mit deren Hilfe findet das
   * Laden und Speichern der Datei statt.
   */
  private File file;

  /**
   * Dies ist eine Referenz auf das PetriNetPanel, in das das geladene
   * Petri-Netz gezeichnet oder von dem das Petri-Netz gespeichert wird.
   */
  private PetriNetPanel panel;

  /**
   * Diese Variable ist der Rückgabewert nachdem sich der Dateidialog
   * geschlossen hat.
   */
  private int returnState;

  /**
   * Diese Methode trifft die Vorbereitungen für die Dateidialoge, wie z. B. die
   * Art der Dateiendungen, die angezeigt werden
   */
  private void prepareFileDialog() {
    // Dateien und Ordner anzeigen
    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    // Nur PNML und XML-Dateien anzeigen
    fileChooser.setFileFilter(new FileNameExtensionFilter("PNML und XML",
        new String[] { "PNML", "XML" }));
    // Keine Datei vorab auswählen
    fileChooser.setSelectedFile(null);
  }

  /**
   * Diese Methode öffnet den Dateidialog zum Laden eines Petri-Netzes
   */
  @Override
  public void openFile() {
    // Den FileChooser als Datei-Lade-Dialog setzen
    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
    // Vorbereitungen (wie anzuzeigende Dateiendungen usw.) vornehmen
    prepareFileDialog();
    // Dialog öffnen und Rückgabewert abwarten
    returnState = fileChooser.showOpenDialog(null);
    // Datei laden
    loadSelectedFile();
  }

  /**
   * Diese Methode lädt die gewählte Datei mit Hilfe des PNML-Parsers
   */
  private void loadSelectedFile() {
    // Wenn der Anwender nicht abgebrochen hat
    if (returnState == JFileChooser.APPROVE_OPTION) {
      // Gewählte Datei sichern
      file = fileChooser.getSelectedFile();
  
      // Wenn die Datei existiert
      if (file.exists()) {
        // Einen neuen PNML-Parser erstellen, der sich gleich initialisiert.
        PNMLParser pnmlParser = new PNMLParser(file, panel);
        // Wenn der PNML-Parser sich initialisieren konnte
        if (pnmlParser.initParser()) {
          // und auch Elemente gelesen hat
          if (pnmlParser.parse()) {
            // den sichtbaren Bereich in die linke obere Ecke legen
            panel.scrollRectToVisible(new Rectangle(0, 0, 100, 100));
            panel.setShouldBeSaved(false);
          }
          // Ansonsten hat die Klasse PNMLParser bereits eine Nachricht an den
          // Anwender ausgegeben.
        }
        // Wenn die Datei nicht existiert,
      } else {
        // den Anwender mit einer Nachricht darüber informieren
        JOptionPane.showMessageDialog(panel,
            "Die Datei " + file.getAbsolutePath() + " wurde nicht gefunden!");
      }
    } else {
      // Bricht der Anwender ab, nichts machen
    }
  }

  /**
   * Diese Methode öffnet den Dateidialog zum Speichern eines Petri-Netzes
   * 
   * @return
   *      true, wenn die Datei gespeichert wurde
   *      false, wenn die Datei nicht gespeichert wurde
   */
  @Override
  public boolean saveFile() {
    // Den FileChooser als Datei-Speichern-Dialog setzen
    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
    // Vorbereitungen (wie anzuzeigende Dateiendungen usw.) vornehmen
    prepareFileDialog();
    
    // Für while-Schleife Variable mit "Datei soll nicht überschrieben werden" initialisieren
    byte fileSavedState = 2;
    do {
      // Dialog öffnen und Rückgabewert abwarten
      returnState = fileChooser.showSaveDialog(null);
      // Wenn Anwender auf "Speichern" geklickt hat, ...
      if (returnState == 0) {
        // ... Datei speichern
        fileSavedState = saveSelectedFile();
      } else {
        // Anwender hat auf "Abbrechen" geklickt
        return false;
      }
      // Wenn auf Status "Datei nicht überschreiben", Dialog wieder öffnen
    } while (fileSavedState == 2);
      
    // Wenn die Datei gespeichert wurde,
    if (fileSavedState == 0) {
      // dies zurückgeben
      return true;
    }
    // returnState = 1 ist Abbrechen, dann ist nichts zu tun
    return false;
  }

  /**
   * Diese Methode speichert die gewählte Datei mit Hilfe des PNML-Parsers
   * 
   * @return
   *      0, wenn die Datei gespeichert wurde
   *      1, wenn die Datei - aus welchen Gründen auch immer - nicht gespeichert wurde
   *      2, wenn der Anwender das Überschreiben der gewählten Datei verneint hat
   */
  private byte saveSelectedFile() {
    // Wenn der Anwender nicht abgebrochen hat
    if (returnState == JFileChooser.APPROVE_OPTION) {
      // Gewählte Datei sichern
      file = fileChooser.getSelectedFile();

      // Wenn die Datei bereits existiert
      if (file.exists()) {
        // den Anwender fragen, ob die Datei überschrieben werden soll
        if (askForOverwriting(fileChooser.getName(file)) == 0) {
          // Wenn ja, und die Datei wurde gespeichert,
          if (saveFileNow()) {
            // dies zurückgeben
            return 0;
          } else {
            // Datei wurde nicht gespeichert
            return 1;
          }
        } // Datei soll nicht überschrieben werden
          return 2;
      // Datei existiert noch nicht, es kann einfach gespeichert werden
      } else {
        if (saveFileNow()) {
          // Wenn Speichern erfolgreich, dies zurückgeben
          return 0;
        }
      }
    } else {
      // Bricht der Anwender ab, nichts machen
    }
    return 1;
  }

  /**
   * Diese Methode führt das eigentliche Speichern der Petrinetz-Daten durch.
   * 
   * @return
   *      true, wenn die Datei gespeichert wurde
   *      false, wenn der Anwender abgebrochen hat
   */
  private boolean saveFileNow() {
    // Einen neuen PNML-Writer erstellen.
    PNMLWriter pnmlWriter = new PNMLWriter(file, panel);

    // Wenn der PNML-Writer ein XML-Dokument erstellen konnte
    if (pnmlWriter.startXMLDocument()) {
      // ... aus dem PetriNetPanel die Figuren und Pfeile holen
      ArrayList<PetriNetFigure> figures = panel.getFigures();
      ArrayList<Kante> arrows = panel.getKanten();

      // Über die PetriNetFigures iterieren
      for (PetriNetFigure f : figures) {
        // Bei einer Stelle die Methode des PNML-Writers zum Speichern
        // einer Stelle aufrufen
        if (f instanceof Stelle) {
          pnmlWriter.addPlace(f.getID(), f.getLabelText(),
              Integer.toString(f.getX()), Integer.toString(f.getY()),
              Integer.toString(((Stelle) f).getNumberOfTokens()));

          // Ansonsten die Methode zum Speichern einer Transition aufrufen
        } else if (f instanceof Transition) {
          pnmlWriter.addTransition(f.getID(), f.getLabelText(),
              Integer.toString(f.getX()), Integer.toString(f.getY()));
        }
      }

      // Über die Pfeile iterieren
      for (Kante k : arrows) {
        // die PNML-Writer-Methode zum Speichern einer Kante aufrufen.
        pnmlWriter.addArc(k.getArrowID(), k.getFigureFrom().getID(), k
            .getFigureTo().getID());
      }

      // PNML-Writer das XML-Dokument schließen lassen
      if (pnmlWriter.finishXMLDocument()) {
        // Datei wurde gespeichert, daher
        // Im Panel das "Noch ungespeicherte Änderungen vorhanden"-Flag
        // verneinen
        panel.setShouldBeSaved(false);
        return true;
      }
    } else {
      // Fehlermeldung wurde schon angezeigt, einfach abbrechen
    }
    return false;
  }

  /**
   * Diese Methode erstellt ein Nachrichtenfenster, in dem der Anwender gefragt
   * wird, ob die gewählte Datei überschrieben werden soll, wenn sie schon
   * existiert.
   * 
   * @param fileName
   *      Der Dateiname, dessen Überschreiben werden soll
   *      
   * @return Die Reaktion des Anwenders als Zahl (0 = Ja, 1 = Nein)
   */
  private int askForOverwriting(String fileName) {
    int overwriteMe = JOptionPane.showOptionDialog(panel,
        "<html>" + fileName + " ist bereits vorhanden.<br>"
        + "Möchten Sie die Datei ersetzen?</html>", "Überschreiben bestätigen",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        new String[] { "Ja", "Nein" }, "Nein");

    return overwriteMe;
  }

  /**
   * Diese Methode setzt das Panel, auf dem die Daten geladen werden oder von
   * dem die Daten des Petri-Netzes gespeichert werden.
   */
  @Override
  public void setPanel(PetriNetPanel panel) {
    this.panel = panel;
  }
}