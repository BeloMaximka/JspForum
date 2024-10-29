package itstep.learning.models.themes;

import itstep.learning.dal.dto.Theme;

import java.util.UUID;

public class ThemeResponseModel {
    private UUID id;
    private UUID sectionId;
    private String title;
    private String slug;
    private String description;

    public ThemeResponseModel(Theme theme) {
        this.id = theme.getId();
        this.sectionId = theme.getSectionId();
        this.title = theme.getTitle();
        this.description = theme.getDescription();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
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
}
