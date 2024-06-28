package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Usuario;

public interface ProfileService {
 
   Optional<Profile> findBy(Integer id);
   Profile save(Profile profile);
   void delete(Profile profile);
   List<Profile> findByStatus(boolean status);
   Profile findByName (String name);
   Profile findByNameException(String name, int idProfile);
   
   Page<Profile> findByNameLikeAndStatus(String name, boolean status, Pageable pageable);

}
