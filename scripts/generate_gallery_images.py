#!/usr/bin/env python3
import os
import glob
from PIL import Image

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.dirname(BASE_DIR)
ICONS_DIR = os.path.join(PROJECT_ROOT, "icons")

ESSENCES_DIR = os.path.join(ICONS_DIR, "essences")
POTIONS_DIR = os.path.join(ICONS_DIR, "potions")
EFFECTS_DIR = os.path.join(ICONS_DIR, "128x")
OUTPUT_DIR = os.path.join(ICONS_DIR, "gallery")

os.makedirs(OUTPUT_DIR, exist_ok=True)

CELL_SIZE = 64
PADDING = 8
COLS = 5
BG_COLOR = (24, 25, 32, 255) # Dark theme background
CELL_BG = (34, 37, 48, 255)  # Slightly lighter cell background
BORDER_COLOR = (50, 55, 70, 255)

def create_grid_image(icon_paths, output_filename):
    rows = (len(icon_paths) + COLS - 1) // COLS
    
    cell_total_size = CELL_SIZE + PADDING * 2
    grid_w = COLS * cell_total_size + PADDING
    grid_h = rows * cell_total_size + PADDING

    canvas = Image.new("RGBA", (grid_w, grid_h), BG_COLOR)

    for idx, path in enumerate(icon_paths):
        r = idx // COLS
        c = idx % COLS

        x = PADDING + c * cell_total_size
        y = PADDING + r * cell_total_size

        # Draw cell background box
        cell_box = Image.new("RGBA", (cell_total_size - PADDING, cell_total_size - PADDING), CELL_BG)
        canvas.paste(cell_box, (x, y))

        if os.path.exists(path):
            try:
                icon = Image.open(path).convert("RGBA")
                icon = icon.resize((CELL_SIZE, CELL_SIZE), Image.Resampling.LANCZOS)
                canvas.paste(icon, (x + PADDING, y + PADDING), icon)
            except Exception as e:
                print(f"Error loading {path}: {e}")

    output_path = os.path.join(OUTPUT_DIR, output_filename)
    canvas.save(output_path, "PNG")
    print(f"Saved: {output_path} ({grid_w}x{grid_h})")
    return output_path

def main():
    # 1. Essences
    essence_files = sorted(glob.glob(os.path.join(ESSENCES_DIR, "*.png")))
    create_grid_image(essence_files, "gallery_essences.png")

    # 2. Potions
    potion_files = sorted(glob.glob(os.path.join(POTIONS_DIR, "*.png")))
    create_grid_image(potion_files, "gallery_potions.png")

    # 3. Status Effects
    effect_files = sorted(glob.glob(os.path.join(EFFECTS_DIR, "*.png")))
    create_grid_image(effect_files, "gallery_effects.png")

if __name__ == "__main__":
    main()
