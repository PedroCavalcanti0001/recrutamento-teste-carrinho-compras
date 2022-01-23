package br.com.improving.carrinho;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe responsável por carregar a interface de usuário.
 */
public class CarrinhoUI {

	/**
	 * Implementa a interface de usuário
	 * <p>
	 * return void
	 */
	public static void abrirMenu(CarrinhoComprasFactory factory) throws IOException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("----- BEM VINDO AO MENU DE COMPRAS-----");
			System.out.println("");
			System.out.println("Digite o numero da opção que deseja:");
			System.out.println("1) Listar todos os carrinhos disponiveis.");
			System.out.println("2) Adiciona um novo carrinho.");
			System.out.println("3) Efetua o checkout de um carrinho pelo nome do cliente.");
			System.out.println("4) Ver o ticket médio.");
			System.out.println("5) Ver produtos de um carrinho pelo cliente.");
			System.out.println("6) Adiciona um item ao carrinho de um cliente\n");
			while (scanner.hasNext()) {
				final String regex = "-?(0|[1-9]\\d*)";
				final String str = scanner.next();
				if (str.matches(regex) && (Integer.parseInt(str) >= 1 && Integer.parseInt(str) <= 6)) {
					int op = Integer.parseInt(str);
					switch (op) {
						case 1:
							if (!factory.getLista().isEmpty()) {
								factory.getLista().stream().map(e ->
										"Cliente: " + e.getCliente() + "\n" +
												"Quantidade de itens: " + e.getQuantidadeTotal() + "\n" +
												"Valor total: " + e.getValorTotal() + "\n" +
												"------------------------- \n"
								).forEach(System.out::println);
							} else {
								System.out.println("\n* ERRO: A lista de carrinhos está vazia.\n");
							}
							System.out.println("Pressione ENTER para prosseguir!");
							System.in.read();
							break;
						case 2:
							while (true) {
								System.out.println("Qual o nome do cliente?");
								Scanner scanner2 = new Scanner(System.in);
								String cliente = scanner2.next();

								if (cliente.isEmpty()) {
									System.out.println("Por favor, digite um nome de cliente válido ou digite SAIR para sair!\n");
								} else if (cliente.equals("SAIR")) {
									System.out.println("Ação cancelada com sucesso.");
									break;
								} else {
									factory.criar(cliente);
									System.out.println("Carrinho de " + cliente + " criado com sucesso.\n");
									break;
								}
							}
							System.out.println("Pressione ENTER para prosseguir!");
							System.in.read();
							break;
						case 3:
							if (factory.getLista().isEmpty()) {
								System.out.println("Nossa base de clientes ainda está vazia. " +
										"Por favor adicione um novo cliente.\n");
							} else {
								while (true) {
									System.out.println("Qual o nome do cliente?");
									Scanner scanner2 = new Scanner(System.in);
									String cliente = scanner2.next();
									if (cliente.isEmpty()) {
										System.out.println("Por favor, digite um nome de cliente válido ou digite SAIR para sair!\n");
									} else if (cliente.equals("SAIR")) {
										System.out.println("Ação cancelada com sucesso.");
										break;
									} else {
										final Optional<CarrinhoCompras> find = factory.buscaCarrinho(cliente);
										if (find.isPresent()) {
											factory.checkout(cliente);
											break;
										} else {
											System.out.println("Cliente não encontrado. Por favor digite o nome de um cliente válido!");
										}
									}
								}
							}
							System.out.println("Pressione ENTER para prosseguir!");
							System.in.read();
							break;
						case 4:
							if (factory.getLista().isEmpty()) {
								System.out.println("Nossa base de clientes ainda está vazia. " +
										"Por favor adicione um novo cliente.\n");
							} else {
								if (factory.getQuantidadeTotal() > 0) {
									System.out.println("O ticket médio é " + factory.getValorTicketMedio() + ".");
								} else {
									System.out.println("Não há nenhum carrinho com itens para calcular ticket médio.");
								}
							}
							System.out.println("Pressione ENTER para prosseguir!");
							System.in.read();
							break;
						case 5:
							if (factory.getLista().isEmpty()) {
								System.out.println("Nossa base de clientes ainda está vazia. " +
										"Por favor adicione um novo cliente.\n");
							} else {
								while (true) {
									System.out.println("Qual o nome do cliente?");
									Scanner scanner2 = new Scanner(System.in);
									String cliente = scanner2.next();
									if (cliente.isEmpty()) {
										System.out.println("Por favor, digite um nome de cliente válido ou digite SAIR para sair!\n");
									} else if (cliente.equals("SAIR")) {
										System.out.println("Ação cancelada com sucesso.");
										break;
									} else {
										final Optional<CarrinhoCompras> find = factory.buscaCarrinho(cliente);
										if (find.isPresent()) {
											if (!find.get().getItens().isEmpty()) {
												for (final Item item : find.get().getItens()) {
													System.out.println("Produto: " + item.getProduto().getDescricao());
													System.out.println("Valor unitário: " + item.getValorUnitario());
													System.out.println("Quantidade: " + item.getQuantidade() + "\n");
												}
												break;
											} else {
												System.out.println("Esse cliente ainda não tem nenhum produto no carrinho.");
											}
										} else {
											System.out.println("Cliente não encontrado. Por favor digite o nome de um cliente válido!");
										}
									}
								}
							}
							System.out.println("Pressione ENTER para prosseguir!");
							System.in.read();
							break;
						case 6:
							if (factory.getLista().isEmpty()) {
								System.out.println("Nossa base de clientes ainda está vazia. " +
										"Por favor adicione um novo cliente.\n");
							} else {
								while (true) {
									System.out.println("Qual o nome do cliente?");
									Scanner scanner2 = new Scanner(System.in);
									String cliente = scanner2.next();
									if (cliente.isEmpty()) {
										System.out.println("Por favor, digite um nome de cliente válido ou digite SAIR para sair!\n");
									} else if (cliente.equals("SAIR")) {
										System.out.println("Ação cancelada com sucesso.");
										break;
									} else {
										final Optional<CarrinhoCompras> find = factory.buscaCarrinho(cliente);
										if (find.isPresent()) {
											final CarrinhoCompras carrinhoCompras = find.get();
											System.out.println("Digite o código do produto:");
											long id = Long.parseLong(scanner.next());
											System.out.println("Digite a quantidade:");
											int quantidade = Integer.parseInt(scanner.next());
											System.out.println("Digite o valor do produto:");
											BigDecimal valor = BigDecimal.valueOf(Long.parseLong(scanner.next()));
											carrinhoCompras.adicionarItem(
													new Produto(
															id,
															"Produto teste"
													),
													valor,
													quantidade
											);
											System.out.println("Produto adicionado com sucesso.");
											break;
										} else {
											System.out.println("Cliente não encontrado. Por favor digite o nome de um cliente válido!");
										}
									}
								}
							}
							System.out.println("Pressione ENTER para prosseguir!");
							System.in.read();
							break;
					}

				} else {
					System.out.println("\n* ERRO: Por favor digite uma opção válida.\n");
					System.out.println("Pressione ENTER para prosseguir!");
					System.in.read();
					break;
				}
				break;

			}
		}
	}
}
