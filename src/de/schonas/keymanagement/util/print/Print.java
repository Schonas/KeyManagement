package de.schonas.keymanagement.util.print;

import de.schonas.keymanagement.Key;
import javafx.print.PrinterJob;

public class Print {

    private PrinterJob printerJob;
    private Key printKey;

    public Print(Key key) {
        this.printerJob = PrinterJob.createPrinterJob();
        printKey = key;
    }

    public void print(){
        printerJob.printPage(null);
    }
}

