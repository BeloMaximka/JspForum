package itstep.learning.models.themes;

import itstep.learning.annotations.Optional;

import java.util.UUID;

public class CreateThemeModel {
    @Optional
    private UUID sectionId;
    private String title;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
