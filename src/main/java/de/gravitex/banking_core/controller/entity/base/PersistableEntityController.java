package de.gravitex.banking_core.controller.entity.base;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface PersistableEntityController<T> extends ViewEntityController<T> {

	public ResponseEntity<T> patch(T entity);
	
	public ResponseEntity<T> delete(Long aEntityId);
	
	public ResponseEntity<T> findById(Long aEntityId);
	
	public ResponseEntity<T> put(T entity);
	
	public ResponseEntity<List<T>> findAll();
}