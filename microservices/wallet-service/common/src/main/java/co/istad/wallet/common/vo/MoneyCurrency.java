package co.istad.wallet.common.vo;

import java.util.Currency;

public enum MoneyCurrency {
    USD(Currency.getInstance("USD")),
    KHR(Currency.getInstance("KHR"));

    MoneyCurrency(Currency currency) {}
}
