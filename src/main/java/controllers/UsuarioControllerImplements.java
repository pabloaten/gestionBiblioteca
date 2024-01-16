package controllers;

import entity.UsuarioDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class UsuarioControllerImplements implements UsuarioController {


    private EntityManager entityManager;


    @Override
    public List<UsuarioDTO> leerAllUsuarios() {
        return null;
    }
}