import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by vinit on 5/22/15.
 */
public class ReadExcelData {
	private static final String FILE_PATH = "SampleData1.xls";

	public List getListFromExcel() throws Exception {
		List<ClubCF> clubCFList = new ArrayList<ClubCF>();
		try {
			FileInputStream fis = new FileInputStream(FILE_PATH);

			HSSFWorkbook workbook = new HSSFWorkbook(fis);

			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				Iterator<HSSFRow> rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					ClubCF clubCF = new ClubCF();
					String name = "";
					String shortCode = "";

					// Get the row object
					HSSFRow row = rowIterator.next();

					// Every row has columns, get the column iterator and
					// iterate over them
					Iterator<HSSFCell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {
						// Get the Cell object
						HSSFCell cell = cellIterator.next();
						if (cell.getCellNum() == 0) {
							clubCF.setNo(cell.getStringCellValue());
						} else if (cell.getCellNum() == 1) {
							clubCF.setName(cell.getStringCellValue());
						} else if (cell.getCellNum() == 2) {
							clubCF.setApis(cell.getStringCellValue());
						} else if (cell.getCellNum() == 3) {
							clubCF.setTags(cell.getStringCellValue());
						}
					}
					clubCFList.add(clubCF);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return clubCFList;
	}
}
