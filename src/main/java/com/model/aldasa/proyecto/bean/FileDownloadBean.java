package com.model.aldasa.proyecto.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@ManagedBean
public class FileDownloadBean {

    public void downloadFile(String filePath) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        File file = new File(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {

            response.setContentType(Files.probeContentType(file.toPath()));
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.setContentLength((int) file.length());

            byte[] buffer = new byte[10024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            facesContext.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}