
package com.devsenior.llavemaestra.repository;
import com.devsenior.llavemaestra.model.Herramienta;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import com.devsenior.llavemaestra.model.Alquiler;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

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

    // Todos los alquileres, del más reciente al más antiguo
    List<Alquiler> findAllByOrderByCreadoEnDesc();

    // Alquileres filtrados por estado, del más reciente al más antiguo
    List<Alquiler> findByEstadoOrderByCreadoEnDesc(EstadoAlquiler estado);

    // Suma de ingresos del mes, excluyendo alquileres cancelados
    @Query("""
    SELECT COALESCE(SUM(a.valorTotal), 0)
    FROM Alquiler a
    WHERE a.creadoEn >= :inicioMes
      AND a.estado <> :cancelado
""")
    long sumarIngresosMes(
            @Param("inicioMes") LocalDateTime inicioMes,
            @Param("cancelado") EstadoAlquiler cancelado
    );

    // Cuenta los alquileres activos
    long countByEstadoIn(List<EstadoAlquiler> estados);

    // Alquileres entregados cuya fecha de fin ya pasó
    @Query("""
    SELECT a
    FROM Alquiler a
    WHERE a.estado = :estado
      AND a.fechaFin < :hoy
    ORDER BY a.fechaFin ASC
""")
    List<Alquiler> buscarVencidos(
            @Param("estado") EstadoAlquiler estado,
            @Param("hoy") LocalDate hoy
    );

    // Herramientas más alquiladas, excluyendo cancelados
    @Query("""
    SELECT a.herramienta, COUNT(a)
    FROM Alquiler a
    WHERE a.estado <> :cancelado
    GROUP BY a.herramienta
    ORDER BY COUNT(a) DESC
""")
    List<Object[]> contarAlquileresPorHerramienta(
            @Param("cancelado") EstadoAlquiler cancelado
    );
    boolean existsByClienteIdAndHerramientaIdAndFechaInicioAndFechaFinAndEstado(
            Long clienteId,
            Long herramientaId,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoAlquiler estado
    );
    boolean existsByClienteIdAndHerramientaIdAndEstado(
            Long clienteId,
            Long herramientaId,
            EstadoAlquiler estado
    );


}
