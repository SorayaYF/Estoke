package com.estoke.inventario.exception.handler;

import com.estoke.inventario.exception.BusinessException;
import com.estoke.inventario.exception.ConverterException;
import com.estoke.inventario.exception.ErroDaApi;
import com.estoke.inventario.exception.IntegracaoException;
import com.estoke.inventario.exception.RegistroNaoEncontradoException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class HandlerErrorDefault {

	private final ErrorConverter errorConverter;

	public HandlerErrorDefault(ErrorConverter errorConverter) {
		this.errorConverter = errorConverter;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Map<String, Object> handle(){
		return errorConverter.criarMapDeErro(ErroDaApi.BODY_INVALIDO,
				"O corpo (body) da requisição possui erros ou não existe");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDefinitionException.class)
	public Map<String, Object> handle(InvalidDefinitionException ide){
		String atributo = ide.getPath().getLast().getFieldName();
		String msgDeErro = "O atributo '" + atributo + "' possui formato inválido";
	    return errorConverter.criarMapDeErro(ErroDaApi.FORMATO_INVALIDO, msgDeErro);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handle(ConstraintViolationException cve) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<Map<String, Object>> listaErros = new ArrayList<>();
        body.put("erros", listaErros);

        cve.getConstraintViolations().forEach(error -> {
            Map<String, Object> detalhe = new LinkedHashMap<>();
            String[] paths = error.getPropertyPath().toString().split("\\.");
            String atributo = paths[paths.length - 1];
            String errorMessage = error.getMessage();

            String mensagemCompleta = "O atributo '" + atributo
                    + "' apresentou o seguinte erro: '" + errorMessage + "'";

            detalhe.put("codigo", ErroDaApi.CONDICAO_VIOLADA.getCodigo());
            detalhe.put("mensagem", mensagemCompleta);
            listaErros.add(detalhe);
        });

        return body;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public Map<String, Object> handle(IllegalArgumentException ie){
		return errorConverter.criarMapDeErro(ErroDaApi.PARAMETRO_INVALIDO, ie.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NullPointerException.class)
	public Map<String, Object> handle(NullPointerException npe){
		return errorConverter.criarMapDeErro(ErroDaApi.PARAMETRO_INVALIDO, npe.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public Map<String, Object> handle(BusinessException be){
		return errorConverter.criarMapDeErro(ErroDaApi.REGRA_VIOLADA, be.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingPathVariableException.class)
	public Map<String, Object> handle(MissingPathVariableException mpve){
		return errorConverter.criarMapDeErro(ErroDaApi.PRECONDICAO_REQUERIDA, mpve.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Map<String, Object> handle(MethodArgumentTypeMismatchException matme) {
		String parameterName = matme.getName();
		Class<?> requiredType = matme.getRequiredType();

		String errorMessage;
		if (requiredType != null && requiredType.isEnum()) {
			String validValues = Arrays.stream(requiredType.getEnumConstants())
					.map(Object::toString)
					.collect(Collectors.joining(", "));
			errorMessage = String.format(
					"Valor inválido para o parâmetro '%s'. Valores permitidos: %s",
					parameterName,
					validValues
			);
		} else {
			errorMessage = String.format(
					"Tipo inválido para o parâmetro '%s'. Esperado: %s",
					parameterName,
					(requiredType != null ? requiredType.getSimpleName() : "Tipo desconhecido")
			);
		}

		return errorConverter.criarMapDeErro(
				ErroDaApi.TIPO_PARAMETRO_INVALIDO,
				errorMessage
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Map<String, Object> handle(HttpRequestMethodNotSupportedException hrmnse){
		return errorConverter.criarMapDeErro(ErroDaApi.METODO_HTTP_NAO_SUPORTADO, hrmnse.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Map<String, Object> handle(MissingServletRequestParameterException mrpe){
		return errorConverter.criarMapDeErro(ErroDaApi.PARAMETRO_OBRIGATORIO, mrpe.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RegistroNaoEncontradoException.class)
	public Map<String, Object> handle(RegistroNaoEncontradoException rnee){
		return errorConverter.criarMapDeErro(ErroDaApi.REGISTRO_NAO_ENCONTRADO, rnee.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConverterException.class)
	public Map<String, Object> handle(ConverterException ce){
		return errorConverter.criarMapDeErro(ErroDaApi.CONVERSAO_INVALIDA, ce.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IntegracaoException.class)
	public Map<String, Object> handle(IntegracaoException ie){
		return errorConverter.criarMapDeErro(ErroDaApi.INTEGRACAO_INVALIDA, ie.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public Map<String, Object> handlePSQLExceptions(
			DataIntegrityViolationException dve){
	    return errorConverter.criarMapDeErro(ErroDaApi.PARAMETRO_INVALIDO,
	    		"Ocorreu um erro de integridade referencial na base de dados");
	}
}
