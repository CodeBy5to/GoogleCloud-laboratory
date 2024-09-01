package org.example.services.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.example.services.config.credentials.CredentialManagerConfig;

public class FirestoreDbConfig {

    private static FirestoreDbConfig instance;
    private final Firestore db;

    public FirestoreDbConfig() {
        this.db = FirestoreOptions.newBuilder()
                .setCredentials(CredentialManagerConfig.getInstance().getCredentials())
                //.setDatabaseId() Colocar si no se usa la bd default del proyecto
                .build().getService();
    }

    public static synchronized FirestoreDbConfig getInstance() {
        if (instance == null) {
            instance = new FirestoreDbConfig();
        }
        return instance;
    }

    public Firestore getDb() {
        return this.db;
    }
}
