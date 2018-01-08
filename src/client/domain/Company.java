package client.domain;

import jorm.Entity;

public class Company extends Entity {
    private String _name;
    private final String _inn;

    public Company(String name, String inn) {
        _name = name;
        _inn = inn;
    }

    public Company(long id, String name, String inn){
        this(name, inn);
        setId(id);
    }

    public String getName() {
        return _name;
    }

    public String getInn() {
        return _inn;
    }

    public void setName(String name){
        _name = name;
        markDirty();
    }
}
