package com.example.pasteboxrest.PastModel;

import com.example.pasteboxrest.PastModel.PasteEnum.Exposure;
import com.example.pasteboxrest.PastModel.PasteEnum.Expiration;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasteBoxRequest {
    private String title;
    private String body;
    private Exposure exposure;
    private Expiration Expiration;
}
