#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Regenerate ALL launcher icon assets from the original logo (logoimposter.png).

Design decisions:
- The original logo is a dark arched emblem on transparency; the current icon
  used a placeholder white blob and never showed the real logo.
- Adaptive foreground: emblem scaled so its content bbox height = 53% of the
  432px canvas -> width ~55% -> fully inside the 66/108 safe zone and the
  72/108 circular mask, never cropped by any launcher mask.
- Adaptive background: solid navy #0B1020 (the app's BackgroundDark), which the
  dark emblem blends into cleanly.
- Legacy mipmaps: navy square + emblem at 72% (no mask on legacy icons, so the
  logo can be larger and stays sharp at 48-192px).
- Splash icon: emblem at 55% of the 432px canvas (192dp circle slot on 12+).
"""
import os
from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
LOGO = os.path.join(ROOT, "logoimposter.png")
RES = os.path.join(ROOT, "app", "src", "main", "res")

NAVY = (11, 17, 32)          # #0B1020
DENSITIES = [("mdpi", 48), ("hdpi", 72), ("xhdpi", 96), ("xxhdpi", 144), ("xxxhdpi", 192)]
CANVAS = 432                 # adaptive + splash canvases


def content_bbox(im):
    """Bounding box of non-transparent pixels (alpha > 10)."""
    px = im.load()
    w, h = im.size
    minx, miny, maxx, maxy = w, h, 0, 0
    for y in range(h):
        for x in range(w):
            if px[x, y][3] > 10:
                minx, miny, maxx, maxy = min(minx, x), min(miny, y), max(maxx, x), max(maxy, y)
    return minx, miny, maxx, maxy


def emblem_scaled(im, bbox, canvas, height_frac):
    """Crop the emblem bbox and scale so its HEIGHT = height_frac * canvas."""
    x0, y0, x1, y1 = bbox
    crop = im.crop((x0, y0, x1 + 1, y1 + 1))
    th = round(canvas * height_frac)
    tw = round(crop.width * th / crop.height)
    return crop.resize((tw, th), Image.LANCZOS)


def paste_centered(canvas, art):
    cw, ch = canvas.size
    canvas.paste(art, ((cw - art.width) // 2, (ch - art.height) // 2), art)


def main():
    logo = Image.open(LOGO).convert("RGBA")
    bbox = content_bbox(logo)

    # 1) Legacy density icons: navy square + emblem at 72%.
    for density, size in DENSITIES:
        d = os.path.join(RES, f"mipmap-{density}")
        os.makedirs(d, exist_ok=True)
        base = Image.new("RGBA", (size, size), NAVY + (255,))
        em = emblem_scaled(logo, bbox, size, 0.72)
        paste_centered(base, em)
        for name in ("ic_launcher.png", "ic_launcher_round.png"):
            base.save(os.path.join(d, name))
        print(f"  mipmap-{density} ({size}px)")

    # 2) Adaptive foreground: transparent 432 canvas, emblem at 53% height.
    #    (Farthest emblem pixel lands inside the 66/108 safe zone on all launchers.)
    fg = Image.new("RGBA", (CANVAS, CANVAS), (0, 0, 0, 0))
    em = emblem_scaled(logo, bbox, CANVAS, 0.53)
    paste_centered(fg, em)
    fg_path = os.path.join(RES, "drawable", "ic_launcher_foreground.png")
    fg.save(fg_path)
    fg_box = fg.getbbox()
    rel = tuple(round(v / CANVAS, 3) for v in (fg_box[0], fg_box[1], fg_box[2], fg_box[3]))
    print(f"  adaptive foreground {fg_path}  content bbox (rel): {rel}")

    # 3) Splash logo: 432 canvas, emblem at 55% height.
    splash = Image.new("RGBA", (CANVAS, CANVAS), (0, 0, 0, 0))
    em = emblem_scaled(logo, bbox, CANVAS, 0.55)
    paste_centered(splash, em)
    splash_path = os.path.join(RES, "drawable", "splash_logo.png")
    splash.save(splash_path)
    print(f"  splash logo {splash_path}")

    print("Done. Update mipmap-anydpi-v26 XMLs (drop <monochrome>) and rebuild.")


if __name__ == "__main__":
    main()