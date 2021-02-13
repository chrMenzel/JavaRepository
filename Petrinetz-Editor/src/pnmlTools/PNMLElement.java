package pnmlTools;

/**
 * Die Klasse PNMLElement repräsentiert ein aus einer Datei gelesenen Elements
 * eines PetriNetzes. Sie enthält keine spezifischen Methoden (außer Getter und
 * Setter), sondern dient als Zwischenspeicher beim Einlesen einer Datei.
 */
public class PNMLElement {
  /**
   * Diese Variable gibt an, von welcher Art (Stelle, Transition, Kante) das
   * Element ist.
   */
  private String kindOfElement;

  /**
   * Diese Variable enthält die ID des PNML-Elements.
   */
  private String id;

  /**
   * Diese Variable enthält die X-Koordinate des PNML-Elements.
   */
  private String x;

  /**
   * Diese Variable enthält die Y-Koordinate des PNML-Elements.
   */
  private String y;

  /**
   * Diese Variable enthält die Bezeichnung des PNML-Elements.
   */
  private String name;

  /**
   * Diese Variable enthält die Anzahl Token des PNML-Elements (nur bei Stellen
   * relevant).
   */
  private String token;

  /**
   * Diese Variable enthält die ID des PNML-Elements, von dem der Pfeil abgeht.
   */
  private String source;

  /**
   * Diese Variable enthält die ID des PNML-Elements, zu der der Pfeil hinführt.
   */
  private String target;

  /**
   * Diese Methode gibt die Art des PNML-Elements zurück.
   * 
   * @return Die Art des PNML-Elements
   */
  public String getKindOfElement() {
    return kindOfElement;
  }

  /**
   * Diese Methode setzt die Art des PNML-Elements.
   * 
   * @param kindOfElement
   *          Die Art des PNML-Elements
   */
  public void setKindOfElement(String kindOfElement) {
    this.kindOfElement = kindOfElement;
  }

  /**
   * Diese Methode gibt die ID des PNML-Elements zurück.
   * 
   * @return Die ID des PNML-Elements
   */
  public String getId() {
    return id;
  }

  /**
   * Diese Methode setzt die ID des PNML-Elements.
   * 
   * @param id
   *          Die ID des PNML-Elements
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Diese Methode gibt die X-Koordinate des PNML-Elements zurück.
   * 
   * @return Die X-Koordinate des PNML-Elements
   */
  public String getX() {
    return x;
  }

  /**
   * Diese Methode setzt die X-Koordinate des PNML-Elements.
   * 
   * @param x
   *          Die X-Koordinate des PNML-Elements
   */
  public void setX(String x) {
    this.x = x;
  }

  /**
   * Diese Methode gibt die Y-Koordinate des PNML-Elements zurück.
   * 
   * @return Die Y-Koordinate des PNML-Elements
   */
  public String getY() {
    return y;
  }

  /**
   * Diese Methode setzt die Y-Koordinate des PNML-Elements.
   * 
   * @param y
   *          Die Y-Koordinate des PNML-Elements
   */
  public void setY(String y) {
    this.y = y;
  }

  /**
   * Diese Methode gibt den Name des PNML-Elements zurück.
   * 
   * @return Der Name des PNML-Elements
   */
  public String getName() {
    return name;
  }

  /**
   * Diese Methode setzt den Name des PNML-Elements.
   * 
   * @param name
   *          Der Name des PNML-Elements
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Diese Methode gibt die Anzahl Token des PNML-Elements zurück (nur bei
   * Stellen relevant).
   * 
   * @return Die Anzahl Token des PNML-Elements
   */
  public String getToken() {
    return token;
  }

  /**
   * Diese Methode setzt die Anzahl Token des PNML-Elements (nur bei Stellen
   * relevant).
   * 
   * @param token
   *          Die Anzahl Token des PNML-Elements
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Diese Methode gibt die ID des PNML-Elements zurück, von der die Kante
   * abgeht.
   * 
   * @return Die ID des PNML-Elements, von der die Kante abgeht.
   */
  public String getSource() {
    return source;
  }

  /**
   * Diese Methode setzt die ID des PNML-Elements, von der die Kante abgeht.
   * 
   * @param source
   *        Die ID des PNML-Elements, von der die Kante abgeht.
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Diese Methode gibt die ID des PNML-Elements zurück, zu der die Kante
   * hinführt.
   * 
   * @return Die ID des PNML-Elements, zu der die Kante hinführt.
   */
  public String getTarget() {
    return target;
  }

  /**
   * Diese Methode setzt die ID des PNML-Elements, zu der die Kante hinführt.
   * 
   * @param target
   *          Die ID des PNML-Elements, zu der die Kante hinführt.
   */
  public void setTarget(String target) {
    this.target = target;
  }
}
