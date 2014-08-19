package test.com.withinet.opaas.utl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.withinet.opaas.controller.common.UserParserException;
import com.withinet.opaas.util.ExcelUserParser;

public class TestExcelUserParser {

	@Test
	public void testParse() throws UserParserException {
		assertTrue (ExcelUserParser.parse("src/test/resources/sheet.xls").size() == 2);
	}
	
	@Test
	public void testParseName() throws UserParserException {
		assertTrue (ExcelUserParser.parse("src/test/resources/sheet.xls").get(0).getFullName().equals("Folarin"));
	}
	
	@Test
	public void testParseEmail() throws UserParserException {
		assertTrue (ExcelUserParser.parse("src/test/resources/sheet.xls").get(0).getEmail().equals("abc@xyz.com"));
	}
	
	@Test
	public void testParseQuota() throws UserParserException {
		assertTrue (ExcelUserParser.parse("src/test/resources/sheet.xls").get(0).getQuota().equals(2));
	}
	
	@Test
	public void testParseRole() throws UserParserException {
		assertTrue (ExcelUserParser.parse("src/test/resources/sheet.xls").get(0).getRole().equals("ADMIN"));
	}


}
