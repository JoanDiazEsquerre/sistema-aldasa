package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;

public interface ProspectRepository extends PagingAndSortingRepository<Prospect, Integer> {
	
	//para asesor
	Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessorAndSucursal(String dniPerson,String surnamesPerson,Person assessor,Sucursal sucursal,Pageable pageable);
	Page<Prospect> findByPersonSurnamesLikeAndPersonAssessorAndSucursal(String surnamesPerson,Person assessor,Sucursal sucursal,Pageable pageable);
	
	
	//Para supervisor
		Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisorAndSucursal(String dni, String surnamesPerson,Person supervisor,Sucursal sucursal,Pageable pageable);
		Page<Prospect> findByPersonSurnamesLikeAndPersonSupervisorAndSucursal(String surnamesPerson,Person supervisor,Sucursal sucursal,Pageable pageable);
		
		
	//para administrador
	Page<Prospect> findByPersonSurnamesLikeAndPersonDniLikeAndSucursal(String surnamesPerson, String dni ,Sucursal sucursal, Pageable pageable);
	Page<Prospect> findByPersonSurnamesLikeAndSucursal(String surnamesPerson, Sucursal sucursal, Pageable pageable);
	
	
	Prospect findByPersonAndSucursal(Person entity, Sucursal sucursal);
	
	List<Prospect> findByPersonAssessor(Person assessor);
	List<Prospect> findByPersonSupervisor(Person supervisor);
	List<Prospect> findAll();

}
