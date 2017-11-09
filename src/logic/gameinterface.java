public interface gameinterface{
	/* 
	@Author: Felix
	Desc: Hauptkomponente des spiels kommuniziert mit ai, spielfeld, netzwerk
	und gui player 0 ist spieler bzw bei Ai vs Ai die erste Ai, player 1 ist bei multiplayer 
	der 2. spieler und bei mensch vs Ai der Mensch
	*/
	private boolean ishost;
	private boolean player0isbot;
	private boolean player1isbot;
//methoden
	public String shoot(int x, int y, int player);
	/* methode um schüsse abzugeben, übergeben wird die zielkoordinate und der zielspieler
	zurück kommt entweder "Wasser", "Treffer" oder "Versenkt" entsprechende anzeige auf dem gui folgt
	der erste schuss iniziert das spiel, der andere spieler antwortet darauf sobald seine flotte plaziert ist
	*/
	public boolean place(int x, int y, int player, ship s);
	/*
	plaziert schiff auf spielfeld von spieler player an koordinate x,y und im gui 
	gibt true bei erfolg und false bei fehler zurück (interne überprüfung der plazierung findet statt)
	*/
	public void placefleet(int player);
	/* plaziert gesamte flotte per zufallsgenerierung solange bis alle schiffe erfolgreich plaziert sind
	*/
	public void save(int id);
	/*speichert spielstand ab($magicfoo) und sendet falls host save message über socket an gegner
	erhält eindeutige id für dateibenennung entweder von host oder selbstgeneriert wenn hostflag true, 
	dann wird speichermessage an gegner übertragen
	*/

