package com.withinet.opaas.wicket;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.model.DownloadRepository;
import com.withinet.opaas.model.domain.Download;
import com.withinet.opaas.wicket.html.Login;
import com.withinet.opaas.wicket.html.Register;
import com.withinet.opaas.wicket.html.Users;


@SuppressWarnings({ "rawtypes", "serial" })
/**
 * Basepage for all pages with common markups
 * 
 * @author Folarin Omotoriogun
 * @since August 23, 2013
 */
public class Index extends WebPage {
	
	@SpringBean 
	DownloadRepository dp;
	public Index () {
		add (new BookmarkablePageLink("login", Login.class));
		add (new BookmarkablePageLink("register", Register.class));
		add (new BookmarkablePageLink("users", Users.class));
		add (new AjaxLink ("download"){
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				dp.save(new Download ());
				throw new RestartResponseAtInterceptPageException (new RedirectPage ("https://sourceforge.net/projects/easypaas/files/easypaas-1.0.0.jar/download"));
			}
		});
		Label downloads = new Label ("downloads", dp.findAll().size());
		add (downloads);
	}

}
