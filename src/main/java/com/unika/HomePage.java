package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import com.unika.forms.ImportFormPanel;
import com.unika.forms.MonitoradorFormPanel;
import com.unika.model.Monitorador;
import lombok.Getter;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.io.Serial;

@Getter
public class HomePage extends WebPage {
	@Serial
	private static final long serialVersionUID = 1L;
	final private FeedbackPanel feedbackPanel;
	private WebMarkupContainer pageContent = new WebMarkupContainer("pageContent");

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

					String jqueryScript = "$('#" + getMarkupId() + "').delay(2600).fadeOut();";
					getResponse().write("<script type=\"text/javascript\">" + jqueryScript + "</script>");
				}
				if(anyErrorMessage()){ // Só em mensagens de erro.
					setVisible(true);

					String jqueryScriptClass = "$('#" + getMarkupId() + "').addClass(\"alert-danger\");";
					getResponse().write("<script type=\"text/javascript\">" + jqueryScriptClass + "</script>");

					String jqueryScript = "$('#" + getMarkupId() + "').delay(2600).fadeOut();";
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


		// Criação da página
		pageContent.add(new MonitoradorListPanel("contentPanel", getFeedbackPanel(), pageContent));
		pageContent.setOutputMarkupId(true);
		pageContent.setOutputMarkupPlaceholderTag(true);
		add(pageContent);

		add(new AjaxLink<Void>("homePage") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				pageContent.get("contentPanel").replaceWith(new MonitoradorListPanel("contentPanel", getFeedbackPanel(), pageContent));
				target.add(pageContent);
			}
		});

		add(new AjaxLink<Void>("formMonitorador") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				pageContent.get("contentPanel").replaceWith(new MonitoradorFormPanel("contentPanel", new Monitorador(), getFeedbackPanel(), pageContent));
				target.add(pageContent);
			}
		});

		add(new AjaxLink<Void>("formImport") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				pageContent.get("contentPanel").replaceWith(new ImportFormPanel("contentPanel", getFeedbackPanel(), pageContent));
				target.add(pageContent);
			}
		});
    }
}
