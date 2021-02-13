package interfaces;

import petrinetzEditor.PetriNetPanel;

/**
 * Das Interface IFileDialog ermöglicht die Kommunikation der Klassen
 * FileDialogWindow und MainFrame. Es macht das MainFrame unabhängiger von der
 * tatsächlichen Implementierung des FileDialogWindow.
 */
public interface IFileDialog {
  void setPanel(PetriNetPanel panel);

  void openFile();

  boolean saveFile();
}