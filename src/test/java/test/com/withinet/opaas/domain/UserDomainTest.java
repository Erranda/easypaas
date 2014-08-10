package test.com.withinet.opaas.domain;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.model.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class UserDomainTest {
	
	@Autowired
	ServiceProperties serviceProperties;
	
	@Test
	public void test() {
		String salt = serviceProperties.getSalt();
	//	String hashedPassword = BCrypt.hashpw("folarin", salt);
	//	System.out.println (hashedPassword + " " + salt);
		User user = new User ();
		//user.setPassword(hashedPassword);
	//	assertTrue (user.getPassword().equals(BCrypt.hashpw("folarin", salt)));
	}

}
