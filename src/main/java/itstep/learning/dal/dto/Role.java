package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Role {
    private UUID id;
    private String name;
    private String displayName;

    public Role(ResultSet rs) throws SQLException {
        id = UUID.fromString(rs.getString("Id"));
        name = rs.getString("Name");
        displayName = rs.getString("DisplayName");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
