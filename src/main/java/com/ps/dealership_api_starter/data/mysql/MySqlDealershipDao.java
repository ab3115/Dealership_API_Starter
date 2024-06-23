package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.DealershipDao;
import com.ps.dealership_api_starter.models.Dealership;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlDealershipDao extends MySqlDaoBase implements DealershipDao {

    public MySqlDealershipDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Dealership> search(String name, String phone)
    {
        List<Dealership> dealerships = new ArrayList<>();
        String sql = "SELECT * FROM dealerships " +
                "WHERE name LIKE ? AND " +
                "phone LIKE ?";

        String nameToSearch = name == null ? "%" : name;
        String phoneToSearch = phone == null ? "%": phone;

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + nameToSearch + "%");
            statement.setString(2, "%" + phoneToSearch + "%");

            ResultSet row = statement.executeQuery();

            while (row.next())
            {
                Dealership dealership = mapRow(row);
                dealerships.add(dealership);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return dealerships;
    }



    @Override
    public Dealership getById(int dealershipId)
    {
        String sql = "SELECT * FROM dealerships WHERE dealership_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, dealershipId);

            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Dealership create(Dealership dealership)
    {

        String sql = "INSERT INTO dealerships(name, address, phone) " +
                " VALUES (?, ?, ?);";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, dealership.getName());
            statement.setString(2, dealership.getAddress());
            statement.setString(3, dealership.getPhone());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int dealershipId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(dealershipId);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int dealershipId, Dealership dealership)
    {
        String sql = "UPDATE dealerships" +
                " SET name = ? " +
                "   , address = ? " +
                "   , phone = ? " +
                " WHERE dealership_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dealership.getName());
            statement.setString(2, dealership.getAddress());
            statement.setString(3, dealership.getPhone());
            statement.setInt(4, dealershipId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int dealershipId)
    {

        String sql = "DELETE FROM dealerships " +
                " WHERE dealership_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, dealershipId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected static Dealership mapRow(ResultSet row) throws SQLException
    {
        int dealershipId = row.getInt("dealership_id");
        String name = row.getString("name");
        String address = row.getString("address");
        String phone = row.getString("phone");

        return new Dealership(dealershipId, name, address, phone);
    }
}
