
package com.devsenior.llavemaestra.config;

import com.devsenior.llavemaestra.model.Alquiler;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import com.devsenior.llavemaestra.model.Categoria;
import com.devsenior.llavemaestra.model.Herramienta;
import com.devsenior.llavemaestra.model.Rol;
import com.devsenior.llavemaestra.model.Usuario;
import com.devsenior.llavemaestra.repository.AlquilerRepository;
import com.devsenior.llavemaestra.repository.HerramientaRepository;
import com.devsenior.llavemaestra.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String CLAVE_DEMO = "llave123";

    private final UsuarioRepository usuarioRepository;
    private final HerramientaRepository herramientaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlquilerRepository alquilerRepository;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            HerramientaRepository herramientaRepository,
            AlquilerRepository alquilerRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.herramientaRepository = herramientaRepository;
        this.alquilerRepository = alquilerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ---------- Usuarios / Personas ----------

        Usuario pedro = usuarioRepository.findByEmail("pedro@correo.com")
                .orElseGet(() -> usuario("Pedro Salazar",
                        "pedro@correo.com", Rol.CLIENTE, true));

        Usuario diana = usuarioRepository.findByEmail("diana@correo.com")
                .orElseGet(() -> usuario("Diana Mejía",
                        "diana@correo.com", Rol.CLIENTE, true));

        Usuario jorge = usuarioRepository.findByEmail("jorge@correo.com")
                .orElseGet(() -> usuario("Jorge Pinzón",
                        "jorge@correo.com", Rol.CLIENTE, false));

        usuarioRepository.findByEmail("admin@llavemaestra.co")
                .orElseGet(() -> usuario("Hernán Rojas",
                        "admin@llavemaestra.co", Rol.ADMIN, true));

        // ---------- Herramientas ----------

        Herramienta taladro = herramientaRepository
                .findByNombreIgnoreCase("Taladro percutor")
                .orElseGet(() -> herramienta(
                        "Taladro percutor", Categoria.ELECTRICA,
                        25000, 100000, 3, true,
                        "Taladro de alta potencia para concreto y mampostería.",
                        "https://loremflickr.com/320/240/drill"
                ));

        Herramienta pulidora = herramientaRepository
                .findByNombreIgnoreCase("Pulidora angular")
                .orElseGet(() -> herramienta(
                        "Pulidora angular", Categoria.ELECTRICA,
                        22000, 8000, 2, true,
                        "Esmeril angular de 4-1/2 pulgadas.",
                        "https://loremflickr.com/320/240/grinder"
                ));

        Herramienta mezcladora = herramientaRepository
                .findByNombreIgnoreCase("Mezcladora de concreto")
                .orElseGet(() -> herramienta(
                        "Mezcladora de concreto", Categoria.CONSTRUCCION,
                        60000, 30000, 1, true,
                        "Mezcladora trompo de 1 bacha.",
                        "https://loremflickr.com/320/240/concrete,mixer"
                ));

        Herramienta andamio = herramientaRepository
                .findByNombreIgnoreCase("Andamio tubular")
                .orElseGet(() -> herramienta(
                        "Andamio tubular", Categoria.CONSTRUCCION,
                        15000, 12000, 4, true,
                        "Estructura metálica para trabajo en altura.",
                        "https://loremflickr.com/320/240/scaffolding"
                ));

        Herramienta guadaña = herramientaRepository
                .findByNombreIgnoreCase("Guadañadora")
                .orElseGet(() -> herramienta(
                        "Guadañadora", Categoria.JARDIN,
                        35000, 15000, 2, true,
                        "Guadaña a gasolina de eje recto.",
                        "https://loremflickr.com/320/240/lawnmower"
                ));

        Herramienta laser = herramientaRepository
                .findByNombreIgnoreCase("Nivel láser")
                .orElseGet(() -> herramienta(
                        "Nivel láser", Categoria.MEDICION,
                        30000, 20000, 1, false,
                        "Nivel láser autonivelante de líneas cruzadas.",
                        "https://loremflickr.com/320/240/laser,level"
                ));


        // ---------- Alquiler de prueba: Jorge Pinzón ----------

        boolean alquilerExiste =
                alquilerRepository.existsByClienteIdAndHerramientaIdAndEstado(
                        jorge.getId(),
                        pulidora.getId(),
                        EstadoAlquiler.ENTREGADO
                );

        if (!alquilerExiste) {
            LocalDate hoy = LocalDate.now();

            Alquiler alquilerJorge = new Alquiler();
            alquilerJorge.setHerramienta(pulidora);
            alquilerJorge.setCliente(jorge);
            alquilerJorge.setFechaInicio(hoy.minusDays(10));
            alquilerJorge.setFechaFin(hoy.minusDays(3));
            alquilerJorge.setDias(8);
            alquilerJorge.setValorTotal(176000);
            alquilerJorge.setDeposito(8000);
            alquilerJorge.setEstado(EstadoAlquiler.ENTREGADO);
            alquilerJorge.setCreadoEn(LocalDateTime.now());

            alquilerRepository.save(alquilerJorge);
        }
    }

    private Usuario usuario(
            String nombre,
            String email,
            Rol rol,
            boolean activo) {

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(CLAVE_DEMO));
        usuario.setRol(rol);
        usuario.setActivo(activo);

        return usuarioRepository.save(usuario);
    }

    private Herramienta herramienta(
            String nombre,
            Categoria categoria,
            int precioDia,
            int deposito,
            int unidades,
            boolean activo,
            String descripcion,
            String imagenUrl) {

        Herramienta herramienta = new Herramienta();
        herramienta.setNombre(nombre);
        herramienta.setCategoria(categoria);
        herramienta.setPrecioDia(precioDia);
        herramienta.setDeposito(deposito);
        herramienta.setUnidades(unidades);
        herramienta.setActiva(activo);
        herramienta.setDescripcion(descripcion);
        herramienta.setImagenUrl(imagenUrl);

        return herramientaRepository.save(herramienta);
    }
}
