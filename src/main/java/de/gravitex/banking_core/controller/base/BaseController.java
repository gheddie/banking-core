package de.gravitex.banking_core.controller.base;

import java.util.List;

public interface BaseController<T> {

	public List<T> findAll();
	
	public void patch(T entity);
}