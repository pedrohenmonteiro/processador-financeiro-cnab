package br.com.pedromonteiro.backend;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public record Transacao(
    Long id,
    Date data,
    BigDecimal valor,
    Long cpf,
    String cartao,
    Time hora,
    String donoDaLoja,
    String nomeDaLoja) {
} 
