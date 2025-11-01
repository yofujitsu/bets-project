package com.csbets.vcsbets.repository;

import com.csbets.vcsbets.entity.bet.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BetsRepository extends JpaRepository<Bet, UUID> {
}
