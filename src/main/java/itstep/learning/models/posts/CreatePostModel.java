package itstep.learning.models.posts;

import itstep.learning.annotations.Optional;

import java.util.UUID;

public class CreatePostModel {
    private UUID themeId;
    @Optional
    private UUID authorId;
    private String title;
    private String description;

    public UUID getThemeId() {
        return themeId;
    }

    public void setThemeId(UUID themeId) {
        this.themeId = themeId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
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
}
