package com.thehxlab.adventureengine.core;

public class Item {
    private String name;
    private String description;
    private String[] useRules; // Targets this item can be used with
    private String[] combineRules; // Combinations with other items

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getUseRules() {
        return useRules;
    }

    public void setUseRules(String[] useRules) {
        this.useRules = useRules;
    }

    public String[] getCombineRules() {
        return combineRules;
    }

    public void setCombineRules(String[] combineRules) {
        this.combineRules = combineRules;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}

