package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.SalesContractsDao;
import com.ps.dealership_api_starter.models.Dealership;
import com.ps.dealership_api_starter.models.SalesContract;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlSalesContractsDao extends MySqlDaoBase implements SalesContractsDao {
    public MySqlSalesContractsDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public SalesContract getSalesContract(int Id) {
        String sql = "SELECT * FROM dealerships WHERE sales_contract = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Id);

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public SalesContract create(SalesContract salesContract) {

        String sql = "INSERT INTO dealerships(name, address, phone) " +
                " VALUES (?, ?, ?);";

        try (Connection connection = getConnection()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }


        protected static SalesContract mapRow (ResultSet row) throws SQLException
        {
            int contractId = row.getInt("contractId");
            String contractDate = row.getString("contractDate");
            String customerName = row.getString("customerName");
            String customerEmail = row.getString("customerEmail");
            int vin = row.getInt("vin");
            double salesTax = row.getDouble("salesTax");
            double recordingFee = row.getDouble("recordingFee");
            double processingFee = row.getDouble("processingFee");
            double totalPrice = row.getDouble("totalPrice");
            String financeOption = row.getString("financeOption");
            double monthlyPayment = row.getDouble("monthlyPayment");
            return new SalesContract(contractId, contractDate, customerName, customerEmail, vin, salesTax, recordingFee, processingFee, totalPrice, financeOption, monthlyPayment);
        }
    }



