package com.example.pasteboxrest.PastController;

import com.example.pasteboxrest.Exceptions.BoxNotExist;
import com.example.pasteboxrest.Exceptions.IncorrectHash;
import com.example.pasteboxrest.Exceptions.NotFindExpInSwitchException;
import com.example.pasteboxrest.PastModel.PasteBox;
import com.example.pasteboxrest.PastModel.PasteBoxRequest;
import com.example.pasteboxrest.PastModel.PasteBoxResponse;
import com.example.pasteboxrest.PastService.PastServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PastController {

    PastServiceImpl service;

    public PastController(PastServiceImpl service) {
        this.service = service;
    }

    @PutMapping("/add")
    public PasteBoxResponse addPaste(@RequestBody PasteBoxRequest pasteBoxRequest) {
        return service.addPaste(pasteBoxRequest);
    }

    @GetMapping("/{hash}")
    public PasteBox getByHash(@PathVariable String hash)throws NotFindExpInSwitchException, BoxNotExist, IncorrectHash {
        return service.getByHash(hash);
    }

    @GetMapping("/get")
    public List<PasteBox> getTenPublicPast() {
        return service.getTenPublicPast();
    }

}
