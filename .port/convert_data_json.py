"""Converts NeoForge-specific datapack JSON in the Twilight Forest resources to Fabric equivalents.

- neoforge:ingredient_type components/compound/difference -> fabric:type components/any/difference
- neoforge:conditions (mod_loaded / custom) -> fabric:load_conditions
- loot condition neoforge:can_item_perform_ability (shears_dig) -> match_tool on #c:tools/shear
- dimension type neoforge:custom_* environment attributes -> removed (client handles sky/weather via mixins)
"""
import glob
import json

changed = 0


def convert_ingredient(o):
    t = o.get('neoforge:ingredient_type')
    if t == 'neoforge:components':
        out = {'fabric:type': 'fabric:components', 'base': o['items'], 'components': o['components']}
        if o.get('strict'):
            out['strict'] = True
        return out
    if t == 'neoforge:compound':
        return {'fabric:type': 'fabric:any', 'ingredients': [convert(c) for c in o['children']]}
    if t == 'neoforge:difference':
        return {'fabric:type': 'fabric:difference', 'base': convert(o['base']), 'subtracted': convert(o['subtracted'])}
    raise ValueError('unknown ingredient type ' + str(t))


def convert_condition(c):
    t = c['type']
    if t == 'neoforge:mod_loaded':
        return {'condition': 'fabric:all_mods_loaded', 'values': [c['modid']]}
    if t == 'neoforge:not':
        return {'condition': 'fabric:not', 'value': convert_condition(c['value'])}
    if t.startswith('neoforge:'):
        raise ValueError('unknown condition ' + t)
    out = dict(c)
    out['condition'] = out.pop('type')
    return out


def convert(o):
    if isinstance(o, list):
        return [convert(x) for x in o]
    if not isinstance(o, dict):
        return o
    if 'neoforge:ingredient_type' in o:
        return convert_ingredient(o)
    if o.get('condition') == 'neoforge:can_item_perform_ability':
        tags = {'shears_dig': '#c:tools/shear', 'axe_strip': '#minecraft:axes'}
        ability = o.get('ability')
        if ability not in tags:
            raise ValueError('unknown ability ' + str(ability))
        return {'condition': 'minecraft:match_tool', 'predicate': {'items': tags[ability]}}
    out = {}
    for k, v in o.items():
        if k == 'neoforge:conditions':
            out['fabric:load_conditions'] = [convert_condition(c) for c in v]
        elif k in ('neoforge:custom_skybox', 'neoforge:custom_weather_effects'):
            continue
        else:
            out[k] = convert(v)
    return out


files = glob.glob('src/generated/resources/data/**/*.json', recursive=True) + glob.glob('src/main/resources/data/**/*.json', recursive=True)
for f in files:
    f = f.replace(chr(92), '/')
    if '/data/neoforge/' in f:
        continue
    raw = open(f, encoding='utf-8').read()
    if 'neoforge:' not in raw:
        continue
    data = json.loads(raw)
    new = convert(data)
    if new != data:
        text = json.dumps(new, indent=2, ensure_ascii=False)
        open(f, 'w', encoding='utf-8', newline='\n').write(text)
        changed += 1
print('converted', changed, 'files')
