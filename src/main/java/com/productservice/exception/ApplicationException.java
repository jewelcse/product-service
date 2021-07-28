package com.productservice.exception;

public class ApplicationException extends RuntimeException{

    private static final long serialVersionUID = 1L;


    public ApplicationException(String msg){

        super(msg);

    }
}
