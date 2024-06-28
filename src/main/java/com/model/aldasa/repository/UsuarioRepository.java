package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	Usuario findByUsernameAndStatus(String username,boolean status);
	Usuario findByUsername(String username);
	Usuario findById(String id);
	List<Usuario> findByStatus(boolean status);
	
	Usuario findByPerson(Person person);
	
    @Query(nativeQuery = true,value = " SELECT * FROM user  WHERE username = :username and id<>:idUser")
    Usuario findByUsernameException(String username, int idUser);
    
    @Query(nativeQuery = true,value = " SELECT * FROM user  WHERE idPerson = :idPersona and id<>:idUser")
    Usuario findByPersonException(int idPersona, int idUser);
    
	Usuario findByUsernameAndPassword(String username, String password);

    
	//@Query(value = "select p from user p WHERE p.estado=:estado order by p.nombre")
	//List<Usuario> findByRolJP(@Param(value = "estado") String estado);
	
	//@Query(value = "select p from Usuario p WHERE p.estado=:estado and p.idRol=13 order by p.nombre")
//	List<Usuario> findByRolGP(@Param(value = "estado") String estado);
    
    List<Usuario> findByProfileIdAndStatus(Integer idProfile,boolean status);
    List<Usuario> findByTeamPersonSupervisorAndStatus(Person personSupervisor, boolean status);
    List<Usuario> findByTeam(Team team);
	List<Usuario> findByProfileId(int idProfile);
    
    Page<Usuario> findByProfileNameLikeAndPersonSurnamesLikeAndPasswordLikeAndUsernameLikeAndStatus(String profileName, String personSurnames, String password, String username, boolean status, Pageable pageable);
	
	
}
