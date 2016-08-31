package com.sharingmoments.util;

import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericResponse {
    private String message;
    private String error;
    private Object dataObject;
    private List<FieldError> errorList;
    

    public GenericResponse(final String message) {
        super();
        this.message = message;
    }
    
    public GenericResponse(final String message, final Object dataObject) {
        super();
        this.message = message;
        this.dataObject = dataObject;
    }

    public GenericResponse(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }
    
    public GenericResponse(final String message, final String error, List<FieldError> errorList) {
        super();
        this.message = message;
        this.error = error;
        this.errorList = errorList;
    }

    public GenericResponse(final List<FieldError> fieldErrors, final List<ObjectError> globalErrors) {
        super();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            this.message = mapper.writeValueAsString(fieldErrors);
            this.error = mapper.writeValueAsString(globalErrors);
        } catch (final JsonProcessingException e) {
            this.message = "";
            this.error = "";
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}

	public List<FieldError> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<FieldError> errorList) {
		this.errorList = errorList;
	}
}
