package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.MovimientoBancario;


public interface DetalleMovimientoBancarioRepository extends JpaRepository<DetalleMovimientoBancario, Integer> {
	
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado, MovimientoBancario movimientoBancario, Pageable pageable);
	
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacion(boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, Pageable pageable);


	
	@Query(nativeQuery = true,value = "SELECT if(sum(importe) is null, 0,sum(importe)) from detallemovimientobancario where idMovimientoBancario = :idMovimientoBancario and estado = 1")
	BigDecimal totalImporteMovimiento(int idMovimientoBancario);
}
