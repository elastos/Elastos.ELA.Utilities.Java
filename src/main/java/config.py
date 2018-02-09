# build a new config.json for test nodes
import json
import sys

#BOM = b'\xef\xbb\xbf'
with open("./nodes/addresses.bak") as addr:
    addresses = addr.read().split("\n")

with open("./nodes/origin/config.json", mode='r', encoding='utf-8-sig') as f:
    i = int(sys.argv[1])
    net_type = sys.argv[2]
    mining = sys.argv[3]

    data = json.loads(f.read())
    data["Configuration"]["NodePort"]      = i + 40000
    data["Configuration"]["HttpInfoPort"]  = i + 40050
    data["Configuration"]["HttpRestPort"]  = i + 40100
    data["Configuration"]["HttpWsPort"]    = i + 40150
    data["Configuration"]["HttpJsonPort"]  = i + 40200
    data["Configuration"]["PowConfiguration"]["MiningSelfPort"] = i + 40300
    data["Configuration"]["PowConfiguration"]["PayToAddr"] = addresses[i - 1]
    data["Configuration"]["SeedList"] = ["127.0.0.1:40002"]
    data["Configuration"]["PowConfiguration"]["ActiveNet"] = net_type
    data["Configuration"]["PowConfiguration"]["AutoMining"] = (mining == "mining")

    print(json.dumps(data, indent = 2))

