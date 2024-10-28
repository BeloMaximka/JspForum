package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.Role;
import itstep.learning.services.MySqlDbService;

import javax.servlet.ServletException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Singleton
public class RoleDao {
    private final Connection connection;

    @Inject
    public RoleDao(MySqlDbService mySqlDbService) throws SQLException {
        this.connection = mySqlDbService.getConnection();
    }

    public List<Role> getAll(UUID userId) throws ServletException {
        try {
            String sql = "SELECT r.Id, r.Name, r.DisplayName FROM userroles JOIN roles r on userroles.RoleId = r.Id WHERE UserId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Role> roles = new LinkedList<>();
            while (resultSet.next()) {
                roles.add(new Role(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
