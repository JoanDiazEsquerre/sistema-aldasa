package com.model.aldasa.descarga;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/downloadServlet")
public class DownloadServlet extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        String folderPath = "C:\\ARCHIVO-CONTRATO\\"; // Ruta de la carpeta donde se encuentra el archivo
        
        File file = new File(folderPath + fileName);
        
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.getWriter().write("El archivo no existe");
        }
    }

}
