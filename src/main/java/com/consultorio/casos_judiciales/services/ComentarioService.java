package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.ComentariosRequest;
import com.consultorio.casos_judiciales.models.Casos;
import com.consultorio.casos_judiciales.models.Comentarios;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.ComentariosRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CasosService casosService;

    public Comentarios generateComentario(ComentariosRequest request, String token, int id) throws BadRequestException {

        String abogado_id = jwtService.getIDFromToken(token);
        Optional<Usuarios> usuarios = usuarioService.findUSerByIDActive(abogado_id);

        Optional<Casos>casos = casosService.findCasosById(id);

        if (usuarios.isEmpty()){
            throw new BadRequestException("User with id: '" +abogado_id+"' not found");
        }

        if (casos.isEmpty()){
            throw new BadRequestException("Case with id: '" +casos+"' not found");
        }

        Comentarios comentarios = Comentarios.builder()
                .comentario(request.getComentario())
                .caso(casos.get())
                .createdAt(new Date())
                .usuario(usuarios.get())
                .build();
        return comentariosRepository.save(comentarios);
    }
}
