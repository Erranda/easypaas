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
	
	private Boolean authorized;
	
	private Panel target;

	public TeamAddMemberSectionWidget(String id, Panel target) {
		super(id);
		this.target = target;
	}

	public TeamAddMemberSectionWidget(String id, TeamTableWidget table,
			boolean b) {
		this (id, table);
		authorized = b;
	}
	
	@Override
	public void onInitialize () {
		super.onInitialize();
		add (new TeamAddFileWidget ("team-add-by-file-widget", target, authorized));
		add (new TeamAddMemberWidget ("team-add-member-widget", target, authorized));
	}

}
