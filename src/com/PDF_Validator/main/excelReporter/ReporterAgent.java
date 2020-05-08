package com.PDF_Validator.main.excelReporter;

import com.PDF_Validator.main.validator.PdfComparison;

public class ReporterAgent {

	Appender appender = new Appender();

	public void getReport(String navFileLocation) {

		// Get The Generated List by CONDITION
		appender.appendStrToFile(navFileLocation, PdfComparison.createList);

	}
	
}
