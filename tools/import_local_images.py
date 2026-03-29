from __future__ import annotations

import shutil
from pathlib import Path

SRC = Path('SOURCE_IMAGES')
DST = Path('../app/src/main/assets/product_images')

"""انسخ الصور هنا ثم سمّها بنفس product id.
مثال: Bread.png -> bread.png
هذا السكربت مجرد أداة تنظيم أولية.
"""

mapping_examples = {
    'Bread.png': 'bread.png',
    'Butter Popcorn.png': 'butter_popcorn.png',
    'Feta Pie.png': 'feta_pie.png',
}

DST.mkdir(parents=True, exist_ok=True)
for source_name, target_name in mapping_examples.items():
    source = SRC / source_name
    target = DST / target_name
    if source.exists():
        shutil.copy2(source, target)
        print(f'Copied {source_name} -> {target_name}')
