package controllers;

import entity.UsuarioDTO;

import java.util.List;

public interface UsuarioController {
    List<UsuarioDTO> leerAllUsuarios();
}
