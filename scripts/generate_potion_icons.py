#!/usr/bin/env python3
"""
Generate tinted potion icons from the base PotionSmall.png.
Each potion gets a unique color applied to the liquid part of the icon.

Usage: python3 generate_potion_icons.py
"""

from PIL import Image, ImageEnhance
import numpy as np
import os

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ICONS_DIR = os.path.join(BASE_DIR, "src/main/resources/Common/Icons/ItemsGenerated")
SOURCE_ICON = os.path.join(ICONS_DIR, "PotionSmall.png")

POTION_COLORS = {
    "Bleeding":        (204, 34,  34),
    "Blindness":       (26,  26,  46),
    "Burn":            (255, 102, 0),
    "Decay":           (74,  55,  40),
    "Fire_Resistance": (232, 93,  4),
    "Frozen":          (136, 221, 255),
    "Glowing":         (255, 255, 170),
    "Haste":           (255, 170, 0),
    "High_Jump":       (102, 255, 102),
    "Instant_Damage":  (102, 0,   102),
    "Instant_Health":  (255, 51,  51),
    "Invisibility":    (204, 204, 238),
    "Jump_Boost":      (34,  204, 85),
    "Levitation":      (187, 136, 255),
    "Mining_Fatigue":  (139, 105, 20),
    "Nausea":          (153, 255, 51),
    "Night_Vision":    (0,   170, 204),
    "Poison":          (51,  170, 51),
    "Regeneration":    (255, 102, 170),
    "Resistance":      (136, 136, 170),
    "Slow_Falling":    (170, 221, 255),
    "Slowness":        (51,  68,  136),
    "Speed":           (51,  153, 255),
    "Strength":        (170, 0,   0),
    "Water_Breathing": (34,  170, 170),
    "Weakness":        (136, 136, 51),
}


def tint_potion(source_img, color):
    """Tint the colored (liquid) parts of the potion icon with the given color."""
    img = source_img.copy().convert("RGBA")
    pixels = np.array(img, dtype=np.float32)

    r, g, b, a = pixels[:,:,0], pixels[:,:,1], pixels[:,:,2], pixels[:,:,3]

    luminance = 0.299 * r + 0.587 * g + 0.114 * b

    is_visible = a > 10
    is_saturated = np.zeros_like(a, dtype=bool)
    max_c = np.maximum(np.maximum(r, g), b)
    min_c = np.minimum(np.minimum(r, g), b)
    chroma = max_c - min_c
    is_saturated = (chroma > 30) & is_visible

    is_gray = (~is_saturated) & is_visible
    is_dark_gray = is_gray & (luminance < 80)

    mask = is_saturated & ~is_dark_gray

    tr, tg, tb = color
    for ch_idx, tint_val in enumerate([tr, tg, tb]):
        channel = pixels[:,:,ch_idx]
        tinted = (luminance / 255.0) * tint_val
        tinted = np.clip(tinted * 1.1, 0, 255)
        channel[mask] = tinted[mask]

    result = Image.fromarray(pixels.astype(np.uint8), "RGBA")
    return result


def main():
    if not os.path.exists(SOURCE_ICON):
        print(f"ERROR: Source icon not found: {SOURCE_ICON}")
        return

    source = Image.open(SOURCE_ICON).convert("RGBA")
    print(f"Loaded base icon: {SOURCE_ICON} ({source.size[0]}x{source.size[1]})")

    generated = 0
    for name, color in POTION_COLORS.items():
        output_path = os.path.join(ICONS_DIR, f"Weapon_Bomb_Potion_{name}.png")
        tinted = tint_potion(source, color)
        tinted.save(output_path)
        print(f"  Generated: Weapon_Bomb_Potion_{name}.png  (color: {color})")
        generated += 1

    print(f"\nDone! Generated {generated} potion icons.")


if __name__ == "__main__":
    main()
