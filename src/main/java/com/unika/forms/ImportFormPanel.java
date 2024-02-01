package com.unika.forms;

import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.lang.Bytes;

import java.io.File;

public class ImportFormPanel extends Panel {
    private static final long serialVersionUID = 7680031748038474194L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();

    public ImportFormPanel(String id) {
        super(id);

        FileUploadField fileUploadField = new FileUploadField("importField");

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel"){
            private static final long serialVersionUID = -6225292489343766625L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(anyMessage()); // hum?
            }
        };
        add(feedbackPanel);

        Form form = new Form("importForm"){
            private static final long serialVersionUID = 1118389335380636056L;
            @Override
            protected void onSubmit() {
                super.onSubmit();

                FileUpload fileUpload = fileUploadField.getFileUpload();

                try {
                    File file = new File(fileUpload.getClientFileName());
                    System.out.println(file.getAbsolutePath());
                    fileUpload.writeTo(file);
                    info(monitoradorApi.importarXLSX(file));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                    error(e.getMessage());
                }
            }
        };

        form.setMultiPart(true); // Set to true to use enctype='multipart/form-data', and to process file uploads by default multiPart = false
        form.setMaxSize(Bytes.megabytes(2));
        form.add(fileUploadField);
        add(form);
    }
}
