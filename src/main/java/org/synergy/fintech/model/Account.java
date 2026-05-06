package org.synergy.fintech.model;

/**
 * Модель банковского счета.
 *
 * Класс представляет собой модель банковского счета, которая хранит идентификатор счета,
 * имя владельца и текущий баланс. Поддерживает операции пополнения и списания средств.
 *
 * @see #deposit(double)
 * @see #withdraw(double)
 */
public class Account {
    /**
     * Идентификатор счета.
     * Уникальный номер, присваиваемый каждому счету при создании.
     */
    private final int id;

    /**
     * Имя владельца счета.
     * ФИО клиента, которому принадлежит данный счет.
     */
    private String ownerName;

    /**
     * Текущий баланс счета.
     * Сумма денежных средств на счете в рублях.
     */
    private double balance;

    /**
     * Конструктор для создания нового счета.
     *
     * @param id уникальный идентификатор счета
     * @param ownerName ФИО владельца счета (не может быть пустым или null)
     * @param balance начальный баланс счета (не может быть отрицательным)
     * @throws IllegalArgumentException если ownerName равен null, пуст или содержит только пробелы
     * @throws IllegalArgumentException если balance меньше нуля
     */
    public Account(int id, String ownerName, double balance) {
        this.id = id;
        setOwnerName(ownerName);
        setBalance(balance);
    }

    /**
     * Возвращает идентификатор счета.
     *
     * @return уникальный идентификатор счета
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя владельца счета.
     *
     * @return ФИО владельца счета
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Возвращает текущий баланс счета.
     *
     * @return текущий баланс счета в рублях
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Устанавливает имя владельца счета.
     *
     * @param ownerName ФИО владельца счета
     * @throws IllegalArgumentException если ownerName равен null, пуст или содержит только пробелы
     */
    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("ФИО клиента не может быть пустым.");
        }
        this.ownerName = ownerName;
    }

    /**
     * Устанавливает баланс счета.
     *
     * @param balance новый баланс счета в рублях
     * @throws IllegalArgumentException если balance меньше нуля
     */
    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным.");
        }
        this.balance = balance;
    }

    /**
     * Пополняет счет на указанную сумму.
     *
     * Увеличивает текущий баланс счета на сумму, переданную в качестве параметра.
     * Сумма пополнения должна быть строго больше нуля.
     *
     * @param amount сумма пополнения в рублях (должна быть больше нуля)
     * @throws IllegalArgumentException если amount меньше или равно нулю
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть больше нуля.");
        }
        balance += amount;
    }

    /**
     * Списывает средства со счета.
     *
     * Уменьшает текущий баланс счета на указанную сумму.
     * Сумма списания должна быть строго больше нуля.
     * На счете должно быть достаточно средств для списания.
     *
     * @param amount сумма списания в рублях (должна быть больше нуля)
     * @throws IllegalArgumentException если amount меньше или равно нулю
     * @throws IllegalArgumentException если amount превышает текущий баланс счета
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма списания должна быть больше нуля.");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("Недостаточно средств на счете.");
        }

        balance -= amount;
    }

    /**
     * Возвращает строковое представление счета.
     *
     * Форматирует информацию о счете в читаемом виде на русском языке.
     * Формат: "Счет №{id}, владелец: {ownerName}, баланс: {balance} руб."
     *
     * @return строковое представление счета
     */
    @Override
    public String toString() {
        return "Счет №" + id +
                ", владелец: " + ownerName +
                ", баланс: " + String.format("%.2f", balance) + " руб.";
    }
}