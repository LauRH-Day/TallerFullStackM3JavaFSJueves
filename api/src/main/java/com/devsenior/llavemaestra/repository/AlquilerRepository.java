
package com.devsenior.llavemaestra.repository;

import com.devsenior.llavemaestra.model.Alquiler;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    // Alquileres del cliente, del más reciente al más antiguo
    List<Alquiler> findByClienteEmailOrderByCreadoEnDesc(String email);

    // Cuenta los alquileres activos de un cliente
    long countByClienteIdAndEstadoIn(
            Long clienteId,
            List<EstadoAlquiler> estados
    );

    // Cuenta los alquileres activos de una herramienta que se cruzan
    // con el periodo solicitado. Las fechas de los extremos se incluyen.
    @Query("""
        SELECT COUNT(a)
        FROM Alquiler a
        WHERE a.herramienta.id = :herramientaId
          AND a.estado IN :estados
          AND a.fechaInicio <= :fechaFin
          AND a.fechaFin >= :fechaInicio
    """)
    long contarCruces(
            @Param("herramientaId") Long herramientaId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("estados") List<EstadoAlquiler> estados
    );
}
