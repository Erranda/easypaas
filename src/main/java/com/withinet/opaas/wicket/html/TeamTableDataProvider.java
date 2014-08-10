/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.withinet.opaas.model.domain.User;

/**
 * 
 * @author Folarin
 *
 */
public class TeamTableDataProvider extends SortableDataProvider<User, String> {
	
	public TeamTableDataProvider (Long userId) {
		//Initialize projects for user
		User u = new User ();
		u.setID (21L);
		u.setCreated(new Date());
		u.setEmail("felix@xyz.com");
		u.setFullName("Felix Omogoye");
		u.setLocation("null");
		u.setPassword("ufihbavdiabvif");
		u.setStatus("Active");
		userUsers.add(u);
		
		User u1 = new User ();
		u1.setID (22L);
		u1.setCreated(new Date());
		u1.setEmail("fabby@xyz.com");
		u1.setFullName("Fab Olunah");
		u1.setLocation("null");
		u1.setPassword("ufihbavdiasdsabvif");
		u1.setStatus("Active");
		userUsers.add(u1);
	
	}
	
	private List<User> userUsers = new ArrayList<User> ();
	
	@Override
	public Iterator<? extends User> iterator(long arg0, long arg1) {
		return userUsers.subList((int) arg0, Math.min((int) userUsers.size(), (int) arg1)).iterator();
	}

	@Override
	public IModel<User> model(User arg0) {
		return Model.of(arg0);
	}

	@Override
	public long size() {
		return userUsers.size();
	}

}
