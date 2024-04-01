package com.libreforge.integration.dictionaries.api.dto;

public class DictionaryItemDTO {

    private String code;
    private Object value;
    private String filter1;

    public DictionaryItemDTO() {
    }

    public DictionaryItemDTO(String code, Object value) {
        this.code = code;
        this.value = value;
    }

    public DictionaryItemDTO(String code, Object value, String filter1) {
        this.code = code;
        this.value = value;
        this.filter1 = filter1;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFilter1() {
        return filter1;
    }

    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }
}
