package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe responsável pela criação e recuperação dos carrinhos de compras.
 */
public class CarrinhoComprasFactory {

	void verificarCarrinhosExpirados() {

		Thread t = new Thread(() -> {
			while (true) {
				try {
					lista.removeIf(CarrinhoCompras::verificarSessao);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

	/**
	 * Retorna a lista de carrinhos.
	 * <p>
	 * return List<CarrinhoCompras>
	 */
	private List<CarrinhoCompras> lista;


	public CarrinhoComprasFactory() {
		this.lista = new ArrayList<>();
		verificarCarrinhosExpirados();
	}

	/**
	 * Cria e retorna um novo carrinho de compras para o cliente passado como parâmetro.
	 * <p>
	 * Caso já exista um carrinho de compras para o cliente passado como parâmetro, este carrinho deverá ser retornado.
	 *
	 * @param identificacaoCliente
	 * @return CarrinhoCompras
	 */
	public CarrinhoCompras criar(String identificacaoCliente) {
		final Optional<CarrinhoCompras> buscaCarrinho = buscaCarrinho(identificacaoCliente);
		if (buscaCarrinho.isPresent()) {
			return buscaCarrinho.get();
		} else {
			final CarrinhoCompras cliente = new CarrinhoCompras(identificacaoCliente);
			lista.add(cliente);
			return cliente;
		}
	}


	/**
	 * Retorna o valor do ticket médio no momento da chamada ao método.
	 * O valor do ticket médio é a soma do valor total de todos os carrinhos de compra dividido
	 * pela quantidade de carrinhos de compra.
	 * O valor retornado deverá ser arredondado com duas casas decimais, seguindo a regra:
	 * 0-4 deve ser arredondado para baixo e 5-9 deve ser arredondado para cima.
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTicketMedio() {
		BigDecimal valorTotal = getValorTotal();
		int quantidadeTotal = getQuantidadeTotal();
		return valorTotal.divide(new BigDecimal(quantidadeTotal).round(MathContext.DECIMAL32), 2, RoundingMode.HALF_UP);
	}

	public int getQuantidadeTotal() {
		return lista.stream().mapToInt(CarrinhoCompras::getQuantidadeTotal).reduce(0, Integer::sum);
	}

	public BigDecimal getValorTotal() {
		return lista.stream()
				.map(CarrinhoCompras::getValorTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * Retorna um boleando caso o cliente exista.
	 *
	 * @return boolean
	 */
	public boolean checkout(String identificacaoCliente) {
		Optional<CarrinhoCompras> find = lista.stream().filter(e -> e.getCliente().equals(identificacaoCliente)).findFirst();
		if (find.isPresent()) {
			CarrinhoCompras carrinhoCompras = find.get();
			System.out.println("Checkout efetuado com sucesso.");
			System.out.println(
					"Cliente: " + carrinhoCompras.getCliente() + "\n" +
							"Quantidade de itens: " + carrinhoCompras.getQuantidadeTotal() + "\n" +
							"Valor total: " + carrinhoCompras.getValorTotal() + "\n");
			invalidar(identificacaoCliente);
			return true;
		}
		System.out.println("O cliente não existe!");
		return false;
	}

	/**
	 * Invalida um carrinho de compras quando o cliente faz um checkout ou sua sessão expirar.
	 * Deve ser efetuada a remoção do carrinho do cliente passado como parâmetro da listagem de carrinhos de compras.
	 *
	 * @param identificacaoCliente
	 * @return Retorna um boolean, tendo o valor true caso o cliente passado como parämetro tenha um carrinho de compras e
	 * e false caso o cliente não possua um carrinho.
	 */
	public boolean invalidar(String identificacaoCliente) {
		final Optional<CarrinhoCompras> buscarCarrinho = buscaCarrinho(identificacaoCliente);

		if (buscarCarrinho.isPresent()) {
			CarrinhoCompras carrinho = buscarCarrinho.get();
			lista.remove(carrinho);
			return true;
		}
		return false;
	}

	/**
	 * Busca um carrinho de compras pelo nome do cliente.
	 *
	 * @param identificacaoCliente
	 * @return Optional<CarrinhoCompras>
	 */
	public Optional<CarrinhoCompras> buscaCarrinho(String identificacaoCliente) {
		return lista
				.stream()
				.filter(e -> e.getCliente().equals(identificacaoCliente))
				.findFirst();
	}

	/**
	 * Lista de todos os carrinhos disponiveis
	 *
	 * @return List<CarrinhoCompras>
	 */
	public List<CarrinhoCompras> getLista() {
		return lista;
	}
}
