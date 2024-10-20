package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.expections.HttpException;
import itstep.learning.services.LocalStorageService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Singleton
public class StorageServlet extends RestServlet {
    private final LocalStorageService storageService;

    @Inject
    public StorageServlet(LocalStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "File not found");
        }
        String filePath = pathInfo.substring(1);
        File file = this.storageService.getFile(filePath);
        if(file == null) {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "File at path " + filePath + " not found");
        }

        resp.setContentType(ResolveContentType(file.getName()));
        long size= file.length();
        if(size > 4096) {
            size = 4096;
        }
        byte[] buffer = new byte[(int)size];
        int len;
        try (FileInputStream fix = new FileInputStream(file); OutputStream out = resp.getOutputStream();) {
            while ((len = fix.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }

    private String ResolveContentType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        String extension = dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
        switch (extension) {
            case "jpg": extension = "jpeg"; break;
            case "jpeg":
            case "png":
            case "bmp":
            case "gif":
                return "image/" + extension;
        }
        return extension;
    }
}
