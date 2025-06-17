package com.thehxlab.adventureengine.core;

public enum Direction {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private final int index;

    Direction(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Direction fromIndex(int index) {
        for (Direction dir : values()) {
            if (dir.index == index) {
                return dir;
            }
        }
        return null;
    }
}
