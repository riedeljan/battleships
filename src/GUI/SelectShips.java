package GUI;

import Data.DataContainer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class SelectShips {

    public SelectShips() {

        // Fenster für die Auswahl der Schiffe
        JDialog selectships = new JDialog();
        selectships.setModal(true);
        selectships.setUndecorated(true);
        selectships.setContentPane(Box.createVerticalBox());
        selectships.setMinimumSize(new Dimension(400,400));
        selectships.setBackground(Color.BLACK);

        List<JSpinner> spinners = new ArrayList<JSpinner>();



        // JLabel Waehle Anzahl Schiffe
        JLabel label1 = new JLabel("Waehle die Anzahl der Schiffe");
        label1.setForeground(Color.WHITE);
        label1.setBackground(Color.BLACK);
        label1.setFont(new Font("Tahoma", Font.PLAIN,20));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box vbox = Box.createVerticalBox();


        Box vbox_label = Box.createVerticalBox();
        Box vbox_spinner = Box.createVerticalBox();

			/*
			 * JLabel mit dem Text "Laenge ..." wird initialisiert
			 * und zusätzlich jeweils ein spinner
			 */
        for (int i = DataContainer.getMaxShipLength(); i >=2; i--) {
            JLabel labelSize = new JLabel("Laenge " + i + " : ");
            labelSize.setForeground(Color.WHITE);
            labelSize.setBackground(Color.BLACK);
            labelSize.setFont(new Font("Tahoma", Font.PLAIN,20));
            vbox_label.add(labelSize);

            final SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 10, 1);
            final JSpinner Size = new JSpinner(model);
            Size.setMinimumSize(new Dimension(50,25));
            Size.setMaximumSize(new Dimension(50,25));
            Size.setPreferredSize(new Dimension(50,25));


            vbox_spinner.add(Size);

			/*
			 * Der JSpinner wird zur Liste spinners hinzugefuegt
			 */
            spinners.add(Size);
        }


        // Bestätigung Button
        JButton ok = new JButton("OK");
        ok.setBackground(Color.BLACK);
        ok.setForeground(Color.WHITE);
        ok.setFont(new Font("Tahoma", Font.PLAIN, 20));
        ok.setEnabled(true);

        ok.addActionListener(
                (e) -> {
                    DataContainer.setShipStack();
                    if (DataContainer.setShipTypePush(spinners,
                            DataContainer.getOccupancy())){

                        selectships.dispose();
                        new PlaceShips();
                    }else{
                        /*
                        Falls die zulaessige Anzahl der Schiffe überschritten wurde
                        werden die Stacks neu erstellt
                         */
                        DataContainer.setShipStack();
                    }


                }
        );
        // abbrechen Button (schließt den JDialog)
        JButton abbrechen = new JButton("abbrechen");
        abbrechen.setBackground(Color.BLACK);
        abbrechen.setForeground(Color.WHITE);
        abbrechen.setFont(new Font("Tahoma", Font.PLAIN, 20));
        abbrechen.addActionListener(
                (e) -> {
                    selectships.dispose(); }
        );



        Box btn_box = Box.createHorizontalBox();




        btn_box.add(ok);
        btn_box.add(Box.createHorizontalStrut(2));
        btn_box.add(abbrechen);

        vbox.add(Box.createVerticalStrut(10));
        vbox.add(label1);
        vbox.add(Box.createVerticalStrut(20));

        Box horizont = Box.createHorizontalBox();
        horizont.add(vbox_label);
        horizont.add(vbox_spinner);
        vbox.add(horizont);

        selectships.add(vbox);

        selectships.add(btn_box);
        selectships.pack();
        selectships.setLocationRelativeTo(null);
        selectships.setVisible(true);

    }
}
