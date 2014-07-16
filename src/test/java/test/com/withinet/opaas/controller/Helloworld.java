package test.com.withinet.opaas.controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.withinet.opaas.Application;
import com.withinet.opaas.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class Helloworld {

    @Autowired
    private Validator validator;

	@Test
	public void test() {
		User object = new User ();
		//object.setFullName ("Folarin O");
		object.setEmail("folarinomotoriogun12314@gmail.com");
		object.setPassword("Password");
		object.setStatus("registered");
		object.setPlatformName("TEST PLATFORM");
		object.setCreated(new Date ());
		getName (object);
	}
	
	public void getName (@Valid User valid) {
		final Set<ConstraintViolation<User>> violations = validator.validate(valid);
		 assertEquals( 2, violations.size() );
		 for (ConstraintViolation<User> violation : violations) {
			 System.out.println (violation.getMessage());
		 }
	}

}
