package com.libreria.controlador;

import com.libreria.entidades.Autor;
import com.libreria.errores.ErrorServicio;
import com.libreria.servicios.AutorServicio;
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
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/editar-autor")
    public String crear(@RequestParam(required = false) String accion, ModelMap model,@RequestParam (required = false) String id) {
        if (accion==null) {
            accion="Crear";
        }
        
        Autor autor=new Autor();
        if (id !=null && !id.isEmpty()) {
            try {
                autor=autorServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                model.addAttribute("error",ex.getMessage());
            }
        }
        model.put("autor", autor);
        model.put("accion",accion);
        return "form-autor.html";
    }
    
    @PostMapping("/actualizar-autor")
    public String actualizarAutor(ModelMap model,@RequestParam String id, @RequestParam String nombre){
        try {
            if (id==null || id.isEmpty()) {
                autorServicio.crearAutor(nombre);
            }else{
                autorServicio.modificarAutor(nombre, id);
            }
            return "redirect:/";
        } catch (ErrorServicio e) {
            Autor autor =new Autor();
            autor.setId(id);
            autor.setNombre(nombre);
            
            model.put("accion", "Actualizar");
            model.put("error",e.getMessage());
            model.put("autor",autor);
            return "form-autor.html";
        }
    }

    @GetMapping("/listado")
    public String listado(ModelMap model) {
        List<Autor> autores = autorServicio.listadoAutores();
        model.put("autores", autores);
        return "autores";
    }
    
    @PostMapping("/eliminar-autor")
    public String deshabilitar(@RequestParam(required = false)String id){
        try {
            autorServicio.deshabilitar(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/autor/listado";
    }



}
