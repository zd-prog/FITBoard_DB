package by.ziziko.fitboard.Entities;

import java.io.Serializable;

public class Post implements Serializable {

    private String title;
    private String category;
    private String date;
    private String text;
    private String author;
    private String image;

    public Post()
    {

    }

    public Post(String title, String category, String date, String text, String author, String image) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.text = text;
        this.author = author;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
