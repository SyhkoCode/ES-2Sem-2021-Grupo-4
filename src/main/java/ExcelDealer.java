import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
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
			packet = OPCPackage.open(new File(excel_file));
			wb = new XSSFWorkbook(packet);
			sheet = wb.getSheetAt(0);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readFile() {

		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;

		System.out.println(getMethods(0, "ParsingException"));
		System.out.println(getClasses());

	}

	public List<String> getMethods(int col_index, String name) {
		List<String> list = new ArrayList<>();

		for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row != null) {
				if (row.getCell(2).getStringCellValue().equals(name))
					if (!list.contains(row.getCell(col_index).toString()))
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
				if (!list.contains(row.getCell(2).toString()) && r != 0)
					list.add(row.getCell(2).toString());
			}
		}
		return list;
	}

	public String getExcel_file() {
		return excel_file;
	}

	public void setExcel_file(String excel_file) {
		this.excel_file = excel_file;
	}

	public OPCPackage getPacket() {
		return packet;
	}

	public void setPacket(OPCPackage packet) {
		this.packet = packet;
	}

	public XSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(XSSFWorkbook wb) {
		this.wb = wb;
	}

	public static void main(String[] args) {
		ExcelDealer er = new ExcelDealer("Code_Smells.xlsx");
		er.readFile();  
	}

}
