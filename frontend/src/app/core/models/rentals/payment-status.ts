export enum PaymentStatus{
    PENDING = "PENDING",
    SUCCESSFUL = "SUCCESSFUL",
    FAILED = "FAILED",
    ON_SPOT = "ON_SPOT",
    CANCELLED = "CANCELLED",
    REFUNDED = "REFUNDED"
}

export const PaymentStatusLabel: Record<PaymentStatus, string> = {
    [PaymentStatus.PENDING]: "PENDING",
    [PaymentStatus.SUCCESSFUL]: "SUCCESSFUL",
    [PaymentStatus.FAILED]: "FAILED",
    [PaymentStatus.ON_SPOT]: "ON SPOT",
    [PaymentStatus.CANCELLED]: "CANCELLED",
    [PaymentStatus.REFUNDED]: "REFUNDED"
};