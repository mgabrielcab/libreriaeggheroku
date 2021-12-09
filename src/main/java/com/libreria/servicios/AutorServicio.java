package com.libreria.servicios;

import com.libreria.entidades.Autor;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws ErrorServicio {
        validar(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        autorRepositorio.save(autor);
    }

    /**
     * Modifica el Nombre del Autor
     *
     * @param nombre
     * @param id
     * @throws ErrorServicio
     */
    @Transactional
    public void modificarAutor(String nombre, String id) throws ErrorServicio {
        validar(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        }
    }

    /**
     * Habilita un autor
     *
     * @param id
     * @throws ErrorServicio
     */
    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(true);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el Autor");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor");
        }
    }

    private void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty() || nombre.isEmpty()) {
            throw new ErrorServicio("Nombre ingresado invalido");
        }
    }

    public List<Autor> listadoAutores() {
        return autorRepositorio.buscarAutoresActivos();
    }

    public Autor autor(String idAutor) {
        Optional<Autor> respuesta = autorRepositorio.findById(idAutor);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            return autor;
        } else {
            return null;
        }
    }
    
    
    public Autor buscarPorId(String id) throws ErrorServicio{
        Optional<Autor> autorBuscado=autorRepositorio.findById(id);
        if (autorBuscado.isPresent()) {
            Autor autor=autorBuscado.get();
            return autor;
        }else{
            throw new ErrorServicio("No se encontro al autor");
        }
    }
}
