package com.sky.order;

import com.sky.common.BusinessException;

import java.util.Map;
import java.util.Set;

public class OrderStateMachine {
    public static final int PENDING_PAYMENT = 1;
    public static final int TO_BE_CONFIRMED = 2;
    public static final int CONFIRMED = 3;
    public static final int DELIVERY_IN_PROGRESS = 4;
    public static final int COMPLETED = 5;
    public static final int CANCELLED = 6;

    private static final Map<Integer, Set<Integer>> ALLOWED = Map.of(
            PENDING_PAYMENT, Set.of(TO_BE_CONFIRMED, CANCELLED),
            TO_BE_CONFIRMED, Set.of(CONFIRMED, CANCELLED),
            CONFIRMED, Set.of(DELIVERY_IN_PROGRESS),
            DELIVERY_IN_PROGRESS, Set.of(COMPLETED)
    );

    public void ensureCanTransition(int currentStatus, int targetStatus) {
        if (!ALLOWED.getOrDefault(currentStatus, Set.of()).contains(targetStatus)) {
            throw new BusinessException("订单状态不允许从 " + statusName(currentStatus) + " 流转到 " + statusName(targetStatus));
        }
    }

    public static String statusName(int status) {
        return switch (status) {
            case PENDING_PAYMENT -> "待付款";
            case TO_BE_CONFIRMED -> "待接单";
            case CONFIRMED -> "已接单";
            case DELIVERY_IN_PROGRESS -> "派送中";
            case COMPLETED -> "已完成";
            case CANCELLED -> "已取消";
            default -> "未知";
        };
    }
}
