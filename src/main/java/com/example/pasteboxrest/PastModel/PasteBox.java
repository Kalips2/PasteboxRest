package com.example.pasteboxrest.PastModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasteBox {
    private String title;
    private String body;
}
