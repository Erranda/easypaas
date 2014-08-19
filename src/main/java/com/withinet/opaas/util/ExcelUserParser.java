/**
 * 
 */
package com.withinet.opaas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.withinet.opaas.controller.common.UserParserException;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
public abstract class ExcelUserParser  {
	public static List<User> parse (String fileLocation) throws UserParserException {
		List<User> users = new ArrayList<User> ();
		FileInputStream file;
		try {
			file = new FileInputStream(new File(fileLocation));
			HSSFWorkbook  workbook = new HSSFWorkbook  (file);
			HSSFSheet sheet = workbook.getSheetAt(0);
			//Get iterator to all the rows in current sheet
			Iterator<Row> rowIt = sheet.iterator();
			int errorCount = 0;
			
			int namePos = 0;
			int emailPos = 0;
			int quotaPos = 0;
			int rolePos = 0;
			
			boolean foundName = false;
			boolean foundEmail = false;
			boolean foundQuota = false;
			boolean foundRole = false;
			
			int rowCount = 0;
			
			while (rowIt.hasNext()) {
				Row row = rowIt.next();
				
				Iterator<Cell> cellIt = row.cellIterator();
				
				if (rowCount == 0) {
					while (cellIt.hasNext()) {
						Cell cell = cellIt.next();
						switch(cell.getCellType()) {
			                case Cell.CELL_TYPE_STRING:
			                    if (rowCount == 0) {
			                    	if (!cell.getStringCellValue().toLowerCase().trim().matches(".*name.*") && foundName == false)
				                    	namePos++;
				                    else 
				                    	foundName = true;
				                    if (!cell.getStringCellValue().toLowerCase().trim().matches(".*email.*") && foundEmail == false)
				                    	emailPos++;
				                    else
				                    	foundEmail = true;
				                    if (!cell.getStringCellValue().toLowerCase().trim().matches(".*role.*") && foundRole == false)
				                    	rolePos++;
				                    else
				                    	foundRole = true;
				                    if (!cell.getStringCellValue().toLowerCase().trim().matches(".*quota.*") && foundQuota == false)
				                    	quotaPos++;
				                    else 
				                    	foundQuota = true;
			                    }
			                    break;
			            }
					}
					if (!foundName || !foundEmail || !foundQuota || !foundRole)
						throw new UserParserException ("A required column could not be identified as Name, Email, Quota, Role");
				} else {
					try {
						User user = new User ();
						user.setFullName(row.getCell (namePos).getStringCellValue());
						user.setEmail(row.getCell (emailPos).getStringCellValue());
						user.setQuota((int) row.getCell (quotaPos).getNumericCellValue());
						user.setRole(row.getCell (rolePos).getStringCellValue());
						users.add(user);
					} catch (Exception e) {
						errorCount++;
					}
				}
				rowCount++;
			}
			
		} catch (FileNotFoundException e) {
			throw new UserParserException ("An error occured while reading the file");
		} catch (IOException e) {
			throw new UserParserException ("An error occured while reading the file");
		}
		return users;
	}
}
