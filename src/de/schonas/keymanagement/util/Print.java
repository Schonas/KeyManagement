package de.schonas.keymanagement.util;

import de.schonas.keymanagement.main.Key;
import javafx.embed.swing.SwingNode;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Print {

    private PrinterJob printerJob;

    public Print(){
        this.printerJob = PrinterJob.createPrinterJob();
    }

    public void printAllocationPaper(String jobName, Key key, Stage stage){

        PrinterJob.createPrinterJob();

        printerJob.getJobSettings().setJobName(jobName + key.getID());

        initPrint(getAllocationPaper(key), printerJob, stage);

    }

    public void printRemovalPaper(String jobName, Key key, Stage stage){

        PrinterJob.createPrinterJob();

        printerJob.getJobSettings().setJobName(jobName + key.getID());

        initPrint(getRemovalPaper(key), printerJob, stage);

    }

    private Node getAllocationPaper(Key key){

        return new SwingNode();
    }

    private Node getRemovalPaper(Key key){
        return new SwingNode();
    }

    private void initPrint(Node node, PrinterJob printerJob, Stage stage){
        if (printerJob != null)
            if (printerJob.showPrintDialog(stage.getOwner()))
                if (printerJob.printPage(node))
                    printerJob.endJob();
    }
}

