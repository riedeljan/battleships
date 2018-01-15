package data;


import GUI.TableView;
import gameboard.Board;
import network.Network;

import java.util.Stack;

/**@author Felix
 * @desc Statische Klasse welche die Huaptlogik des Spiels enthält und Netzwerk, AI und GUI verknüpft
 *
 */
public class Game{
	/**
	 * board Objekt für den Spieler
	 */
	private static Board map= new Board();


	//methoden

	//Plazierungs methoden


    /*
    methode verschiebt schiff an andere koordinate und überprüft ob die verschiebung valide ist
    ansonsten wird die position zurückgesetzt
    bei erfolg wird true ausgegeben und bei misserfolg false
     */

	/**
	 * bewegt das selectedShip aus dem Datacontainer an Stelle x,y
	 * @param x X-Zielkoordinate
	 * @param y Y-Zielkoordinate
	 * @return gibt true bei erfolgreicher Platzierung zurück und false bei falscher Platzierung.
	 */
    public static boolean moveShip(int x, int y){
        Ship s = DataContainer.getSelectedShip();
        return map.moveShip(x,y,s);

    }
	//aendert orientierung des schiffs

	/**
	 * ändert Platzierung des selectedShip aus dem Datacontainer auf den Wert i mod 4.
	 * @param i neue Orrientierung zwischen 0 und 3
	 */
	public static void rotateShip(int i){
		Ship s = DataContainer.getSelectedShip();
		map.rotateShip(i, s);
	}
	/*
	plaziert schiff auf spielbrett
		 */

	/**
	 * Platziert übergebenes Schiff s auf dem Board.
	 * @param s zu platzierendes Schiff
	 * @return gibt true bei erfolgreicher Platzierung und false bei fehler zurück.
	 */
	public static boolean placeShip(Ship s){
		return map.place(s);
	}

	/**
	 * algoritzmus zur generierung von Flottenvorschlägen
	 */
	public static void generatefleet(){
		int occ=DataContainer.getOccupancy();
		Stack<Integer> lengths= new Stack<>();
		int highlen= 2;
		occ -=DataContainer.getMaxShipLength();
		while(occ>0){
			for(int i = 2; i<=highlen;i++){
				if(occ - i >= 0){
					lengths.push(i);
					occ -= i;
				}
				else if(occ - i == -1){
					lengths.push(i - 1);
					occ -= (i-1);
				}
			}
			highlen++;
		}
		lengths.push(DataContainer.getMaxShipLength()); //größtes schiff soll oben auf stack liegen
	}


	//spielmethoden

	/**
	 * @desc Methode des Spielers um auf seinen Gegner zu schießen, abhängig vom Spielmodus entweder Ai oder Netzwerkgegner.
	 * @param x X-Zielkoordinate
	 * @param y Y-Zielkoordinate
	 * @return gibt entweder 0 für wasser, 1 für treffer oder 2 für versenkt zurück.
	 */
	public static int shoot(int x, int y) {
		if(DataContainer.getAllowed()) {
			int val;
			if(DataContainer.getGameType().equals("ss") ||DataContainer.getGameType().equals("bdf")) {
				val=0;
				//DEBUG
				val = map.checkboard(x,y);
			}
			else if (DataContainer.getGameType().equals("mp")) {
				//val=0;
				val = Network.networkShoot(x,y);
				if(val == 0){
					DataContainer.setAllowed(false);
				}
				//multiplayer shoot
			}
			else{
				return -1;
			}
			map.setPlayershots(x, y, val);
			DataContainer.getPlayerShootTable().setValueAt(val,y,x);

			if(val==2) {
				displayHits(x,y,0,DataContainer.getPlayerShootTable());
			}
			return val;
		}
		else{
			return -1;
		}
	}

	/**
	 * @desc Methode um auf den Spieler zu schießen. wird bei netzwerkspiel bzw von AI aufgerufen.
	 * @param x X-Zielkoordinate
	 * @param y Y-Zielkoordinate
	 * @return gibt 0 für Wasser, 1 für Treffer und 2 für Versenkt zurück.
	 */
	public static int getHit(int x, int y){
		int i = map.checkboard(x,y);
		DataContainer.getTable().setValueAt(i,y,x);
		if(i==0){
			DataContainer.setAllowed(true);
		}
		if(i==2){
			displayHits(x,y,0,DataContainer.getTable());
		}
		return i;
	}
	public static void hitloop(){
		while(!DataContainer.getAllowed()){
			Network.networkHit();
		}
	}

