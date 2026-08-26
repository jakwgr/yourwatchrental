export enum PaymentMethod{
    CASH = "CASH",
    CARD = "CARD"
}

export const PaymentMethodLabel: Record<PaymentMethod, string> = {
    [PaymentMethod.CASH]: "CASH",
    [PaymentMethod.CARD]: "CARD"
};