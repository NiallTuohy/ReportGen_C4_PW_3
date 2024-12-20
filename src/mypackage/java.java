package mypackage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.RootElement;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;

/*
************************************************************************************************************************************
This Java program has been written to convert reports generated on the PW machine in C4 in B2, Westport from csv to pdf. To assist 
this action I have used libraries provided by Itext PDF, 
see https://itextpdf.com/
These libraries are provided free to use in accordance with license: 
AGPLv3 Version 3, 19 November 2007 Copyright (C) 2007 Free Software Foundation, Inc. 

applicable entries in the license conditions are:
"The Program" refers to any copyrightable work licensed under this License.

This License explicitly affirms your unlimited permission to run the unmodified Program.

In other words,
As I have not modified any of the provided classes or Jar's provided by ItextPDF, which are referred to as "The Program".
I can use them here with unlimited permission. However if "The Program" was to be copied it must contain a copyright notice so
that the new user of the copied program is aware of the conditions of licensing. Which I have attached below:

Copyright (C) 
This program is free software: you can redistribute it and/or modify it under the terms of the 
GNU Affero General Public License as published by the Free Software Foundation, either version 3 of 
the License, or (at your option) any later version.This program is distributed in the hope that it will
be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
You should have received a copy of the GNU Affero General Public License along with this program. 
If not, see https://www.gnu.org/licenses/.

A full copy of this license will be provided in the application folders saved as AGPLv3 - GNU AFFERO GENERAL PUBLIC LICENSE _ iText PDF.pdf

As a pre-caution I am also making this program open source. 

Note that the java.java class has been written from scratch and only calls the unmodified itextPDF libraries. 
This program was written in December 2024. If you have an queries/issues or require additional programs you can reach out to me at:
nialltuohycv@gmail.com

Best Wishes,

Niall Tuohy

************************************************************************************************************************************
*/

public class java {

	public static int NotTitle=0,c=1,c1=1,c2=1,c3=1,c4=1,headingfound=0,foundfirst=0;;static LayoutArea currentArea;static int currentPageNumber;
	static int nextAreaNumber;
	
	public static void main(String[] args) throws IOException {
		
		String CSV = readCSV();
		String[] array = toArray(CSV);
		Document document = createPDF(array);
		FormatData(array,document);
		document.close();
	}
	