	/**
	 * @desc gibt Tile Objekt an Stelle x,y des playerBoards aus dem Board zurück
	 * @param x X-Wert des zurückzugebenden Objekts
	 * @param y Y-Wert des zurückzugebenden Objekts
	 * @return gibt ein Tile Objekt zurück, welches an der entsprechenden x,y stelle im array steht
	 */
	public static Tile getPlayerboard(int x, int y){
		return map.getPlayerboardAt(x,y);
	}

	/**
	 * ruft removeShip im Board mit dem übergebenen Schiff s auf.
	 * @param s zu löschendes Schiff, dass an removeShip übergeben wird.
	 */
	public static void removeShip(Ship s) {
		map.removeShip(s);
	}

	/**
	 *@desc rekursive Funktion um Schiffe auf der Oberfläche als versenkt anzuzeigen.
	 * @param x X-Wert von dem aus die umliegenden Felder geprüft werden
	 * @param y Y-Wert von dem aus die umliegenden Felder geprüft werden
	 * @param direction Richtung, die bei mehrmaligen aufrufen übergeben wird um die suche effizienter durchzuführen
	 * @param table Tableview auf der die Operation durchgeführt werden soll
	 */
	private static void displayHits(int x, int y,int direction, TableView table) {
		//todo optimize corner checks
		table.setValueAt(2, y, x);
		if (x - 1 >= 0) {
			if (table.getValueAt(y, x - 1).equals(1) && (direction == 0 || direction == 1)) {
				displayHits(x - 1, y, 1, table);
			} else if (table.getValueAt(y, x - 1).equals(9)) {
				table.setValueAt(0, y, x - 1);
			}
		}
		if (x + 1 < DataContainer.getGameboardWidth()) {
			if (table.getValueAt(y, x + 1).equals(1) && (direction == 0 || direction == 2)) {
				displayHits(x + 1, y, 2, table);
			} else if (table.getValueAt(y, x + 1).equals(9)) {
				table.setValueAt(0, y, x + 1);
			}
		}
		if (y - 1 >= 0) {
			if (table.getValueAt(y - 1, x).equals(1) && (direction == 0 || direction == 3)) {
				displayHits(x, y - 1, 3, table);
			} else if (table.getValueAt(y - 1, x).equals(9)) {
				table.setValueAt(0, y - 1, x);
			}
		}
		if (y + 1 < DataContainer.getGameboardHeight()) {
			if (table.getValueAt(y + 1, x).equals(1) && (direction == 0 || direction == 4)) {
				displayHits(x, y + 1, 4, table);
			} else if (table.getValueAt(y + 1, x).equals(9)) {
				table.setValueAt(0, y + 1, x);
			}
		}
		if (x - 1 >= 0 && y - 1 >= 0) {
			if (table.getValueAt(y - 1, x - 1).equals(9)) {
				table.setValueAt(0, y - 1, x - 1);
			}
		}
		if (x + 1 < DataContainer.getGameboardWidth() && y - 1 >= 0) {
			if (table.getValueAt(y - 1, x + 1).equals(9)) {
				table.setValueAt(0, y - 1, x + 1);
			}
		}
		if (x + 1 < DataContainer.getGameboardWidth() && y + 1 < DataContainer.getGameboardHeight()) {
			if (table.getValueAt(y + 1, x + 1).equals(9)) {
				table.setValueAt(0, y + 1, x + 1);
			}
		}
		if (x - 1 >= 0 && y + 1 < DataContainer.getGameboardHeight()) {
			if (table.getValueAt(y + 1, x - 1).equals(9)) {
				table.setValueAt(0, y + 1, x - 1);
			}
		}
	}

	/**
	 * gibt board objekt des Games zurück
	 * @return Board Objekt des Games
	 */
	public static Board getMap(){
	return map;
	}
}