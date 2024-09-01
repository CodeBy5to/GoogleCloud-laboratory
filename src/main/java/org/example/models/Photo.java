package org.example.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Photo implements Identifiable{

    private String id;
    private String name;
    private String driveId;
    private String description;

    @Override
    public void setDocumentId(String id) {
        this.id = id;
    }

    @Override
    public String getDocumentId() {
        return this.id;
    }
}
