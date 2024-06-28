package com.model.aldasa.util;

import java.awt.Color;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UtilXls {
	public static CellStyle styleCell(XSSFWorkbook libro, char tipo) {
		CellStyle cellStyleDefault = libro.createCellStyle();
		Font cellFont = libro.createFont();
		
		XSSFFont fontRow = libro.createFont();
		fontRow.setFontHeightInPoints((short) 11);
		fontRow.setFontName("Calibri");
		if (tipo == 'B') {// CANTIDAD NORMALES {
			cellStyleDefault.setBorderBottom(BorderStyle.THIN);
			cellStyleDefault.setBorderTop(BorderStyle.THIN);
			cellStyleDefault.setBorderRight(BorderStyle.THIN);
			cellStyleDefault.setBorderLeft(BorderStyle.THIN);
			cellStyleDefault.setWrapText(true);
			cellStyleDefault.setVerticalAlignment(VerticalAlignment.TOP); 
		}

		if (tipo == 'A') {
			fontRow.setFontHeightInPoints((short) 11);
			cellStyleDefault.setAlignment(HorizontalAlignment.CENTER);
			cellStyleDefault.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyleDefault.setWrapText(true);
			
			fontRow.setBold(true);

			cellStyleDefault.setBorderBottom(BorderStyle.THIN);
			cellStyleDefault.setBorderTop(BorderStyle.THIN);
			cellStyleDefault.setBorderRight(BorderStyle.THIN);
			cellStyleDefault.setBorderLeft(BorderStyle.THIN);
		}

	

		
		cellStyleDefault.setFont(fontRow);

		return cellStyleDefault;
	}
}
