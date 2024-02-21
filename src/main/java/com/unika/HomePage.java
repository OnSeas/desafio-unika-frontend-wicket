package com.unika;

import lombok.Getter;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serial;

@Getter
public class HomePage extends WebPage {
	@Serial
	private static final long serialVersionUID = 1L;

	final private FeedbackPanel feedbackPanel;

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
    }
}