	private static Document createPDF(String[] array) throws IOException {	//create PDF and write array to PDF
		
		String dest = "hello_world.pdf";
		PdfWriter writer = new PdfWriter(dest);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		
		return document;
	}

	
	public static final Rectangle[] COLUMNS = {
            new Rectangle(36, 36, 1000, 1000),
            new Rectangle(305, 36, 254, 770)
	 };
	
	
private static void FormatData(String[] array, Document document) throws IOException {
		int End = 0, x=0;
	
		Paragraph[] Paragraphs = new Paragraph[2];
		String[] array2 = new String[25];
		Paragraph p = new Paragraph();
		Paragraph p2 = new Paragraph();
		String[] array3 = new String[25];
		String[] array4 = new String[25];
		String[] array9 = new String[25];
		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
			
		for(int pointer=0;pointer<array.length-1;pointer=pointer+End+1) {
				if(array[pointer]==null) {break;}
			
				//different lengths of array2 everytime as the csv has different numbers of semicolons on each line
				//using CRC code as a line end in the csv. End is the length of each line.
					for(x=pointer;x<array.length-1;x++) {
						if(array[x]==null) {break;}
						if(array[x].contains("E+20")||array[x].contains("CRC")) {
							End=x-pointer;
							break;
						}
					}
			
			if(array[pointer]== null) {break;}	//exit loop when array values are null	
			array2 = getRow(array,pointer,End);
			int a = checkrow(array2,"CYCLE:",End);
			array3 = checkParagraph1(array2,End);	//check for first column
			array9 = checkParagraph3(array3,End);	//check for headings with "----------"
			array4 = checkParagraph2(array2,End);	//check for second column
			Paragraphs = ArrayToParagraph(array2,a,End,array3,array4);
			p=Paragraphs[0];p2=Paragraphs[1];
			
			//set column 1 parameters
			p.setBold();
			p.setFont(font);
			p.setFontSize(12);
			p.setKeepWithNext(true);
			p.setFirstLineIndent(0);
			p.setCharacterSpacing(0);
			p.setPaddingTop(0);
			
			//set column 2 parameters
			p2.setFont(font);
			p2.setFontSize(12);
			p2.setKeepWithNext(true);
			p2.setCharacterSpacing(0);
			p2.setKeepTogether(true);
			
			// Creating a table with 2 columns 
			float [] pointColumnWidths = {200F, 200F}; //these are the cell widths
			Table table = new Table(pointColumnWidths); 	
			
			if(headingfound==0) {	//check for row with "--------"
			// Adding cell 1 to the table 
			Cell cell1 = new Cell();   	// Creating a cell 
			cell1.add(p);         		// Adding content to the cell 
			table.addCell(cell1);      	// Adding cell to the table       
			cell1.setBorder(null);		//removing grid lines from cell
			
			// Adding cell 2 to the table
			Cell cell2 = new Cell();      // Creating a cell 
			cell2.add(p2);      		  // Adding content to the cell 
			table.addCell(cell2);   	  // Adding cell to the table 
			cell2.setBorder(null);		  //removing grid lines from cell			
			}
			
			if(headingfound==1) { //if row has "--------"
				
				//create new paragraph for "-------" entries. we don't want them in the table
				Paragraph p3 = new Paragraph();
				p3.setFont(font);
				p3.setFontSize(16);
				p3.setKeepWithNext(true);
				p3.setFirstLineIndent(90);
				p3.setCharacterSpacing(0);
				p3.setPaddingTop(1);
				p3.setBold();
				
				for(int v=0;v<array9.length+1;v++) {
					if(array9[v]==null) {break;}
					p3.add(array9[v]);
				}
				document.add(p3);
				headingfound=0;			//reset bit after line has been added to paragraph 3
			}
						
			document.add(table);
		}		
	}


	private static String[] checkParagraph3(String[] array3, int End) {
		String[] array10 = new String[25];
	
		for(int x=0;x<End+1;x++) {
			
					if(array3[x]==null) {break;}
					if(foundfirst==0) {array10[x] = "";} //when no "-----" leave array blank
					
					//catch for second "-----"
					if(array3[x].contains("--------------------" )&& foundfirst==1) {
						array10[x] = array3[x];
						foundfirst=0;				
						headingfound=1;
						return array10;
					}
					
					//add everything after "----" to array
					if(foundfirst==1) {
					array10[x]=array3[x];
					headingfound=1;
					}

					//catch for first "-----"
					if(array3[x].contains("--------------------" )&& foundfirst==0) {
						array10[x] = array3[x];
						foundfirst=1;				
						headingfound=1;
						return array10;
					}
			
		}
		return array10;		
}


