package com.neurotech.currencyAPI.Exception;

public class CambioNotFoundException extends RuntimeException {
    public CambioNotFoundException(String msg){
        super(msg);
    }
}
