package test.com.withinet.opaas.wicket.pages;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.withinet.opaas.Application;
import com.withinet.opaas.wicket.html.Login;
import com.withinet.opaas.wicket.services.WebInitializer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebInitializer.class,
		Application.class })

public class WebApplicationTest {

	@Autowired
	private TestService testService;

	@Test
	public void testApplication() {
		WicketTester wicketTester = testService.getWicketTester();
		wicketTester.startPage(Login.class);
		wicketTester.assertRenderedPage(Login.class);
	}

}
