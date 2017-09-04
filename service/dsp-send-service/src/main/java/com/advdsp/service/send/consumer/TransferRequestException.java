package com.advdsp.service.send.consumer;

import java.io.IOException;

/**
 * Created by william on 2017/7/21.
 */
public class TransferRequestException extends IOException{
    public TransferRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
