package com.PDF_Validator.main.validator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.PDF_Validator.main.resourcePath.ResourcePaths;
import com.taguru.utility.PDFUtil;

import de.redsix.pdfcompare.PdfComparator;


public class PdfComparison {

	public static List<String> createList = new ArrayList<>();

	public static void PDFComparater(String srcFile, String tgtFile, String Filename, String destinationResultPath) {

		PDFUtil pdfUtil = new PDFUtil();
		try {

			// Get Result Folder Organised


			// Get PDFs
			File Src_pdfFile = new File(srcFile);
			File tgt_pdfFile1 = new File(tgtFile);

			// Load PDFs
			PDDocument pdDocument_SRC = PDDocument.load(Src_pdfFile);
			PDDocument pdDocument_TGT = PDDocument.load(tgt_pdfFile1);

			// Get the number of pages
			List allPages_SRC = pdDocument_SRC.getDocumentCatalog().getAllPages();
			List allPages_TGT = pdDocument_TGT.getDocumentCatalog().getAllPages();

			int i =1; int j =1;
			boolean validateAlignment = false;

			if (allPages_SRC.size() != allPages_TGT.size()) {

				// Until the same amount of lines
				for (i = 1; i <= Math.min(allPages_SRC.size(), allPages_TGT.size()); i++) {
					PDFTextStripper stripper = new PDFTextStripper();
					stripper.setStartPage(i);
					stripper.setEndPage(i);
					String text_SRC = stripper.getText(pdDocument_SRC).replaceAll("javaWhitespace", " ");
					String text_TGT = stripper.getText(pdDocument_TGT).replaceAll("javaWhitespace", " ");

					String[] lines_SRC = text_SRC.split("\n");
					String[] lines_TGT = text_TGT.split("\n");
					int passCreator=1;
					String createPassString;
					String createFailString;

					for (j = 0; j < Math.min(lines_SRC.length, lines_TGT.length) ; j++) {
						String src =  lines_SRC[j].replace("\n", "*");
						String tgt = lines_TGT[j].replace("\n", "*");
						if (src.equals(tgt)) {
							passCreator++;
						} else {
							createFailString = Filename+"*"+Filename+"*"+src.trim()+"*"+tgt.trim()+"*"+"FAIL"+"*"+"Mismatch in line number - " + (j+1) + "*"+ i + "\n";
							createList.add(createFailString);
						}
					}

//					if (passCreator > 1) {
//						createPassString = Filename+"*"+"Source File is same as Target"+"*"+"Target file is same as Source"+"*"+"PASS"+"*"+"Matched "+ passCreator + " lines";
//						//					createList.add(createPassString);
//					}
				}


				// When SOURCE has more lines
				if(allPages_SRC.size() > allPages_TGT.size()) {
					for (int x = i; x <= allPages_SRC.size(); x++) {
						PDFTextStripper stripper = new PDFTextStripper();
						stripper.setStartPage(x);
						stripper.setEndPage(x);
						String text_SRC = stripper.getText(pdDocument_SRC).replaceAll("javaWhitespace", " ");

						String[] lines_SRC = text_SRC.split("\n");

						for (int y = 0; y < lines_SRC.length; y++) {
							String src = lines_SRC[y];
							String createIssueString = "Source - " + src.trim() + " Target -" + " No value on TARGET file " + "This line exists only on SOURCE File \n";
							String createFailWhenNoTARGET = Filename+"*" + src + "*"+ "No line is found on Target" + "*FAIL*"+createIssueString+"*"+x+"\n";
							createList.add(createFailWhenNoTARGET);
						}
					}
				} else {
					// When TARGET has more lines
					for (int x = i; x <= allPages_TGT.size(); x++) {
						PDFTextStripper stripper = new PDFTextStripper();
						stripper.setStartPage(x);
						stripper.setEndPage(x);

						String text_TGT = stripper.getText(pdDocument_TGT).replaceAll("visiblespace", " ");

						String[] lines_TGT = text_TGT.split("\n");
						for ( int y = 0; y < lines_TGT.length; y++) {
							String tgt = lines_TGT[y];
							String createIssueString = "Source - " + "No value on SOURCE file " + "Target - " + tgt.trim() + " This line exists only on TARGET File \n";
							String createFailWhenNoSOURCE = Filename+"*" + "No line is found on Source" + "*"+ tgt  + "*FAIL*"+createIssueString+"*"+x+"\n";
							createList.add(createFailWhenNoSOURCE);
						}
					}
				}
			} else {
				int failCounter=0;
				// Until the same amount of lines
				for (i = 1; i <= Math.min(allPages_SRC.size(), allPages_TGT.size()); i++) {
					PDFTextStripper stripper = new PDFTextStripper();
					stripper.setStartPage(i);
					stripper.setEndPage(i);
					String text_SRC = stripper.getText(pdDocument_SRC).replaceAll("javaWhitespace", " ");
					String text_TGT = stripper.getText(pdDocument_TGT).replaceAll("javaWhitespace", " ");

					String[] lines_SRC = text_SRC.split("\n");
					String[] lines_TGT = text_TGT.split("\n");
					int passCreator=1;
					String createPassString;
					String createFailString;

					for (j = 0; j < Math.min(lines_SRC.length, lines_TGT.length) ; j++) {
						String src =  lines_SRC[j].replace("\n", "*");
						String tgt = lines_TGT[j].replace("\n", "*");
						if (src.equals(tgt)) {
							passCreator++;
						} else {
							createFailString = Filename+"*"+Filename+"*"+src.trim()+"*"+tgt.trim()+"*"+"FAIL"+"*"+"Mismatch in line number - " + (j+1) + "*" + i +"\n";
							createList.add(createFailString);
							failCounter++;
						}
					}
				}
				
				if (failCounter == 0) {
					validateAlignment = true;
				}
			}

			// when there is no line or word mismatch, it will validate alignments
			if (validateAlignment) {

				String fileSRC=srcFile;
				String fileTGT=tgtFile;
				String timeStamps = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());

				// Path to put Reports
				String diffectPDF = destinationResultPath;

				Color colorCode = new Color(255, 102, 102); 
				File inputFile1 = new File(fileSRC);
				File inputFile2 = new File(fileTGT);
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				String outputDirLocation=diffectPDF;
				String filename=inputFile1.getName();
				int n=filename.lastIndexOf("\\");
				String s,s1;
				s = filename.substring(n + 1);
				int x = s.lastIndexOf('.');
				s1 = s.substring(0,x);
				String path = outputDirLocation;
				pdfUtil.highlightPdfDifference(true);
				pdfUtil.highlightPdfDifference(colorCode);
				pdfUtil.setImageDestinationPath(path);

				try {
					boolean ifSame = pdfUtil.comparePdfFilesBinaryMode(fileSRC, fileTGT);
					File f = new File(path +"\\"+s1+"_1_diff.png");
					String fileName1=path +"\\PDFFileCompare_" + inputFile1.getName() + "VS" + inputFile2.getName() + timeStamp + ".png";
					f.renameTo(new File(path +"\\PDFFileCompare_" + inputFile1.getName() + "_VS_" + inputFile2.getName() + timeStamp + ".png"));
					File file = new File(fileName1);
					boolean exists = file.exists();
					String Status="";String Comment="";
					if (!ifSame) {
						Status="FAIL";
						Comment="Check the file: "+fileName1+ " for detailed issue/issues.";
						String load=Filename+"*"+Filename +"*"+"Allignment Mismatch on Source"+"*"+"Alignment Mismatch on Target"+"*"+"FAIL"+"*"+ Comment +"*Alignment Problem Detected"+System.lineSeparator();
						createList.add(load);
					}
					
						
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					e.getMessage();
				}



			}



			pdDocument_SRC.close();
			pdDocument_TGT.close();


		} catch(Exception e){
			System.out.print(e);
		}

	}

}
