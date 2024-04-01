package com.libreforge.integration.dictionaries.api;

import com.libreforge.integration.dictionaries.api.dto.DictionaryDTO;
import com.libreforge.integration.dictionaries.api.dto.DictionaryItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
public class DictionaryController {

    private static final DictionaryDTO DICTIONARY_YES_NO_OPTIONS =
        new DictionaryDTO("yes_no_options", "Yes/No", Arrays.asList(
            new DictionaryItemDTO("yes", "Yes"),
            new DictionaryItemDTO("no", "No")
        ));

    private static final DictionaryDTO DICTIONARY_GENDER =
            new DictionaryDTO("gender", "Gender", Arrays.asList(
                    new DictionaryItemDTO("male", "Male"),
                    new DictionaryItemDTO("female", "Female"),
                    new DictionaryItemDTO("non_binary", "Non Binary")
            ));

    private static final DictionaryDTO DICTIONARY_VEHICLE_MAKE =
        new DictionaryDTO("vehicle_make", "Vehicle Make", Arrays.asList(
            new DictionaryItemDTO("acura", "Acura"),
            new DictionaryItemDTO("audi", "Audi"),
            new DictionaryItemDTO("bmw", "BMW")
        ));

    private static final DictionaryDTO DICTIONARY_VEHICLE_MODEL =
        new DictionaryDTO("vehicle_model", "Vehicle Model", Arrays.asList(

            new DictionaryItemDTO("adx", "ADX", "acura"),
            new DictionaryItemDTO("acura", "acura", "acura"),
            new DictionaryItemDTO("mdx", "MDX", "acura"),
            new DictionaryItemDTO("a5_coupe", "A5 Coupe", "audi"),
            new DictionaryItemDTO("s6", "S6", "audi"),
            new DictionaryItemDTO("q3", "Q3", "audi"),
            new DictionaryItemDTO("q5", "Q5", "audi"),
            new DictionaryItemDTO("q7", "Q7", "audi")
        ), 1);

    private static final Logger LOG = LoggerFactory.getLogger(DictionaryController.class);

    @GetMapping(value = "/api/common/dictionaries", produces = "application/json")
    public Map<String, DictionaryDTO> getAllDictionaries()
            throws Exception {

        LOG.info("/api/common/dictionaries called");

        return Map.of(
            DICTIONARY_YES_NO_OPTIONS.getCode(), DICTIONARY_YES_NO_OPTIONS,
            DICTIONARY_VEHICLE_MAKE.getCode(), DICTIONARY_VEHICLE_MAKE,
            DICTIONARY_VEHICLE_MODEL.getCode(), DICTIONARY_VEHICLE_MODEL,
            DICTIONARY_GENDER.getCode(), DICTIONARY_GENDER
        );
    }
}
