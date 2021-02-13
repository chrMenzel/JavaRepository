package pnmlTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import petrinetzEditor.PetriNetPanel;

/**
 * Diese Klasse implementiert eine einfache XML-Ausgabe für PNML-Dateien.
 */
public final class PNMLWriter {

  private PetriNetPanel panel;

  /**
   * Dies ist eine Referenz zum Java Datei Objekt.
   */
  private File pnmlDatei;

  /**
   * Dies ist eine Referenz zum XML Writer. Diese Referenz wird durch die
   * Methode startXMLDocument() initialisiert.
   */
  private XMLStreamWriter writer = null;

  /**
   * Dieser Konstruktor erstellt einen neuen Writer für PNML Dateien, dem die
   * PNML Datei als Java {@link File} übergeben wird.
   * 
   * @param pnml
   *          Java {@link File} Objekt der PNML Datei
   * @param panel
   *          Das PetriNetPanel, aus dem die Elemente gespeichert werden
   */
  public PNMLWriter(final File pnml, PetriNetPanel panel) {
    super();

    pnmlDatei = pnml;
    this.panel = panel;
  }

  /**
   * Diese Methode beginnt ein neues XML Dokument und initialisiert den XML
   * Writer für diese Datei.
   * 
   * @return true, wenn das XML-Document angelegt werden konnte, sonst false
   */
  public boolean startXMLDocument() {
    try {
      FileOutputStream fos = new FileOutputStream(pnmlDatei);
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      // writer = factory.createXMLStreamWriter(fos);
      writer = factory.createXMLStreamWriter(fos, "UTF-8"); // lt. Newsgroup
      // XML Dokument mit Version 1.0 und Kodierung UTF-8 beginnen
      writer.writeStartDocument("UTF-8", "1.0");
      writer.writeStartElement("pnml");
      writer.writeStartElement("net");
      return true;

    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(panel,
          "<html>Die Datei konnte nicht erstellt werden.<br><br>"
              + "Die Original-Nachricht lautet: " + e.getMessage() + "</html>");

      return false;

    } catch (XMLStreamException e) {
      JOptionPane.showMessageDialog(panel,
          "<html>Die Datei konnte wegen eines sogenannten "
              + "XML-Verarbeitungsfehlers nicht erstellt werden.<br><br>"
              + "Die Original-Nachricht lautet: " + e.getMessage() + "</html>");

      return false;
    }
  }

  /**
   * Diese Methode beendet das Schreiben eines Petrinetzes als XML Datei.
   * 
   * @return true, wenn die Datei geschlossen werden konnte, sonst false
   */
  public boolean finishXMLDocument() {
    if (writer != null) {
      try {
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();
        return true;

      } catch (XMLStreamException e) {
        JOptionPane.showMessageDialog(panel,
            "<html>Die Datei konnte wegen eines sogenannten "
                + "XML-Verarbeitungsfehlers nicht erstellt werden.<br><br>"
                + "Die Original-Nachricht lautet: " + e.getMessage()
                + "</html>");

        return false;
      }
    } else {
      System.err.println("Das Dokument wurde noch nicht gestartet!");
      return false;
    }
  }

  /**
   * Diese Methode fügt eine neue Transition zum XML Dokument hinzu. Vor dieser
   * Methode muss startXMLDocument() aufgerufen worden sein.
   * 
   * @param id
   *          Indentifikationstext der Transition
   * @param label
   *          Beschriftung der Transition
   * @param xPosition
   *          x Position der Transition
   * @param yPosition
   *          y Position der Transition
   */
  public void addTransition(final String id, final String label,
      final String xPosition, final String yPosition) {
    if (writer != null) {
      try {
        writer.writeStartElement("", "transition", "");
        writer.writeAttribute("id", id);

        writer.writeStartElement("", "name", "");
        writer.writeStartElement("", "value", "");
        writer.writeCharacters(label);
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeStartElement("", "graphics", "");
        writer.writeStartElement("", "position", "");
        writer.writeAttribute("x", xPosition);
        writer.writeAttribute("y", yPosition);
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeEndElement();
      } catch (XMLStreamException e) {
        JOptionPane.showMessageDialog(panel, "<html>Die Transition " + label
            + " konnte wegen eines sogenannten "
            + "XML-Verarbeitungsfehlers nicht gespeichert werden.<br><br>"
            + "Die Original-Nachricht lautet: " + e.getMessage() + "</html>");

      }

    } else {
      System.err.println("Das Dokument muss zuerst gestartet werden!");
    }
  }

  /**
   * Diese Methode fügt eine neue Stelle zum XML Dokument hinzu. Vor dieser
   * Methode muss startXMLDocument() aufgerufen worden sein.
   * 
   * @param id
   *          Indentifikationstext der Stelle
   * @param label
   *          Beschriftung der Stelle
   * @param xPosition
   *          x Position der Stelle
   * @param yPosition
   *          y Position der Stelle
   * @param initialMarking
   *          Anfangsmarkierung der Stelle
   */
  public void addPlace(final String id, final String label,
      final String xPosition, final String yPosition,
      final String initialMarking) {
    if (writer != null) {
      try {
        writer.writeStartElement("", "place", "");
        writer.writeAttribute("id", id);

        writer.writeStartElement("", "name", "");
        writer.writeStartElement("", "value", "");
        writer.writeCharacters(label);
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeStartElement("", "initialMarking", "");
        writer.writeStartElement("", "token", "");
        writer.writeStartElement("", "value", "");
        writer.writeCharacters(initialMarking);
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeStartElement("", "graphics", "");
        writer.writeStartElement("", "position", "");
        writer.writeAttribute("x", xPosition);
        writer.writeAttribute("y", yPosition);
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeEndElement();
      } catch (XMLStreamException e) {
        JOptionPane.showMessageDialog(panel, "<html>Die Stelle " + label
            + " konnte wegen eines sogenannten "
            + "XML-Verarbeitungsfehlers nicht gespeichert werden.<br><br>"
            + "Die Original-Nachricht lautet: " + e.getMessage() + "</html>");
      }

    } else {
      System.err.println("Das Dokument muss zuerst gestartet werden!");
    }
  }

  /**
   * Diese Methode fügt eine neue Kante zum XML Dokument hinzu. Vor dieser
   * Methode muss startXMLDocument() aufgerufen worden sein.
   * 
   * @param id
   *          Indentifikationstext der Stelle
   * @param source
   *          Indentifikationstext des Startelements der Kante
   * @param target
   *          Indentifikationstext der Endelements der Kante
   */
  public void addArc(final String id, final String source, final String target) {
    if (writer != null) {
      try {
        writer.writeStartElement("", "arc", "");
        writer.writeAttribute("id", id);
        writer.writeAttribute("source", source);
        writer.writeAttribute("target", target);
        writer.writeEndElement();
      } catch (XMLStreamException e) {
        JOptionPane.showMessageDialog(panel, "<html>Die Kante " + id + " von "
            + source + " nach " + target + "konnte wegen eines sogenannten "
            + "XML-Verarbeitungsfehlers nicht gespeichert werden.<br><br>"
            + "Die Original-Nachricht lautet: " + e.getMessage() + "</html>");
      }

    } else {
      System.err.println("Das Dokument muss zuerst gestartet werden!");
    }
  }
}
