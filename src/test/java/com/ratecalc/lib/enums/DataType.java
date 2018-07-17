package com.ratecalc.lib.enums;

/**
 * This is an enum that holds the equivalent java data types. Simple form used in cucumber and defined here
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public enum DataType {
    STRING("java.lang.String"),
    INTEGER("java.lang.Integer"),
    LONG("java.lang.Long"),
    DOUBLE("java.lang.Double"),
    FLOAT("java.lang.Float"),
    BOOLEAN("java.lang.Boolean"),
    CHARACTER("java.lang.Character"),
    ARRAY("java.util.Array"),
    ARRAYLIST("java.util.ArrayList"),
    LIST("java.util.List"),
    HASH("java.util.Hash"),
    HASHSET("java.util.HashSet"),
    HASHMAP("java.util.HashMap"),
    MAP("java.util.Map"),
    UNKNOWN("UNKNOWN");

    private String theType;

    DataType(String theType){
        this.theType = theType;
    }

    public static DataType getDataType(String dataType){
        switch (dataType.toUpperCase()){
            case ("STRING"):
                return STRING;
            case ("INTEGER"):
                return INTEGER;
            case ("LONG"):
                return LONG;
            case ("DOUBLE"):
                return DOUBLE;
            case ("FLOAT"):
                return FLOAT;
            case ("BOOLEAN"):
                return BOOLEAN;
            case ("CHARACTER"):
                return CHARACTER;
            case ("ARRAY"):
                return ARRAY;
            case ("ARRAYLIST"):
                return ARRAYLIST;
            case ("LIST"):
                return LIST;
            case ("HASH"):
                return HASH;
            case ("HASHSET"):
                return HASHSET;
            case ("HASHMAP"):
                return HASHMAP;
            case ("MAP"):
                return MAP;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return theType;
    }

}
