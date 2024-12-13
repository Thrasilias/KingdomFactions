package nl.dusdavidgames.kingdomfactions.modules.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Randomizer<E> {

    private List<E> list;
    private Random random;

    // Constructor accepting a List
    public Randomizer(List<E> list) {
        this.list = list != null ? list : new ArrayList<>();
        this.random = new Random();
    }

    // Constructor accepting an Array
    public Randomizer(E[] list) {
        this.list = list != null ? new ArrayList<>(Arrays.asList(list)) : new ArrayList<>();
        this.random = new Random();
    }

    // Default Constructor
    public Randomizer() {
        this.list = new ArrayList<>();
        this.random = new Random();
    }

    // Set List with List input
    public Randomizer<E> setList(List<E> list) {
        this.list = list != null ? list : new ArrayList<>();
        return this;
    }

    // Set List with Array input
    public Randomizer<E> setList(E[] list) {
        this.list = list != null ? new ArrayList<>(Arrays.asList(list)) : new ArrayList<>();
        return this;
    }

    // Get random result from the list
    public E result() {
        if (list.isEmpty()) {
            return null; // Return null if list is empty
        }
        return list.get(random.nextInt(list.size()));
    }

    // Check if the list has a valid result
    public boolean hasResult() {
        return !list.isEmpty();
    }

    // Get the list
    public List<E> getList() {
        return list;
    }

}
