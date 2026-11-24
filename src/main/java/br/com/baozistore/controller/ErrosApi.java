package br.com.baozistore.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrosApi {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail camposInvalidos(MethodArgumentNotValidException erro) {
        ProblemDetail resposta = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Revise os campos informados.");
        resposta.setProperty("erros", erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .toList());
        return resposta;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail jsonInvalido() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "JSON inválido. Confira a sintaxe, os tipos e as datas no formato AAAA-MM-DD.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail idInvalido() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "O ID na URL deve ser um número inteiro.");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail recursoInexistente(ResponseStatusException erro) {
        return erro.getBody();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflito() {
        // O banco impede a exclusão de registros referenciados por pedidos.
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "O vínculo entre cliente, produto e pedido impede a operação. Verifique se os registros ainda existem ou exclua primeiro o pedido vinculado.");
    }
}
