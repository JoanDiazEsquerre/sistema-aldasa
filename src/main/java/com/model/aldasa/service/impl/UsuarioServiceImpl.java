package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.UsuarioRepository;
import com.model.aldasa.service.UsuarioService;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario entity) {
		return usuarioRepository.save(entity);
	}

	@Override
	public void delete(Usuario entity) {
		usuarioRepository.delete(entity);
	}
	
	@Override
	public List<Usuario> findByStatus(boolean status) {
		return usuarioRepository.findByStatus(status);
	}
	
	@Override
	public Usuario findByPerson(Person person) {
		return usuarioRepository.findByPerson(person);
	}
	
	@Override
	public Usuario findByUsernameAndStatus(String username, boolean status) {
		return usuarioRepository.findByUsernameAndStatus(username,status);
	}
	
	@Override
	public Usuario findByUsernameException(String username, int idUser){
		return usuarioRepository.findByUsernameException(username, idUser);
	}
	
	@Override
	public Usuario findByPersonException(int idPersona, int idUser) {
		return usuarioRepository.findByPersonException(idPersona, idUser);
	}
	
	@Override
	public List<Usuario> findByProfileIdAndStatus(Integer idProfile,boolean status) {
		return usuarioRepository.findByProfileIdAndStatus(idProfile,status);
	}

	@Override
	public List<Usuario> findByTeamPersonSupervisorAndStatus(Person personSupervisor, boolean status) {
		return usuarioRepository.findByTeamPersonSupervisorAndStatus(personSupervisor, status);
	}

	@Override
	public Usuario findByUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}

	@Override
    public Page<Usuario> findByProfileNameLikeAndPersonSurnamesLikeAndPasswordLikeAndUsernameLikeAndStatus(String profileName,String personSurnames, String password, String username, boolean status, Pageable pageable) {
        return usuarioRepository.findByProfileNameLikeAndPersonSurnamesLikeAndPasswordLikeAndUsernameLikeAndStatus(profileName,personSurnames, password, username, status, pageable);
    }

	@Override
	public List<Usuario> findByTeam(Team team) {
		return usuarioRepository.findByTeam(team);
	}

	@Override
	public List<Usuario> findByProfileId(int idProfile) {
		return usuarioRepository.findByProfileId(idProfile);
	}

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario findByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByUsernameAndPassword(username, password);
	}
	

}
