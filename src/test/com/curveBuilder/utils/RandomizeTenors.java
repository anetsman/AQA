package com.curveBuilder.utils;

import com.curveBuilder.prototypes.TenorName;
import com.curveBuilder.windowObjects.MainWindow;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * create a list of random tenors, depends on parameters requested
 */
public class RandomizeTenors {

    private MainWindow mainWindow;
    private int numberOfTenors;
    private boolean fromOneInstrument;
    private boolean isDependOnAge;

    public RandomizeTenors(RandomTenorsBuilder builder) {
        this.mainWindow = builder.mainWindow;
        this.numberOfTenors = builder.numberOfTenors;
        this.fromOneInstrument = builder.fromOneInstrument;
        this.isDependOnAge = builder.isDependOnAge;
    }

    public List<String> getRandomTenors() {
        List<TenorName> randomTenors;
        Map<String, Integer> numberOfTenorsByInstrument = new HashMap<>();

        /*
         * Creating list of <TenorName> elements based on the current presented
         */
        randomTenors = mainWindow.getAllTenorsNames().stream()
                .skip(1)
                .map(TenorName::new)
                .peek(tenorName ->
                {
                    if (numberOfTenorsByInstrument.containsKey(tenorName.getInstrument())) {
                        numberOfTenorsByInstrument.put(tenorName.getInstrument(), numberOfTenorsByInstrument.get(tenorName.getInstrument()) + 1);
                    } else {
                        numberOfTenorsByInstrument.put(tenorName.getInstrument(), 1);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        /*
          Editing list depends on request for number of instruments
         */
        if (fromOneInstrument) {
            List<String> instruments = new ArrayList<>();

            numberOfTenorsByInstrument.entrySet().removeIf(entry -> entry.getValue() < numberOfTenors);

            randomTenors = randomTenors.stream()
                    .filter(tenorName -> numberOfTenorsByInstrument.get(tenorName.getInstrument()) != null)
                    .collect(Collectors.toCollection(ArrayList::new));

            randomTenors.forEach(tenorName ->
                {
                if (!instruments.contains(tenorName.getInstrument())) {
                    instruments.add(tenorName.getInstrument());
                }
            });

            String instrument = instruments.get((int) (Math.random() * instruments.size()));
            randomTenors = randomTenors.stream()
                    .filter(tenorName -> instrument.equals(tenorName.getInstrument()))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            List<String> instruments = new ArrayList<>();
            int numberOfOne = numberOfTenors/2;
            int numberOfTwo = numberOfTenors - numberOfOne;

            numberOfTenorsByInstrument.entrySet().removeIf(entry -> entry.getValue() < numberOfOne);

            randomTenors.forEach(tenorName ->
            {
                if (!instruments.contains(tenorName.getInstrument()) && numberOfTenorsByInstrument.containsKey(tenorName.getInstrument())) {
                    instruments.add(tenorName.getInstrument());
                }
            });

            List<TenorName> selectedTenors = new ArrayList<>();
            List<TenorName> randomPart1;
            List<TenorName> randomPart2;

            randomPart1 = randomTenors.stream()
                    .filter(tenorName -> tenorName.getInstrument().equals(instruments.get(0)))
                    .collect(Collectors.toCollection(ArrayList::new));
            randomPart2 = randomTenors.stream()
                    .filter(tenorName -> tenorName.getInstrument().equals(instruments.get(1)))
                    .collect(Collectors.toCollection(ArrayList::new));

            Random r = new Random();

            while (selectedTenors.size() < numberOfOne + numberOfTwo) {
                TenorName randomTenor;
                if (selectedTenors.size() >= numberOfOne) {
                    randomTenor = randomPart2.get(r.nextInt(randomPart2.size()));
                } else randomTenor = randomPart1.get(r.nextInt(randomPart1.size()));
                if (!selectedTenors.contains(randomTenor)) {
                    selectedTenors.add(randomTenor);
                }
            }

//            while (part.size() < numberOfOne ) {
//                TenorName randomTenor = randomPart2.get(r.nextInt(randomPart2.size()));
//                if (!part.contains(randomTenor)) {
//                    part.add(randomTenor);
//                }
//            }

            randomTenors = randomTenors.stream()
                    .filter(selectedTenors::contains)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (isDependOnAge) {
//            randomTenors.sort(Comparator.comparing(tenorName -> Integer.valueOf(tenorName.getAge().split("\\D")[0])));
            randomTenors.sort(TenorName.TenorNameComparator);
        }

        int difference = randomTenors.size() - numberOfTenors;
        Random random = new Random();
        while (difference != 0) {
            randomTenors.remove(random.nextInt(randomTenors.size()));
            difference--;
        }

        return randomTenors.stream()
                .map(TenorName::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static class RandomTenorsBuilder {
        private final MainWindow mainWindow;
        private int numberOfTenors;
        private boolean fromOneInstrument;
        private boolean isDependOnAge;

        public RandomTenorsBuilder(MainWindow mainWindow) {
            this.mainWindow = mainWindow;
        }

        public RandomTenorsBuilder numberOfTenors(int numberOfTenors) {
            this.numberOfTenors = numberOfTenors;
            return this;
        }

        public RandomTenorsBuilder fromOneInstrument(boolean fromOneInstrument) {
            this.fromOneInstrument = fromOneInstrument;
            return this;
        }

        public RandomTenorsBuilder isDependOnAge(boolean isDependOnAge) {
            this.isDependOnAge = isDependOnAge;
            return this;
        }

        public RandomizeTenors build() {
            return new RandomizeTenors(this);
        }
    }
}
