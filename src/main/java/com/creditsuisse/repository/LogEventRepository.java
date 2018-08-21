package com.creditsuisse.repository;

import com.creditsuisse.model.db.LogEvent;
import org.springframework.data.repository.CrudRepository;

public interface LogEventRepository extends CrudRepository<LogEvent, String> {

}
