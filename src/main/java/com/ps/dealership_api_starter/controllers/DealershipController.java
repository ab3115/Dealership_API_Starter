package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.DealershipDao;
import com.ps.dealership_api_starter.models.Dealership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("dealerships")
@CrossOrigin

public class DealershipController
{
    private DealershipDao dealershipDao;

    @Autowired
    public DealershipController(DealershipDao dealershipDao)
    {
        this.dealershipDao = dealershipDao;
    }



    @GetMapping("")
    public List<Dealership> search(@RequestParam(name="name", required = false) String name,
                                   @RequestParam(name="phone", required = false) String phone
    )
    {
        try
        {
            return dealershipDao.search(name, phone);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("{id}")
    public Dealership getById(@PathVariable int id )
    {
        try
        {
            var product = dealershipDao.getById(id);

            if(product == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return product;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public Dealership addDealership(@RequestBody Dealership dealership)
    {
        try
        {
            return dealershipDao.create(dealership);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{id}")
    public void updateDealership(@PathVariable int id, @RequestBody Dealership dealership)
    {
        try
        {
            dealershipDao.update(id, dealership);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{id}")
    public void deleteDealership(@PathVariable int id)
    {
        try
        {
            var product = dealershipDao.getById(id);

            if(product == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            dealershipDao.delete(id);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
