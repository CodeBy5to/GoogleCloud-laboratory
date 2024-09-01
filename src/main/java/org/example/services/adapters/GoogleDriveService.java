package org.example.services.adapters;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import org.example.services.config.credentials.CredentialManagerConfig;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleDriveService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final Drive service;


    public GoogleDriveService() throws GeneralSecurityException, IOException {
        this.service = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, null)
                .setHttpRequestInitializer(
                        new HttpCredentialsAdapter(CredentialManagerConfig.getInstance().getCredentials())
                )
                .build();
    }



}
