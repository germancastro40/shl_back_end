package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.ComentariosRequest;
import com.consultorio.casos_judiciales.models.Casos;
import com.consultorio.casos_judiciales.models.Comentarios;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.ComentariosRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComentarioService {

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CasosService casosService;

    public Comentarios generateComentario(ComentariosRequest request, String token, String caseId) throws BadRequestException {
        String userId = jwtService.getIDFromToken(token);
        Usuarios user = usuarioService.findUSerById(userId)
                .orElseThrow(() -> new BadRequestException("User with id: '" + userId + "' not found"));

        Casos caseEntity = casosService.findCasosById(caseId)
                .orElseThrow(() -> new BadRequestException("Case with id: '" + caseId + "' not found"));

        if (!usuarioService.isUserALawyerOrAdmin(userId)) {
            throw new BadRequestException("User with id: '" + userId + "' does not have the necessary permissions");
        }

        Comentarios comentario = Comentarios.builder()
                .comentario(request.getComentario().toLowerCase())
                .caso(caseEntity)
                .createdAt(new Date())
                .usuario(user)
                .build();
        return comentariosRepository.save(comentario);
    }

}
