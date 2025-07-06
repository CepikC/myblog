package kz.yandex.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Post {
    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private int likesCount;
    private List<String> tags;
    private List<Comment> comments;

    public Post() {
        this.tags = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(Long id, String title, String text, String imagePath, int likesCount,
                List<String> tags, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
        this.tags = tags;
        this.comments = comments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }


    public List<String> getTextParts() {
        return text != null ? Arrays.asList(text.split("\\R{2,}")) : List.of();
    }

    public String getTagsAsText() {
        return String.join(" ", tags);
    }

    public String getTextPreview() {
        return text != null && text.length() > 100
                ? text.substring(0, 100) + "..."
                : text;
    }

}

