package com.libreforge.integration.dictionaries.api.dto;

import java.util.ArrayList;
import java.util.List;

public class DictionaryDTO {

    private String code;
    private String name;
    private int filtersCount = 0;

    private List<DictionaryItemDTO> items = new ArrayList<>();

    public DictionaryDTO() {
    }

    public DictionaryDTO(String code, String name, List<DictionaryItemDTO> items) {
        this.code = code;
        this.name = name;
        this.items = items;
    }

    public DictionaryDTO(String code, String name, List<DictionaryItemDTO> items, int filtersCount) {
        this.code = code;
        this.name = name;
        this.items = items;
        this.filtersCount = filtersCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DictionaryItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DictionaryItemDTO> items) {
        this.items = items;
    }

    public int getFiltersCount() {
        return filtersCount;
    }

    public void setFiltersCount(int filtersCount) {
        this.filtersCount = filtersCount;
    }
}
