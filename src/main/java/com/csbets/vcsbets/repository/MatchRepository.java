package com.csbets.vcsbets.repository;

import com.csbets.vcsbets.entity.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
