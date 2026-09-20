package com.miguel.higher_lower_game;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Usuario registrar(@RequestBody Usuario usuario) {
        String passwordPlano = usuario.getPassword();
        String passwordCifrado = passwordEncoder.encode(passwordPlano);
        usuario.setPassword(passwordCifrado);
        return usuarioRepository.save(usuario);

    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario datosLogin) {
        Usuario usuario= usuarioRepository.findByNombre(datosLogin.getNombre());

        if(usuario== null){
            throw new UsuarioNoEncontradoException("Usuario no encontrado");

        }
        boolean passwordCorrecta = passwordEncoder.matches(datosLogin.getPassword(), usuario.getPassword());

        if(!passwordCorrecta){
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        }   

        return jwtUtil.generarToken(usuario.getNombre());
    }
    @PostMapping("/record")
    public Usuario actualizarRecord(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> datos) {
        String token = authHeader.replace("Bearer ", "");
        String nombreUsuario = jwtUtil.extraerNombreUsuario(token);

        Usuario usuario = usuarioRepository.findByNombre(nombreUsuario);

        int nuevaRacha = ((Number) datos.get("racha")).intValue();
        String categoria = (String) datos.get("categoria");

        if ("golesCarrera".equals(categoria) && nuevaRacha > usuario.getRecordCarrera()) {
            usuario.setRecordCarrera(nuevaRacha);
        } else if ("golesSeleccion".equals(categoria) && nuevaRacha > usuario.getRecordSeleccion()) {
            usuario.setRecordSeleccion(nuevaRacha);
        } else if ("golesTemporada".equals(categoria) && nuevaRacha > usuario.getRecordTemporada()) {
            usuario.setRecordTemporada(nuevaRacha);
        }
        usuarioRepository.save(usuario);
    return usuario;
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
    
}
