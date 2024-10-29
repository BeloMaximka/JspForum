package itstep.learning.models.posts;

import itstep.learning.dal.dto.Post;

import java.util.Date;
import java.util.UUID;

public class PostResponseModel {
    private UUID id;
    private UUID themeId;
    private UUID authorId;
    private String title;
    private String slug;
    private String description;
    private Date createDate;

    public PostResponseModel(Post post) {
        id = post.getId();
        themeId = post.getThemeId();
        authorId = post.getAuthorId();
        title = post.getTitle();
        description = post.getDescription();
        createDate = post.getCreateDate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
