package com.pontointeligente.ponto_inteligente;

import com.pontointeligente.ponto_inteligente.dto.VagaEventoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/spots")
public class SpotsStatusController {

    private final JdbcTemplate jdbcTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public SpotsStatusController(JdbcTemplate jdbcTemplate, SimpMessagingTemplate messagingTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @PatchMapping("/{id}/status")
    public Map<String, Object> atualizarStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> dados) {

        String status = dados.get("status");

        if (status == null ||
                !status.matches("LIVRE|OCUPADA|INDISPONIVEL")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status inválido. Use LIVRE, OCUPADA ou INDISPONIVEL."
            );
        }

        int linhas = jdbcTemplate.update(
                "UPDATE pontos SET status = ? WHERE id = ?",
                status, id
        );

        if (linhas == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Baia não encontrada."
            );
        }

        // Dispara o evento WebSocket para todos os clientes conectados
        VagaEventoDTO evento = new VagaEventoDTO((long) id, status, null, null);
        messagingTemplate.convertAndSend("/topic/vagas", evento);

        return jdbcTemplate.queryForMap(
                "SELECT * FROM pontos WHERE id = ?", id
        );
    }
}