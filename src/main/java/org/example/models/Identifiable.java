package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable {
    void setDocumentId(String id);
    String getDocumentId();
}
