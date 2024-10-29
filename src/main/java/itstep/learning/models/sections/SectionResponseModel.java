package itstep.learning.models.sections;

import itstep.learning.dal.dto.Section;

import java.util.UUID;

public class SectionResponseModel {
    private UUID id;
    private String title;
    private String slug;

    public SectionResponseModel(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
