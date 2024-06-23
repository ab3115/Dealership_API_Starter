package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.LeaseContract;

public interface LeaseContractsDao {

    LeaseContract getLeaseContract(int id);

    LeaseContract create(int id);
}
