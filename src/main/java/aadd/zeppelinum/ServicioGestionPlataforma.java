package aadd.zeppelinum;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import aadd.persistencia.dto.PlatoDTO;
import aadd.persistencia.dto.RestauranteDTO;
import aadd.persistencia.dto.UsuarioDTO;
import aadd.persistencia.jpa.EntityManagerHelper;
import aadd.persistencia.jpa.bean.CategoriaRestaurante;
import aadd.persistencia.jpa.bean.Plato;
import aadd.persistencia.jpa.bean.Restaurante;
import aadd.persistencia.jpa.bean.TipoUsuario;
import aadd.persistencia.jpa.bean.Usuario;
import aadd.persistencia.jpa.dao.CategoriaRestauranteDAO;
import aadd.persistencia.jpa.dao.PlatoDAO;
import aadd.persistencia.jpa.dao.RestauranteDAO;
import aadd.persistencia.jpa.dao.UsuarioDAO;

public class ServicioGestionPlataforma {

    private static ServicioGestionPlataforma servicio;

    public static ServicioGestionPlataforma getServicioGestionPlataforma() {
        if (servicio == null) {
            servicio = new ServicioGestionPlataforma();
        }
        return servicio;
    }
    
    public Integer registrarUsuario(String nombre, String apellidos, LocalDate fechaNacimiento, String email,String clave, TipoUsuario tipo) {      

        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario usu = new Usuario();
            usu.setNombre(nombre);
            usu.setApellidos(apellidos);
            usu.setFechaNacimiento(fechaNacimiento);
            usu.setEmail(email);
            usu.setClave(clave);
            usu.setTipo(tipo);
            usu.setValidado(false);

            UsuarioDAO.getUsuarioDAO().save(usu, em);

            em.getTransaction().commit();
            return usu.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
    
    public boolean validarUsuario(Integer usuario) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Usuario usu = UsuarioDAO.getUsuarioDAO().findById(usuario);
            usu.setValidado(true);
            
            em.getTransaction().commit();           
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public Integer registrarRestaurante(String nombre, Integer responsable, List<Integer> categorias) {

        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();

            Restaurante r = new Restaurante();          
            r.setResponsable(UsuarioDAO.getUsuarioDAO().findById(responsable));
            r.setNombre(nombre);
            r.setFechaAlta(LocalDate.now());
            r.setValoracionGlobal(0d);
            r.setNumPenalizaciones(0);
            r.setNumValoraciones(0);
            
            CategoriaRestaurante cr ;
            List<CategoriaRestaurante> lista = new LinkedList<CategoriaRestaurante>();
            for(Integer i : categorias) {
            	 cr = CategoriaRestauranteDAO.getCategoriaRestauranteDAO().findById(i);
            	 if(cr!=null) {
            		 lista.add(cr);
            	 }
            }
            r.setCategorias(lista);

            RestauranteDAO.getRestauranteDAO().save(r, em);
            
            em.getTransaction().commit();
            return r.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
    
    public boolean addCategoriaARestaurante(Integer categoria, Integer restaurante) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		 try {
	            em.getTransaction().begin();

	                    
	            Restaurante r = RestauranteDAO.getRestauranteDAO().findById(restaurante);
	            CategoriaRestaurante cr =  CategoriaRestauranteDAO.getCategoriaRestauranteDAO().findById(categoria);
	            
	            
	            r.getCategorias().add(cr);
	            cr.getRestaurantes().add(r);

	            RestauranteDAO.getRestauranteDAO().save(r, em);
	            CategoriaRestauranteDAO.getCategoriaRestauranteDAO().save(cr, em);
	            
	            em.getTransaction().commit();
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        } finally {
	        	 if (em.getTransaction().isActive()) {
	                 em.getTransaction().rollback();
	             }
	             em.close();
	        }	
	}

    public boolean nuevoPlato(String titulo, String descripcion, double precio, Integer restaurante) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();

            Restaurante r = RestauranteDAO.getRestauranteDAO().findById(restaurante);
            Plato p = new Plato();
            p.setDescripcion(descripcion);
            p.setTitulo(titulo);
            p.setPrecio(precio);
            p.setRestaurante(r);
            p.setDisponibilidad(true);

            PlatoDAO.getPlatoDAO().save(p, em);

            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
    
    public boolean nuevaCategoria(String categoria) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();

         
            CategoriaRestaurante cr = new CategoriaRestaurante();
            cr.setCategoria(categoria);
            CategoriaRestauranteDAO.getCategoriaRestauranteDAO().save(cr, em);

            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
    
    public boolean isUsuarioRegistrado(String email) {
        List<UsuarioDTO> u = UsuarioDAO.getUsuarioDAO().findByEmail(email);
        if(u != null && !u.isEmpty()) {
            return true;
        }
        return false;
    }

    public UsuarioDTO login(String email, String clave) { 
        List<UsuarioDTO> usuarios = UsuarioDAO.getUsuarioDAO().findByEmailClave(email, clave);
        if(usuarios.isEmpty()) {
            System.out.println("Usuario no encontrado, email o clave incorrectos");
            return null;
        }
        else {
            System.out.println("Usuario logueado "+usuarios.get(0).getNombre());
            return usuarios.get(0);
        }
    }
    
    public List<PlatoDTO> getMenuByRestaurante(Integer restaurante) {
        return PlatoDAO.getPlatoDAO().findPlatosDisponiblesByRestaurante(restaurante);
    }
    
    public List<RestauranteDTO> getRestaurantesByFiltros(String keyword, boolean verNovedades, boolean ordernarByValoracion, boolean ceroIncidencias){
        if(keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        LocalDate fecha = null;
        if(verNovedades) { // filtramos por aquellos dados de alta la última semana
            fecha = LocalDate.now();
            fecha = fecha.minusWeeks(1);
        }
        return RestauranteDAO.getRestauranteDAO().findRestauranteByFiltros(keyword, fecha, ordernarByValoracion, ceroIncidencias);
    }
    
    public boolean setDisponbilidadPlato(Integer plato, boolean disponibilidad) {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Plato p = PlatoDAO.getPlatoDAO().findById(plato);
            p.setDisponibilidad(disponibilidad);
            
            em.getTransaction().commit();           
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}