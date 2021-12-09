package com.libreria.controlador;

import com.libreria.entidades.Editorial;
import com.libreria.errores.ErrorServicio;
import com.libreria.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditoriallControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/editar-editorial")
    public String crear(@RequestParam(required = false) String accion, @RequestParam(required = false) String id, ModelMap model) {
        if (accion == null) {
            accion = "Crear";
        }

        Editorial editorial = new Editorial();
        if (id != null && !id.isEmpty()) {
            try {
                editorial = editorialServicio.editorial(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(EditoriallControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.put("editorial", editorial);
        model.put("accion", accion);
        return "form-editorial.html";
    }

    @PostMapping("/actualizar-editorial")
    public String actualizarEditorial(ModelMap model, @RequestParam String id, @RequestParam String nombre) {
        try {
            if (id == null || id.isEmpty()) {
                editorialServicio.crearEditorial(nombre);
            } else {
                editorialServicio.modificar(id, nombre);
            }
            return "redirect:/";
        } catch (ErrorServicio e) {
            Editorial editorial = new Editorial();
            editorial.setId(id);
            editorial.setNombre(nombre);

            model.put("accion", "Actualizar");
            model.put("error", e.getMessage());
            model.put("editorial", editorial);
            return "form-editorial.html";
        }
    }

    @GetMapping("/listado")
    public String listaEditorial(ModelMap model) {
        List<Editorial> editoriales = editorialServicio.listadoEditoriales();
        model.put("editoriales", editoriales);
        return "editoriales";
    }
    
    @PostMapping("/eliminar-editorial")
    public String deshabilitar(@RequestParam(required = false)String id){
        try {
            editorialServicio.deshabilitar(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(EditoriallControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/editorial/listado";
    }
}
