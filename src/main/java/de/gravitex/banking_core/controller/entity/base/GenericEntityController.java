package de.gravitex.banking_core.controller.entity.base;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface GenericEntityController<T> {

	public ResponseEntity<List<T>> findAll();
}