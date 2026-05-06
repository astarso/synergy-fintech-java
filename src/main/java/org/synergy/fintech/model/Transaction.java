package org.synergy.fintech.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Модель финансовой операции.
 *
 * Класс {@code Transaction} представляет собой неизменяемую модель банковской транзакции,
 * которая записывает следующие данные операции:
 * <ul>
 *     <li>Дата и время выполнения операции (автоматически устанавливается на текущее время)</li>
 *     <li>Тип операции (например, "Создание счета", "Пополнение счета", "Перевод средств")</li>
 *     <li>Идентификатор счета отправителя</li>
 *     <li>Идентификатор счета получателя</li>
 *     <li>Сумма операции</li>
 * </ul>
 *
 * Объекты данного класса являются неизменяемыми после создания.
 *
 * @see Account
 * @see BankService
 */
public class Transaction {
    /**
     * Дата и время выполнения операции.
     *
     * Поле автоматически устанавливается в значение {@link LocalDateTime#now()}
     * в момент создания объекта транзакции.
     */
    private final LocalDateTime dateTime;

    /**
     * Тип выполненной операции.
     *
     * Возможные значения включают:
     * <ul>
     *     <li>"Создание счета" — при открытии нового счета</li>
     *     <li>"Пополнение счета" — при увеличении баланса счета</li>
     *     <li>"Перевод средств" — при переводе между счетами</li>
     * </ul>
     */
    private final String operationType;

    /**
     * Идентификатор счета отправителя.
     *
     * Для операций, не связанных с отправителем (например, пополнение счета),
     * может содержать значение {@code 0} или другое условное значение.
     */
    private final int fromAccountId;

    /**
     * Идентификатор счета получателя.
     *
     * Для операций, не связанных с получателем (например, создание счета),
     * может содержать значение {@code 0} или другое условное значение.
     */
    private final int toAccountId;

    /**
     * Сумма операции в рублях.
     *
     * Значение указывается с точностью до двух знаков после запятой.
     * Для операций без суммы (например, создание счета) может содержать значение {@code 0.0}.
     */
    private final double amount;

    /**
     * Создает новый объект транзакции.
     *
     * При создании объекта поле {@link #dateTime} автоматически устанавливается
     * в значение текущего системного времени посредством вызова {@link LocalDateTime#now()}.
     *
     * @param operationType тип операции (не должно быть {@code null})
     * @param fromAccountId идентификатор счета отправителя
     * @param toAccountId  идентификатор счета получателя
     * @param amount        сумма операции в рублях
     */
    public Transaction(String operationType, int fromAccountId, int toAccountId, double amount) {
        this.dateTime = LocalDateTime.now();
        this.operationType = operationType;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    /**
     * Возвращает строковое представление транзакции.
     *
     * Форматирует данные транзакции в читаемом виде для вывода в консоль.
     * Формат вывода: {@code [dd.MM.yyyy HH:mm:ss] ТипОперации, счет отправителя: ID, счет получателя: ID, сумма: X.XX руб.}
     *
     * Пример возвращаемой строки:
     * <pre>
     * [25.12.2024 14:30:00] Перевод средств, счет отправителя: 101, счет получателя: 205, сумма: 1500.00 руб.
     * </pre>
     *
     * @return строка с описанием транзакции в указанном формате
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        return "[" + dateTime.format(formatter) + "] " +
                operationType +
                ", счет отправителя: " + fromAccountId +
                ", счет получателя: " + toAccountId +
                ", сумма: " + String.format("%.2f", amount) + " руб.";
    }
}