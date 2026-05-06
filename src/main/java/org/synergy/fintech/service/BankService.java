package org.synergy.fintech.service;

import org.synergy.fintech.model.Account;
import org.synergy.fintech.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис банковских операций, управляющий счетами и транзакциями.
 *
 * <p>BankService является центральным классом бизнес-логики приложения,
 * обеспечивающим управление банковскими счетами (Account) и выполнение
 * финансовых операций (Transaction).</p>
 *
 * <p>Основные функции сервиса:</p>
 * <ul>
 *   <li>Создание новых банковских счетов</li>
 *   <li>Получение списка всех счетов</li>
 *   <li>Пополнение счета</li>
 *   <li>Перевод средств между счетами</li>
 *   <li>Просмотр истории транзакций</li>
 * </ul>
 *
 * @see Account
 * @see Transaction
 */
public class BankService {

    /**
     * Хранилище всех банковских счетов.
     *
     * <p>Список содержит объекты Account, представляющие все счета,
     * созданные в системе. Каждый счет идентифицируется уникальным номером.</p>
     *
     * @see Account
     */
    private final List<Account> accounts = new ArrayList<>();

    /**
     * Полная история всех выполненных операций.
     *
     * <p>Список содержит объекты Transaction, фиксирующие все операции,
     * совершенные в системе: создание счетов, пополнения и переводы.</p>
     *
     * @see Transaction
     */
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Счетчик для автоматической генерации уникальных идентификаторов счетов.
     *
     * <p>Значение увеличивается на единицу при каждом создании нового счета
     * методом {@link #createAccount(String, double)}. Начальное значение равно 1.</p>
     */
    private int nextAccountId = 1;

    /**
     * Создает новый банковский счет с указанными параметрами.
     *
     * <p>Метод создает новый объект Account с автоматически сгенерированным
     * идентификатором, добавляет его в хранилище счетов и записывает
     * транзакцию типа "Создание счета" в историю операций.</p>
     *
     * @param ownerName      имя владельца счета (не может быть пустым)
     * @param initialBalance начальный баланс счета (может быть нулевым или положительным)
     * @return созданный объект Account с уникальным идентификатором
     * @see Account
     * @see Transaction
     */
    public Account createAccount(String ownerName, double initialBalance) {
        Account account = new Account(nextAccountId++, ownerName, initialBalance);
        accounts.add(account);

        transactions.add(new Transaction(
                "Создание счета",
                account.getId(),
                account.getId(),
                initialBalance
        ));

        return account;
    }

    /**
     * Возвращает список всех существующих банковских счетов.
     *
     * <p>Метод возвращает ссылку на внутреннее хранилище счетов.
     * Любые изменения в возвращенном списке затронут внутреннее состояние
     * сервиса.</p>
     *
     * @return список всех счетов типа List&lt;Account&gt;
     * @see Account
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Пополняет баланс указанного счета на заданную сумму.
     *
     * <p>Метод находит счет по его идентификатору, увеличивает его баланс
     * на указанную сумму и записывает транзакцию типа "Пополнение счета"
     * в историю операций. После успешного пополнения выводится сообщение
     * в консоль.</p>
     *
     * @param accountId идентификатор счета для пополнения
     * @param amount    сумма пополнения (должна быть положительной)
     * @throws IllegalArgumentException если счет с указанным идентификатором не найден
     * @throws IllegalArgumentException если сумма пополнения меньше или равна нулю
     * @see Account#deposit(double)
     * @see Transaction
     */
    public void deposit(int accountId, double amount) {
        Account account = findAccountById(accountId);
        account.deposit(amount);

        transactions.add(new Transaction(
                "Пополнение счета",
                accountId,
                accountId,
                amount
        ));

        System.out.println("Счет успешно пополнен.");
    }

    /**
     * Выполняет перевод средств между двумя счетами.
     *
     * <p>Метод находит оба счета по их идентификаторам, списывает указанную
     * сумму с исходного счета (withdraw), зачисляет ее на целевой счет (deposit)
     * и записывает транзакцию типа "Перевод средств" в историю операций.
     * После успешного перевода выводится сообщение в консоль.</p>
     *
     * <p>Операция выполняется как единая последовательность действий:
     * сначала списание, затем зачисление. Если списание прошло успешно,
     * зачисление также должно пройти успешно.</p>
     *
     * @param fromAccountId идентификатор счета отправителя
     * @param toAccountId   идентификатор счета получателя
     * @param amount        сумма перевода (должна быть положительной)
     * @throws IllegalArgumentException если счет отправителя не найден
     * @throws IllegalArgumentException если счет получателя не найден
     * @throws IllegalArgumentException если сумма перевода меньше или равна нулю
     * @throws IllegalArgumentException если на счете отправителя недостаточно средств
     * @see Account#withdraw(double)
     * @see Account#deposit(double)
     * @see Transaction
     */
    public void transfer(int fromAccountId, int toAccountId, double amount) {
        Account fromAccount = findAccountById(fromAccountId);
        Account toAccount = findAccountById(toAccountId);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        transactions.add(new Transaction(
                "Перевод средств",
                fromAccountId,
                toAccountId,
                amount
        ));

        System.out.println("Перевод выполнен успешно.");
    }

    /**
     * Выводит в консоль полную историю всех выполненных транзакций.
     *
     * <p>Метод проверяет историю транзакций. Если список транзакций пуст,
     * выводится сообщение "История операций пуста.". В противном случае
     * последовательно выводятся все транзакции в порядке их выполнения.</p>
     *
     * @see Transaction
     */
    public void printTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("История операций пуста.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    /**
     * Находит банковский счет по его идентификатору.
     *
     * <p>Вспомогательный метод для поиска счета в внутреннем хранилище
     * с использованием потокового API Java (stream/filter/findFirst).
     * Если счет с указанным идентификатором не найден, выбрасывается
     * исключение IllegalArgumentException.</p>
     *
     * @param accountId идентификатор искомого счета
     * @return объект Account с указанным идентификатором
     * @throws IllegalArgumentException если счет с указанным идентификатором не найден
     * @see Account
     */
    private Account findAccountById(int accountId) {
        return accounts.stream()
                .filter(account -> account.getId() == accountId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Счет с номером " + accountId + " не найден."));
    }
}