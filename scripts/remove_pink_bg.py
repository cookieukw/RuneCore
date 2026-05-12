#!/usr/bin/env python3
"""Remove magenta/pink background from frame PNG files."""

import sys
from pathlib import Path
from PIL import Image
import numpy as np

FRAMES_DIR = Path(__file__).parent.parent / "src/main/resources/Common/UI/Custom/StatusEffects/Frames"

# Tolerance for matching the pink/magenta color (0-255)
TOLERANCE = 40

def remove_pink(img_path: Path) -> int:
    img = Image.open(img_path).convert("RGBA")
    data = np.array(img)

    r, g, b, a = data[:,:,0], data[:,:,1], data[:,:,2], data[:,:,3]

    # Magenta/pink mask: high R, low G, high B
    mask = (
        (r.astype(int) - g.astype(int) > TOLERANCE) &
        (b.astype(int) - g.astype(int) > TOLERANCE) &
        (g < 128)
    )

    count = int(mask.sum())
    data[mask] = [0, 0, 0, 0]  # fully transparent
    Image.fromarray(data).save(img_path)
    return count

def main():
    if not FRAMES_DIR.exists():
        print(f"Frames directory not found: {FRAMES_DIR}")
        sys.exit(1)

    pngs = list(FRAMES_DIR.rglob("*.png"))
    if not pngs:
        print("No PNG files found.")
        sys.exit(0)

    total = 0
    for png in sorted(pngs):
        count = remove_pink(png)
        rel = png.relative_to(FRAMES_DIR)
        print(f"  {rel}  →  {count} pixels removed")
        total += count

    print(f"\nDone! {len(pngs)} files processed, {total} pink pixels removed.")

if __name__ == "__main__":
    main()
