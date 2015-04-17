package com.slusarzparadowski.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dominik on 2015-04-01.
 */
public class Settings {

    private int id;
    private int idUser;
    private boolean autoSaving;
    private boolean autoDeleting;

    public Settings(){
        this.id = 0;
        this.idUser = 0;
        this.autoSaving = false;
        this.autoDeleting = false;
    }

    public Settings(boolean autoSaving,  boolean autoDeleting){
        this.id = 0;
        this.idUser = 0;
        this.autoSaving = autoSaving;
        this.autoDeleting = autoDeleting;
    }

    public Settings(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.idUser = cursor.getInt(1);
        this.autoSaving = cursor.getInt(2) == 0 ? false : true;
        this.autoDeleting = cursor.getInt(3) == 0 ? false : true;
    }

    public boolean isAutoSaving() {
        return autoSaving;
    }

    public void setAutoSaving(boolean autoSaving) {
        this.autoSaving = autoSaving;
    }

    public boolean isAutoDeleting() {
        return autoDeleting;
    }

    public void setAutoDeleting(boolean autoDeleting) {
        this.autoDeleting = autoDeleting;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
