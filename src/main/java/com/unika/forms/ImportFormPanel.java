package com.unika.forms;

import com.unika.Panels.MonitoradorListPanel;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

    private final WebMarkupContainer pageContent;

    public ImportFormPanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer pageContent) {
        super(id);
        this.feedbackPanel = feedbackPanel;
        this.pageContent = pageContent;

        FileUploadField fileUploadField = new FileUploadField("importField");

        Form<FileUpload> form = new Form<>("importForm");

        form.add(new AjaxButton("submitAjax", form) {
            @Serial
            private static final long serialVersionUID = 8938068611108208621L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                FileUpload fileUpload = fileUploadField.getFileUpload();

                try {
                    File file = new File("C:\\Projetos\\zArquivos\\recebidos\\" + fileUpload.getClientFileName());
                    System.out.println(file.getAbsolutePath());
                    fileUpload.writeTo(file);
                    monitoradorApi.importarXLSX(file);
                    feedbackPanel.success("Monitodares importados com sucesso!");
                    pageContent.get("contentPanel").replaceWith(new MonitoradorListPanel("contentPanel", feedbackPanel, pageContent));
                    target.add(feedbackPanel, pageContent);
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
        form.setMaxSize(Bytes.megabytes(2));
        form.add(fileUploadField);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // jQuery para fazer sideBar ficar com css de selecionado de acordo com a página
        response.render(OnDomReadyHeaderItem.forScript("$('#formImportID').addClass(\"active\"); " +
                "$('#homePageID').removeClass(\"active\"); " +
                "$('#formMonitoradorID').removeClass(\"active\");"));
    }
}
