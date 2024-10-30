package itstep.learning.dal.dao;

import com.google.inject.Inject;
import itstep.learning.dal.dto.Rate;
import itstep.learning.expections.HttpException;
import itstep.learning.models.rates.CreateRateModel;
import itstep.learning.models.themes.CreateThemeModel;
import itstep.learning.models.themes.UpdateThemeModel;
import itstep.learning.services.MySqlDbService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RateDao {
    private final Connection connection;

    @Inject
    public RateDao(MySqlDbService mySqlDbService) throws SQLException {
        this.connection = mySqlDbService.getConnection();
    }

    public List<Rate> getByItemId(UUID itemId) throws ServletException {
        try {
            String sql = "SELECT * FROM rates WHERE ItemId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, itemId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Rate> rates = new LinkedList<>();
            while (resultSet.next()) {
                rates.add(new Rate(resultSet));
            }
            return rates;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public Rate get(UUID itemId, UUID userId) throws ServletException {
        try {
            String sql = "SELECT * FROM rates WHERE ItemId = ? AND UserId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, itemId.toString());
            preparedStatement.setString(2, userId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Rate with itemId %s and userId %s not found.", itemId, userId));
            }
            return new Rate(resultSet);
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void create(CreateRateModel rate) throws ServletException {
        try {
            String sql = "INSERT INTO rates (ItemId, UserId, Rate) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, rate.getItemId().toString());
            preparedStatement.setString(2, rate.getUserId().toString());
            preparedStatement.setInt(3, rate.getRate());
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "You have already rated this item.");
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void update(CreateRateModel rate) throws ServletException {
        try {
            String sql = "UPDATE rates SET Rate = ? WHERE ItemId = ? AND UserId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, rate.getRate());
            preparedStatement.setString(2, rate.getItemId().toString());
            preparedStatement.setString(3, rate.getUserId().toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Rate not found.");
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    public void delete(UUID itemId, UUID userId) throws ServletException {
        try {
            String sql = "DELETE FROM rates  WHERE ItemId = ? AND UserId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, itemId.toString());
            preparedStatement.setString(2, userId.toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format("Rate with itemId %s and userId %s not found.", itemId, userId));
            }
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
