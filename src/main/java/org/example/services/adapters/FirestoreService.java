package org.example.services.adapters;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Identifiable;
import org.example.services.config.FirestoreDbConfig;
import org.example.services.util.common.MapperUtil;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class FirestoreService<T extends Identifiable> {
    private final Class<T> clazz;
    protected final CollectionReference collection;

    @SuppressWarnings("unchecked")
    public FirestoreService() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        var db = FirestoreDbConfig.getInstance().getDb();
        this.collection = db.collection(clazz.getSimpleName());
    }

    public List<T> getAll() {
        var response = new ArrayList<T>();
        try {
            ApiFuture<QuerySnapshot> future = collection.get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                T object = document.toObject(clazz);
                object.setDocumentId(document.getId());
                response.add(object);
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error in get {} collection", clazz.getSimpleName());
        }
        return response;
    }

    public void save(T object) {
        DocumentReference docRef = collection.document();
        if(object.getDocumentId() != null) docRef = collection.document(object.getDocumentId());

        var documentRequest = MapperUtil.convertToMap(object);

        try {
            ApiFuture<WriteResult> future = docRef.set(documentRequest);
            log.info("Save {} at {}", clazz.getSimpleName(), future.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error on save in {} collection", clazz.getSimpleName());
        }
    }

    public void delete(T object) {
        var docRef = collection.document(object.getDocumentId());
        try {
            ApiFuture<WriteResult> future = docRef.delete();
            log.info("deleted {} with id {} at {}", clazz.getSimpleName(), object.getDocumentId(),
                    future.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error on delete in {} collection", clazz.getSimpleName());
        }
    }

    public T findById(String id) {
        DocumentReference docRef = collection.document(id);
        try {
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return document.toObject(clazz);
            } else {
                log.info("No such {}!", clazz.getSimpleName());
                return null;
            }
        } catch (Exception e) {
            log.error("Error retrieving {}: {}", clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }


}
