package itstep.learning.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class LocalStorageService {
    private final static String storagePath = "D:/storage";
    private final RandomFileNameService fileNameService;
    private final int bufferSize = 1024;

    @Inject
    public LocalStorageService(RandomFileNameService fileNameService) {
        this.fileNameService = fileNameService;
    }

    public File getFile(String fileName) {
        return new File(storagePath, fileName);
    }

    public String saveFile(FileItem fileItem) throws IOException {
        String extension = getString(fileItem);

        String savedName;
        File file;
        do {
            savedName = fileNameService.generateFilename(24) + extension;
            file = new File(storagePath, savedName);
            String absolutePath = file.getAbsolutePath();
            System.out.println("File path: " + absolutePath);
        } while (file.exists());

        long size = fileItem.getSize();
        if (size > bufferSize) {
            size = bufferSize;
        }
        byte[] buffer = new byte[(int) size];
        int len;
        try (FileOutputStream fos = new FileOutputStream(file)) {
            InputStream in = fileItem.getInputStream();
            while ((len = in.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }

        return savedName;
    }

    private static String getString(FileItem fileItem) throws IOException {
        if (fileItem == null) {
            throw new IOException("File is null");
        }
        if (fileItem.getSize() == 0) {
            throw new IOException("File is empty");
        }

        String fileName = fileItem.getName();
        if (fileName == null) {
            throw new IOException("File name is null");
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IOException("File name contains no extension");
        }
        String extension = fileName.substring(dotIndex);
        if (".".equals(extension)) {
            throw new IOException("File extension is empty");
        }
        return extension;
    }
}
