package br.com.improving.carrinho;

public class ProdutoInvalidoException extends RuntimeException{

	public ProdutoInvalidoException(String message) {
		super(message);
	}

}
