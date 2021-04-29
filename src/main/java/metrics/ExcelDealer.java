package metrics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Allow to create and read Excel files
 * 
 * @author Pedro Pereira, Tiago Mendes, Pedro Pinheiro
 * @version 1.0
 *
 */
public class ExcelDealer {

	/** Create an Excel file with the given name, content and Excel sheet name.
	 * 
	 * @param path, path of the Excel file to be created
	 * @param rows, string arrays with the information to insert on the Excel file
	 * @param sheetName, name of the Excel sheet
	 * @throws Exception, we throw exceptions to be dealt with on the GUI
	 */
	public static void createExcelFile(String path, List<String[]> rows, String sheetName) throws Exception {
		File file = new File(path);
		file.delete();
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		for (int i = 0; i != rows.size(); i++)
			createRow(sheet.createRow(i), rows.get(i), i, i != 0 ? null : style);

		for (int i = 0; i != rows.get(0).length; i++)
			sheet.autoSizeColumn(i);

		FileOutputStream outputStream = new FileOutputStream(path + ".xlsx");
		wb.write(outputStream);
		outputStream.close();
		wb.close();
	}

	/** Write a row on the Excel file creation with the given information and CellStyle
	 * 
	 * @param row, excel row to be filled the given information
	 * @param info, information to be written on the given row
	 * @param id, 
	 * @param style, style given to the given Excel row
	 */
	private static void createRow(XSSFRow row, String[] info, int id, CellStyle style) {
		if (id != 0)
			row.createCell(0).setCellValue(id);
		for (int i = 0; i != info.length; i++) {
			XSSFCell cell = row.createCell(id != 0 ? i + 1 : i);
			if (style != null)
				cell.setCellStyle(style);
			cell.setCellValue(info[i]);
		}
	}

////////////////////                   READING METHODS                   ////////////////////      

	/** Returns a list of every cell on a given Excel file column
	 * 
	 * @param path, path of the Excel file to be read
	 * @param sheet_Index, index of the Excel sheet to be read
	 * @param col_Index, index of the Excel column desired
	 * @param repetition, true if repeated values are desired, false if not
	 * @return List<String> with all the given column cells
	 * @throws Exception, we throw exceptions to be dealt with on the GUI
	 */
	public static List<String> getAllCellsOfColumn(String path, int sheet_Index, int col_Index, boolean repetition)
			throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(new File(path)));
		XSSFSheet sheet = wb.getSheetAt(sheet_Index);

		List<String> values = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null && row.getCell(col_Index) != null)
				if (repetition)
					values.add(row.getCell(col_Index).toString());
				else if (!values.contains(row.getCell(col_Index).toString()))
					values.add(row.getCell(col_Index).toString());
		}

		wb.close();
		return values;
	}

	/** Returns the total of the sum of all integers in a given column of the given Excel file and sheet
	 * 
	 * @param path, path of the Excel file to be read
	 * @param sheet_Index, index of the Excel sheet to be read
	 * @param col_Index, index of the Excel column desired
	 * @return returns total of the sum of all integers in a given column
	 * @throws Exception, we throw exceptions to be dealt with on the GUI
	 */
	public static int sumAllColumn(String path, int sheet_Index, int col_Index) throws Exception {
		List<String> collumn = getAllCellsOfColumn(path, sheet_Index, col_Index, true);
		int total = 0;

		for (String value : collumn) {
			try {
				total += Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
		}
		return total;
	}

	/** Returns all the rows in the given Excel file and sheet
	 * 
	 * @param path, path of the Excel file to be read
	 * @param sheet_Index, index of the Excel sheet to be read
	 * @return returns List<Object[]> with all the rows of the given Excel file and sheet
	 * @throws Exception, we throw exceptions to be dealt with on the GUI
	 */
	public static List<Object[]> getAllRows(String path, int sheet_Index) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(new File(path)));
		XSSFSheet sheet = wb.getSheetAt(sheet_Index);

		int row_size = sheet.getRow(0).getPhysicalNumberOfCells();
		List<Object[]> rows = new ArrayList<>();

		for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
			Object[] rowList = new Object[row_size];
			XSSFRow row = sheet.getRow(j);
			if (row != null) {
				for (int i = 0; i < row_size; i++)
					rowList[i] = row.getCell(i);
				
				rows.add(rowList);
			}
		}
		wb.close();
		return rows;
	}

	/** Returns a specific row of the given Excel file, sheet and row
	 * 
	 * @param path, path of the Excel file to be read
	 * @param sheet_Index, index of the Excel sheet to be read
	 * @param row_Index, index of the Excel row to be read
	 * @return returns Object[] correspondent to be desired row
	 * @throws Exception, we throw exceptions to be dealt with on the GUI
	 */
	public static Object[] getRow(String path, int sheet_Index, int row_Index) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(new File(path)));
		XSSFSheet sheet = wb.getSheetAt(sheet_Index);

		Object[] rowList = new Object[sheet.getRow(0).getPhysicalNumberOfCells()];
		XSSFRow row = sheet.getRow(row_Index);

		for (int i = 0; i != rowList.length; i++)
			rowList[i] = row.getCell(i);

		wb.close();
		return rowList;
	}
}