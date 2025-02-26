package com.estoke.inventario.exception;

public enum ErroDaApi {
	
	BODY_INVALIDO("001"),
	FORMATO_INVALIDO("002"),
	CONDICAO_VIOLADA("003"),
	PARAMETRO_INVALIDO("004"),
	REGRA_VIOLADA("005"),
	PRECONDICAO_REQUERIDA("006"),
	TIPO_PARAMETRO_INVALIDO("007"),
	METODO_HTTP_NAO_SUPORTADO("008"),
	PARAMETRO_OBRIGATORIO("009"),
	REGISTRO_NAO_ENCONTRADO("010"),
	CONVERSAO_INVALIDA("011"),
	INTEGRACAO_INVALIDA("012");
	
	private String codigo;
	
	private ErroDaApi(String codigo){
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
}
