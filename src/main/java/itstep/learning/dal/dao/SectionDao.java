package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.Section;
import itstep.learning.expections.HttpException;
import itstep.learning.models.sections.CreateSectionModel;
import itstep.learning.services.MySqlDbService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Singleton
public class SectionDao {
    private final Connection connection;

    @Inject
    public SectionDao(MySqlDbService mySqlDbService) throws SQLException {
        this.connection = mySqlDbService.getConnection();
    }

    public List<Section> getAll() throws ServletException {
        try {
            String sql = "SELECT * FROM sections WHERE DeleteDate IS NULL";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            List<Section> sections = new LinkedList<>();
            while (resultSet.next()) {
                sections.add(new Section(resultSet));
            }
            return sections;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public Section get(UUID id) throws ServletException {
        try {
            String sql = "SELECT * FROM sections WHERE Id = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Section with id %s not found.", id));
            }
            return new Section(resultSet);
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void create(CreateSectionModel section) throws ServletException {
        try {
            String sql = "INSERT INTO sections (Id, Title) VALUES (UUID(), ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, section.getTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void update(UUID id, CreateSectionModel section) throws ServletException {
        try {
            String sql = "UPDATE sections SET Title = ? WHERE Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, section.getTitle());
            preparedStatement.setString(2, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Section with id %s not found.", id));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void delete(UUID id) throws ServletException {
        try {
            String sql = "UPDATE sections SET DeleteDate = NOW() WHERE Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Section with id %s not found.", id));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
