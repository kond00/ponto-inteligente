package com.pontointeligente.ponto_inteligente.dto;

import java.time.LocalDateTime;

public class VagaEventoDTO {
    private Long vagaId;
    private String status;
    private Long veiculoId;
    private String placaVeiculo;
    private LocalDateTime timestamp;

    public VagaEventoDTO(Long vagaId, String status, Long veiculoId, String placaVeiculo) {
        this.vagaId = vagaId;
        this.status = status;
        this.veiculoId = veiculoId;
        this.placaVeiculo = placaVeiculo;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getVagaId() { return vagaId; }
    public String getStatus() { return status; }
    public Long getVeiculoId() { return veiculoId; }
    public String getPlacaVeiculo() { return placaVeiculo; }
    public LocalDateTime getTimestamp() { return timestamp; }
}