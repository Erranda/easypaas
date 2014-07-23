package com.withinet.opaas.controller.common;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


public class DomainConstraintValidator < T extends Object> {
	
    private Validator validator;
    
    public boolean isValid (T object) {
		 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	     validator = factory.getValidator();
	     Set<ConstraintViolation<T>> constraintViolations =
	             validator.validate(object);
	     return (constraintViolations.size() == 0);
	}
}
