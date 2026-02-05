package com.cookie.runecore.api;

@FunctionalInterface
public interface IEffectAction {
    void apply(CastContext ctx);
}
