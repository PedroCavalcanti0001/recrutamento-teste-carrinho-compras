package br.com.improving.carrinho;

import java.io.IOException;
import java.math.BigDecimal;

public class Main {

	public static void main(String[] args) {
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
		try {
			CarrinhoUI.abrirMenu(factory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
