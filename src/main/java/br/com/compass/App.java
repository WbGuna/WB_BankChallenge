package br.com.compass;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

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
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number!");
                skipLines(3);
                scanner.next(); 
                continue; 
            }

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
                    System.out.println("Invalid option! Please try again");
            }
        }
    }

    public static void login(Scanner scanner) {
        System.out.println("===== Login =====");
        
        System.out.print("Username: ");
        String usuario = scanner.nextLine();

        System.out.print("Password: ");
        String senha = scanner.nextLine();

        contaLogada = contaController.buscaPorUsuarioESenha(usuario, senha);
        if (contaLogada != null) {
            skipLines(3);
            System.out.println("Login successful!");
            System.out.println("Welcome, " + contaLogada.getCliente().getNome() + "   -   Account: " + contaLogada.getTipoConta() + "   -   Account Number: " + contaLogada.getIdConta());
            bankMenu(scanner);
        } else {
            System.out.println("Invalid username or password. Please try again.");
            skipLines(3);
            mainMenu(scanner);
        }
    }

    public static void bankMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            skipLines(1);
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number!");
                skipLines(3);
                scanner.next(); 
                continue; 
            }

            int option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
            case 1:
                System.out.print("Enter the amount to be deposited: ");
                Double valorDep = scanner.nextDouble();
                contaController.depositar(contaLogada.getIdConta(), valorDep);
                System.out.println("Deposit successfully made!");
                break;
            case 2:
                System.out.print("Enter the amount you want to withdraw: ");
                Double valorSaq = scanner.nextDouble();
                contaController.sacar(contaLogada.getIdConta(), valorSaq);
                System.out.println("Withdrawal successfully made!");
                break;
            case 3:
                Double saldo = contaController.consultarSaldo(contaLogada.getIdConta());
                System.out.println("Your current balance is: " + saldo);
                break;
            case 4:
                System.out.print("Enter the destination account number: ");
                Long idContaDestino = scanner.nextLong();
                System.out.print("Enter the amount you want to transfer: ");
                Double valorTransf = scanner.nextDouble();
                contaController.transferir(contaLogada.getIdConta(), idContaDestino, valorTransf);
                System.out.println("Transfer successfully made!");
                break;
            case 5:
                List<Transacao> extrato = contaController.extratoBancario(contaLogada.getIdConta());
                System.out.println("===== Bank Statement =====");
                for (Transacao transacao : extrato) {
                    System.out.println("Date/Time: " + transacao.getDataHora().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                    System.out.println("Transaction Type: " + transacao.getTipoTransacao());
                    System.out.println("Amount: " + transacao.getValor());
                    System.out.println("---------------------------");
                }
                break;
            case 0:
                System.out.println("Exiting...");
                skipLines(3);
                running = false;
                return;
            default:
                System.out.println("Invalid option! Please try again.");
        }

        }
    }

    private static void abrirConta(Scanner scanner) {
        System.out.println("===== Account Opening =====");

        // Cliente
        Cliente cliente = new Cliente();
        System.out.print("Your full name: ");
        cliente.setNome(validarEntrada(scanner, "Your name cannot be empty.", input -> input.matches("^[\\p{L} .'-]+$")));

        System.out.print("Enter your date of birth in the format (DD-MM-YYYY): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        cliente.setDataNascimento(validarData(scanner, "Invalid date format. Please use DD-MM-YYYY.", formatter));

        System.out.print("CPF or CNPJ: ");
        cliente.setCpf(validarEntrada(scanner, "Invalid CPF/CNPJ format. Please enter a valid value.", input -> input.matches("\\d{11}") || input.matches("\\d{14}")));

        System.out.print("Phone: ");
        cliente.setTelefone(validarEntrada(scanner, "Please enter a valid value.", input -> input.matches("\\d{10,11}")));

        System.out.print("Email: ");
        cliente.setEmail(validarEntrada(scanner, "Please enter a valid value.", input -> input.contains("@")));

        try {
            clienteController.novoCliente(cliente);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        // Tipo de conta
        TipoConta tipoConta = escolherTipoConta(scanner);
        
        // Conta 
        Conta conta = new Conta();
        conta.setTipoConta(tipoConta);
        System.out.print("Username for your account: ");
        conta.setUsuario(scanner.nextLine());
        System.out.print("Password for your account: ");
        conta.setSenha(scanner.nextLine());
        conta.setCliente(cliente);

        try {
            contaController.novaConta(conta);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Your account has been successfully created.");
    }
    
    private static TipoConta escolherTipoConta(Scanner scanner) {
        System.out.println("Account Type:");
        System.out.println("1. CONTA_CORRENTE");
        System.out.println("2. CONTA_POUPANCA");
        System.out.println("3. CONTA_SALARIO");

        while (true) {
            System.out.print("Choose an option: ");
            
            try {
                int tipoContaOption = scanner.nextInt();
                scanner.nextLine();
                
                switch (tipoContaOption) {
                    case 1:
                        return TipoConta.CONTA_CORRENTE;
                    case 2:
                        return TipoConta.CONTA_POUPANCA;
                    case 3:
                        return TipoConta.CONTA_SALARIO;
                    default:
                        System.out.println("Invalid choice, the default account type generated is CONTA_CORRENTE.");
                        return TipoConta.CONTA_CORRENTE;
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter the correct value.");
                scanner.next(); 
            }
        }
    }


    public static String validarEntrada(Scanner scanner, String mensagemErro, Predicate<String> validacao) {
        String entrada;
        while (true) {
            entrada = scanner.nextLine();
            if (validacao.test(entrada)) {
                break;
            } else {
                System.out.println(mensagemErro);
                System.out.print("Enter the correct value: ");
            }
        }
        return entrada;
    }

    private static LocalDate validarData(Scanner scanner, String mensagemErro, DateTimeFormatter formatter) {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println(mensagemErro);
                System.out.print("Enter a date in the format (12-01-2025): ");
            }
        }
    }

    public static void skipLines(int numberOfLines) {
        for (int i = 0; i < numberOfLines; i++) {
            System.out.println();
        }
    }
}
