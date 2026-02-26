package de.gravitex.banking_core.controller.entity.base;

public interface PersistableEntityController<T> extends ViewEntityController<T> {

	public void patch(T entity);
}