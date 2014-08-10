/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Folarin
 *
 */
public class TeamAddMemberSectionWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3913459210509162699L;

	public TeamAddMemberSectionWidget(String id) {
		super(id);
		add (new TeamAddFileWidget ("team-add-by-file-widget"));
		add (new TeamAddMemberWidget ("team-add-member-widget"));
	}

}
