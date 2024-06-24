package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.Dealership;
import com.ps.dealership_api_starter.models.Vehicle;
import java.util.List;

public interface VehiclesDAO {

    List<Vehicle> getAllVehicles(
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
    );


    Vehicle create(Vehicle vehicle);

    void update(int vin, Vehicle vehicle);

    void delete(int vin);

}
