package com.example.pasteboxrest.Repository;

import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteboxEntity;

import java.util.List;

public interface PastRepository {
    String addPaste(PasteBoxRequest pasteBoxRequest);

    PasteBox getByHash(String hash);

    List<PasteBox> getTenPublicPast();

    int getMaxId();
}
