package com.PDF_Validator.main.excelReporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.PDF_Validator.main.resourcePath.ResourcePaths;

public class ListFileUtils {

	static ReportGenerationHelperClass reportGenerationHelperObj = new ReportGenerationHelperClass();

	public static void GenerateValueFromExcel(String nav, String timeStamp) throws IOException, InterruptedException {

		File f1 = new File(nav);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet XSSFSheetSummary = reportGenerationHelperObj.createReportSheet("Error Status", workbook);
		createSummaryReport(XSSFSheetSummary, workbook);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(f1));
		String line = null;
		int line_count=1;
		String[] temp;
		//delimiter
		String delimiter = "\\*";
		while ((line = bufferedReader.readLine()) != null) {
			temp = line.split(delimiter);
			writeDataIntoCellsMismatch(line_count, XSSFSheetSummary, workbook, temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]);
			line_count++;
		}
		bufferedReader.close();
		String path1= ResourcePaths.resultPath;
		
		reportGenerationHelperObj.writeWorkbook(path1 + "\\"+timeStamp+"_Outputs\\" + "FailLog.xlsx", workbook);
		workbook.close();


		f1.delete();
	}

	private static void createSummaryReport(XSSFSheet XSSFSheetMismatch, XSSFWorkbook workbook) {
		String[] columnNames = {"Source Name", "Target Name", "Line on Source File","Line on Target File","Status","Error on Line","Page No."};
		reportGenerationHelperObj.createRowInExcel(0, columnNames, XSSFSheetMismatch, workbook, reportGenerationHelperObj.createCellStyle(workbook));
	}
	static int rownum = 1;

	private static void writeDataIntoCellsMismatch(int row, XSSFSheet XSSFSheet, XSSFWorkbook workbook, String srcFile, String tgtFile, String srcContent,String tgtContent,String sts,String issue, String pageNo) {
		String[] rowData = {srcFile, tgtFile, srcContent,tgtContent,sts,issue, pageNo};
		reportGenerationHelperObj.createRowInExcel(row, rowData, XSSFSheet, workbook, reportGenerationHelperObj.createCellStyle1(workbook));
	}
}
