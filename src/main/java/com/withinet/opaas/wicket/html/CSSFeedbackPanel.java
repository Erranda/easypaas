package com.withinet.opaas.wicket.html;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

public class CSSFeedbackPanel extends FeedbackPanel {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -978068936192680553L;

	public CSSFeedbackPanel(final String id, final IFeedbackMessageFilter filter) {
        super(id, filter);
    }
 
    public CSSFeedbackPanel(final String id) {
        super(id);
    }
 
    @Override
    protected Component newMessageDisplayComponent(final String id,
            final FeedbackMessage message) {
        final Component newMessageDisplayComponent = super
                .newMessageDisplayComponent(id, message);
 
        /*
         * CSS class resulting: feedbackUNDEFINED feedbackDEBUG feedbackINFO
         * feedbackWARNING feedbackERROR feedbackFATAL
         */
        newMessageDisplayComponent
                .add(new AttributeAppender("class", new Model<String>(
                        "feedback" + message.getLevelAsString()), " "));
        return newMessageDisplayComponent;
    }
 
}