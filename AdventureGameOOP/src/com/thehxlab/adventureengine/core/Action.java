package com.thehxlab.adventureengine.core;

public abstract class Action {
	
	protected GameWorld world;

    public Action(GameWorld world) {
        this.world = world;
    }

    public abstract void execute(String target);

    protected Player getPlayer() {
        return world.getPlayer();
    }
}
