package org.example.services;

import org.example.models.Photo;
import org.example.services.adapters.FirestoreService;

public class PhotoService extends FirestoreService<Photo> {
    private static PhotoService instance;

    public PhotoService() {
        super();
    }

    public static synchronized PhotoService getInstance() {
        if (instance == null) {
            instance = new PhotoService();
        }
        return instance;
    }

}
