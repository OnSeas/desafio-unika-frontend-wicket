package com.unika.forms;

import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
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


        // Esse é um exemplo bom que funiona pra retonar os erros na tela com o fbp
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel")
        {
            private static final long serialVersionUID = -6225292489343766625L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(anyMessage());
            }
        };
        add(feedbackPanel);

        Form<FileUpload> form = new Form<>("importForm");

        form.add(new AjaxButton("submitAjax", form) {
            private static final long serialVersionUID = 8938068611108208621L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                FileUpload fileUpload = fileUploadField.getFileUpload();

                try {
                    File file = new File(fileUpload.getClientFileName());
                    System.out.println(file.getAbsolutePath());
                    fileUpload.writeTo(file);
                    success(monitoradorApi.importarXLSX(file));
                    ModalWindow.closeCurrent(target);
                } catch (Exception e){
                    feedbackPanel.error(e.getMessage());
                    target.add(feedbackPanel);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        form.setMultiPart(true); // Set to true to use enctype='multipart/form-data', and to process file uploads by default multiPart = false
        form.setMaxSize(Bytes.megabytes(2));
        form.add(fileUploadField);
        add(form);
    }
}
