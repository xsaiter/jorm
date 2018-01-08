package client.mappers;

import client.domain.Company;
import jorm.Entity;
import jorm.Mapper;
import jorm.DbContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyMapper extends Mapper<Company> {
    public CompanyMapper(DbContext context) {
        super(context);
    }

    @Override
    protected String sqlQueryById() {
        return "select id, name, inn from company where id = ?";
    }

    @Override
    protected Company map(ResultSet rs) throws SQLException {
        long id = rs.getLong(0);
        String name = rs.getString(1);
        String inn = rs.getString(2);
        return new Company(id, name, inn);
    }

    @Override
    public void insert(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void delete(Entity entity) {

    }

}
