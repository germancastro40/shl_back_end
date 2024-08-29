package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.CasosRequest;
import com.consultorio.casos_judiciales.models.Casos;
import com.consultorio.casos_judiciales.models.Comentarios;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.CasosRepository;
import com.consultorio.casos_judiciales.enums.EstadoCasos;
import com.consultorio.casos_judiciales.enums.Status;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CasosService {

    @Autowired
    private CasosRepository casosRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    public List<Casos>getAllCases(){
        List<Casos> allCases = casosRepository.findAll();
        allCases.removeIf( c -> c.getStatus() != Status.ACTIVE );
        return allCases;
    }

    public Casos createCase(CasosRequest request, String token) throws BadRequestException {
        String abogadoId = jwtService.getIDFromToken(token);
        Usuarios abogado = usuarioService.findUSerById(abogadoId)
                .orElseThrow(() -> new BadRequestException("Lawyer with id: '" + abogadoId + "' not found"));

        Usuarios client = usuarioService.findUSerById(request.getCliente_id())
                .orElseThrow(() -> new BadRequestException("Client with id: '" + request.getCliente_id() + "' not found"));

        if (!usuarioService.isUserALawyerOrAdmin(abogadoId)) {
            throw new BadRequestException("The user isn't a lawyer or an admin");
        }

        if (!usuarioService.isUserAClient(request.getCliente_id())) {
            throw new BadRequestException("The user isn't a client");
        }

        Casos casos = Casos.builder()
                .nombreCaso(request.getNombreCaso().toLowerCase())
                .abogado(abogado)
                .cliente(client)
                .estado(EstadoCasos.ABIERTO)
                .descripcion(request.getDescripcion().toLowerCase())
                .status(Status.ACTIVE)
                .build();

        return casosRepository.save(casos);
    }

    public Optional<Casos> findCasosById(String id){
        Casos casos = casosRepository.findCasoByIdAndStatus(id, Status.ACTIVE).orElseThrow();
        List<Comentarios> comentariosOrdenados = casos.getComentarios().stream()
                .sorted(Comparator.comparing(Comentarios::getCreatedAt).reversed())
                .collect(Collectors.toList());
        casos.setComentarios(comentariosOrdenados);
        return Optional.of(casos);
    }
}
