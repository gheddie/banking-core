package de.gravitex.banking_core.controller.entity.base;

import java.util.List;

public interface GenericEntityController<T> {

	public List<T> findAll();
}