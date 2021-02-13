package pnmlTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import petrinetzEditor.PetriNetPanel;

/**
 * Diese Klasse implementiert die Grundlage für einen einfachen PNML-Parser.
 */
public class PNMLParser {

  private PetriNetPanel panel;
  private ArrayList<PNMLElement> elements = new ArrayList<PNMLElement>();
  private PNMLElement newElement;

  /**
   * Dies ist eine Referenz zum Java Datei Objekt.
   */
  private File pnmlDatei;

  /**
   * Dies ist eine Referenz zum XML Parser. Diese Referenz wird durch die
   * Methode parse() initialisiert.
   */
  private XMLEventReader xmlParser = null;

  /**
   * Diese Variable dient als Zwischenspeicher für die ID des zuletzt gefundenen
   * Elements.
   */
  private String lastId = null;

  /**
   * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Token Elements
   * liest.
   */
  private boolean isToken = false;

  /**
   * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Name Elements
   * liest.
   */
  private boolean isName = false;

  /**
   * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Value Elements
   * liest.
   */
  private boolean isValue = false;

  /**
   * Dieser Konstruktor erstellt einen neuen Parser für PNML Dateien, dem die
   * PNML Datei als Java {@link File} übergeben wird.
   * 
   * @param pnml
   *          Java {@link File} Objekt der PNML Datei
   * @param panel
   *          Das PetriNetPanel, in dem die Elemente dargestellt werden
   */
  public PNMLParser(final File pnml, PetriNetPanel panel) {
    super();
    this.panel = panel;
    this.pnmlDatei = pnml;
  }

  /**
   * Diese Methode öffnet die PNML Datei als Eingabestrom und initialisiert den
   * XML Parser.
   * 
   * @return true, wenn der Parser intialisiert werden konnte ansonsten false
   */
  public final boolean initParser() {
    try {
      InputStream dateiEingabeStrom = new FileInputStream(pnmlDatei);
      XMLInputFactory factory = XMLInputFactory.newInstance();
      try {
        xmlParser = factory.createXMLEventReader(dateiEingabeStrom);
      } catch (XMLStreamException e) {
        JOptionPane
            .showMessageDialog(
                panel,
                "<html>Die Datei wurde gefunden, allerdings konnte die Datei "
                    + "nicht eingelesen werden.<br><br>"
                    + "Es handelt sich um einen sogenannten XML-Verarbeitungsfehler.<br><br>"
                    + "Die Original-Nachricht lautet: " + e.getMessage()
                    + "</html>");
        return false;
      }
    } catch (FileNotFoundException e) {
      JOptionPane
          .showMessageDialog(panel, "Die Datei " + pnmlDatei.getAbsolutePath()
              + " wurde nicht gefunden!");
      return false;
    }
    return true;
  }

  /**
   * Diese Methode liest die XML Datei und delegiert die gefundenen XML Elemente
   * an die entsprechenden Methoden.
   * 
   * @return true, wenn beim parsen kein Fehler auftrat, ansonsten false
   */
  public final boolean parse() {
    while (xmlParser.hasNext()) {
      try {
        XMLEvent event = xmlParser.nextEvent();
        switch (event.getEventType()) {
        case XMLStreamConstants.START_ELEMENT:
          handleStartEvent(event);
          break;
        case XMLStreamConstants.END_ELEMENT:
          String name = event.asEndElement().getName().toString().toLowerCase();
          if (name.equals("token")) {
            isToken = false;
          } else if (name.equals("name")) {
            isName = false;
          } else if (name.equals("value")) {
            isValue = false;
          }
          break;
        case XMLStreamConstants.CHARACTERS:
          if (isValue && lastId != null) {
            Characters ch = event.asCharacters();
            if (!ch.isWhiteSpace()) {
              handleValue(ch.getData());
            }
          }
          break;
        case XMLStreamConstants.END_DOCUMENT:
          // schließe den Parser
          xmlParser.close();
          break;
        default:
        }
      } catch (XMLStreamException e) {
        JOptionPane.showMessageDialog(panel,
            "<html>Die Datei entspricht nicht dem PNML-Format<br>"
                + "und kann daher nicht eingelesen werden.</html>");
        return false;
      }
    }

    // Vom Panel alle bisherigen Daten löschen
    panel.clearData();

    // Das Maximum der IDs zu transition und place bestimmen.
    // Grund dafür ist, dass sonst immer bei 0 angefangen wird.
    // Wenn man nun ein selbst erstelltes Netz speichern und wieder laden
    // würde, würden also die neu gesetzten Elemente wieder die IDs
    // place0, place1, ... (und entsprechend für transition) erhalten
    // Das verstößt zum einen gegen die Eindeutigkeit der ID, zum anderen
    // würden die Kantenverbindungen falsch dargestellt werden.
    int maxTransitionID = examineID("transition");
    int maxPlaceID = examineID("place");

    // Wenn die IDs gar nicht dem Schema entsprachen, ist die maximale
    // ID weiterhin -1. Ansonsten wird nun der Ausgangswert für die
    // jeweilige ID gesetzt.
    if (maxPlaceID > -1) panel.setNumberOfPlaceID(maxPlaceID + 1);
    if (maxTransitionID > -1) panel
        .setNumberOfTransitionID(maxTransitionID + 1);

    // Die gesammelten Elemente durchlaufen, um sie ins Panel zu zeichnen
    for (PNMLElement element : elements) {
      switch (element.getKindOfElement()) {
      case "Transition":
        panel.newTransitionFromFile(element.getId(),
            Integer.parseInt(element.getX()), Integer.parseInt(element.getY()),
            element.getName());
        break;
      case "Place":
        panel.newPlaceFromFile(element.getId(),
            Integer.parseInt(element.getX()), Integer.parseInt(element.getY()),
            element.getName(), Integer.parseInt(element.getToken()));
        break;
      case "Arrow":
        panel.newArrowFromFile(element.getId(), element.getSource(),
            element.getTarget());
        break;
      default:
        // Andere Elemente ignorieren
        break;
      }
    }
    return true;
  }

