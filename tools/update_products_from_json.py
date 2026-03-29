from __future__ import annotations

import json
from pathlib import Path

products_path = Path('../app/src/main/assets/hayday_products.json')
machines_path = Path('../app/src/main/assets/hayday_machines.json')

products = json.loads(products_path.read_text(encoding='utf-8'))
machines = json.loads(machines_path.read_text(encoding='utf-8'))

print(f'Products: {len(products)}')
print(f'Machines: {len(machines)}')
print('Update the JSON files directly to expand the dataset.')
