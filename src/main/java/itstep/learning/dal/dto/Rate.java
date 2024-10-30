package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Rate {
    private UUID itemId;
    private UUID userId;
    private int rate;

    public Rate(ResultSet rs) throws SQLException {
        itemId = UUID.fromString(rs.getString("ItemId"));
        userId = UUID.fromString(rs.getString("UserId"));
        rate = rs.getInt("Rate");
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
