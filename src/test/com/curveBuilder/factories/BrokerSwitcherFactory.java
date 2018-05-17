package com.curveBuilder.factories;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.prototypes.SwitcherFactory;
import com.curveBuilder.windowObjects.PopUpInfoWindow;
import com.curveBuilder.prototypes.ISwitcherStrategy;
import com.curveBuilder.utils.WebDriverHelper;

import java.util.HashMap;
import java.util.Map;

public class BrokerSwitcherFactory extends SwitcherFactory {
    private final Button button;

    public BrokerSwitcherFactory(Button button, boolean onInstrumentLevel, boolean onRiskGroupLevel) {
        this.button = button;
        HashMap<SwitchStates, ISwitcherStrategy> strategies = new HashMap<>();
        strategies.put(SwitchStates.ON, new SwitcherStrategyOn(onInstrumentLevel, onRiskGroupLevel));
        strategies.put(SwitchStates.OFF, new SwitcherStrategyOff(onInstrumentLevel, onRiskGroupLevel));
        super.setStrategies(strategies);
    }

    private void switchBroker(boolean currentState, boolean onInstrumentLevel, boolean onRiskGroupLevel) {
        if (currentState) {
            if (onInstrumentLevel) {
                button.getButtonElement().click();
                PopUpInfoWindow.confirmActionOnPopUp();
                WebDriverHelper.quickSleep();
                WebDriverHelper.performCtrlShiftClick(button.getButtonElement());
                PopUpInfoWindow.confirmActionOnPopUp();
            } else if (onRiskGroupLevel) {
                button.getButtonElement().click();
                PopUpInfoWindow.confirmActionOnPopUp();
                WebDriverHelper.quickSleep();
                WebDriverHelper.performCtrlClick(button.getButtonElement());
                PopUpInfoWindow.confirmActionOnPopUp();
            }
        } else {
            if (onInstrumentLevel) {
                WebDriverHelper.performCtrlShiftClick(button.getButtonElement());
                PopUpInfoWindow.confirmActionOnPopUp();
            } else if (onRiskGroupLevel) {
                WebDriverHelper.performCtrlClick(button.getButtonElement());
                PopUpInfoWindow.confirmActionOnPopUp();
            } else {
                button.getButtonElement().click();
                PopUpInfoWindow.confirmActionOnPopUp();
            }
        }
    }

    private class SwitcherStrategyOn implements ISwitcherStrategy {
        private final boolean onInstrumentLevel;
        private final boolean onRiskGroupLevel;

        SwitcherStrategyOn(boolean onInstrumentLevel, boolean onRiskGroupLevel) {
            this.onInstrumentLevel = onInstrumentLevel;
            this.onRiskGroupLevel = onRiskGroupLevel;
        }

        public void switchToState() {
            boolean isBrokerEnabled = button.getCurrentState() == SwitchStates.ON;
            switchBroker(isBrokerEnabled, onInstrumentLevel, onRiskGroupLevel);

        }
    }

    private class SwitcherStrategyOff implements ISwitcherStrategy {
        private final boolean onInstrumentLevel;
        private final boolean onRiskGroupLevel;

        SwitcherStrategyOff(boolean onInstrumentLevel, boolean onRiskGroupLevel) {
            this.onInstrumentLevel = onInstrumentLevel;
            this.onRiskGroupLevel = onRiskGroupLevel;
        }

        public void switchToState() {
            boolean brokerDisabled = button.getCurrentState() == SwitchStates.OFF;
            switchBroker(brokerDisabled, onInstrumentLevel, onRiskGroupLevel);
        }
    }
}
