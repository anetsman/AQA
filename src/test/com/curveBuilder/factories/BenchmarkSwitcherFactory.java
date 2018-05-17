package com.curveBuilder.factories;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.prototypes.ISwitcherStrategy;
import com.curveBuilder.webElements.MainWindowElements;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkSwitcherFactory {
    private static Map<SwitchStates, ISwitcherStrategy> strategies = new HashMap<>();
    private final MainWindowElements mainWindowElements;

    private BenchmarkSwitcherFactory instance = this;

    public BenchmarkSwitcherFactory(Button button, MainWindowElements mainWindowElements) {
        this.mainWindowElements = mainWindowElements;
        strategies.put(SwitchStates.ACTIVE, new SwitcherStrategyActive(button));
        strategies.put(SwitchStates.NON_ACTIVE, new SwitcherStrategyNonActive(button));
        strategies.put(SwitchStates.SHOW_ON_TOP, new SwitcherStrategyShowOnTop(button));
    }

    public BenchmarkSwitcherFactory getInstance() {
        return instance;
    }

    public ISwitcherStrategy findStrategyForKey(SwitchStates key) {
        return strategies.get(key);
    }

    private class SwitcherStrategyActive implements ISwitcherStrategy {
        private Button button;

        SwitcherStrategyActive(Button button) {
            this.button = button;
        }

        public void switchToState() {
            switch (button.getCurrentState()) {
                case ACTIVE:
                    break;
                case NON_ACTIVE:
                    switchState(button);
                    break;
                case SHOW_ON_TOP:
                    switchState(button);
                    switchState(mainWindowElements.benchmarkButton());
                    break;
            }
        }
    }

    private class SwitcherStrategyNonActive implements ISwitcherStrategy {
        private Button button;

        SwitcherStrategyNonActive(Button button) {
            this.button = button;
        }

        public void switchToState() {
            switch (button.getCurrentState()) {
                case ACTIVE:
                    switchState(button);
                    switchState(mainWindowElements.benchmarkButton());
                    break;
                case NON_ACTIVE:
                    break;
                case SHOW_ON_TOP:
                    switchState(button);
                    break;
            }
        }
    }

    private class SwitcherStrategyShowOnTop implements ISwitcherStrategy {
        private Button button;

        SwitcherStrategyShowOnTop(Button button) {
            this.button = button;
        }

        public void switchToState() {
            switch (button.getCurrentState()) {
                case ACTIVE:
                    switchState(button);
                    break;
                case NON_ACTIVE:
                    switchState(button);
                    switchState(mainWindowElements.benchmarkButton());
                    break;
                case SHOW_ON_TOP:
                    break;
            }
        }
    }
}
