package br.com.compass;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import br.com.compass.controller.ClienteController;
import br.com.compass.controller.ContaController;
import br.com.compass.enuns.TipoConta;
import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
import br.com.compass.model.Transacao;

public class App {
    
    private static ClienteController clienteController;
    private static ContaController contaController;
    private static Conta contaLogada;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            clienteController = ClienteController.getInstance();
            contaController = ContaController.getInstance();
            mainMenu(scanner);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("Application closed");
        }
    }

    public static void mainMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Criar Conta          ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Informe sua opção: ");

            int option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    abrirConta(scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Escolha invalida! Tente novamente.");
            }
        }
    }

    public static void login(Scanner scanner) {
        System.out.println("===== Login =====");
        System.out.print("Nome de Usuário: ");
        String usuario = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        contaLogada = contaController.buscaPorUsuarioESenha(usuario, senha);
        if (contaLogada != null) {
            skipLines(3);
            System.out.println("Login successful!");
            System.out.println("Bem vindo, " + contaLogada.getCliente().getNome() + "   -   Conta: " + contaLogada.getTipoConta() + "   -   Número Conta: " + contaLogada.getIdConta());
            bankMenu(scanner);
        } else {
            System.out.println("Seu 'Nome de Usuário' ou 'Senha' estão invalidos! Tente novamente.");
            skipLines(3);
            mainMenu(scanner);
        }
    }

    public static void bankMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            skipLines(1);
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Depositar            ||");
            System.out.println("|| 2. Sacar                ||");
            System.out.println("|| 3. Verificar Saldo      ||");
            System.out.println("|| 4. Transferência        ||");
            System.out.println("|| 5. Extrado da conta     ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Informe sua escolha: ");

            int option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
                case 1:
                    System.out.print("Informe o valor a ser depositado: ");
                    Double valorDep = scanner.nextDouble();
                    contaController.depositar(contaLogada.getIdConta(), valorDep);
                    System.out.println("Deposito efetuado com sucesso!");
                    break;
                case 2:
                    System.out.print("Informe o valor que deseja sacar: ");
                    Double valorSaq = scanner.nextDouble();
                    contaController.sacar(contaLogada.getIdConta(), valorSaq);
                    System.out.println("Saque efetuado com sucesso");
                    break;
                case 3:
                    Double saldo = contaController.consultarSaldo(contaLogada.getIdConta());
                    System.out.println("Seu saldo atual é: " + saldo);
                    break;
                case 4:
                    System.out.print("Informe o número da conta de destino: ");
                    Long idContaDestino = scanner.nextLong();
                    System.out.print("Informe o valor que deseja transferir: ");
                    Double valorTransf = scanner.nextDouble();
                    contaController.transferir(contaLogada.getIdConta(), idContaDestino, valorTransf);
                    System.out.println("Tranferência efetuada com sucesso");
                    break;
                case 5:
                    List<Transacao> extrato = contaController.extratoBancario(contaLogada.getIdConta());
                    System.out.println("===== Extrato Bancário =====");
                    for (Transacao transacao : extrato) {
                        System.out.println("Data/Hora: " + transacao.getDataHora().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                        System.out.println("Tipo transação: " + transacao.getTipoTransacao());
                        System.out.println("Valor: " + transacao.getValor());
                        System.out.println("---------------------------");
                    }
                    break;
                case 0:
                    System.out.println("Saindo...");
                    running = false;
                    return;
                default:
                    System.out.println("Opção Invalida! Tente novamente.");
            }
        }
    }

    private static void abrirConta(Scanner scanner) {
        System.out.println("===== Abertura de conta =====");

        // Cliente
        Cliente cliente = new Cliente();
        System.out.print("Seu nome completo: ");
        cliente.setNome(validarEntrada(scanner, "Seu nome não pode ser vazio.", input -> input.matches("^[\\p{L} .'-]+$")));

        System.out.print("Informe sua data de nascimento no formato exemplificado (DD-MM-YYYY): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        cliente.setDataNascimento(validarData(scanner, "Formato da data invalido. Please use DD-MM-YYYY.", formatter));

        System.out.print("CPF or CNPJ: ");
        cliente.setCpf(validarEntrada(scanner, "Formato do CPF/CNPJ invalido. Inserir um valor valido", input -> input.matches("\\d{11}") || input.matches("\\d{14}")));

        System.out.print("Telefone: ");
        cliente.setTelefone(validarEntrada(scanner, "Informe um valor valido", input -> input.matches("\\d{10,11}")));

        System.out.print("Email: ");
        cliente.setEmail(validarEntrada(scanner, "Informe um valor valido", input -> input.contains("@")));

        try {
            clienteController.novoCliente(cliente);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; 
        }

        // Tipo de conta
        System.out.println("Account Type:");
        System.out.println("1. CONTA_CORRENTE");
        System.out.println("2. CONTA_POUPANCA");
        System.out.println("3. CONTA_SALARIO");
        System.out.print("Choose an option: ");
        int tipoContaOption = scanner.nextInt();
        scanner.nextLine();
        TipoConta tipoConta;
        switch (tipoContaOption) {
            case 1:
                tipoConta = TipoConta.CONTA_CORRENTE;
                break;
            case 2:
                tipoConta = TipoConta.CONTA_POUPANCA;
                break;
            case 3:
                tipoConta = TipoConta.CONTA_SALARIO;
                break;
            default:
                System.out.println("Sua escolha foi invalida a conta padrão gerada é CONTA CORRENTE");
                tipoConta = TipoConta.CONTA_CORRENTE;
        }

        // Conta para cliente
        Conta conta = new Conta();
        conta.setTipoConta(tipoConta);
        System.out.print("Nome de usuário para sua conta: ");
        conta.setUsuario(scanner.nextLine());
        System.out.print("Senha da sua conta: ");
        conta.setSenha(scanner.nextLine());
        conta.setCliente(cliente);

        try {
            contaController.novaConta(conta);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; 
        }

        System.out.println("Sua conta foi criada com sucesso");
    }

    

    private static String validarEntrada(Scanner scanner, String mensagemErro, java.util.function.Predicate<String> validacao) {
        while (true) {
            String entrada = scanner.nextLine();
            if (validacao.test(entrada)) {
                return entrada;
            } else {
                System.out.println(mensagemErro);
            }
        }
    }

    private static LocalDate validarData(Scanner scanner, String mensagemErro, DateTimeFormatter formatter) {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println(mensagemErro);
            }
        }
    }

    public static void skipLines(int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            System.out.println();
        }
    }
}
