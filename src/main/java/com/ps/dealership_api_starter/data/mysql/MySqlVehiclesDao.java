package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.VehiclesDAO;
import com.ps.dealership_api_starter.models.Dealership;
import com.ps.dealership_api_starter.models.Vehicle;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import static com.ps.dealership_api_starter.data.mysql.MySqlDealershipDao.mapRow;

@Component
public class MySqlVehiclesDao extends MySqlDaoBase implements VehiclesDAO {
    public MySqlVehiclesDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Vehicle> getAllVehicles(
            double minPrice,
            double maxPrice,
            String make,
            String model,
            int minYear,
            int maxYear,
            String color,
            int minMiles,
            int maxMiles,
            String vehicleType
    ) {
        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles " +
                "WHERE price BETWEEN ? AND ? AND " +
                "make LIKE ? AND " +
                "model LIKE ? AND " +
                "year BETWEEN ? AND ? AND " +
                "color LIKE ? AND " +
                "odometer BETWEEN ? AND ? AND " +
                "vehicle_type LIKE ?";

        double minPriceToSearch = minPrice == 0 ? getMinPrice() : minPrice;
        double maxPriceToSearch = maxPrice == 0 ? getMaxPrice() : maxPrice; // Whether you intentionally put 0 or not, it'll always be the max price.
        String makeToSearch     = make == null ? "%" : make;
        String modelToSearch    = model == null ? "%" : model;
        int    minYearToSearch  = minYear == 0 ? getMinYear() : minYear;
        int    maxYearToSearch  = maxYear == 0 ? getMaxYear() : maxYear;
        String colorToSearch    = color == null ? "%" : color;
        int    minMilesToSearch = minMiles == 0 ? getMinMiles() : minMiles;
        int    maxMilesToSearch = maxMiles == 0 ? getMaxMiles() : maxMiles;
        String typeToSearch     = vehicleType == null ? "%" : vehicleType;

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setDouble(1, minPriceToSearch);
            preparedStatement.setDouble(2, maxPriceToSearch);
            preparedStatement.setString(3, "%" + makeToSearch + "%");
            preparedStatement.setString(4, "%" + modelToSearch + "%");
            preparedStatement.setInt(5, minYearToSearch);
            preparedStatement.setInt(6, maxYearToSearch);
            preparedStatement.setString(7, "%" + colorToSearch + "%");
            preparedStatement.setInt(8, minMilesToSearch);
            preparedStatement.setInt(9, maxMilesToSearch);
            preparedStatement.setString(10, "%" + typeToSearch + "%");

            try (ResultSet row = preparedStatement.executeQuery();) {
                while (row.next()) {
                    Vehicle vehicle = mapRow(row);
                    vehicles.add(vehicle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return vehicles;
    }

    private int getMaxMiles() {
        return 0;
    }

    private int getMinMiles() {
        return 0;
    }

    private int getMaxYear() {
        return 0;
    }

    private int getMinYear() {
        return 0;
    }

    private double getMaxPrice() {
        return 0;
    }

    private double getMinPrice() {
        return 0;
    }


    @Override
    public Vehicle create(Vehicle vehicle) {

        String sql = "INSERT INTO vehicles(vin, year, make, model, vehicle_type, color, odometer, price, sold) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setInt(1, vehicle.getVin());
            preparedStatement.setInt(2, vehicle.getYear());
            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5, vehicle.getVehicleType());
            preparedStatement.setString(6, vehicle.getColor());
            preparedStatement.setInt(7, vehicle.getOdometer());
            preparedStatement.setDouble(8, vehicle.getPrice());
            preparedStatement.setBoolean(9, vehicle.isSold());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys();) {
                    if (generatedKeys.next()) {
                        int vin = generatedKeys.getInt(1);

                        return getByVin(vin);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }



    public Vehicle getByVin(int vin) {
        String sql = "SELECT * FROM vehicles WHERE vin = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement  preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, vin);

            try (ResultSet row =  preparedStatement.executeQuery()) {
                if (row.next()) {
                    return mapRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }



    @Override
    public void update(int vin, Vehicle vehicle) {
        String sql = "UPDATE vehicles" +
                " SET year = ? " +
                "   , make = ? " +
                "   , model = ? " +
                "   , vehicle_type = ? " +
                "   , color = ? " +
                "   , odometer = ? " +
                "   , price = ? " +
                "   , sold = ? " +
                " WHERE vin = ?;";

        try (
                Connection connection = getConnection();
                PreparedStatement  preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setInt(1, vehicle.getYear());
            preparedStatement.setString(2, vehicle.getMake());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setString(4, vehicle.getVehicleType());
            preparedStatement.setString(5, vehicle.getColor());
            preparedStatement.setInt(6, vehicle.getOdometer());
            preparedStatement.setDouble(7, vehicle.getPrice());
            preparedStatement.setBoolean(8, vehicle.isSold());
            preparedStatement.setInt(9, vehicle.getVin());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int vin) {
        String sql = "DELETE FROM vehicles " +
                " WHERE vin = ?;";

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, vin);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    protected static Vehicle mapRow(ResultSet row) throws SQLException {
        int     vin         = row.getInt("vin");
        int     year        = row.getInt("year");
        String  make        = row.getString("make");
        String  model       = row.getString("model");
        String  vehicleType = row.getString("vehicle_type");
        String  color       = row.getString("color");
        int     odometer    = row.getInt("odometer");
        double  price       = row.getDouble("price");
        boolean sold        = row.getBoolean("sold");

        return new Vehicle(vin, year, make, model, vehicleType, color, odometer, price, sold);
    }

}
