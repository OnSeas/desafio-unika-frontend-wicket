package com.unika.Panels;

import com.unika.dialogs.ConfirmationLink;
import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.UF;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.File;
import java.io.Serial;

public class InfoMonitoradorPanel extends Panel {
    @Serial
    private static final long serialVersionUID = 2097601353745752544L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final Monitorador monitorador;
    final FeedbackPanel feedbackPanel;

    public InfoMonitoradorPanel(String id, Monitorador monitorador, FeedbackPanel feedbackPanel) {
        super(id);
        this.monitorador = monitorador;
        this.feedbackPanel = feedbackPanel;

        add(new Label("infoTitle", Model.of("Info de Monitorador")));

        WebMarkupContainer pessoaCardWMC = new WebMarkupContainer("pessoaCardWMC");
        pessoaCardWMC.setOutputMarkupId(true);
        pessoaCardWMC.setOutputMarkupPlaceholderTag(true);
        add(pessoaCardWMC);

        WebMarkupContainer listEnderecoWMC = new WebMarkupContainer("listEnderecoWMC"){
            @Serial
            private static final long serialVersionUID = -1827581833305981720L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                this.setVisible(!monitorador.getEnderecoList().isEmpty());
            }
        };
        addListaEndereco(listEnderecoWMC);
        pessoaCardWMC.add(listEnderecoWMC);

        pessoaCardWMC.add(
                new Label("tipoPessoa", Model.of(monitorador.getTipoPessoa().getLabel())),
                new Label("email", Model.of(monitorador.getEmail())),
                new Label("dataNasicmento", Model.of(monitorador.getDataNascimento()))
        );

        if (monitorador.getTipoPessoa() == TipoPessoa.PESSOA_FISICA){
            pessoaCardWMC.add(
                    new Label("nomeLabel", Model.of("Nome")),
                    new Label("identificadorLabel", Model.of("CPF")),
                    new Label("rgIsncricaoLabel", Model.of("RG")),
                    new Label("nome", Model.of(monitorador.getNome())),
                    new Label("identificador", Model.of(monitorador.getCpf())),
                    new Label("rgIsncricao", Model.of(monitorador.getRg()))
            );
        } else{
            pessoaCardWMC.add(
                    new Label("nomeLabel", Model.of("Razão Social")),
                    new Label("identificadorLabel", Model.of("CNPJ")),
                    new Label("rgIsncricaoLabel", Model.of("Inscrição Social")),
                    new Label("nome", Model.of(monitorador.getRazaoSocial())),
                    new Label("identificador", Model.of(monitorador.getCnpj())),
                    new Label("rgIsncricao", Model.of(monitorador.getInscricaoEstadual()))
            );
        }

        AjaxLink<Void> desativarAjax = new ConfirmationLink<>("ativo", "Tem certeza que deseja DESATIVAR o monitorador?") {
            @Serial
            private static final long serialVersionUID = -505817807318118040L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                desativarMontorador();
                target.add(pessoaCardWMC, feedbackPanel);
            }
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(monitorador.getAtivo());
            }
        };


        AjaxLink<Void> ativarAjax = new ConfirmationLink<>("inativo", "Tem certeza que deseja ATIVAR o monitorador?") {
            @Serial
            private static final long serialVersionUID = 1671159503226842675L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                ativarMontorador();
                target.add(pessoaCardWMC, feedbackPanel);
            }
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(!monitorador.getAtivo());
            }
        };
        pessoaCardWMC.add(desativarAjax, ativarAjax);

        add(new DownloadLink("reportDownload", new AbstractReadOnlyModel<>() {
            @Serial
            private static final long serialVersionUID = -8630718144901310523L;
            @Override
            public File getObject() {
                File reportFile;
                try {
                    reportFile = monitoradorApi.gerarRelatorio(monitorador.getId());
                }
                catch (Exception e){
                    reportFile = null;
                    feedbackPanel.error(e.getMessage());
                }
                return reportFile;
            }
        }));
    }

    private void desativarMontorador() {
        try {
            monitoradorApi.desativarMonitorador(monitorador.getId());
            monitorador.setAtivo(false);
            feedbackPanel.success("Monitorador Desativado!");
        } catch (Exception e) {
            feedbackPanel.error(e.getMessage());
        }
    }
    private void ativarMontorador() {
        try {
            monitoradorApi.ativarMonitorador(monitorador.getId());
            monitorador.setAtivo(true);
            feedbackPanel.success("Monitorador Ativado!");
        } catch (Exception e) {
            feedbackPanel.error(e.getMessage());
        }
    }

    private void addListaEndereco(WebMarkupContainer wmc){
        wmc.add(new ListView<>("enderecos", monitorador.getEnderecoList()) {
            @Serial
            private static final long serialVersionUID = 7611437418587202266L;
            @Override
            protected void populateItem(ListItem<Endereco> listItem) {
                listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                listItem.add(new Label("endereco", new PropertyModel<Endereco>(listItem.getModel(),"endereco")));
                listItem.add(new Label("bairro", new PropertyModel<Endereco>(listItem.getModel(),"bairro")));
                listItem.add(new Label("cidade", new PropertyModel<Endereco>(listItem.getModel(),"cidade")));
                listItem.add(new Label("estado", new PropertyModel<UF>(listItem.getModel(),"estado").getObject().getSigla()));
            }
        });
    }
}
