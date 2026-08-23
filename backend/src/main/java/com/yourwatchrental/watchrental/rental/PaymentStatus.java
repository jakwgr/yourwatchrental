package com.yourwatchrental.watchrental.rental;

public enum PaymentStatus {
    PENDING,     // oczekuje na płatność
    SUCCESSFUL,  // płatność zakończona poprawnie
    FAILED,      // płatność nieudana
    ON_SPOT,
    CANCELLED,
    REFUNDED
}
