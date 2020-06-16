package Automation;

import java.awt.Color;
import java.io.IOException;

import com.taguru.utility.PDFUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
//import static logincontroller.CompareCSV.compareFiles;

public class PdfTPdf {
	static String outputDirLocation;
	static JFrame frame;
	//ReportGenerationHelperClass reportGenerationHelperObj = new ReportGenerationHelperClass();
	public static void appendStrToFile(String fileName,String str)
	{
		try {

			// Open given file in append mode.
			BufferedWriter out = new BufferedWriter(
					new FileWriter(fileName, true));
			out.write(str);
			out.close();
		}
		catch (IOException e) {
			System.out.println("Exception occoured:" + e);
		}
	}

	public static void compare(String srcfile,String trgfile,String output,String Temp_file1,String Folder_name) {

		int src_count=0;
		int trg_count=0;
		String file1=srcfile;
		String file2=trgfile;

		// System.out.println("File 1 : " + file1);
		// System.out.println("File 2 : " + file2);
		PDFUtil pdfUtil = new PDFUtil();
		try {
//			System.out.println("Get Source File Page Count : " + pdfUtil.getPageCount(file1));
//			System.out.println("PDF TO PDF");
			src_count=pdfUtil.getPageCount(file1);
		} catch (IOException e) {

			e.printStackTrace();
		} 
		try {
			// System.out.println("Get Target File Page Count : " + pdfUtil.getPageCount(file2));
			trg_count=pdfUtil.getPageCount(file2);
		} catch (IOException e) {

			e.printStackTrace();
		} 
		if(src_count!=trg_count){
			if(src_count>trg_count){
				String str2=Folder_name +","+file1+","+file2+",FAIL,"+"Source count is not matching with target count where Source count="+src_count+" and Target Count="+trg_count+". Source count is greater than Target count."+System.lineSeparator();
				appendStrToFile(Temp_file1,str2);
				//System.out.println("here 1");
			}
			if(src_count<trg_count){
				String str2=Folder_name +","+file1+","+file2+",FAIL,"+"Source count is not matching with target count where Source count="+src_count+" and Target Count="+trg_count+". Target count is greater than Source count."+System.lineSeparator();
				appendStrToFile(Temp_file1,str2);
				//   System.out.println("here 2");
			}
		}else{
			Color colorCode = new Color(255, 102, 102); 
			File inputFile1 = new File(srcfile);
			File inputFile2 = new File(trgfile);
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			outputDirLocation=output;
			//System.out.println("output path:" +outputDirLocation);
			//System.out.println("Source File name:" +srcfile);
			String filename=srcfile;
			int n=filename.lastIndexOf("\\");
			String s,s1;
			s = filename.substring(n + 1);
			int i = s.lastIndexOf('.');
			s1 = s.substring(0,i);

			//System.out.println("index value:" +n+ "  filenme:"+s+ "only filename:"+s1);
			String path = outputDirLocation;
			// System.out.println("path value:" + path);
			// String fileName = path + "\\CSVFileCompare_" + inputFile1.getName() + "VS" + inputFile2.getName() + timeStamp + ".xlsx";
			//if you need to store the result
			pdfUtil.highlightPdfDifference(true);
			pdfUtil.highlightPdfDifference(colorCode);
			pdfUtil.setImageDestinationPath(path);

			try {
				pdfUtil.comparePdfFilesBinaryMode(file1, file2);
				File f = new File(path +"\\"+s1+"_1_diff.png");
				String fileName1=path +"\\PDFFileCompare_" + inputFile1.getName() + "VS" + inputFile2.getName() + timeStamp + ".png";
				f.renameTo(new File(path +"\\PDFFileCompare_" + inputFile1.getName() + "VS" + inputFile2.getName() + timeStamp + ".png"));
				File file = new File(fileName1);
				boolean exists = file.exists();
				String Status="";String Comment="";
				if (file.exists() && file.isFile())
				{
					Status="FAIL";
					Comment="Check the file: "+fileName1+ " for detailed issue/issues.";
				}else
				{
					Status="PASS";
					Comment="No Issues."; 
				}
				String load=Folder_name +","+file1+","+file2+","+Status+","+Comment +System.lineSeparator();
				appendStrToFile(Temp_file1,load);
				//JOptionPane.showMessageDialog(frame, "Execution Completed Please Check The Report Directory");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
