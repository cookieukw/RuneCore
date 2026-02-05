package com.cookie.runecore.api;

public class RuneEffect {
    private final String id;
    private final IEffectAction action;
    private final int durationTicks;
    private final boolean isInstant;

    public RuneEffect(String id, boolean isInstant, int durationTicks, IEffectAction action) {
        this.id = id;
        this.isInstant = isInstant;
        this.durationTicks = durationTicks;
        this.action = action;
    }

    public void execute(CastContext ctx) {
        this.action.apply(ctx);
    }
    
    public String getId() { return id; }

    public int getDurationTicks() { return durationTicks; }

    public boolean isInstant() { return isInstant; }
}
