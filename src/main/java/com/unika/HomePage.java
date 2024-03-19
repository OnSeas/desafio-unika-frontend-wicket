package com.unika;

import com.unika.forms.ImportFormPanel;
import lombok.Getter;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.io.Serial;

@Getter
public class HomePage extends WebPage {
	@Serial
	private static final long serialVersionUID = 1L;
	final private FeedbackPanel feedbackPanel;
	private final ModalWindow modalWindow = new ModalWindow("modalWindow");

	public HomePage() {
		feedbackPanel = new FeedbackPanel("feedbackPanel"){ // FeedbackPanel geral para todas as validações.
			@Serial
			private static final long serialVersionUID = 2492540429304416843L;
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(false); // Incialmente hiden
				if(anyMessage(250) && !anyErrorMessage()){ // O anyMessage(250) deveria retornar só mensagens de sucesso, mas em alguns casos está gerando bug. Ent a segunda validação está resolvendo isto por enquanto.
					setVisible(true);

					String jqueryScriptClass = "$('#" + getMarkupId() + "').addClass(\"alert-success\");";
					getResponse().write("<script type=\"text/javascript\">" + jqueryScriptClass + "</script>");

					String jqueryScript = "$('#" + getMarkupId() + "').delay(4100).fadeOut();";
					getResponse().write("<script type=\"text/javascript\">" + jqueryScript + "</script>");
				}
				if(anyErrorMessage()){ // Só em mensagens de erro.
					setVisible(true);

					String jqueryScriptClass = "$('#" + getMarkupId() + "').addClass(\"alert-danger\");";
					getResponse().write("<script type=\"text/javascript\">" + jqueryScriptClass + "</script>");

					String jqueryScript = "$('#" + getMarkupId() + "').delay(4100).fadeOut();";
					getResponse().write("<script type=\"text/javascript\">" + jqueryScript + "</script>");
				}
			}
		};
		feedbackPanel.add(new AjaxEventBehavior("click") {
			@Serial
			private static final long serialVersionUID = 3239147858404127676L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				feedbackPanel.setVisible(false);
				target.add(feedbackPanel);
			}
		});
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		add(feedbackPanel);

		modalWindow.setOutputMarkupId(true);
		modalWindow.setOutputMarkupPlaceholderTag(true);
		modalWindow.setResizable(false);
		modalWindow.showUnloadConfirmation(false);
		modalWindow.setInitialWidth(30);
		modalWindow.setWidthUnit("%");
		modalWindow.setInitialHeight(230);
		add(modalWindow);


		add(new AjaxLink<Void>("homePage") {
			@Serial
			private static final long serialVersionUID = -3861353272569056434L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ListarMonitoradores.class);
			}
		});

		add(new AjaxLink<Void>("formMonitorador") {
			@Serial
			private static final long serialVersionUID = -6420487693954995450L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(MonitoradorForm.class);
			}
		});

		add(new AjaxLink<Void>("formImport") {
			@Serial
			private static final long serialVersionUID = 4025427636012099392L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.setContent(new ImportFormPanel(modalWindow.getContentId(), getFeedbackPanel()));
				modalWindow.show(target);
			}
		});
    }
}
