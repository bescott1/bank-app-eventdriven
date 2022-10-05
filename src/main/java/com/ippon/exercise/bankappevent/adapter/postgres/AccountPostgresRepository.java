package com.ippon.exercise.bankappevent.adapter.postgres;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountPostgresRepository extends CrudRepository<AccountDAO, Integer> {

  List<AccountDAO> findAll();
  Optional<AccountDAO> findById(int id);
}
