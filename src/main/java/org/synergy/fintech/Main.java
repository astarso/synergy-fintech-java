package org.synergy.fintech;

import org.synergy.fintech.model.Account;
import org.synergy.fintech.service.BankService;

import java.util.List;
import java.util.Scanner;

/**
 * Точка входа в приложение финансовой системы.
 * <p>
 * Данный класс реализует интерактивное консольное меню для управления
 * банковскими счетами и финансовыми операциями. Пользователю доступны
 * следующие операции:
 * <ul>
 *   <li>Создание нового банковского счета</li>
 *   <li>Просмотр списка всех существующих счетов</li>
 *   <li>Пополнение баланса счета</li>
 *   <li>Перевод средств между счетами</li>
 *   <li>Просмотр истории выполненных операций</li>
 *   <li>Завершение работы приложения</li>
 * </ul>
 * <p>
 * Приложение работает в цикле взаимодействия с пользователем до тех пор,
 * пока не будет выбран пункт меню "Выход" (0).
 *
 * @see BankService
 * @see Account
 */
public class Main {

    /**
     * Главный метод приложения, запускающий интерактивное консольное меню.
     * <p>
     * Метод инициализирует сканер для чтения пользовательского ввода и
     * экземпляр сервиса банковских операций. После этого запускается
     * основной цикл программы, в котором отображается меню с доступными
     * действиями, считывается выбор пользователя и выполняется
     * соответствующая операция.
     * <p>
     * Основной цикл работает следующим образом:
     * <ol>
     *   <li>Отображается меню с пронумерованными пунктами действий</li>
     *   <li>Считывается целое число, введенное пользователем</li>
     *   <li>Выполняется диспетчеризация к соответствующему обработчику
     *       с использованием switch-выражения (синтаксис стрелочных функций)</li>
     *   <li>При возникновении {@link IllegalArgumentException} выводится
     *       сообщение об ошибке, после чего цикл продолжается</li>
     * </ol>
     * <p>
     * Цикл завершается при выборе пункта меню "0" (Выход).
     *
     * @param args аргументы командной строки (не используются)
     * @throws IllegalArgumentException если переданы некорректные данные
     *                                  в методы BankService
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankService();

        boolean running = true;

        while (running) {
            System.out.println("\n=== Финансовая система ===");
            System.out.println("1. Создать счет");
            System.out.println("2. Показать все счета");
            System.out.println("3. Пополнить счет");
            System.out.println("4. Выполнить перевод");
            System.out.println("5. Показать историю операций");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1 -> createAccount(scanner, bankService);
                    case 2 -> showAccounts(bankService);
                    case 3 -> deposit(scanner, bankService);
                    case 4 -> transfer(scanner, bankService);
                    case 5 -> bankService.printTransactions();
                    case 0 -> {
                        running = false;
                        System.out.println("Работа программы завершена.");
                    }
                    default -> System.out.println("Некорректный пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }
    }

    /**
     * Создает новый банковский счет с заданными параметрами.
     * <p>
     * Метод запрашивает у пользователя полное имя владельца счета и
     * начальный баланс, после чего вызывает сервис для создания счета
     * и выводит информацию о созданном счете.
     *
     * @param scanner   объект Scanner для чтения ввода с консоли
     * @param bankService экземпляр сервиса BankService для выполнения
     *                    банковских операций
     */
    private static void createAccount(Scanner scanner, BankService bankService) {
        System.out.print("Введите ФИО клиента: ");
        String ownerName = scanner.nextLine();

        System.out.print("Введите начальный баланс: ");
        double balance = readDouble(scanner);

        Account account = bankService.createAccount(ownerName, balance);
        System.out.println("Счет создан: " + account);
    }

    /**
     * Отображает список всех существующих банковских счетов.
     * <p>
     * Метод получает список всех счетов от банковского сервиса и
     * выводит информацию о каждом счете в консоль. Если список счетов
     * пуст, выводится соответствующее сообщение.
     *
     * @param bankService экземпляр сервиса BankService для получения
     *                    списка счетов
     */
    private static void showAccounts(BankService bankService) {
        List<Account> accounts = bankService.getAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Счета отсутствуют.");
            return;
        }

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    /**
     * Выполняет пополнение баланса указанного банковского счета.
     * <p>
     * Метод запрашивает у пользователя идентификатор счета и сумму
     * пополнения, затем вызывает соответствующий метод банковского
     * сервиса для зачисления средств.
     *
     * @param scanner   объект Scanner для чтения ввода с консоли
     * @param bankService экземпляр сервиса BankService для выполнения
     *                    операции пополнения
     */
    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.print("Введите номер счета: ");
        int accountId = readInt(scanner);

        System.out.print("Введите сумму пополнения: ");
        double amount = readDouble(scanner);

        bankService.deposit(accountId, amount);
    }

    /**
     * Выполняет перевод средств между двумя банковскими счетами.
     * <p>
     * Метод запрашивает у пользователя идентификатор счета отправителя,
     * идентификатор счета получателя и сумму перевода, затем вызывает
     * соответствующий метод банковского сервиса для выполнения операции.
     *
     * @param scanner   объект Scanner для чтения ввода с консоли
     * @param bankService экземпляр сервиса BankService для выполнения
     *                    операции перевода
     */
    private static void transfer(Scanner scanner, BankService bankService) {
        System.out.print("Введите номер счета отправителя: ");
        int fromAccountId = readInt(scanner);

        System.out.print("Введите номер счета получателя: ");
        int toAccountId = readInt(scanner);

        System.out.print("Введите сумму перевода: ");
        double amount = readDouble(scanner);

        bankService.transfer(fromAccountId, toAccountId, amount);
    }

    /**
     * Считывает целое число из консольного ввода.
     * <p>
     * Метод ожидает ввод целого числа. Если пользователь вводит
     * некорректные данные (не целое число), выводится приглашение
     * ввести целое число и считывание повторяется. После успешного
     * считывания числа метод потребляет оставшийся символ новой строки.
     *
     * @param scanner объект Scanner для чтения ввода с консоли
     * @return считанное целое число, введенное пользователем
     */
    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Введите целое число: ");
            scanner.next();
        }

        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    /**
     * Считывает дробное число (с плавающей точкой) из консольного ввода.
     * <p>
     * Метод ожидает ввод дробного числа. Если пользователь вводит
     * некорректные данные (не число), выводится приглашение ввести
     * число и считывание повторяется. После успешного считывания
     * числа метод потребляет оставшийся символ новой строки.
     *
     * @param scanner объект Scanner для чтения ввода с консоли
     * @return считанное дробное число, введенное пользователем
     */
    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Введите число: ");
            scanner.next();
        }

        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}