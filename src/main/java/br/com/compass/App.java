package br.com.compass;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import br.com.compass.controller.ClienteController;
import br.com.compass.controller.ContaController;
import br.com.compass.enuns.TipoConta;
import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;

public class App {
    
	private static ClienteController clienteController;
    private static ContaController contaController;

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

            int option = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

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
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public static void login(Scanner scanner) {
        System.out.println("===== Login =====");
        System.out.print("Username: ");
        String usuario = scanner.nextLine();
        
        System.out.print("Password: ");
        String senha = readPassword();

        Conta conta = contaController.buscaPorUsuarioESenha(usuario, senha);
        if (conta != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + conta.getCliente().getNome() + "!");
            bankMenu(scanner);
        } else {
            System.out.println("Invalid username or password. Please try again.");
            mainMenu(scanner);
        }
    }

    private static String readPassword() {
        StringBuilder password = new StringBuilder();
        try {
            while (true) {
                char ch = (char) System.in.read();
                if (ch == '\n' || ch == '\r') {
                    break;
                }
                System.out.print('*');
                password.append(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return password.toString().trim();
    }

    public static void bankMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

            switch (option) {
                case 1:
                    // ToDo...
                    System.out.println("Deposit.");
                    break;
                case 2:
                    // ToDo...
                    System.out.println("Withdraw.");
                    break;
                case 3:
                    // ToDo...
                    System.out.println("Check Balance.");
                    break;
                case 4:
                    // ToDo...
                    System.out.println("Transfer.");
                    break;
                case 5:
                    // ToDo...
                    System.out.println("Bank Statement.");
                    break;
                case 0:
                    System.out.println("Exiting...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void abrirConta(Scanner scanner) {
        System.out.println("===== Account Opening =====");
        
        // Criar novo cliente
        Cliente cliente = new Cliente();
        System.out.print("Client Name: ");
        cliente.setNome(scanner.nextLine());
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        cliente.setDataNascimento(LocalDate.parse(scanner.nextLine()));
        System.out.print("CPF: ");
        cliente.setCpf(scanner.nextLine());
        System.out.print("Phone: ");
        cliente.setTelefone(scanner.nextLine());
        System.out.print("Email: ");
        cliente.setEmail(scanner.nextLine());
        clienteController.novoCliente(cliente);
        
        // Escolher tipo de conta
        System.out.println("Account Type:");
        System.out.println("1. CONTA_CORRENTE");
        System.out.println("2. CONTA_POUPANCA");
        System.out.println("3. CONTA_SALARIO");
        System.out.print("Choose an option: ");
        int tipoContaOption = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha
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
                System.out.println("Invalid option! Defaulting to CONTA_CORRENTE.");
                tipoConta = TipoConta.CONTA_CORRENTE;
        }

        // Criar nova conta associada ao novo cliente
        Conta conta = new Conta();
        conta.setTipoConta(tipoConta);
        System.out.print("Username: ");
        conta.setUsuario(scanner.nextLine());
        System.out.print("Password: ");
        conta.setSenha(scanner.nextLine());
        conta.setCliente(cliente);
        contaController.novaConta(conta);
        
        System.out.println("Account and client created successfully!");
    }
}
