import re,sys
lines=open('.port/build.log',encoding='utf-8',errors='replace').read().replace(chr(92),'/').splitlines()
seen=set();out=[]
i=0
while i<len(lines):
    l=lines[i]
    m=re.search(r'/java/(.*?\.java):(\d+): error: (.*)',l)
    if m:
        blk=[l]
        j=i+1
        while j<len(lines) and not re.search(r'\.java:\d+: error:',lines[j]) and j<i+8: blk.append(lines[j]); j+=1
        key=(m.group(1),m.group(2),m.group(3))
        if key not in seen:
            seen.add(key)
            sym=[b.strip() for b in blk[1:] if 'symbol' in b or 'location' in b or 'required' in b or 'found' in b or 'reason' in b]
            out.append(f"{m.group(1)}:{m.group(2)}: {m.group(3)} || "+" | ".join(sym)+" || "+(blk[1].strip() if len(blk)>1 else ''))
        i=j
    else: i+=1
flt=sys.argv[1] if len(sys.argv)>1 else ''
for o in out:
    if flt in o: print(o[:400])
print(len(out),'unique',file=sys.stderr)
