package com.example.openwxchat.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateschemeParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String path;

    private String query;
}