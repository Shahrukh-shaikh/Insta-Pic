package com.example.shahrukh.instapic;

public class Insta {
    private String title;
    private String image;
    private String username;
    private String Profileimg;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    private String Description;

    public Insta(){

    }

    public Insta(String title, String image,String Description, String username, String Profileimg) {
        this.title = title;
        this.image = image;
        this.Description = Description;
        this.username = username;
        this.Profileimg = Profileimg;
    }

    public void setProfileimg(String profileimg) {
        Profileimg = profileimg;
    }

    public String getProfileimg() {
        return Profileimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
