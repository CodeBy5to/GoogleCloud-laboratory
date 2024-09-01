package org.example.services.config.credentials;

import com.google.api.services.drive.DriveScopes;
import lombok.Getter;

import java.util.Set;

@Getter
public enum Scopes {

    DRIVE(null, DriveScopes.all()),
    FIRESTORE("https://www.googleapis.com/auth/datastore", null);


    final String singleScope;
    final Set<String> allScopes;

    Scopes(String singleScope, Set<String> allScopes){
        this.singleScope = singleScope;
        this.allScopes = allScopes;
    }

}
