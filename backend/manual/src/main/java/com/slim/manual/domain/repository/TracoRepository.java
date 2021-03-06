package com.slim.manual.domain.repository;

import java.util.List;

import com.slim.manual.domain.model.Manual;
import com.slim.manual.domain.model.Traco;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TracoRepository extends JpaRepository<Traco,Integer> {
    List<Traco> findByManual(Manual manual);
}
