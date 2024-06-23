package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.SalesContract;

public interface SalesContractsDao {

    SalesContract getSalesContract(int id);

    SalesContract create(SalesContract contract);
}
