package com.example.pasteboxrest.PastService;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.Exceptions.NotFindExpInSwitchException;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteBoxResponse;

import java.util.List;

public interface PastService {

    PasteBoxResponse addPaste(PasteBoxRequest pasteBoxRequest);
    PasteBox getByHash(String hash) throws NotFindExpInSwitchException, BoxNotExist, IncorrectHash;
   List<PasteBox> getTenPublicPast();

}
