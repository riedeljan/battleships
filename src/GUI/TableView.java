package GUI;

import Data.DataContainer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Diese Klasse erzeugt ein Objekt das ein Spielfeld enthält
 */
public class TableView extends JTable {

    public TableView(){

        // Das array names wird über die Breite des Spielfeldes initialisiert
        Object[] name = new Integer[DataContainer.getSpielFeldBreite()];

        // Das 2D Array wird über Spielfeldbreite und Hoehe initialisiert
        Object[][] data = new Integer[DataContainer.getSpielFeldHoehe()][DataContainer.getSpielFeldBreite()];

        // Array name wird mit 1 - breite numeriert
        for(int i = 0; i < DataContainer.getSpielFeldBreite(); i++){
            name[i] = i;
        }
        // Array data wird komplett mit 0 befüllt. 0 steht hierbei für den Wert für WASSER
        for(int i = 0; i < DataContainer.getSpielFeldHoehe();i++){
            for(int j = 0; j < DataContainer.getSpielFeldBreite(); j++){
                data[i][j] = 0;
            }
        }

        // Hier wird die Editierbarkeit im TableModel überschrieben
        this.setModel(new DefaultTableModel(data, name){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        });

        TableCellRenderer renderer = new OwnTableRenderer();

        // zeigt Gitternetzlinien an
        setShowGrid(true);
        setDefaultRenderer(Object.class, renderer);

        // Felder der Tabelle werden mit einer bestimmten Groesse erstellt
        setRowHeight(20);
        TableColumnModel columnModel = getColumnModel();
        for (int i = 0; i < getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(20);


        }
    }
}
