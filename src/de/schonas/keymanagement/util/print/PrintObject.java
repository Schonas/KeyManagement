package de.schonas.keymanagement.util.print;

import javafx.scene.Node;

import java.awt.*;
import java.awt.print.*;

import static de.schonas.keymanagement.main.MainPage.CURRENT_KEY;

public class PrintObject implements Printable {

    private static Font paragraph = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    private static Font topic = new Font(Font.SANS_SERIF, Font.BOLD, 20);

    private String text;

    /**
     * Objekt für Druck
     * @param text Text der zwischen Über- und Unterschrift steht
     */
    public PrintObject (String text){
        this.text = text;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        g.setFont(topic);
        g.drawString("Schlüsselverwaltung", 100, 100);

        g.setFont(paragraph);
        g.drawString(text, 100, 140);

        g.drawString("_____________", 100, 180);
        g.drawString("_____________", 300, 180);
        g.drawString("Mitarbeiter/in", 100, 200);
        g.drawString("Verwalter/in", 300, 200);

        return Printable.PAGE_EXISTS;
    }
}