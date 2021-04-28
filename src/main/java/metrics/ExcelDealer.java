package metrics;

import java.io.File;
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
	private XSSFSheet sheet;

	public ExcelDealer(String filename) {
		excel_file = filename;
		try {
			System.out.println(filename);
			packet = OPCPackage.open(new File(filename));
			wb = new XSSFWorkbook(packet);
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
	}
	
//	String[] titles = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
//			"LOC_method", "CYCLO_method" };

//	pathToSave + "\\" + file_name
	
	public static void createExcelFile(String path, List<String[]> rows, String sheetName) {
//		System.out.println(Arrays.toString(rows.get(1)));
		File file = new File(path);
		file.delete();
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i != rows.get(0).length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(rows.get(0)[i]);
		}

		for (int i = 0; i != rows.size()-1; i++) {
			row = sheet.createRow(i + 1);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(i + 1);
			for (int j = 0; j != rows.get(i+1).length; j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(rows.get(i+1)[j]);
			}
		}
		
		for (int i = 0; i != rows.get(0).length; i++)
			sheet.autoSizeColumn(i);

		try (FileOutputStream outputStream = new FileOutputStream(path + ".xlsx")) {
			wb.write(outputStream);
			outputStream.close();
			wb.close();
		} catch (IOException e) {

		}
	}

////////////////////                   READING METHODS                   ////////////////////                     

	public List<String> getClassMethods(int col_index, String classname) { // Devolve todas as células na coluna do
																			// índice escolhido relativas à classe dada
		List<String> list = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null) {
				if (row.getCell(2).getStringCellValue().equals(classname))
					if (row.getCell(col_index) != null && !list.contains(row.getCell(col_index).toString()))
						list.add(row.getCell(col_index).toString());
			}
		}

		return list;
	}

	public List<String> getClasses() { // Devolve uma lista com todas as classes do Excel, sem repetições
		List<String> list = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null) {
				if (!list.contains(row.getCell(2).toString()) && r != 0)
					list.add(row.getCell(2).toString());
			}
		}

		return list;
	}

	public List<Object[]> getAllRows() { // Devolve uma lista em que cada array é uma linha inteira
																// do Excel - útil para a GUI
		int row_size = sheet.getRow(0).getPhysicalNumberOfCells();
		List<Object[]> list = new ArrayList<>();

		for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
			Object[] rowList = new Object[row_size]; 
			XSSFRow row = sheet.getRow(j);
			if (row != null) {
				int counter = 0;
				for (int i = 0; i <= row_size; i++) {
					if (counter < rowList.length) {
						rowList[counter] = row.getCell(i);
						counter++;
					}
				}
				list.add(rowList);
			}
		}
		return list;
	}

	public Object[] getExcelHeader() { // Devolve o cabeçalho de cada coluna do Excel - útil para a
															// GUI, i.e, "package","class","NOM_Class", etc
		int row_size = sheet.getRow(0).getPhysicalNumberOfCells();
		Object[] rowList = new Object[row_size];
		XSSFRow row = sheet.getRow(0);
		int counter = 0;
		for (int i = 0; i < row_size; i++) {
			if (counter < rowList.length) {
				rowList[counter] = row.getCell(i);
				counter++;
			}

		}
		return rowList;
	}

	public List<String> getAllCellsOfColumn(int column_index) { // Devolve todas as células dado o índice da coluna
		List<String> list = new ArrayList<>();
		for (String classname : getClasses()) {
			List<String> aux = getClassMethods(column_index, classname);
			for (String str : aux) {
				if (!list.contains(str) || column_index == 3)
					list.add(str);
			}
		}
		return list;
	}

	public int sumLinesOfCode() { // Função que retorna o total de linhas de código do projeto
		List<String> list = getAllCellsOfColumn(5);
		double total = 0;
		
		for (int i = 0; i < list.size(); i++) {
			total += Double.parseDouble(list.get(i));
		}

		return (int) total;
	}

	public String getExcel_file() {
		return excel_file;
	}
}