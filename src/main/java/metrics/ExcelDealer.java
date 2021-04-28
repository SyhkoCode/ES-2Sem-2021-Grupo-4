package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDealer {

	public static void createExcelFile(String path, List<String[]> rows, String sheetName) throws Exception {
		File file = new File(path);
		file.delete();
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		for (int i = 0; i != rows.size() - 1; i++)
			createRow(sheet.createRow(i), rows.get(i), i, i != 0 ? null : style);

		for (int i = 0; i != rows.get(0).length; i++)
			sheet.autoSizeColumn(i);

		FileOutputStream outputStream = new FileOutputStream(path + ".xlsx");
		wb.write(outputStream);
		outputStream.close();
		wb.close();
	}

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

	public static List<String> getClassMethods(String path, int sheet_Index, int col_Index, String className)
			throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(new File(path)));
		XSSFSheet sheet = wb.getSheetAt(sheet_Index);

		List<String> values = new ArrayList<>();

		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row != null && row.getCell(2).toString().equals(className))
				if (row.getCell(col_Index) != null && !values.contains(row.getCell(col_Index).toString()))
					values.add(row.getCell(col_Index).toString());

		}

		wb.close();
		return values;
	}

	public static List<String> getAllCellsOfColumn(String path, int sheet_Index, int col_Index, boolean repetion)
			throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(new File(path)));
		XSSFSheet sheet = wb.getSheetAt(sheet_Index);

		List<String> values = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null && row.getCell(col_Index) != null)
				if (repetion)
					values.add(row.getCell(col_Index).toString());
				else if (!values.contains(row.getCell(col_Index).toString()))
					values.add(row.getCell(col_Index).toString());
		}

		wb.close();
		return values;
	}

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