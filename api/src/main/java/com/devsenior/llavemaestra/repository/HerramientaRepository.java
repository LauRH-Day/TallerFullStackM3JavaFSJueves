package com.devsenior.llavemaestra.repository;
import com.devsenior.llavemaestra.model.Herramienta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HerramientaRepository extends JpaRepository<Herramienta, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Herramienta h WHERE h.id = :id")
    Optional<Herramienta> findByIdForUpdate(@Param("id") Long id);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
    Optional<Herramienta> findByNombreIgnoreCase(String nombre);
}