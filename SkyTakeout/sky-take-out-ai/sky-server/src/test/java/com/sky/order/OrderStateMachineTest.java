package com.sky.order;

import com.sky.common.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderStateMachineTest {

    private final OrderStateMachine stateMachine = new OrderStateMachine();

    @Test
    void allowsPlannedOrderTransitions() {
        assertDoesNotThrow(() -> stateMachine.ensureCanTransition(1, 2));
        assertDoesNotThrow(() -> stateMachine.ensureCanTransition(1, 6));
        assertDoesNotThrow(() -> stateMachine.ensureCanTransition(2, 3));
        assertDoesNotThrow(() -> stateMachine.ensureCanTransition(2, 6));
        assertDoesNotThrow(() -> stateMachine.ensureCanTransition(3, 4));
        assertDoesNotThrow(() -> stateMachine.ensureCanTransition(4, 5));
    }

    @Test
    void rejectsIllegalOrderTransitions() {
        assertThrows(BusinessException.class, () -> stateMachine.ensureCanTransition(5, 6));
        assertThrows(BusinessException.class, () -> stateMachine.ensureCanTransition(6, 2));
        assertThrows(BusinessException.class, () -> stateMachine.ensureCanTransition(3, 6));
    }
}
