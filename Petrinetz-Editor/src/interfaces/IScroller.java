package interfaces;

import javax.swing.JScrollPane;

import petrinetzEditor.PetriNetPanel;

/**
 * Das Interface IScroller ermöglicht die Kommunikation der Klassen Scroller und
 * PetriNetPanel. Es macht das PetriNetPanel unabhängiger von der tatsächlichen
 * Implementierung des Scrollers.
 */
public interface IScroller {
  void adjustScrollPanesRight(int value, int widthOfFigures);

  void adjustScrollPanesDown(int value, int heightOfFigures);

  void adjustScrollPanesLeft(int value, int maxValue, int widthOfFigures,
      boolean includingMovingFigures);

  void adjustScrollPanesUp(int value, int maxValue, int heightOfFigures,
      boolean includingMovingFigures);

  void repaintScrollPane();

  void slowDown();

  void setPanel(PetriNetPanel panel);

  void setScrollPane(JScrollPane scrollPane);
}
