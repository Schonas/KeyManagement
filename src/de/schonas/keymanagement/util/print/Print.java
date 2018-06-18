package de.schonas.keymanagement.util.print;
import java.awt.print.*;

public class Print {
    private PrinterJob prjob;
    private PageFormat pfUse;

    public static void main(String[] args) {
        int iResMul = 4;  // 1 = 72 dpi; 4 = 288 dpi
        if (0 < args.length)
            try {
                iResMul = Integer.parseInt(args[0]);
            } catch (Exception ignored) {
            }
        Print myPrint = new Print();
        if (1 < args.length || myPrint.setupDialogs())
            try {
                myPrint.print(iResMul);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        System.exit(0);
    }

    private Print() {
        prjob = PrinterJob.getPrinterJob();
        pfUse = prjob.defaultPage();
    }

    private boolean setupDialogs() {
        PageFormat pfDflt = pfUse;
        pfUse = prjob.pageDialog(pfDflt);
        return (pfUse == pfDflt) ? false : prjob.printDialog();
    }

    private void print(int iResMul) throws PrinterException {
        // See file MyPrintableObject.java:
        PrintObject prObj = new PrintObject();
        prObj.iResMul = iResMul;
        prjob.setPrintable(prObj, pfUse);
        prjob.print();
    }
}