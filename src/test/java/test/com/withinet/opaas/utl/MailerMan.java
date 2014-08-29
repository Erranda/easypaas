/**
 * 
 */
package test.com.withinet.opaas.utl;

import static org.junit.Assert.assertTrue;

import javax.mail.MessagingException;

import org.junit.Test;

import com.withinet.opaas.controller.common.UserParserException;
import com.withinet.opaas.util.ExcelUserParser;
import com.withinet.opaas.util.MailMan;

/**
 * @author Folarin
 *
 */
public class MailerMan {
	
	@Test
	public void testParse() throws UserParserException {
		MailMan mailer = new MailMan ();	
		try {
			mailer.sendMessage("Registeration Withinet Cloud OSGi Platform","folarinomotoriogun@gmail.com", "Hello world");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
