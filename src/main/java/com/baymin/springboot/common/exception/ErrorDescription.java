package com.baymin.springboot.common.exception;

/**
 * Created by ebaizon on 4/20/2017.
 */
public class ErrorDescription {

    public static final String MISSING_PARAM = "parameter %s is empty or null";
    public static final String INVALID_PARAM = "parameter %s is invalid";
    public static final String INVALID_ENTITY_TYPE = "entity type %s is invalid";
    public static final String NOT_ALLOWED_PARAM = "parameter %s value not allowed";
    public static final String MISMATCH_PARAM = "parameter %s mismatch";
    public static final String RECORD_NOT_EXIST = "record of %s through %s not exist";
    public static final String RECORD_ALREADY_EXIST = "record of %s through %s already exist";
    public static final String RECORD_SAVE_ERROR = "record of %s through save error";
    public static final String JSON_PARSE_ERROR = "json string %s parse error";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error.";

}
