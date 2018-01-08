package jorm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DbContext {
    private final List<Item> _items = new ArrayList<>();
    private final HashMap<Class, Mapper<?>> _mappers = new HashMap<>();

    protected void registerMapper(Mapper<?> mapper){
        _mappers.put(mapper.getClass(), mapper);
    }

    protected Mapper<?> getMapper(Class type){
        return _mappers.get(type);
    }

    private Mapper<?> getMapper(Entity entity){
        return getMapper(entity.getClass());
    }

    public void addNew(final Entity entity) {
        assert entity.isGhost();

        Item item = newItem(entity, States.NEW);
        addIfNotContains(item);
    }

    public void addDirty(final Entity entity) {
        assert !entity.isGhost();

        Item item = newItem(entity, States.DIRTY);
        addIfNotContains(item);
    }

    public void addRemoved(final Entity entity) {
        assert !entity.isGhost();

        Item item = newItem(entity, States.REMOVED);
        addIfNotContains(item);
    }

    private void addClean(final Entity entity) {
        assert !entity.isGhost();

        getMapper(entity).addToLoaded(entity);
    }

    private static Item newItem(Entity entity, States state) {
        return new Item(entity, state);
    }

    private void addIfNotContains(final Item item) {
        if (_items.contains(item)) {
            _items.add(item);
        }
    }

    public void saveChanges() {
        insertNewObjects();
        updateDirtyObjects();
        deleteRemovedObjects();
    }

    private void insertNewObjects() {
        List<Entity> entities = filter(States.NEW);
        for(Entity entity : entities){
            getMapper(entity).insert(entity);
        }
    }

    private void updateDirtyObjects() {
        List<Entity> entities = filter(States.DIRTY);
        for(Entity entity : entities){
            getMapper(entity).update(entity);
        }
    }

    private void deleteRemovedObjects() {
        List<Entity> entities = filter(States.REMOVED);
        for(Entity entity : entities){
            getMapper(entity).delete(entity);
        }
    }

    private List<Entity> filter(States state) {
        return _items.stream().filter(i -> i._state == state).map(i -> i._entity).collect(Collectors.toList());
    }

    public enum States {
        NEW, DIRTY, REMOVED
    }

    public static class Item {
        private final Entity _entity;
        private final States _state;

        public Item(Entity entity, States state) {
            _entity = entity;
            _state = state;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Item)) {
                throw new RuntimeException("types not equal: " + obj.getClass().toString());
            }
            Item item = (Item) obj;
            return this._entity.equals(item._entity) && this._state == item._state;
        }
    }
}
