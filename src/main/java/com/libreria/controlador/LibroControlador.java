package com.libreria.controlador;

import com.libreria.entidades.Autor;
import com.libreria.entidades.Editorial;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.servicios.AutorServicio;
import com.libreria.servicios.EditorialServicio;
import com.libreria.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/editar-libro")
    public String crear(@RequestParam(required = false) String accion, @RequestParam(required = false) String id, ModelMap model) {
        if (accion == null) {
            accion = "Crear";
        }

        Libro libro = new Libro();
        if (id != null && !id.isEmpty()) {
            try {
                libro = libroServicio.buscarPorId(id);
            } catch (ErrorServicio e) {
                Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        List<Autor> autores = autorServicio.listadoAutores();
        List<Editorial> editoriales = editorialServicio.listadoEditoriales();
        model.put("libro", libro);
        model.put("accion", accion);
        model.put("editoriales", editoriales);
        model.put("autores", autores);
        return "form-libro";
    }

    @PostMapping("/actualizar-libro")
    public String actualizarLibro(ModelMap model, @RequestParam String isbn, @RequestParam String id, @RequestParam String titulo, @RequestParam int anio, @RequestParam int ejemplares, @RequestParam String autor, @RequestParam String editorial) {

        try {

            if (id == null || id.isEmpty()) {
                Long convIsbn = Long.parseLong(isbn);
                Autor esteAutor = autorServicio.autor(autor);
                Editorial estaEditorial = editorialServicio.editorial(editorial);
                libroServicio.crear(convIsbn, titulo, anio, ejemplares, esteAutor, estaEditorial);
            } else {
                Long cIsbn = Long.parseLong(isbn);
                libroServicio.modificar(cIsbn, titulo, anio, ejemplares, id);
            }
            return "redirect:/libro/listado";
        } catch (ErrorServicio e) {
            Libro libro = new Libro();
            libro.setId(id);
            libro.setIsbn(Long.parseLong(isbn));
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            List<Autor> autores = autorServicio.listadoAutores();
            List<Editorial> editoriales = editorialServicio.listadoEditoriales();

            model.put("accion", "Actualizar");
            model.put("error", e.getMessage());
            model.put("libro", libro);
            model.put("autores", autores);
            model.put("editoriales", editoriales);
            return "form-libro.html";
        }
    }

    @GetMapping("/listado")
    public String listado(ModelMap model) {
        List<Libro> libros = libroServicio.listarLibros();
        model.put("libros", libros);
        return "libros";
    }

    @PostMapping("/eliminar-libro")
    public String deshabilitar(@RequestParam(required = false) String id) {
        try {
            libroServicio.baja(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/libro/listado";
    }

    @PostMapping("/prestar-libro")
    public String prestar(@RequestParam(required = false) String id, @RequestParam(required = false) int prest, ModelMap model) throws ErrorServicio {
        try {
            libroServicio.prestar(id, prest);
            return "redirect:/libro/listado";
        } catch (ErrorServicio ex) {
            Libro libro = new Libro();
            libro = libroServicio.buscarPorId(id);
            model.put("error", ex.getMessage());
            model.put("libro", libro);
            return "form-libro.html";
        }
    }

    @PostMapping("/devolver-libro")
    public String devolucion(ModelMap model, @RequestParam(required = false)String id, @RequestParam(required = false) int dev) throws ErrorServicio {
        try {
            libroServicio.agregar(id, dev);
            return "redirect:/libro/listado";
        } catch (ErrorServicio ex) {
            Libro libro=new Libro();
            libro=libroServicio.buscarPorId(id);
            model.put("error",ex.getMessage());
            model.put("libro", libro);
            return "form-libro";
        }
    }
}
