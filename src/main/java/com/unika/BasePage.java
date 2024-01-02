package com.unika;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public class BasePage extends WebPage {
    private static final long serialVersionUID = -5986383950881090047L;

    public BasePage(){
        super();
    }

    public BasePage(final PageParameters params) {
        super(params);
    }
}
