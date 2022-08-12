package com.example.pasteboxrest.Repository;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.Exceptions.NotFindExpInSwitchException;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteEnum.Expiration;
import com.example.pasteboxrest.PastModel.PasteboxEntity;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.List;

public interface PastRepository {
    String addPaste(PasteBoxRequest pasteBoxRequest);

    PasteBox getByHash(String hash) throws NotFindExpInSwitchException, BoxNotExist, IncorrectHash, IncorrectHash;

    List<PasteBox> getTenPublicPast();

    int getMaxId();

    boolean isAlive(Expiration expiration, Timestamp timestamp) throws NotFindExpInSwitchException;

    void deleteByHash(PasteboxEntity instance);
}
