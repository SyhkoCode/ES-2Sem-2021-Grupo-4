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
	private String excel_file;
	private OPCPackage packet;
	private XSSFWorkbook wb;
	private static XSSFSheet sheet;
	private List<Integer> ignoredIndexes;

	public ExcelDealer(String filename, boolean read) {
		excel_file = filename;
		ignoredIndexes = new ArrayList<>();
		try {
			if( read ) {
				packet = OPCPackage.open(new File(excel_file));
				wb = new XSSFWorkbook(packet);
				sheet = wb.getSheetAt(0);
				ignoredIndexes.add(7);
				ignoredIndexes.add(10);
			} else {
				wb = new XSSFWorkbook();
			}
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}

	}

public void createExcelFile(String file_name, String pathToSave, List<String[]> rows) {
		File file = new File(pathToSave + "\\" + file_name);
		file.delete();
		XSSFSheet sheet = wb.createSheet(file_name);
		String[] titles = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
				"is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method" };

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i != titles.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(titles[i]);
		}

		for (int i = 0; i != rows.size(); i++) {
			row = sheet.createRow(i + 1);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(i + 1);
			for (int j = 0; j != rows.get(i).length; j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(rows.get(i)[j]);
			}
		}
		for (int i = 0; i != titles.length; i++)
			sheet.autoSizeColumn(i);

		try (FileOutputStream outputStream = new FileOutputStream(
				new String(pathToSave + "\\" + file_name + ".xlsx"))) {
			wb.write(outputStream);
			outputStream.close();
		} catch (IOException e) {

		}
	}

	public void writeFile(String file_name, List<Object[]> values) {
		XSSFSheet sheet = wb.createSheet(file_name);
		List<Object[]> bookData = new ArrayList<>();
		Object[] titles = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
				"is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method" };
		bookData.add(titles);
		bookData.addAll(values);
		int rowCount = 0;
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		for (Object[] aBook : bookData) {
			XSSFRow row = sheet.createRow(rowCount++);

			int columnCount = 0;

			for (Object field : aBook) {
				XSSFCell cell = row.createCell(columnCount++);
				if( row.getRowNum() == 0 )
					cell.setCellStyle(style);
				if( field instanceof String ) {
					cell.setCellValue((String) field);
				} else if( field instanceof Integer ) {
					cell.setCellValue((Integer) field);
				}
			}
		}

		try (FileOutputStream outputStream = new FileOutputStream(new String(file_name + ".xlsx"))) {
			wb.write(outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
////////////////////                   READING METHODS                   ////////////////////                     
	
	public List<String> getClassMethods(int col_index, String classname) {
		List<String> list = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null) {
				if ( row.getCell(2).getStringCellValue().equals(classname) )
					if ( row.getCell(col_index) != null && !list.contains(row.getCell(col_index).toString()) )
						list.add(row.getCell(col_index).toString());
			}
		}
		return list;
	}

	public List<String> getClasses() {
		List<String> list = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null) {
				if ( !list.contains(row.getCell(2).toString()) && r != 0 )
					list.add(row.getCell(2).toString());
			}
		}
		return list;
	}

	public List<Object[]> getAllRows() {
		int row_size = sheet.getRow(0).getPhysicalNumberOfCells();
		List<Object[]> list = new ArrayList<>();
		for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
			Object[] rowList = new Object[row_size - 2]; // -2 para ignorar as colunas dos booleans que o stor não quer
			XSSFRow row = sheet.getRow(j);
			if ( row != null ) {
				int counter = 0;
				for (int i = 0; i <= row_size; i++) {
					if( !ignoredIndexes.contains(i) && counter < rowList.length ) {
						rowList[counter] = row.getCell(i);
						counter++;
					}
				}
				list.add(rowList);
			}
		}
		return list;
	}

	public Object[] getExcelHeader() {
		int row_size = sheet.getRow(0).getPhysicalNumberOfCells();
		Object[] rowList = new Object[row_size - 2];
		XSSFRow row = sheet.getRow(0);
		int counter = 0;
		for (int i = 0; i < row_size; i++) {
			if (!ignoredIndexes.contains(i) && counter < rowList.length) {
				rowList[counter] = row.getCell(i);
				counter++;
			}

		}
		return rowList;
	}

	public List<String> getAllCellsOfRow(int column_index) {
		List<String> list = new ArrayList<>();
		for (String classname : getClasses()) {
			List<String> aux = getClassMethods(column_index, classname);
			for (String str : aux) {
				if ( !list.contains(str) || column_index == 3 )
					list.add(str);
			}
		}
		System.out.println(list);
		return list;
	}

	public int sumLinesOfCode() {
		List<String> list = getAllCellsOfRow(5);
		double total = 0;

		for (int i = 0; i < list.size(); i++)
			total += Double.parseDouble(list.get(i));
		return (int) total;
	}

	public String getExcel_file() {
		return excel_file;
	}


	public void addAllToIgnoredIndexes(List<Integer> indexes) {
		ignoredIndexes.addAll(indexes);
	}

	public void addToIgnoredIndexes(int index) {
		ignoredIndexes.add(index);
	}

}