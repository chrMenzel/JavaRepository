package petrinetzEditor;

import javax.swing.*;

/**
 * Die Klasse Main enthält die main-Methode, von der die Anwendung startet.
 */
public class Main {
  /**
   * Die main-Methode wird bei Start der Anwendung als erstes durchgeführt. Sie
   * stößt die Erstellung der GUI dieser SWING-Anwendung an.
   * 
   * @param args
   *          Programmparameter als String-Array
   */
  public static void main(String[] args) {
    // Diesen Thread im Event-Dispatch-Thread (EDT) einplanen
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        // PetriNetPanel ist eine von JPanel abgeleitete Klasse
        // Hier wird also der Fensterinhalt des Petrinetz-Editors erstellt
        PetriNetPanel panel = new PetriNetPanel();

        // Das Fenster wird geöffnet und das eben eingerichtete Panel diesem
        // hinzugefügt
        new MainFrame(
            "Petrinetz-Editor von Christian Menzel, Matrikel-Nummer 5399165",
            panel);
      }
    });
  }
}
