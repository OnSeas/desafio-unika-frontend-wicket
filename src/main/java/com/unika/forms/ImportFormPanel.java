package com.unika.forms;

import com.unika.ListarMonitoradores;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.lang.Bytes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serial;

public class ImportFormPanel extends Panel {
    @Serial
    private static final long serialVersionUID = 7680031748038474194L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final FeedbackPanel feedbackPanel;

    public ImportFormPanel(String id, FeedbackPanel feedbackPanel) {
        super(id);
        this.feedbackPanel = feedbackPanel;

        FileUploadField fileUploadField = new FileUploadField("importField");

        Form<FileUpload> form = new Form<>("importForm");

        form.add(new AjaxLink<>("ajaxCancelar") {
            @Serial
            private static final long serialVersionUID = -8806215908629462715L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                ModalWindow.closeCurrent(target);
            }
        });


        form.add(new AjaxButton("submitAjax", form) {
            @Serial
            private static final long serialVersionUID = 8938068611108208621L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                FileUpload fileUpload = fileUploadField.getFileUpload();
                try {
                    File file = new File("C:\\Projetos\\zArquivos\\recebidos\\" + fileUpload.getClientFileName());
                    fileUpload.writeTo(file);
                    monitoradorApi.importarXLSX(file);
                    ModalWindow.closeCurrent(target);
                    setResponsePage(new ListarMonitoradores("Monitodares importados com sucesso!"));
                } catch (FileNotFoundException | NullPointerException ex){
                    feedbackPanel.error("É necessário enviar um arquivo!");
                } catch (Exception e){
                    feedbackPanel.error(e.getMessage());
                }
                target.add(feedbackPanel);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        form.setMultiPart(true);
        form.setMaxSize(Bytes.megabytes(50));
        form.add(fileUploadField);
        add(form);
    }
}
