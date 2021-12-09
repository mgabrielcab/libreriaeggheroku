package com.libreria.servicios;

import com.libreria.entidades.Editorial;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    /**
     * Crea la editorial
     *
     * @param nombre
     * @throws ErrorServicio
     */
    @Transactional
    public void crearEditorial(String nombre) throws ErrorServicio {
        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        editorialRepositorio.save(editorial);
    }

    /**
     * Modifica el nombre de la editorial
     *
     * @param id
     * @param nombre
     * @throws ErrorServicio
     */
    @Transactional
    public void modificar(String id, String nombre) throws ErrorServicio {
        validar(nombre);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("El nombre ingresado es invalido");
        }
    }

    /**
     * Da de baja la editorial
     *
     * @param id
     * @throws ErrorServicio
     */
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial");
        }
    }

    /**
     * Da el alta ade la editorial
     *
     * @param id
     * @throws ErrorServicio
     */
    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(true);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial");
        }
    }

    private void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Ha ingresado un nombre Invalido");
        }
    }

    public List<Editorial> listadoEditoriales() {
        return editorialRepositorio.buscarEditorialesActivas();
    }

    public Editorial editorial(String idEditorial) throws ErrorServicio {
        validar(idEditorial);
        Optional<Editorial> respuesta = editorialRepositorio.findById(idEditorial);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            return editorial;
        } else {
            return null;
        }
    }
    
}
