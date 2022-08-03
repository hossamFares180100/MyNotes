package com.example.mynotes;

public class ScreenItem {

    String title,description;
    int screenImage;

    public ScreenItem(String title, String description, int screenImage) {
        this.title = title;
        this.description = description;
        this.screenImage = screenImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScreenImage() {
        return screenImage;
    }

    public void setScreenImage(int screenImage) {
        this.screenImage = screenImage;
    }
}
