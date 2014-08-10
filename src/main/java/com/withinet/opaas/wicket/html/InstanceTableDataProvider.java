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

import com.withinet.opaas.model.domain.Instance;

/**
 * 
 * @author Folarin
 *
 */
public class InstanceTableDataProvider extends SortableDataProvider<Instance, String> {
	
	public InstanceTableDataProvider (Long userId) {
		//Initialize projects for user
		Instance i = new Instance ();
		i.setCreated(new Date());
		i.setId(2L);
		i.setCpanelUrl("#");
		i.setHostName("live-1");
		i.setContainerType("Felix");
		i.setPort(9090);
		i.setProjectName("Hello world");
		i.setStatus("Live");
		i.setOwnerName("Folarin");
		userInstances.add(i);
		
		Instance i1 = new Instance ();
		i1.setId(1L);
		i1.setCreated(new Date());
		i1.setCpanelUrl("#");
		i1.setHostName("live-1");
		i1.setContainerType("Equinox");
		i1.setPort(9090);
		i1.setProjectName("Hello world");
		i1.setStatus("Dead");
		i1.setOwnerName("Ade");
		userInstances.add(i1);
	}
	
	private List<Instance> userInstances = new ArrayList<Instance> ();
	
	@Override
	public Iterator<? extends Instance> iterator(long arg0, long arg1) {
		return userInstances.subList((int) arg0, Math.min((int) userInstances.size(), (int) arg1)).iterator();
	}

	@Override
	public IModel<Instance> model(Instance arg0) {
		return Model.of(arg0);
	}

	@Override
	public long size() {
		return userInstances.size();
	}

}
