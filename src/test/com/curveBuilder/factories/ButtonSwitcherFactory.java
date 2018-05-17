package com.curveBuilder.factories;

import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.prototypes.SwitcherFactory;
import com.curveBuilder.windowObjects.MarketButton;
import com.curveBuilder.windowObjects.Tenor;
import com.curveBuilder.prototypes.ISwitcherStrategy;
import com.curveBuilder.utils.WebDriverHelper;

import java.util.HashMap;
import java.util.Map;

public class ButtonSwitcherFactory extends SwitcherFactory {
    private final Tenor tenor;

    public ButtonSwitcherFactory(Tenor tenor, Button button) {
        this.tenor = tenor;
        HashMap<SwitchStates, ISwitcherStrategy> strategies = new HashMap<>();
        strategies.put(SwitchStates.ON, new SwitcherStrategyON(button));
        strategies.put(SwitchStates.OFF, new SwitcherStrategyOff(button));
        strategies.put(SwitchStates.READY, new SwitcherStrategyReady(button));
        super.setStrategies(strategies);
    }

    private class SwitcherStrategyON implements ISwitcherStrategy {
        private Button button;

        SwitcherStrategyON(Button button) {
            this.button = button;
        }

        private Button getNewButton() {
            return (button.getClass() == MarketButton.class) ? tenor.getTenorElements().marketButton() : tenor.getTenorElements().houseButton();
        }

        public void switchToState() {
            switch (button.getCurrentState()) {
                case OFF:
                    WebDriverHelper.performCtrlClick(button.getButtonElement());
                    switchState(getNewButton());
                    break;
                case READY:
                    switchState(button);
//                    while (getNewButton().getCurrentState() != SwitchStates.ON) {
//                        switchState(getNewButton());
//                    }
                    break;
                case ON:
                    break;
            }
        }
    }

    private class SwitcherStrategyOff implements ISwitcherStrategy {
        private Button button;

        SwitcherStrategyOff(Button button) {
            this.button = button;
        }

        private Button getNewButton() {
            return (button.getClass() == MarketButton.class) ? tenor.getTenorElements().marketButton() : tenor.getTenorElements().houseButton();
        }

        public void switchToState() {
            switch (button.getCurrentState()) {
                case ON:
                    switchState(button);
                    WebDriverHelper.performCtrlClick(getNewButton().getButtonElement());
//                    switchState(getNewButton());
                    break;
                case READY:
                    WebDriverHelper.performCtrlClick(button.getButtonElement());
//                    while (getNewButton().getCurrentState() != SwitchStates.OFF) {
//                        switchState(getNewButton());
//                    }
                    break;
                case OFF:
                    break;
            }
        }
    }

    private class SwitcherStrategyReady implements ISwitcherStrategy {
        private Button button;

        SwitcherStrategyReady(Button button) {
            this.button = button;
        }

        public void switchToState() {
            switch (button.getCurrentState()) {
                case ON:
                    switchState(button);
                    break;
                case READY:
                    break;
                case OFF:
//                    switchState(button);
                    WebDriverHelper.performCtrlClick(button.getButtonElement());
                    break;
            }
        }
    }
}
