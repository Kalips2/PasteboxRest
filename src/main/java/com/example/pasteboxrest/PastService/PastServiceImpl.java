package com.example.pasteboxrest.PastService;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteBoxResponse;
import com.example.pasteboxrest.Repository.PastRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PastServiceImpl implements PastService{
    private PastRepositoryImpl repository;
    @Value("${http.value}")
    private String host;

    public PastServiceImpl(PastRepositoryImpl repository) {
        this.repository = repository;
    }

    public PasteBoxResponse addPaste(PasteBoxRequest pasteBoxRequest) {
        return new PasteBoxResponse(host + repository.addPaste(pasteBoxRequest));

    }

    @Override
    public PasteBox getByHash(String hash) throws BoxNotExist, IncorrectHash {
        return repository.getByHash(hash) ;
    }

    @Override
    public List<PasteBox> getAllPublicPaste() {
        return repository.getAllPublicPaste();
    }
}
