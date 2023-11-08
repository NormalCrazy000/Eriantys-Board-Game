package it.polimi.ingsw.network.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to generate the solutions of complex roots
 */
public class GeneratorPositionRegions {
    /**
     * This method is used to generate solution of complex square
     *
     * @param exponential type double: exponential of complex square
     * @return Map with key type {@link Integer} and value {@link ArrayList<Double>}: map with solutions
     */
    public Map<Integer, ArrayList<Double>> complexSquare(double exponential) {
        Map<Integer, ArrayList<Double>> result = new HashMap<>();
        for (int i = 0; i < exponential; i++) {
            result.put(i, new ArrayList<>());
        }
        for (int i = 0; i < exponential; i++) {
            result.get(i).add(Math.cos(2 * i * Math.PI / exponential));
            result.get(i).add(Math.sin(2 * i * Math.PI / exponential));
        }
        return result;
    }
}
