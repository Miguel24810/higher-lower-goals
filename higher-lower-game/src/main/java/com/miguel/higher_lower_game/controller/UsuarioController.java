package com.miguel.higher_lower_game.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import com.miguel.higher_lower_game.dto.ActualizarRecordDTO;
import com.miguel.higher_lower_game.dto.UsuarioCredencialesDTO;
import com.miguel.higher_lower_game.dto.UsuarioRankingDTO;
import com.miguel.higher_lower_game.exception.CredencialesInvalidasException;
import com.miguel.higher_lower_game.exception.UsuarioNoEncontradoException;
import com.miguel.higher_lower_game.model.Usuario;
import com.miguel.higher_lower_game.repository.UsuarioRepository;
import com.miguel.higher_lower_game.security.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/registro")
    public UsuarioRankingDTO registrar(@Valid @RequestBody UsuarioCredencialesDTO datosRegistro) {
        if (usuarioRepository.findByNombre(datosRegistro.nombre()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(datosRegistro.nombre());
        usuario.setPassword(passwordEncoder.encode(datosRegistro.password()));
        return aDto(usuarioRepository.save(usuario));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioCredencialesDTO datosLogin) {
        Usuario usuario= usuarioRepository.findByNombre(datosLogin.nombre());

        if(usuario== null){
            throw new UsuarioNoEncontradoException("Usuario no encontrado");

        }
        boolean passwordCorrecta = passwordEncoder.matches(datosLogin.password(), usuario.getPassword());

        if(!passwordCorrecta){
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        }   

        return jwtUtil.generarToken(usuario.getNombre());
    }
    @PostMapping("/record")
    public UsuarioRankingDTO actualizarRecord(@RequestHeader("Authorization") String authHeader,
                                               @RequestBody ActualizarRecordDTO datos) {
        String token = authHeader.replace("Bearer ", "");
        String nombreUsuario = jwtUtil.extraerNombreUsuario(token);

        Usuario usuario = usuarioRepository.findByNombre(nombreUsuario);

        int nuevaRacha = datos.racha();
        String categoria = datos.categoria();

        if ("golesCarrera".equals(categoria) && nuevaRacha > usuario.getRecordCarrera()) {
            usuario.setRecordCarrera(nuevaRacha);
        } else if ("golesSeleccion".equals(categoria) && nuevaRacha > usuario.getRecordSeleccion()) {
            usuario.setRecordSeleccion(nuevaRacha);
        } else if ("golesTemporada".equals(categoria) && nuevaRacha > usuario.getRecordTemporada()) {
            usuario.setRecordTemporada(nuevaRacha);
        }
        usuarioRepository.save(usuario);
    return aDto(usuario);
}
    @GetMapping("/ranking")
    public List<UsuarioRankingDTO> ranking() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioRankingDTO> resultado = new ArrayList<>();

        for (Usuario u : usuarios) {
            UsuarioRankingDTO dto = new UsuarioRankingDTO(
                u.getNombre(),
                u.getRecordCarrera(),
                u.getRecordSeleccion(),
                u.getRecordTemporada()
            );
            resultado.add(dto);
    }

    return resultado;
}

    private UsuarioRankingDTO aDto(Usuario usuario) {
        return new UsuarioRankingDTO(usuario.getNombre(), usuario.getRecordCarrera(),
            usuario.getRecordSeleccion(), usuario.getRecordTemporada());
    }
    
}
