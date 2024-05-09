package System;

import Data.CreditsData;

public interface CreditsManager {
    public default void setCreditForCancellationOnSameDay(int creditForCancellationOnSameDay) {
        CreditsData.setCreditForCancellationOnSameDay(creditForCancellationOnSameDay);
    }
    public default void setCreditForCancellationBefore(int creditForCancellationBefore) {
        CreditsData.setCreditForCancellationBefore(creditForCancellationBefore);
    }
    public default int getCreditForCancellationOnSameDay() {
        return CreditsData.getCreditForCancellationOnSameDay();
    }
    public default int getCreditForCancellationBefore() {
        return CreditsData.getCreditForCancellationBefore();
    }
}
