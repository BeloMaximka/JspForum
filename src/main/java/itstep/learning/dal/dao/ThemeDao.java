package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.Theme;
import itstep.learning.expections.HttpException;
import itstep.learning.models.themes.CreateThemeModel;
import itstep.learning.models.themes.UpdateThemeModel;
import itstep.learning.services.MySqlDbService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Singleton
public class ThemeDao {
    private final Connection connection;
    private final SectionDao sectionDao;

    @Inject
    public ThemeDao(MySqlDbService mySqlDbService, SectionDao sectionDao) throws SQLException {
        this.connection = mySqlDbService.getConnection();
        this.sectionDao = sectionDao;
    }

    public List<Theme> getAll(UUID sectionId) throws ServletException {
        try {
            sectionDao.get(sectionId);
            String sql = "SELECT * FROM themes WHERE SectionId = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sectionId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Theme> themes = new LinkedList<>();
            while (resultSet.next()) {
                themes.add(new Theme(resultSet));
            }
            return themes;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public Theme get(UUID id) throws ServletException {
        try {
            String sql = "SELECT * FROM themes WHERE Id = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Theme with id %s not found.", id));
            }
            return new Theme(resultSet);
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void create(CreateThemeModel theme) throws ServletException {
        try {
            String sql = "INSERT INTO themes (SectionId, Title, Description) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, theme.getSectionId().toString());
            preparedStatement.setString(2, theme.getTitle());
            preparedStatement.setString(3, theme.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void update(UUID id, UpdateThemeModel theme) throws ServletException {
        try {
            String sql = "UPDATE themes SET SectionId = ?, Title = ?, Description = ? WHERE Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, theme.getSectionId().toString());
            preparedStatement.setString(2, theme.getTitle());
            preparedStatement.setString(3, theme.getDescription());
            preparedStatement.setString(4, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Theme with id %s not found.", id));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,
                    String.format("Section with id %s not found.", theme.getSectionId()));
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void delete(UUID id) throws ServletException {
        try {
            String sql = "UPDATE themes SET DeleteDate = NOW() WHERE Id = ? AND DeleteDate IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Theme with id %s not found.", id));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
