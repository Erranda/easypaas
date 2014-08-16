package com.withinet.opaas.wicket.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;

public class JavascriptEventConfirmation extends AttributeModifier {
 
  public JavascriptEventConfirmation(String event, String msg) {
    super(event, true, new Model(msg));
  }
 
  protected String newValue(final String currentValue, final String replacementValue) {
    return replacementValue + ";" + currentValue;
  }
}