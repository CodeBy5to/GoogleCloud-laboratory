package org.example;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.example.models.Photo;
import org.example.services.PhotoService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


public class Main {




    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String PHOTO_FILE_PATH = "/photo.png";




    public static String getMimeType(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.probeContentType(path);
    }


    public static void uploadFile(Drive driveService, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        File fileMetadata = new File();
        fileMetadata.setName(path.getFileName().toString());

        var file = new java.io.File(filePath);
        var mimeType = getMimeType(filePath);
        var mediaContent = new FileContent(mimeType, file);

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webContentLink, webViewLink")
                .execute();

        System.out.println("File ID: " + uploadedFile.getId());
        System.out.println("Web Content Link: " + uploadedFile.getWebContentLink());
        System.out.println("Web View Link: " + uploadedFile.getWebViewLink());
    }



    private static List<File> listFiles(Drive driveSerice) throws IOException {
        // Print the names and IDs for up to 10 files.
        FileList result = driveSerice.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
        return files;
    }



    public static void downloadFile(Drive driveService, String fileId, String destinationPath) throws IOException {
        OutputStream outputStream = new FileOutputStream(destinationPath);
        driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        outputStream.close();
    }




    public static void main(String... args) throws IOException, GeneralSecurityException {



        PhotoService photoService = PhotoService.getInstance();


/*
        var photoSave = new Photo(null,"PRUEBA", "idNoexiste", "prueba");
        photoService.save(photoSave);
*/


/*        var photoDelete = new Photo("44sFYuMiOeXoR2xZ58","PRUEBA REALIZADA", "idNoexiste", "prueba");
        photoService.delete(photoDelete);*/


        var photos = photoService.getAll();


        var photoById = photoService.findById("NUP97iEhjXbe4ciBSzl");



        photos.forEach(photo -> {
            System.out.println(photo.toString());
        });




/*        About about = service.about().get().setFields("storageQuota").execute();
        double totalSpace = about.getStorageQuota().getLimit() / (1024.0 * 1024.0 * 1024.0);
        double usedSpace = about.getStorageQuota().getUsage();
        System.out.println("Total space: " + totalSpace + "GB");
        System.out.println("Used space: " + usedSpace + " Bytes");*/

        //uploadFile(service, filePath);
        //var files = listFiles(service);



        //downloadFile(service,fileId, destinationPath);

    }
}