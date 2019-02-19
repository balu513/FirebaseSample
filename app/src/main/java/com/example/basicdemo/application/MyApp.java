package com.example.basicdemo.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {

    // GIT Commands
    //https://help.github.com/articles/adding-an-existing-project-to-github-using-the-command-line/
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
