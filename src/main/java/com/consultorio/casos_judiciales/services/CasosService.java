package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.CasosRequest;
import com.consultorio.casos_judiciales.models.Caso;
import com.consultorio.casos_judiciales.models.Usuario;
import com.consultorio.casos_judiciales.repositories.CasosRepository;
import com.consultorio.casos_judiciales.utils.EstadoCasos;
import com.consultorio.casos_judiciales.utils.Status;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CasosService {

    @Autowired
    private CasosRepository casosRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    public Caso createCase(CasosRequest request, String token) throws BadRequestException {
        String abogado_id = jwtService.getIDFromToken(token);
        Optional<Usuario> client = usuarioService.findUSerByIDActive(request.getCliente_id());
        Optional<Usuario> abogado = usuarioService.findUSerByIDActive(abogado_id);


        if (!usuarioService.isUserALawyer(abogado_id) ) {
            throw new BadRequestException("The user isn't a lawyer");
        } else if (!usuarioService.isUserAClient(request.getCliente_id())) {
            throw new BadRequestException("The user isn't a client");
        }

        Caso caso = Caso.builder()
                .nombreCaso(request.getNombreCaso().toLowerCase())
                .abogado(abogado.get())
                .cliente(client.get())
                .estado(EstadoCasos.ABIERTO)
                .descripcion(request.getDescripcion().toLowerCase())
                .estado(EstadoCasos.ABIERTO)
                .build();
        return casosRepository.save(caso);
    }
    public Optional<Caso> findCasosById(int id){
        return casosRepository.findCasoByIdAndStatus(id, Status.ACTIVE);
    }
}