  private int examineID(String kindOfElement) {
    int lengthOfElementString = kindOfElement.length();
    int maxIdNumber = -1;

    for (PNMLElement element : elements) {
      // Wenn die ID des Elements mit dem übergebenen String übereinstimmt
      if (element.getId().startsWith(kindOfElement)) {
        // Reststring holen (das sollte die Zahl hinter "place" bzw.
        // "transition")
        String rest = element.getId().substring(lengthOfElementString);
        try {
          // Diese als String vorliegende Zahl in eine Integer-Zahl
          // umwandeln und aus dieser Zahl und dem bisherigen Maximum
          // das Maximum bestimmen
          maxIdNumber = Math.max(maxIdNumber, Integer.parseInt(rest));
        } catch (NumberFormatException e) {
          // Wenn sich der Reststring nicht in einen Integer-Wert
          // umwandeln lässt, kein Problem
        }
      }
    }

    // Das Maximum der ID-Nummer zurückgeben
    return maxIdNumber;
  }

  /**
   * Diese Methode behandelt den Start neuer XML Elemente, in dem der Name des
   * Elements überprüft wird und dann die Behandlung an spezielle Methoden
   * delegiert wird.
   * 
   * @param event
   *          {@link XMLEvent}
   */
  private void handleStartEvent(final XMLEvent event) {
    StartElement element = event.asStartElement();
    if (element.getName().toString().toLowerCase().equals("transition")) {
      handleTransition(element);
    } else if (element.getName().toString().toLowerCase().equals("place")) {
      handlePlace(element);
    } else if (element.getName().toString().toLowerCase().equals("arc")) {
      handleArc(element);
    } else if (element.getName().toString().toLowerCase().equals("name")) {
      isName = true;
    } else if (element.getName().toString().toLowerCase().equals("position")) {
      handlePosition(element);
    } else if (element.getName().toString().toLowerCase().equals("token")) {
      isToken = true;
    } else if (element.getName().toString().toLowerCase().equals("value")) {
      isValue = true;
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn Text innerhalb eines Value Elements
   * gelesen wird.
   * 
   * @param value
   *          Der gelesene Text als String
   */
  private void handleValue(final String value) {
    if (isName) {
      setName(lastId, value);
    } else if (isToken) {
      setMarking(lastId, value);
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn ein Positionselement gelesen wird.
   * 
   * @param element
   *          das Positionselement
   */
  private void handlePosition(final StartElement element) {
    String x = null;
    String y = null;
    Iterator<?> attributes = element.getAttributes();
    while (attributes.hasNext()) {
      Attribute attr = (Attribute) attributes.next();
      if (attr.getName().toString().toLowerCase().equals("x")) {
        x = attr.getValue();
      } else if (attr.getName().toString().toLowerCase().equals("y")) {
        y = attr.getValue();
      }
    }
    if (x != null && y != null && lastId != null) {
      setPosition(lastId, x, y);
    } else {
      System.err.println("Unvollständige Position wurde verworfen!");
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn ein Transitionselement gelesen wird.
   * 
   * @param element
   *          das Transitionselement
   */
  private void handleTransition(final StartElement element) {
    String transitionId = null;
    Iterator<?> attributes = element.getAttributes();
    while (attributes.hasNext()) {
      Attribute attr = (Attribute) attributes.next();
      if (attr.getName().toString().toLowerCase().equals("id")) {
        transitionId = attr.getValue();
        break;
      }
    }
    if (transitionId != null) {
      newTransition(transitionId);
      lastId = transitionId;
    } else {
      System.err.println("Transition ohne id wurde verworfen!");
      lastId = null;
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn ein Stellenelement gelesen wird.
   * 
   * @param element
   *          das Stellenelement
   */
  private void handlePlace(final StartElement element) {
    String placeId = null;
    Iterator<?> attributes = element.getAttributes();
    while (attributes.hasNext()) {
      Attribute attr = (Attribute) attributes.next();
      if (attr.getName().toString().toLowerCase().equals("id")) {
        placeId = attr.getValue();
        break;
      }
    }
    if (placeId != null) {
      newPlace(placeId);
      lastId = placeId;
    } else {
      System.err.println("Stelle ohne id wurde verworfen!");
      lastId = null;
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn ein Kantenelement gelesen wird.
   * 
   * @param element
   *          das Kantenelement
   */
  private void handleArc(final StartElement element) {
    String arcId = null;
    String source = null;
    String target = null;
    Iterator<?> attributes = element.getAttributes();
    while (attributes.hasNext()) {
      Attribute attr = (Attribute) attributes.next();
      if (attr.getName().toString().toLowerCase().equals("id")) {
        arcId = attr.getValue();
      } else if (attr.getName().toString().toLowerCase().equals("source")) {
        source = attr.getValue();
      } else if (attr.getName().toString().toLowerCase().equals("target")) {
        target = attr.getValue();
      }
    }
    if (arcId != null && source != null && target != null) {
      newArc(arcId, source, target);
    } else {
      System.err.println("Unvollständige Kante wurde verworfen!");
    }
    // Die id von Kanten wird nicht gebraucht
    lastId = null;
  }

  /**
   * Diese Methode kann überschrieben werden, um geladene Transitionen zu
   * erstellen.
   * 
   * @param id
   *          Identifikationstext der Transition
   */
  public void newTransition(final String id) {
    // System.out.println("Transition mit id " + id + " wurde gefunden.");
    newElement = newElement(id);
    newElement.setKindOfElement("Transition");
  }

  /**
   * Diese Methode kann überschrieben werden, um geladene Stellen zu erstellen.
   * 
   * @param id
   *          Identifikationstext der Stelle
   */
  public void newPlace(final String id) {
    // System.out.println("Stelle mit id " + id + " wurde gefunden.");
    newElement = newElement(id);
    newElement.setKindOfElement("Place");
  }

  /**
   * Diese Methode kann überschrieben werden, um geladene Kanten zu erstellen.
   * 
   * @param id
   *          Identifikationstext der Kante
   * @param source
   *          Identifikationstext des Startelements der Kante
   * @param target
   *          Identifikationstext des Endelements der Kante
   */
  public void newArc(final String id, final String source, final String target) {
    // System.out.println("Kante mit id " + id + " von " + source + " nach "
    // + target + " wurde gefunden.");
    newElement = newElement(id);
    newElement.setKindOfElement("Arrow");
    newElement.setSource(source);
    newElement.setTarget(target);
  }

  /**
   * Diese Methode kann überschrieben werden, um die Positionen der geladenen
   * Elemente zu aktualisieren.
   * 
   * @param id
   *          Identifikationstext des Elements
   * @param x
   *          x Position des Elements
   * @param y
   *          y Position des Elements
   */
  public void setPosition(final String id, final String x, final String y) {
    // System.out.println("Setze die Position des Elements " + id + " auf (" + x
    // + ", " + y + ")");
    for (PNMLElement e : elements) {
      if (e.getId() == id) {
        e.setX(x);
        e.setY(y);
      }
    }
  }

  /**
   * Diese Methode kann überschrieben werden, um den Beschriftungstext der
   * geladenen Elemente zu aktualisieren.
   * 
   * @param id
   *          Identifikationstext des Elements
   * @param name
   *          Beschriftungstext des Elements
   */
  public void setName(final String id, final String name) {
    // System.out.println("Setze den Namen des Elements " + id + " auf " +
    // name);
    for (PNMLElement e : elements) {
      if (e.getId() == id) {
        e.setName(name);
      }
    }
  }

  /**
   * Diese Methode kann überschrieben werden, um die Markierung der geladenen
   * Elemente zu aktualisieren.
   * 
   * @param id
   *          Identifikationstext des Elements
   * @param marking
   *          Markierung des Elements
   */
  public void setMarking(final String id, final String marking) {
    // System.out.println("Setze die Markierung des Elements " + id + " auf "
    // + marking);
    for (PNMLElement e : elements) {
      if (e.getId() == id) {
        e.setToken(marking);
      }
    }
  }

  private PNMLElement newElement(String id) {
    newElement = new PNMLElement();
    newElement.setId(id);
    elements.add(newElement);
    return newElement;
  }
}
