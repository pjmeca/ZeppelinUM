package aadd.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import aadd.persistencia.dto.PedidoDTO;
import aadd.persistencia.dto.RestauranteDTO;
import aadd.persistencia.jpa.bean.TipoUsuario;
import aadd.persistencia.mongo.bean.ItemPedido;
import aadd.persistencia.mongo.bean.TipoEstado;
import aadd.zeppelinum.ServicioGestionPedido;
import aadd.zeppelinum.ServicioGestionPlataforma;

class Test {

	@org.junit.jupiter.api.Test
	void crearUsuario() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		LocalDate fechaNacimiento = LocalDate.of(1990, 1, 8);
		Integer usuario = servicio.registrarUsuario("Periquita", "Palotes", fechaNacimiento, "periquita@palotes.es",
				"12345", TipoUsuario.RESTAURANTE);
		assertTrue(usuario != null);
	}

	@org.junit.jupiter.api.Test
	void validarUsuario() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		boolean exito = servicio.validarUsuario(1);
		assertTrue(exito);
	}

	@org.junit.jupiter.api.Test
	void crearRestaurantePlato() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();

		Integer rest = servicio.registrarRestaurante("Puerta de Murcia", 1, "Rio Madera", "30110", null, "Murcia",
				38.009109654488476, -1.1339542029796663, null);
		Integer rest2 = servicio.registrarRestaurante("Pistatxo", 1, "Alfaro", "30001", 12, "Murcia", 37.98654993575417,
				-1.1305437741450695, null);
		Integer rest3 = servicio.registrarRestaurante("El Barrilero de Jose", 1, "Marqués de Espinardo", "30100", 4,
				"Murcia", 38.00805160364204, -1.152337749004084, null);
		assertTrue(rest != null);
		assertTrue(rest2 != null);
		assertTrue(rest3 != null);
		boolean exito = servicio.nuevoPlato("Marmitako de bonito", "plato de bonito, patatas y cebolla con verduras",
				20d, rest);
		assertTrue(exito);

	}

	@org.junit.jupiter.api.Test
	public void loginTest() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		assertTrue(servicio.login("periquita@palotes.es", "12345") != null);
		assertFalse(servicio.login("mdclg3@um.es", "loquesea") != null);
		assertFalse(servicio.login("periquita@palotes.es", "123456") != null);
	}

	@org.junit.jupiter.api.Test
	public void checkUsuarioTest() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		assertTrue(servicio.isUsuarioRegistrado("periquita@palotes.es"));
		assertFalse(servicio.isUsuarioRegistrado("mdclg3@um.es"));
	}

	@org.junit.jupiter.api.Test
	void crearPlato() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		boolean exito = servicio.nuevoPlato("Plato no disponible", "plato que voy a cambiar a no disponible", 20d, 1);
		assertTrue(exito);
	}

	@org.junit.jupiter.api.Test
	public void platosByRestaurante() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		assertTrue(servicio.getMenuByRestaurante(1).size() == 1);
	}

	@org.junit.jupiter.api.Test
	public void buscarRestaurantes() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		assertTrue(servicio.getRestaurantesByFiltros("periqui", true, true, false).size() == 1);
		assertTrue(servicio.getRestaurantesByFiltros("venta", true, true, true).size() == 0);
	}

	@org.junit.jupiter.api.Test
	void buscarRestaurantesOrdenados() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();

		List<RestauranteDTO> restaurantes = servicio.getRestaurantesByCercanía(38.02410905700919, -1.1740641907325182,
				20, 0);
		for (RestauranteDTO r : restaurantes) {
			System.out.println(r.getNombre());
		}
		assertTrue(restaurantes.size() > 0);
	}

	@org.junit.jupiter.api.Test
	void crearUsuario2() {
		ServicioGestionPlataforma servicio = ServicioGestionPlataforma.getServicioGestionPlataforma();
		LocalDate fechaNacimiento = LocalDate.of(1990, 1, 8);
		Integer usuario = servicio.registrarUsuario("Mari", "Legaz", fechaNacimiento, "mclg@um.es", "12345",
				TipoUsuario.CLIENTE);
		assertTrue(usuario != null);
	}

	@org.junit.jupiter.api.Test
	void crearOpinion() {
		ServicioGestionPedido servicio = ServicioGestionPedido.getServicioGestionPedido();
		servicio.opinar(2, 2, "Todo estupendo y muy rico", 10d);
		servicio.opinar(2, 1, "La comida llegó un poco fría", 7.5d);
		servicio.opinar(2, 3, "El menú es un poco escaso, pero todo muy bueno", 8d);
		servicio.opinar(2, 3, "Nos trajeron un plato cambiado", 5d);
		// servicio.opinar(2, 4, "Siempre repetimos", 10d);
	}

	@org.junit.jupiter.api.Test
	void buscarOpiniones() {
		ServicioGestionPedido servicio = ServicioGestionPedido.getServicioGestionPedido();
		assertTrue(servicio.findByUsuario(2).size() == 4);
		assertTrue(servicio.findByRestaurante(3).size() == 2);
	}

	@org.junit.jupiter.api.Test
	void pedido() {
		ServicioGestionPedido servicio = ServicioGestionPedido.getServicioGestionPedido();
		List<ItemPedido> l = new ArrayList<ItemPedido>();
		l.add(new ItemPedido(3, 1, 6.0));
		assertTrue(servicio.registrarPedido("pedidoA",1, 10, LocalDateTime.now(), LocalDateTime.now(), "esto es un comentario",
				"atico", 50.0, 21, LocalDateTime.now(), l));
		assertTrue(servicio.editarEstado("pedidoA", TipoEstado.ACEPTADO, LocalDateTime.now()));
		assertTrue(servicio.asignarRepartidor("pedidoA", 42));
		List<PedidoDTO> list = servicio.findByUsuarioRestaurante(1, 10);
		PedidoDTO p=list.get(0);
		assertTrue(p.getCliente()==1 && p.getRestaurante()==10&& p.getComentarios().equals("esto es un comentario") 
				&& p.getDatosDireccion().equals("atico") && p.getImporte()==50.0);
	}

}