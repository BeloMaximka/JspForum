package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.User;
import itstep.learning.expections.HttpException;
import itstep.learning.models.user.CreateUserModel;
import itstep.learning.models.user.UpdateUserModel;
import itstep.learning.services.MySqlDbService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.UUID;

@Singleton
public class UserDao {
    private final Connection connection;

    @Inject
    public UserDao(MySqlDbService mySqlDbService) throws SQLException {
        this.connection = mySqlDbService.getConnection();
    }

    public User get(String userName) throws ServletException {
        try {
            String sql = "SELECT * FROM users WHERE UserName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("User with username %s not found.", userName));
            }
            return new User(resultSet);
        }
        catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void create(CreateUserModel user) throws ServletException {
        try {
            String sql = "INSERT INTO users (UserName, Email, AvatarUrl, Birthdate, Password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getAvatarUrl());
            preparedStatement.setDate(4, user.getBirthdate());
            preparedStatement.setString(5, user.getPasswordHash());
            preparedStatement.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                    String.format("User with email '%s' and username '%s' already exists.", user.getEmail(), user.getUserName()));
        }
        catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void update(UUID id, UpdateUserModel user) throws ServletException {
        try {
            String sql = "UPDATE users SET AvatarUrl = ?, Birthdate = ? WHERE Id = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getAvatarUrl());
            preparedStatement.setDate(2, new java.sql.Date(user.getBirthDate().getTime()));
            preparedStatement.setString(3, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("User with id %s not found.", id));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
