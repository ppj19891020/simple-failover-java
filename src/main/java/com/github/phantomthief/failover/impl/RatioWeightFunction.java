package com.github.phantomthief.failover.impl;

/**
 * @author huangli
 * Created on 2020-05-06
 */
public class RatioWeightFunction<T> extends AbstractWeightFunction<T> {

    private static final double DEFAULT_FAIL_DECREASE_RATE = 0.5;
    private static final double DEFAULT_SUCCESS_INCREASE_RATE = 0.01;

    private final double failKeepRateOfCurrentWeight;
    private final double successIncreaseRateOfMaxWeight;
    private final double downThreshold;

    public RatioWeightFunction() {
        this(DEFAULT_FAIL_DECREASE_RATE, DEFAULT_SUCCESS_INCREASE_RATE);
    }

    public RatioWeightFunction(double failKeepRateOfCurrentWeight, double successIncreaseRateOfMaxWeight) {
        this(failKeepRateOfCurrentWeight, successIncreaseRateOfMaxWeight, DEFAULT_RECOVER_THRESHOLD);
    }

    public RatioWeightFunction(double failKeepRateOfCurrentWeight, double successIncreaseRateOfMaxWeight,
            int recoverThreshold) {
        this(failKeepRateOfCurrentWeight, successIncreaseRateOfMaxWeight, recoverThreshold, 0);
    }

    public RatioWeightFunction(double failKeepRateOfCurrentWeight, double successIncreaseRateOfMaxWeight,
            int recoverThreshold, double downThreshold) {
        super(recoverThreshold);
        if (failKeepRateOfCurrentWeight < 0 || failKeepRateOfCurrentWeight > 1) {
            throw new IllegalArgumentException(
                    "bad failDecreaseRateOfCurrentWeight:" + failKeepRateOfCurrentWeight);
        }
        if (successIncreaseRateOfMaxWeight < 0 || successIncreaseRateOfMaxWeight > 1) {
            throw new IllegalArgumentException("bad successIncreaseRateOfMaxWeight:" + successIncreaseRateOfMaxWeight);
        }
        if (downThreshold < 0) {
            throw new IllegalArgumentException("bad downThreshold:" + downThreshold);
        }
        this.failKeepRateOfCurrentWeight = failKeepRateOfCurrentWeight;
        this.successIncreaseRateOfMaxWeight = successIncreaseRateOfMaxWeight;
        this.downThreshold = downThreshold;
    }

    @Override
    protected double computeSuccess(double maxWeight, double minWeight, int priority, double currentOldWeight,
            T resource) {
        return currentOldWeight + maxWeight * successIncreaseRateOfMaxWeight;
    }

    @Override
    protected double computeFail(double maxWeight, double minWeight, int priority, double currentOldWeight,
            T resource) {
        double x = currentOldWeight * failKeepRateOfCurrentWeight;
        return x < downThreshold ? minWeight : x;
    }
}
