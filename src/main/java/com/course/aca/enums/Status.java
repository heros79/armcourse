package com.course.aca.enums;

/**
 * Standard enum with three values: APPROVED(1), DISAPPROVED(2), PENDING(3).
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public enum Status {
    APPROVED(1),
    DISAPPROVED(2),
    PENDING(3);

    private int statusId;

    /**
     * Initializes private {@link Status#statusId} variable.
     *
     * @param statusId Stores passed value.
     */
    Status(int statusId){
        this.statusId = statusId;
    }

    /**
     * Getter method for instances.
     *
     * @param statusId
     * @return Instance of this enum with passed statusId parameter.
     */
    public static Status getStatus(int statusId) {
        switch (statusId) {
            case 1:
                return APPROVED;
            case 2:
                return DISAPPROVED;
            case 3:
                return PENDING;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getStatusId() {
        return statusId;
    }
}
