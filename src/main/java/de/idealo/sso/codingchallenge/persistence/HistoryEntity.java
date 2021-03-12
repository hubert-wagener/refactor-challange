package de.idealo.sso.codingchallenge.persistence;

import de.idealo.sso.codingchallenge.common.CurrencyEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(precision = 30, scale = 2)
    private BigDecimal amount;

    private CurrencyEnum currencyFrom;

    private CurrencyEnum currencyTo;

    private Date dateCreate;

    @Column(precision = 30, scale = 5)
    private BigDecimal result;

    private String requestType;

    private String requestDate = "";

    public HistoryEntity(BigDecimal amount, CurrencyEnum currencyFrom, CurrencyEnum currencyTo, Date dateCreate, BigDecimal result, String requestType, String requestDate) {
        this.amount = amount;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.dateCreate = dateCreate;
        this.result = result;
        this.requestType = requestType;
        this.requestDate = requestDate;
    }

    public HistoryEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CurrencyEnum getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(CurrencyEnum currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public CurrencyEnum getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(CurrencyEnum currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public BigDecimal getResult() {
        return result;
    }

    public String getStringResult() {
        return String.format("%.3f%n", result);
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
