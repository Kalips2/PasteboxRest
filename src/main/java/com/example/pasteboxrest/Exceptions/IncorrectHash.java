package com.example.pasteboxrest.Exceptions;

public class IncorrectHash extends Exception {
    public IncorrectHash(String incorrectHash) {
        super(incorrectHash);
    }
}
