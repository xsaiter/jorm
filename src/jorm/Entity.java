package jorm;

public abstract class Entity {
    private long _id;

    protected Entity() {
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    private DbContext _context;

    public void setContext(DbContext context) {
        _context = context;
    }

    protected void markDirty() {
        _context.addDirty(this);
    }

    public boolean isGhost() {
        return _id == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Entity other = (Entity) obj;

        if (isGhost() || other.isGhost()) {
            return false;
        }

        return _id == other._id;
    }
}
