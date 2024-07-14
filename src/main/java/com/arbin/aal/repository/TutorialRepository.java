package com.arbin.aal.repository;

import com.arbin.aal.entity.Tutorial;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface TutorialRepository extends CassandraRepository<Tutorial, UUID> {
  @AllowFiltering
  List<Tutorial> findByPublished(boolean published);
  
  List<Tutorial> findByTitleContaining(String title);

  Slice<Tutorial> findByTitleContaining(String title, Pageable pageable);
}
