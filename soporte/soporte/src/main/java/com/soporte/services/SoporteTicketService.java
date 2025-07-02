package com.soporte.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soporte.dto.SoporteTicketDTO;
import com.soporte.models.SoporteTicket;
import com.soporte.repository.SoporteTicketRepository;

@Service
public class SoporteTicketService {

    @Autowired
    private SoporteTicketRepository repository;

    public SoporteTicketDTO guardar(SoporteTicketDTO dto) {
        SoporteTicket ticket = convertirAEntidad(dto);
        SoporteTicket guardado = repository.save(ticket);
        return convertirADTO(guardado);
    }

    public List<SoporteTicketDTO> listar() {
        return repository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<SoporteTicketDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(this::convertirADTO);
    }

    public Optional<SoporteTicketDTO> actualizar(Integer id, SoporteTicketDTO dto) {
        return repository.findById(id)
                .map(ticket -> {
                    ticket.setIdUsuario(dto.getIdUsuario());
                    ticket.setTipoTicket(dto.getTipoTicket());
                    ticket.setDescripcion(dto.getDescripcion());
                    ticket.setEstado(dto.getEstado());
                    ticket.setFechaCreacion(dto.getFechaCreacion());
                    ticket.setFechaResolucion(dto.getFechaResolucion());
                    return convertirADTO(repository.save(ticket));
                });
    }

    public boolean eliminar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private SoporteTicket convertirAEntidad(SoporteTicketDTO dto) {
        SoporteTicket ticket = new SoporteTicket();
        ticket.setIdTicket(dto.getIdTicket());
        ticket.setIdUsuario(dto.getIdUsuario());
        ticket.setTipoTicket(dto.getTipoTicket());
        ticket.setDescripcion(dto.getDescripcion());
        ticket.setEstado(dto.getEstado());
        ticket.setFechaCreacion(dto.getFechaCreacion());
        ticket.setFechaResolucion(dto.getFechaResolucion());
        return ticket;
    }

    private SoporteTicketDTO convertirADTO(SoporteTicket ticket) {
        SoporteTicketDTO dto = new SoporteTicketDTO();
        dto.setIdTicket(ticket.getIdTicket());
        dto.setIdUsuario(ticket.getIdUsuario());
        dto.setTipoTicket(ticket.getTipoTicket());
        dto.setDescripcion(ticket.getDescripcion());
        dto.setEstado(ticket.getEstado());
        dto.setFechaCreacion(ticket.getFechaCreacion());
        dto.setFechaResolucion(ticket.getFechaResolucion());
        return dto;
    }
} 