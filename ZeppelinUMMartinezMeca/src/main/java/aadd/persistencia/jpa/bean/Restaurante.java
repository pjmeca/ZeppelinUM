package aadd.persistencia.jpa.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;
/**
 * Entity implementation class for Entity: Restaurante
 *
 */
@Entity
@Table(name = "restaurante")
@NamedQueries({
    @NamedQuery(name = "Restaurante.findRestaurantesByUsuarioResponsable", query = " SELECT r FROM Restaurante r WHERE r.responsable = :responsable "),
    @NamedQuery(name = "Restaurante.findAllRestaurantes", query = " SELECT r FROM Restaurante r"),
    @NamedQuery(name = "Restaurante.findRestauranteById", query = " SELECT r FROM Restaurante r WHERE r.id = :id")
})
public class Restaurante implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;  
    @Column(name ="fecha_alta", columnDefinition = "DATE")
    private LocalDate fechaAlta;
    @Column(name ="valoracion_global")
    private Double valoracionGlobal;
    @Column(name ="num_valoraciones")
    private Integer numValoraciones;
    @Column(name ="num_penalizaciones")
    private Integer numPenalizaciones;
    
    @JoinColumn(name = "responsable")
    @ManyToOne
    private Usuario responsable;
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Plato> platos;
    @ManyToMany(mappedBy = "restaurantes")
    private List<CategoriaRestaurante> categorias;

    public Restaurante() {
        super();
    }
    
    //getters y setters
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Double getValoracionGlobal() {
		return valoracionGlobal;
	}

	public void setValoracionGlobal(Double valoracionGlobal) {
		this.valoracionGlobal = valoracionGlobal;
	}

	public Integer getNumValoraciones() {
		return numValoraciones;
	}

	public void setNumValoraciones(Integer numValoraciones) {
		this.numValoraciones = numValoraciones;
	}

	public Integer getNumPenalizaciones() {
		return numPenalizaciones;
	}

	public void setNumPenalizaciones(Integer numPenalizaciones) {
		this.numPenalizaciones = numPenalizaciones;
	}

	public Usuario getResponsable() {
		return responsable;
	}

	public void setResponsable(Usuario responsable) {
		this.responsable = responsable;
	}

	public List<Plato> getPlatos() {
		return new LinkedList<>(platos);
	}

	public void setPlatos(List<Plato> platos) {
		this.platos = platos;
	}
	
	public int getNumPlatos() {
		return platos.size();
	}
	
	public List<CategoriaRestaurante> getCategorias() {
		return new LinkedList<>(categorias);
	}

	public void setCategorias(List<CategoriaRestaurante> categorias) {
		this.categorias = categorias;
		for(CategoriaRestaurante c : categorias)
			c.addRestaurante(this);
	}
	
	public void addCategoria(CategoriaRestaurante c) {
		if(!categorias.contains(c))
			categorias.add(c);
	}
}
