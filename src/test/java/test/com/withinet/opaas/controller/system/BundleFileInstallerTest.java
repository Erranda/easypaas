package test.com.withinet.opaas.controller.system;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Test;

import com.withinet.opaas.controller.system.BundleInstaller;
import com.withinet.opaas.controller.system.impl.BundleFileInstallerImpl;
import com.withinet.opaas.model.domain.Bundle;

public class BundleFileInstallerTest {
	BundleInstaller object = new BundleFileInstallerImpl ();
	
	@Test
	public void testJar() throws IOException {
		String filePath = "src/test/resources/test2.jar";
		String destPath = "src/test/resources/temp/"+System.currentTimeMillis();
		Bundle bundle = object.installBundle(filePath, destPath);
		System.out.println( bundle.getSymbolicName() + " " + bundle.getLocation() + new File ("src/test/resources/temp/test2.jar").getAbsoluteFile());
		assertTrue (bundle.getSymbolicName().equals("test2.jar"));
		FileUtils.deleteDirectory(new File(destPath));
	}
	
	@Test
	public void testZip() throws IOException, ParserConfigurationException {
		String filePath = "src/test/resources/test2.zip";
		List<String> bundles = new ArrayList<String> ();
		bundles.add(filePath);
		String destPath = "src/test/resources/temp/"+System.currentTimeMillis();
		List<Bundle> bundled  = object.installZip(filePath, destPath);
		System.out.println ("Zip: " + bundled.size());
		assertTrue (bundled.size() > 0);
		FileUtils.deleteDirectory(new File(destPath));
	}
	
	@Test
	public void testPom() throws IOException, ParserConfigurationException {
		String filePath = "src/test/resources/pom.xml";
		List<String> bundles = new ArrayList<String> ();
		bundles.add(filePath);
		String destPath = "src/test/resources/temp/"+System.currentTimeMillis();
		List<Bundle> bundled = object.installPom(new File(filePath).getAbsolutePath(), destPath);
		System.out.println ("test Pom :" +  bundled.size());
		assertTrue (object.installPom(new File(filePath).getAbsolutePath(), new File(destPath).getAbsolutePath()).size() > 0);
		FileUtils.deleteDirectory(new File(destPath));
	}
	
	@Test
	public void testAll () throws IOException, ParserConfigurationException {
		String filePath0 = "src/test/resources/test1.jar";
		String filePath1 = "src/test/resources/test2.zip";
		String filePath2 = "src/test/resources/pom.xml";
		List<String> bundles = new ArrayList<String> ();
		bundles.add(filePath0);
		bundles.add(filePath1);
		bundles.add(filePath2);
		System.out.println (System.currentTimeMillis());
		String destPath = "src/test/resources/temp/"+System.currentTimeMillis();
		List<Bundle> bundled = object.installBundles(bundles, destPath);
		System.out.println ("test All :" +  bundled.size());
		assertTrue (bundled.size() > 0);
		FileUtils.deleteDirectory(new File(destPath));
	}

}
