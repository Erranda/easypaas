package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;



@SuppressWarnings({ "rawtypes", "serial" })
/**
 * Basepage for all pages with common markups
 * 
 * @author Folarin Omotoriogun
 * @since August 23, 2013
 */
public class PageError extends Authenticated {
	public PageError () {
		add (new BookmarkablePageLink("home-from-error", Login.class));
	}
	
	@Override
    public void onInitialize () {
    	super.onInitialize();
    }
}
