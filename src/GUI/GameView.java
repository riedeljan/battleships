package GUI;

import data.DataContainer;
import network.Network;
import data.Game;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Diese Klasse bildet das eigentliche Spielfenster.
 * Es enthaelt eine MenuBar um Spiele zu speichern/laden.
 * Es enthaelt zwei Spielfelder, wobei das linke das des Spielers ist
 * und das rechte ist das um Schuesse abzufeuern.
 */
 class GameView {

    private TableView tablePlayer;
    private TableView PlayerShootTable;

     GameView(){

        JDialog playView = new JDialog();
        playView.setModal(true);
        playView.setSize((DataContainer.getGameboardWidth()*2 + 100), (DataContainer.getGameboardHeight() + 100));
        playView.setUndecorated(true);
        playView.setContentPane(Box.createVerticalBox());

        /**
         * Erstellung der TableView abhaengig des gewaehlten GameTyp
         */
         switch (DataContainer.getGameType()) {
             case "ss":    //SS steht für schnelles Spiel
                 tablePlayer = DataContainer.getTable();

                 break;
             case "bdf":  // bdf steht für Benutzerdefiniert

                 tablePlayer = DataContainer.getTable();          // das Place ships window wird die Table anlegen

                 break;
             case "mp":
                 tablePlayer = DataContainer.getTable();
                 break;
         }
        PlayerShootTable = new TableView();
        for(int i = 0; i <DataContainer.getGameboardHeight(); i++){
            for(int j = 0; j < DataContainer.getGameboardWidth(); j++){
                PlayerShootTable.setValueAt(9,i,j);
            }
        }
        DataContainer.setPlayerShootTable(PlayerShootTable);


        /**
         * JMenuBar mit den Unterpunkten zum Spiel speichern/laden und schließen.
         */
        JMenuBar bar = new JMenuBar();
        {
            JMenu menu = new JMenu("File");
            {
                JMenuItem item = new JMenuItem("Spiel laden");
                item.addActionListener(
                        (e) -> { //Todo Laden ausführen
                             }
                );
                menu.add(item);
            }
            {
                JMenuItem item = new JMenuItem("Spiel speichern");
                item.addActionListener(
                        (e) -> {
                            if(DataContainer.getGameType().equals("bdf")
                                    || DataContainer.getGameType().equals("ss")) {
                                JFileChooser filechooserSave = new JFileChooser();

                                FileFilter filter = new FileFilter() {
                                    public boolean accept(File f) {
                                        return f.isDirectory()
                                                || f.getName().toLowerCase().endsWith(".txt");
                                    }

                                    public String getDescription() {
                                        return "TXT";
                                    }
                                };
                                filechooserSave.setFileFilter(filter);
                                int state = filechooserSave.showSaveDialog(null);

                                if (state == JFileChooser.APPROVE_OPTION) {
                                    File file = filechooserSave.getSelectedFile();
                                    long timestamp = System.currentTimeMillis();
                                    String filename = file.getAbsolutePath() + "-" + timestamp
                                            + ".txt";
                                    if (DataContainer.getGameType().equals("bdf"))
                                    Backup.save.saveBDF(filename);
                                }
                            }

                        }
                );
                menu.add(item);
            }
            {
                JMenuItem item = new JMenuItem("Beenden");
                item.addActionListener(
                        (e) -> {
                            if(DataContainer.getGameType().equals("mp") || DataContainer.getGameType().equals("mps")) {
                                Network.closeClientConnection();
                                Network.closeHostConnection();
                            }
                            playView.dispose();
                            if(DataContainer.getGameType().equals("mp") || DataContainer.getGameType()
                                    .equals("mps")) {

                            }
                        }
                );
                menu.add(item);
            }
            bar.add(menu);
        }
        /**
         * ScrollPane und TextArea unterhalb des Spielfeldes für
         * saemtliche Informationen
         */
        JScrollPane scrollPane = new JScrollPane();
        JTextArea textArea = new JTextArea(5, 10);
        textArea.setFont(new Font("Verdana", Font.PLAIN, 10));

        scrollPane.getViewport().add(textArea);

        /**
         * TextArea ausgabe
         */
        textArea.append("Das linke Feld ist Ihres.\n");
        if (DataContainer.getGameType().equals("ss") || DataContainer.getGameType().equals("bdf")) {
            textArea.append("Spieler beginnt\n" + "Geben Sie einen Schuss ab,\n" + "indem Sie in eine Zelle des \n"
                    + "rechten Spielfeldes klicken.\n");
        }


        DataContainer.getPlayerShootTable().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                TouchedMouse(event);
            }
            public void mouseReleased(MouseEvent event) {

            }
        });


        Box hbox = Box.createHorizontalBox();
        hbox.add(tablePlayer);
        hbox.add(Box.createHorizontalStrut(10));
        hbox.add(DataContainer.getPlayerShootTable());


        playView.setJMenuBar(bar);
        playView.add(hbox);
        playView.add(scrollPane);
        playView.pack();
        playView.setLocationRelativeTo(null);
        playView.setVisible(true);
        Game.hitloop();
    }

    private void TouchedMouse(MouseEvent e) {
        /*
        speichert die angeklickte Zelle der Table
         */
        Point x = e.getPoint();
        int column = PlayerShootTable.columnAtPoint(x);
        int row = PlayerShootTable.rowAtPoint(x);

        if (e.getButton() == MouseEvent.BUTTON1) {  //Linke Maustaste
            if(DataContainer.getPlayerShootTable().getValueAt(row,column).equals(9)) {
                int i= Game.shoot(column, row);
                if(i==0){
                    Game.hitloop();
                }
            }
            //TODO aufruf schießen Methode
        }
        // rechte Maustaste
        else{

        }



    }
}
