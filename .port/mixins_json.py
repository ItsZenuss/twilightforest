import json,glob,os
def names(pkg):
    return sorted(pkg.replace('/','.')+'.'+os.path.basename(f)[:-5] for f in glob.glob('src/main/java/twilightforest/mixin/'+pkg+'/*.java'))
common=names('compat')+names('event')+names('hooks')+names('ext')
client=names('compat/client')+names('client')
d={"required":True,"minVersion":"0.8","package":"twilightforest.mixin","compatibilityLevel":"JAVA_25","mixins":common,"client":client,"injectors":{"defaultRequire":1}}
json.dump(d,open('src/main/resources/twilightforest.mixins.json','w',newline='\n'),indent=2)
print(len(common),len(client))
