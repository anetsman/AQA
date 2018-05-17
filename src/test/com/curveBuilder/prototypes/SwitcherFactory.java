package com.curveBuilder.prototypes;

import com.curveBuilder.enums.SwitchStates;

import java.util.HashMap;

public abstract class SwitcherFactory {
    private HashMap<SwitchStates, ISwitcherStrategy> strategies;

    public SwitcherFactory getInstance() {
        return this;
    }

    public ISwitcherStrategy findStrategyForKey(SwitchStates key) {
        return strategies.get(key);
    }

    protected void setStrategies(HashMap<SwitchStates, ISwitcherStrategy> strategies) {
        this.strategies = strategies;
    }
}
