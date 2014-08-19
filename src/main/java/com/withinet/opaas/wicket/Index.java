package com.withinet.opaas.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.withinet.opaas.wicket.html.Login;
import com.withinet.opaas.wicket.html.Register;


@SuppressWarnings({ "rawtypes", "serial" })
/**
 * Basepage for all pages with common markups
 * 
 * @author Folarin Omotoriogun
 * @since August 23, 2013
 */
public class Index extends WebPage {
	public Index () {
		add (new BookmarkablePageLink("login", Login.class));
		add (new BookmarkablePageLink("register", Register.class));
	}

}
