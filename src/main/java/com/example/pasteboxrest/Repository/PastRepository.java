package com.example.pasteboxrest.Repository;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteboxEntity;

import java.util.List;

public interface PastRepository {
    String addPaste(PasteBoxRequest pasteBoxRequest);

    PasteBox getByHash(String hash) throws BoxNotExist, IncorrectHash, IncorrectHash;

    List<PasteBox> getAllPublicPaste();

    int getMaxId();

    boolean isAlive(PasteboxEntity instance);

    void deleteByInstance(PasteboxEntity instance);
}
