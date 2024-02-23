package com.unika;

import com.unika.forms.ImportFormPanel;

import java.io.Serial;

public class ImportarMonitoradores extends HomePage{
    @Serial
    private static final long serialVersionUID = -5578285574021626091L;

    public ImportarMonitoradores(){
        add(new ImportFormPanel("importarMonitoradores", getFeedbackPanel()));
    }
}