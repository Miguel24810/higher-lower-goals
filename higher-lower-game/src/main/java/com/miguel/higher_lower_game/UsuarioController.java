package com.miguel.higher_lower_game;

import org.springframework.web.bind.annotation.*;


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
    public Usuario actualizarRecord(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Integer> datos) {
        String token = authHeader.replace("Bearer ", "");
        String nombreUsuario = jwtUtil.extraerNombreUsuario(token);

        Usuario usuario = usuarioRepository.findByNombre(nombreUsuario);

        int nuevaRacha = datos.get("racha");

        if(nuevaRacha>usuario.getRecord()){
            usuario.setRecord(nuevaRacha);
            usuarioRepository.save(usuario);
        }

    return usuario;
}
    
}
