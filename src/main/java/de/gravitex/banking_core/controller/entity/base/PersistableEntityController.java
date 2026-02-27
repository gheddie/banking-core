package de.gravitex.banking_core.controller.entity.base;

import org.springframework.http.ResponseEntity;

public interface PersistableEntityController<T> extends ViewEntityController<T> {

	public ResponseEntity<T> patch(T entity);
}