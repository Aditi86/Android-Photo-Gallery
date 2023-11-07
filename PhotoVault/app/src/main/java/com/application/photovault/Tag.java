package com.application.photovault;

import java.io.Serializable;

/**
 * @author Aditi Patel
 * @author Aakaash Prakash Hemdev
 */

public class Tag implements Serializable {
    public String type;
    private String data;

    public Tag(String type, String data){
        this.type = type;
        this.data = data;
    }

    public void setData(String data){

        this.data = data;
    }

    public String getData(){

        return data;
    }

    public String toString(){

        return type+"="+data;
    }
}
