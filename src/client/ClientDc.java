package client;


import client.mappers.CompanyMapper;
import jorm.DbContext;

public class ClientDc extends DbContext {
    public ClientDc() {
        registerMapper(new CompanyMapper(this));
    }

    public CompanyMapper companyMapper() {
        return (CompanyMapper) getMapper(CompanyMapper.class);
    }
}
