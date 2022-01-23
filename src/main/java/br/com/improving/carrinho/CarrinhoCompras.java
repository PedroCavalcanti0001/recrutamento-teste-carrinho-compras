package br.com.improving.carrinho;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Classe que representa o carrinho de compras de um cliente.
 */
public class CarrinhoCompras {

	/**
	 * Identificação do cliente que é dono do carrinho.
	 */
	private final String cliente;

	/**
	 * Armazena os itens do carrinho.
	 */
	private final List<Item> itens = new ArrayList<>();

	/**
	 * Armazena o horario de criação do carrinho.
	 */
	private final Long createdAt;


	/**
	 * Construtor da classe CarrinhoCompras.
	 *
	 * @param cliente
	 */
	public CarrinhoCompras(String cliente) {
		this.cliente = cliente;
		this.createdAt = System.currentTimeMillis();
	}

	/**
	 * Permite a adição de um novo item no carrinho de compras.
	 * <p>
	 * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
	 * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro.
	 * - Se o valor unitário informado for diferente do valor unitário atual do item, o novo valor unitário do item deverá ser
	 * o passado como parâmetro.
	 * <p>
	 * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras.
	 *
	 * @param produto
	 * @param valorUnitario
	 * @param quantidade
	 */
	public void adicionarItem(Produto produto, BigDecimal valorUnitario, int quantidade) {
		Optional<Item> filter = itens.stream()
				.filter(e -> e.getProduto().getCodigo().equals(produto.getCodigo())).findFirst();
		if (filter.isPresent()) {
			final Item produtoCarrinho = filter.get();
			produtoCarrinho.setQuantidade(produtoCarrinho.getQuantidade() + quantidade);
			if (!produtoCarrinho.getValorUnitario().equals(valorUnitario)) {
				produtoCarrinho.setValorUnitario(valorUnitario);
			}
		} else {
			if (valorUnitario.compareTo(BigDecimal.ZERO) <= 0) {
				throw new ProdutoInvalidoException("O valor do produto deve ser maior que 0.0");
			}
			if (quantidade <= 0) {
				throw new ProdutoInvalidoException("A quantidade do produto deve ser superior a 0");
			}
			itens.add(new Item(produto, valorUnitario, quantidade));
		}



	}

	/**
	 * Permite a remoção do item que representa este produto do carrinho de compras.
	 *
	 * @param produto
	 * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
	 * caso o produto não exista no carrinho.
	 */
	public boolean removerItem(Produto produto) {
		return itens.removeIf(e -> e.getProduto().equals(produto));
	}

	/**
	 * Permite a remoção do item de acordo com a posição.
	 * Essa posição deve ser determinada pela ordem de inclusão do produto na
	 * coleção, em que zero representa o primeiro item.
	 *
	 * @param posicaoItem
	 * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
	 * caso o produto não exista no carrinho.
	 */
	public boolean removerItem(int posicaoItem) {
		return itens.remove(posicaoItem) != null;
	}

	/**
	 * Retorna o valor total do carrinho de compras, que deve ser a soma dos valores totais
	 * de todos os itens que compõem o carrinho.
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotal() {
		return itens.stream().map(
				e -> e.getValorUnitario().multiply(BigDecimal.valueOf(e.getQuantidade()))
		).reduce(new BigDecimal(0), BigDecimal::add)
				.round(MathContext.DECIMAL32);
	}

	/**
	 * Retorna a soma de todos os produtos no carrinho do carrinho.
	 *
	 * @return int
	 */
	public int getQuantidadeTotal() {
		return itens.stream().map(
				Item::getQuantidade
		).reduce(0, Integer::sum);
	}


	/**
	 * Retorna a lista de itens do carrinho de compras.
	 *
	 * @return Collection<Item>
	 */
	public Collection<Item> getItens() {
		return itens;
	}

	/**
	 * Retorna o nome do cliente do carrinho.
	 *
	 * @return itens
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * Retorna o timestamp de criação do carrinho.
	 *
	 * @return Long
	 */
	public Long getCreatedAt() {
		return createdAt;
	}

	/**
	 * Retorna um valor boleado onde true significa que o carrinho está expirado.
	 *
	 * @return boolean
	 */
	public boolean verificarSessao() {
		Long now = System.currentTimeMillis();
		long diff = now - getCreatedAt();
		long minutes = diff / (60 * 1000);
		return minutes >= 15;
	}

	@Override
	public String toString() {
		return "CarrinhoCompras{" +
				"cliente='" + cliente + '\'' +
				", itens=" + itens +
				", createdAt=" + createdAt +
				'}';
	}
}