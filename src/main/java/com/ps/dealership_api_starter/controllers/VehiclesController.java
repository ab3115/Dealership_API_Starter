package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.VehiclesDAO;
import com.ps.dealership_api_starter.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("vehicles")
@CrossOrigin

public class VehiclesController {
    private VehiclesDAO vehiclesDAO;

    @Autowired
    public VehiclesController(VehiclesDAO vehiclesDAO) {
        this.vehiclesDAO = vehiclesDAO;
    }

    @GetMapping("")
    public List<Vehicle> getAllVehicles(@RequestParam(name = "minPrice", required = false) double minPrice,
                                @RequestParam(name = "maxPrice", required = false) double maxPrice,
                                @RequestParam(name = "make", required = false) String make,
                                @RequestParam(name = "model", required = false) String model,
                                @RequestParam(name = "minYear", required = false) int minYear,
                                @RequestParam(name = "maxYear", required = false) int maxYear,
                                @RequestParam(name = "color", required = false) String color,
                                @RequestParam(name = "minMiles", required = false) int minMiles,
                                @RequestParam(name = "maxMiles", required = false) int maxMiles,
                                @RequestParam(name = "type", required = false) String type
    ) {
        try {
            return vehiclesDAO.getAllVehicles(minPrice, maxPrice, make, model, minYear, maxYear, color, minMiles, maxMiles, type);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public Vehicle create(@RequestBody Vehicle vehicle)
    {
        try
        {
            return vehiclesDAO.create(vehicle);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{vin}")
    public void updateDealership(@PathVariable int vin, @RequestBody Vehicle vehicle)
    {
        try
        {
            vehiclesDAO.update(vin, vehicle);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{vin}")
    public void deleteDealership(@PathVariable int vin)
    {
        try
        {
            var vehicle = vehiclesDAO.getByVin(vin);

            if(vehicle == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            vehiclesDAO.delete(vin);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}


