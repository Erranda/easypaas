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

import com.withinet.opaas.model.domain.Bundle;

/**
 * 
 * @author Folarin
 *
 */
public class BundleTableDataProvider extends SortableDataProvider<Bundle, String> {
	
	public BundleTableDataProvider (Long userId) {
		//Initialize projects for user
		Bundle b = new Bundle ();
		b.setID(1L);
		b.setLocation("file://sdkadad/fakfa.jar");
		b.setSymbolicName("Logging.jar");
		b.setUpdated(new Date ());
		userBundles.add(b);
		
		Bundle b1 = new Bundle ();
		b1.setID(1L);
		b1.setLocation("file://sdkadad/fakfa.jar");
		b1.setSymbolicName("Logging.jar");
		b1.setUpdated(new Date ());
		userBundles.add(b1);
	}
	
	private List<Bundle> userBundles = new ArrayList<Bundle> ();
	
	@Override
	public Iterator<? extends Bundle> iterator(long arg0, long arg1) {
		return userBundles.subList((int) arg0, Math.min((int) userBundles.size(), (int) arg1)).iterator();
	}

	@Override
	public IModel<Bundle> model(Bundle arg0) {
		return Model.of(arg0);
	}

	@Override
	public long size() {
		return userBundles.size();
	}

}
