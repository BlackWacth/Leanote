package com.bruce.notebooklist.bean;

/**
 * Created by Bruce on 2017/7/7.
 */

public class Notebook {

    public static final String TYPE_FILE = "TYPE_FILE";
    public static final String TYPE_FOLDER = "TYPE_FOLDER";

    public String type;
    public String title;
    public String path;

    public Notebook(String type, String title, String path) {
        this.type = type;
        this.title = title;
        this.path = path;
    }
}
