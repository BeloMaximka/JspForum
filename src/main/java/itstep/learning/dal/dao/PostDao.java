package itstep.learning.dal.dao;

import com.google.inject.Inject;
import itstep.learning.dal.dto.Post;
import itstep.learning.expections.HttpException;
import itstep.learning.models.posts.CreatePostModel;
import itstep.learning.models.posts.UpdatePostModel;
import itstep.learning.services.MySqlDbService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PostDao {
    private final Connection connection;

    @Inject
    public PostDao(MySqlDbService mySqlDbService) throws SQLException {
        this.connection = mySqlDbService.getConnection();
    }

    public List<Post> getAll(UUID themeId) throws ServletException {
        try {
            String sql = "SELECT * FROM posts WHERE ThemeId = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, themeId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Post> posts = new LinkedList<>();
            while (resultSet.next()) {
                posts.add(new Post(resultSet));
            }
            return posts;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public Post get(UUID id) throws ServletException {
        try {
            String sql = "SELECT * FROM posts WHERE Id = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Theme with id %s not found.", id));
            }
            return new Post(resultSet);
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void create(CreatePostModel post) throws ServletException {
        try {
            String sql = "INSERT INTO posts (ThemeId, AuthorId, Title, Description) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, post.getThemeId().toString());
            preparedStatement.setString(2, post.getAuthorId().toString());
            preparedStatement.setString(3, post.getTitle());
            preparedStatement.setString(4, post.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void update(UUID id, UpdatePostModel post) throws ServletException {
        try {
            String sql = "UPDATE posts SET Title = ?, Description = ? WHERE Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Post with id %s not found.", id));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void delete(UUID id) throws ServletException {
        try {
            String sql = "UPDATE posts SET DeleteDate = NOW() WHERE Id = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Post with id %s not found.", id));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
