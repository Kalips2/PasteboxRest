package com.example.pasteboxrest.converters;

import com.example.pasteboxrest.PastModel.PasteEnum.Exposure;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class StringToExposureConverter implements Converter<String, Exposure> {
    @Override
    public Exposure convert(String source) {
        return Exposure.valueOf(source.trim().toUpperCase(Locale.ROOT));
    }
}
