package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.LeaseContractsDao;
import com.ps.dealership_api_starter.models.LeaseContract;

import javax.sql.DataSource;

public class MySqlLeaseContractsDao extends MySqlDaoBase implements LeaseContractsDao {

    public MySqlLeaseContractsDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public LeaseContract getLeaseContract(int id) {
        return null;
    }

    @Override
    public LeaseContract create(int id) {
        return null;
    }
}
