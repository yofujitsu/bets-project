package com.csbets.vcsbets.repository;

import com.csbets.vcsbets.entity.match.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
}
