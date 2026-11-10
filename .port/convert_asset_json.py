"""Converts NeoForge-specific resource-pack JSON in Twilight Forest to Fabric equivalents.

- blockstate variant {"type": "neoforge:composite", "models": [...]} -> {"fabric:type": "fabric:composite", ...}
- blockstate variant {"type": "twilightforest:<custom>"} -> {"fabric:type": "twilightforest:<custom>"}
  (TF's custom block state model codecs are registered with Fabric's CustomUnbakedBlockStateModel registry)
- item model {"type": "neoforge:trimmed_armor", "base_model": X} -> X
  (Fabric has no dynamic trim-overlay item model; armor icons show without the trim overlay)
"""
import glob
import json

changed = 0


def convert_variant(o):
    if isinstance(o, list):
        return [convert_variant(x) for x in o]
    if not isinstance(o, dict):
        return o
    t = o.get('type')
    if t == 'neoforge:composite':
        out = {k: v for k, v in o.items() if k != 'type'}
        out = {'fabric:type': 'fabric:composite', **out}
        out['models'] = [convert_variant(m) for m in out['models']]
        return out
    if isinstance(t, str) and t.startswith('twilightforest:'):
        out = {k: v for k, v in o.items() if k != 'type'}
        return {'fabric:type': t, **out}
    return o


def convert_blockstate(data):
    if 'variants' in data:
        data['variants'] = {k: convert_variant(v) for k, v in data['variants'].items()}
    if 'multipart' in data:
        for part in data['multipart']:
            part['apply'] = convert_variant(part['apply'])
    return data


def convert_item_model(o):
    if isinstance(o, list):
        return [convert_item_model(x) for x in o]
    if not isinstance(o, dict):
        return o
    if o.get('type') == 'neoforge:trimmed_armor':
        return convert_item_model(o['base_model'])
    return {k: convert_item_model(v) for k, v in o.items()}


for pattern, fn in (('assets/*/blockstates/*.json', convert_blockstate), ('assets/*/items/**/*.json', convert_item_model)):
    for root in ('src/generated/resources/', 'src/main/resources/'):
        for f in glob.glob(root + pattern, recursive=True):
            raw = open(f, encoding='utf-8').read()
            if 'neoforge:' not in raw and '"type": "twilightforest:' not in raw:
                continue
            data = json.loads(raw)
            new = fn(json.loads(raw))
            if new != data:
                open(f, 'w', encoding='utf-8', newline='\n').write(json.dumps(new, indent=2, ensure_ascii=False))
                changed += 1
print('converted', changed, 'files')
