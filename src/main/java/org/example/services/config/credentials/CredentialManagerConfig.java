package org.example.services.config.credentials;

import org.example.Main;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CredentialManagerConfig {

    private static CredentialManagerConfig instance;
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private final GoogleCredentials credentials;

    private static final ArrayList<String> scopes = new ArrayList<>();


    public CredentialManagerConfig() {

        setScopes();

        var credentialPath = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if(credentialPath == null) throw new RuntimeException("credentials json file not found");

        try {
            this.credentials = GoogleCredentials.fromStream(credentialPath)
                    .createScoped(scopes);
        } catch (IOException e) {
            throw new RuntimeException("Generated GoogleCredentials Error");
        }
    }

    private void setScopes() {
        Arrays.stream(Scopes.values()).toList().forEach(scope -> {
            if(scope.getSingleScope() == null) {
                scopes.addAll(scope.getAllScopes());
            } else {
                scopes.add(scope.singleScope);
            }
        });
    }

    public static synchronized CredentialManagerConfig getInstance() {
        if (instance == null) {
            instance = new CredentialManagerConfig();
        }
        return instance;
    }

    public GoogleCredentials getCredentials() {
        return credentials;
    }

}
