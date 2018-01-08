package client;

import client.domain.Company;

public class Main {

    public static void main(String[] argv) {
        ClientDc dc = new ClientDc();
        Company company = dc.companyMapper().find(1);
        company.setName("newName");
        dc.saveChanges();
    }
}
