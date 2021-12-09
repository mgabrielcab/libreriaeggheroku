package com.libreria.servicios;

import com.libreria.entidades.Autor;
import com.libreria.entidades.Editorial;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    //Metodos
    @Transactional
    public void crear(Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial) throws ErrorServicio {
        validacion(isbn, titulo, anio, ejemplares);
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setIsbn(isbn);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
        libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAlta(true);
        libroRepositorio.save(libro);
    }

    @Transactional
    public void modificar(Long isbn, String titulo, Integer anio, Integer ejemplares, String id) throws ErrorServicio {
        validacion(isbn, titulo, anio, ejemplares);
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro");
        }
    }

    @Transactional
    public void baja(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(false);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro");
        }
    }

    @Transactional
    public void alta(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(true);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro");
        }
    }

    @Transactional
    public void prestar(String id, int prestado) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            //Obtengo primero los libros actuales
            int totalAct = libro.getEjemplaresRestantes();

            //valido que tenga stock, si tiene lo presto
            if (validarStock(totalAct, prestado)) {
                int prestAnterior = libro.getEjemplaresPrestados();
                libro.setEjemplaresPrestados(prestAnterior + prestado);
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - prestado);
                libroRepositorio.save(libro);
            } else {
                throw new ErrorServicio("No se puede prestar el libro -stock");
            }
        } else {
            throw new ErrorServicio("No se encuentra el Libro");
        }
    }

    @Transactional
    public void agregar(String id, int devuelto) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            int auxEjPrest = libro.getEjemplaresPrestados() - devuelto;
            int auxEjRest = libro.getEjemplaresRestantes() + devuelto;
            if (auxEjPrest >= 0) {
                libro.setEjemplaresPrestados((libro.getEjemplaresPrestados() - devuelto));
            }else{
                throw new ErrorServicio("Stock en negativo");
            }
            if (auxEjRest<=libro.getEjemplares()) {
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + devuelto);
            }else{
                throw new ErrorServicio("Error");
            }

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el Libro");
        }
    }

    private Boolean validarStock(int ejRest, int ejPrest) {
        if (ejRest >= ejPrest) {
            return true;
        } else {
            return false;
        }
    }

    private void validacion(Long isbn, String titulo, Integer anio, Integer ejemplares) throws ErrorServicio {
        if (isbn == null || isbn == 0L) {
            throw new ErrorServicio("El isbn ingresado es inválido");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El titulo ingresado es invalido");
        }
        if (anio == null || anio == 0) {
            throw new ErrorServicio("El Año ingresado es invalido");
        }
        if (ejemplares == null || ejemplares == 0) {
            throw new ErrorServicio("Número de ejemplares ingresados invalido");
        }
    }

    public List<Libro> listarLibros() {
        return libroRepositorio.buscarLibrosActivos();
    }

    public Libro buscarPorId(String id) throws ErrorServicio {
        validarId(id);
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            return libro;
        } else {
            return null;
        }
    }

    private void validarId(String id) throws ErrorServicio {
        if (id == null) {
            throw new ErrorServicio("Error en el id del libro");
        }
    }

}