	private static Paragraph[] ArrayToParagraph(String[] array2, int a,int End, String[] array3, String[] array4) throws IOException {
		
			Paragraph[] Paragraphs = new Paragraph[2];
			Paragraph p = new Paragraph();
			Paragraph p2 = new Paragraph();
			
			if(a==1) { //title setting below
				NotTitle=1;
				
				for(int x=0;x<End;x++) {
						if(array3[x]==null) {break;}
					
						if(x>0&&array3[x].toString().length()>0) {
							p.add(array3[x]);						
						}
						else p.add("");
						
						if(array4[x]==null) {break;}
						if(x>0&&array4[x].toString().length()>0) {
							p2.add(array4[x]);						
						}
						else p2.add("");
				}
			}
			
			else if(NotTitle ==1 ) {
				
				for(int x=0;x<End;x++) {
						if(array3[x]==null) {break;}
						
						if(x>0&&array3[x].toString().length()>0) {
							p.add(array3[x]);						
						}
						else p.add("");
							
						if(array4[x]==null) {break;}
						if(x>0&&array4[x].toString().length()>0) {
							p2.add(array4[x]);						
						}
						else p2.add("");
				}
			}	
			Paragraphs[0] = p;
			Paragraphs[1] = p2;
			return Paragraphs;
}
	
	
	private static String[] checkParagraph2(String[] array2, int End) {
		
		String[] array5 = new String[25];
		
		for(int x=0;x<End+1;x++) {
				if(array2[x]==null) {break;}
	
				if(array2[x].contains("e:") && array5[x]==null) {
					array5[x]=array2[x].substring(array2[x].indexOf("e:")+2, array2[x].length());
					
						if(array5[x].equals("")) {
							array5[x]=array2[x+1];
							return array5;		//added return to prevent overwriting value on next x iteration
						}
				}
				
				if(array2[x].contains("E:") && array5[x]==null ) {
					array5[x]=array2[x].substring(array2[x].indexOf("E:")+2, array2[x].length());
					
						if(array5[x].equals("") || array5[x].equals(" ")) {
							array5[x]=array2[x+1];
							return array5;		//once we have something in array 5 we should return
						}
				}
				
				if(array2[x].contains(":") && array5[x]==null ) {
					array5[x]=array2[x].substring(array2[x].indexOf(":")+1, array2[x].length());
					
						if(array5[x].equals("")) {
							array5[x]=array2[x+1];
							return array5; 
						}
				}
					
				if(array5[x]==null ) {
				 array5[x]="";
				}				
			 }
			return array5;
	}


	private static String[] checkParagraph1(String[] array2, int End) {
		
		String[] array6 = new String[25];
		
			for(int x=0;x<End+1;x++) {
					if(array2[x]==null) {break;}
					if(array2[x].contains(":")) {
							if(array2[x].contains("time:")) {
									array6[x]=array2[x].substring(0, array2[x].indexOf("time:")+5);
									return array6;
							}
							else		array6[x]=array2[x].substring(0, array2[x].indexOf(":")+1);
					return array6;
					}
					else array6[x]=array2[x];
			}
			return array6;
		}
		

	private static int checkrow(String[] array2, String checkfor,int End) {
		
		int a=0;
		
		for(int x=0;x<End+1;x++) {
				if(array2[x]== null) {return a;}	//exit loop when array values are null	
				if(array2[x].contains(checkfor) == true) {
					a=1;
				}
		}
	return a;
}


	private static String[] getRow(String[] array, int pointer,int End) {
		
		String[] array2 = new String[25]; 		
		
		for(int x=0;x<End+1;x++) {
			array2[x] = array[pointer];
			pointer++;
		}
		RemoveCheckSums(array2,End);
		return array2;
}


	private static String[] RemoveCheckSums(String[] array2,int End) {
		//the csv file has checksums in it, basically long gibberish numbers that no one wants.
			for(int x=0;x<End+1;x++) {
					if(array2[x]==null) {break;}
					if(array2[x].contains("E+20")||array2[x].contains("CRC")) {
						array2[x]="";
					}
			}
		return array2;
	}


	private static String[] toArray(String string) {		//Format the csv into a report style layout
		
		int start=0,prevstart = 0;
		String[] array = new String[10000];
		//first iteration needs to be outside loop to get index right
		prevstart = start;
		start = string.indexOf(";",prevstart+1);
		String word1 = string.substring(prevstart, start);
		array[0]=word1;
		
				for(int a=0;a<100000;a++) {
				
					prevstart = start;
					start = string.indexOf(";",prevstart+1);
					if(start<0) {break;}
					word1 = string.substring(prevstart+1, start);
					array[a+1]=word1;		
				}
		return array;
	}


	public static String readCSV() throws FileNotFoundException {	//read in the csv generated report file
		
		String outString;
		Scanner in = new Scanner(new FileReader("C:\\ReportGen_C4_PW\\Cycle_Report_2024-10-30_08-15-20.csv"));
		StringBuilder sb = new StringBuilder();
		
		while(in.hasNext()) {
		    sb.append(in.nextLine());
		}
		in.close();
		outString = sb.toString();
		return outString;
	}
}