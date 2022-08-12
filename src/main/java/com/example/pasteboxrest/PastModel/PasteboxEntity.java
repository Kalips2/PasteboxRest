package com.example.pasteboxrest.PastModel;

import com.example.pasteboxrest.PastModel.PasteEnum.Expiration;
import com.example.pasteboxrest.PastModel.PasteEnum.Exposure;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "pastebox")
public class PasteboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String body;
    @Column
    private String hash;
    @Column
    @Enumerated(EnumType.STRING)
    private Exposure exposure;
    @Column
    @Enumerated(EnumType.STRING)
    private Expiration expiration;
    @Column
    private Timestamp TimeCreated;

    public PasteboxEntity(String title, String body, String hash, Exposure exposure, Expiration expiration, Timestamp timeCreated) {
        this.title = title;
        this.body = body;
        this.hash = hash;
        this.exposure = exposure;
        this.expiration = expiration;
        TimeCreated = timeCreated;
    }
}
