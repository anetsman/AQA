package com.curveBuilder.windowObjects;

import com.curveBuilder.enums.FitFlags;
import com.curveBuilder.enums.SwitchStates;
import com.curveBuilder.factories.BenchmarkSwitcherFactory;
import com.curveBuilder.prototypes.Button;
import com.curveBuilder.utils.WebDriverHelper;
import com.curveBuilder.webElements.MainWindowElements;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by anetsman on 2017-06-06.
 */
public class MainWindow {

    private WebDriverHelper helper;
    private MainWindowElements mainWindowElements;
    private List<String> allTenors;

    public MainWindow(WebDriverHelper helper) {
        this.helper = helper;
        this.mainWindowElements = new MainWindowElements();
    }

    public MainWindowElements getMainWindowElements() {
        return mainWindowElements;
    }

    public void selectCurve(String curveName) {
        helper.fillInField(mainWindowElements.curveSelector(), curveName);
        mainWindowElements.currency(curveName).click();
//        helper.waitForElementsAppear(mainWindowElements.riskGroups());
        WebDriverHelper.quickSleep();
    }

    public void switchToRiskGroup(String riskGroupName) {
        helper.waitForElementAppears(mainWindowElements.riskGroup(riskGroupName));
        mainWindowElements.riskGroup(riskGroupName).click();
        helper.waitForElementsAppear(mainWindowElements.tenorsListLocator);
    }

    public List<String> getAllTenorsNames() {
        if (allTenors == null) {
            this.allTenors = mainWindowElements.allTenorsNames().stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        }
        return allTenors;
    }

    public ArrayList<Tenor> getAllVisibleTenors() {
        return getAllVisibleTenorsNames().stream()
                .map(name -> new Tenor(name, helper))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> getAllVisibleTenorsNames() {
        return mainWindowElements.allTenorsNames().stream()
                .map(WebElement::getText)
                .filter(name -> !Objects.equals(name, ""))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void sortByBenchmark(SwitchStates state) {
        Button benchmarkButton = mainWindowElements.benchmarkButton();
        BenchmarkSwitcherFactory benchmarkSwitcherFactory = new BenchmarkSwitcherFactory(benchmarkButton, mainWindowElements);
        benchmarkSwitcherFactory.getInstance().findStrategyForKey(state).switchToState();
    }

    public void setGlobalStep(double value, boolean apply) {
        mainWindowElements.setGlobalStepButton().click();
        mainWindowElements.globalStepInputField().click();
        helper.fillInField(mainWindowElements.globalStepInputField(), value);
        if (apply) {
            mainWindowElements.globalStepApproveButton().click();
            mainWindowElements.applyGlobalStepBtn().click();
        } else {
            mainWindowElements.globalStepDeclineButton().click();
            mainWindowElements.cancelGlobalStepBtn().click();
        }
    }

    public void sortByFitFlags(FitFlags fitFlag) {
        sortByFitFlags(new ArrayList<FitFlags>() {{
            add(fitFlag);
        }});
    }

    public void sortByFitFlags(List<FitFlags> fitFlags) {
        ArrayList<FitFlags> allFitFlags = new ArrayList<FitFlags>() {{
            add(FitFlags.F);
            add(FitFlags.O);
            add(FitFlags.S);
            add(FitFlags.I);
        }};

        Map<FitFlags, Boolean> currentFitFlags = new HashMap<>();

        mainWindowElements.activeFitFlags().forEach(element -> currentFitFlags.put(FitFlags.valueOf(element.getText()), true));
        mainWindowElements.disabledFitFlags().forEach(element -> currentFitFlags.put(FitFlags.valueOf(element.getText()), false));

        for (FitFlags fitFlag : allFitFlags) {
            if (fitFlags.contains(fitFlag)) {
                if (!currentFitFlags.get(fitFlag)) {
                    mainWindowElements.fitFlagCheckBox(fitFlag).click();
                }
            } else {
                if (currentFitFlags.get(fitFlag)) {
                    mainWindowElements.fitFlagCheckBox(fitFlag).click();
                }
            }
        }
    }

    public WebElement getUpperTenor(String tenorName) {
        if (allTenors == null) {
            allTenors = getAllVisibleTenorsNames();
        }
        return mainWindowElements.getTenorByName(allTenors.get(allTenors.indexOf(tenorName) - 1));
    }
}
