# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/


## getblockcount
description: get block count

##### Request
```
{   
  "method":"getblockcount"
}
```
##### Response

```
{
    "result": 171454
    "id": null,
    "error": null,
    "jsonrpc": "2.0",
}
```


## estimatesmartfee
description: estimate transaction fee smartly

#### Parameter Description

| name | type | description |
| ---- | ---- | ----------- |
| confirmations | int | in how many blocks do you want your transaction to be packed |

result:
| name | type | description |
| ---- | ---- | ----------- |
|  -   | int  | fee rate, the unit is sela per KB |

##### Request
```
{
  "method": "estimatesmartfee",
  "params":{
	"confirmations": 5
  }
}
```

##### Response
```
{
    "error": null,
    "id": null,
    "jsonrpc": "2.0",
    "result": 10000
}

```


## listunspent
description: list all utxo of given addresses 

#### Parameter Description

| name | type | description |
| ---- | ---- | ----------- |
| addresses | array[string] | addresses |

##### Request
```
{
    "method":"listunspent",
    "params":{"addresses": ["8ZNizBf4KhhPjeJRGpox6rPcHE5Np6tFx3", "EeEkSiRMZqg5rd9a2yPaWnvdPcikFtsrjE"]}
}
```

##### Response
```
{
    "error": null,
    "id": null,
    "jsonrpc": "2.0",
    "result": [
        {
            "assetid": "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
            "txid": "9132cf82a18d859d200c952aec548d7895e7b654fd1761d5d059b91edbad1768",
            "vout": 0,
            "address": "8ZNizBf4KhhPjeJRGpox6rPcHE5Np6tFx3",
            "amount": "33000000",
            "confirmations": 1102,
            "outputlock": 0
        },
        {
            "assetid": "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
            "txid": "3edbcc839fd4f16c0b70869f2d477b56a006d31dc7a10d8cb49bd12628d6352e",
            "vout": 0,
            "address": "8ZNizBf4KhhPjeJRGpox6rPcHE5Np6tFx3",
            "amount": "0.01255707",
            "confirmations": 846,
            "outputlock": 0
        }
    ]
```

## getblock

#### Parameter Description

| name | type | description |
| ---- | ---- | ----------- |
| blockhash | string | the blockchain hash | 
| verbosity | int | the verbosity of result, can be 0, 1, 2 |

##### Request
```
{
    "method":"getblock",
    "params":{
        "blockhash":"e41e9b025cee0b10c895819b83e05c95775e254b221a8ef4b41097423dccaf43",
        "verbosity":2
    }
}
```

##### Response
```
{
    "error": null,
    "id": null,
    "jsonrpc": "2.0",
    "result": {
        "hash": "3ca6bcc86bada4642fea709731f1653bd34b28ab15b790e102e14e0d7bd138d8",
        "confirmations": 1,
        "strippedsize": 498,
        "size": 498,
        "weight": 1992,
        "height": 2001,
        "version": 0,
        "versionhex": "00000000",
        "merkleroot": "219184ea3c0a2973b90b8402c8405b76d7fbe10a268f6de7e4f48e93f5d03df7",
        "tx": [
            {
                "txid": "219184ea3c0a2973b90b8402c8405b76d7fbe10a268f6de7e4f48e93f5d03df7",
                "hash": "219184ea3c0a2973b90b8402c8405b76d7fbe10a268f6de7e4f48e93f5d03df7",
                "size": 192,
                "vsize": 192,
                "version": 0,
                "locktime": 2001,
                "vin": [
                    {
                        "txid": "0000000000000000000000000000000000000000000000000000000000000000",
                        "vout": 65535,
                        "sequence": 4294967295
                    }
                ],
                "vout": [
                    {
                        "value": "0.01255707",
                        "n": 0,
                        "address": "8VYXVxKKSAxkmRrfmGpQR2Kc66XhG6m3ta",
                        "assetid": "b037db964a231458d2d6ffd5ea18944c4f90e63d547c5d3b9874df66a4ead0a3",
                        "outputlock": 0
                    },
                    {
                        "value": "0.02929985",
                        "n": 1,
                        "address": "EXca4DJwqCXa6vbJmpovwatHiP8HRTVS1Z",
                        "assetid": "b037db964a231458d2d6ffd5ea18944c4f90e63d547c5d3b9874df66a4ead0a3",
                        "outputlock": 0
                    }
                ],
                "blockhash": "3ca6bcc86bada4642fea709731f1653bd34b28ab15b790e102e14e0d7bd138d8",
                "confirmations": 1,
                "time": 1527324355,
                "blocktime": 1527324355,
                "type": 0,
                "payloadversion": 4,
                "payload": {
                    "CoinbaseData": "ELA"
                },
                "attributes": [
                    {
                        "usage": 0,
                        "data": "46444170b0e427d2"
                    }
                ],
                "programs": []
            }
        ],
        "time": 1527324355,
        "mediantime": 1527324355,
        "nonce": 0,
        "bits": 545259519,
        "difficulty": "1",
        "chainwork": "00000000",
        "previousblockhash": "c0433b918f500392869aa14cf7a909430fd94502b5c9f05421c9da7519bd6a65",
        "nextblockhash": "0000000000000000000000000000000000000000000000000000000000000000",
        "auxpow": "01000000010000000000000000000000000000000000000000000000000000000000000000000000002cfabe6d6d3ca6bcc86bada4642fea709731f1653bd34b28ab15b790e102e14e0d7bd138d80100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ffffff7f00000000000000000000000000000000000000000000000000000000000000000ce39baabcdbb4adce38c5f23314c5f63a536bbcc8f0a47c7054c36ca27f5acd771d095b0000000002000000"
    }
}
```

## getrawtransaction

#### Parameter Description

| name | type | description |
| ---- | ---- | ----------- |
| txid | string | transaction hash |
| verbose | bool | verbose of result |


##### Request
```
{
	"method":"getrawtransaction",
	"params":{"txid": "2458ecfed403ed69414e0609b5505e54ad588ee679949c7e78865947a7b8a96f", "verbose": true}
}
```

##### Response
```
{
    "id": null,
    "error": null,
    "jsonrpc": "2.0",
    "result": {
        "txid": "6864bbf52a3e140d40f1d707bae31d006265efc54dcb58e34037645060ce3e16",
        "hash": "6864bbf52a3e140d40f1d707bae31d006265efc54dcb58e34037645060ce3e16",
        "size": 192,
        "vsize": 192,
        "version": 0,
        "locktime": 1000,
        "vin": [
            {
                "txid": "0000000000000000000000000000000000000000000000000000000000000000",
                "vout": 65535,
                "sequence": 4294967295
            }
        ],
        "vout": [
            {
                "value": "0.01255707",
                "n": 0,
                "address": "8VYXVxKKSAxkmRrfmGpQR2Kc66XhG6m3ta",
                "assetid": "b037db964a231458d2d6ffd5ea18944c4f90e63d547c5d3b9874df66a4ead0a3",
                "outputlock": 0
            },
            {
                "value": "0.02929985",
                "n": 1,
                "address": "ENTogr92671PKrMmtWo3RLiYXfBTXUe13Z",
                "assetid": "b037db964a231458d2d6ffd5ea18944c4f90e63d547c5d3b9874df66a4ead0a3",
                "outputlock": 0
            }
        ],
        "blockhash": "0000000000000000000000000000000000000000000000000000000000000000",
        "confirmations": 4158,
        "time": 1524737766,
        "blocktime": 1524737766,
        "type": 0,
        "payloadversion": 4,
        "payload": {
            "CoinbaseData": "ELA"
        },
        "attributes": [
            {
                "usage": 0,
                "data": "b52165c186769037"
            }
        ],
        "programs": []
    }
}
```

## sendrawtransaction
description: send a raw transaction to node

#### Parameter Description

| name | type | description |
| ---- | ---- | ----------- |
| data | string | raw transaction data in hex |

##### Request
```
{
	"method":"sendrawtransaction",
	"params":{"data":"**************"}
}
```

##### Response
```
{
  "result":"764691821f937fd566bcf533611a5e5b193008ea1ba1396f67b7b0da22717c02",
  "id": null,
  "jsonrpc": "2.0",
  "error": null
}
```


## getblockhash
description: return the hash of the specific blockchain height.

#### Parameter Description
| name | type | description |
| ---- | ---- | ----------- |
| height | integer | the height of blockchain |

##### Request
```
{
	"method":"getblockhash",
	"params":{"height":1}
}
```

##### Response
```
{
    "id": null,
    "jsonrpc": "2.0",
    "result": "3893390c9fe372eab5b356a02c54d3baa41fc48918bbddfbac78cf48564d9d72",
    "error": null
}
```

## discretemining
description: generate one or more blocks instantly  

#### Parameter Description
| name | type | description |
| ---- | ---- | ----------- |
| count | integer | count of blocks | 

##### Request
```
{
	"method":"discretemining",
	"params":{"count":1}
}
```

##### Response
```
{
    "id": null,
    "jsonrpc": "2.0",
    "result": [
        "741d8131f0eea94c1c72c8bb1f0e9051a0a98441e131585bf5bf01868bf0ef46"
    ],
    "error": null
}
```


## getreceivedbyaddress
description: get the balance of an address

#### Parameter Description
| name | type | description |
| ---- | ---- | ----------- |
| address | string | address |

##### Request
```
{
	"method":"getreceivedbyaddress",
	"params":{"address":"8VYXVxKKSAxkmRrfmGpQR2Kc66XhG6m3ta"}
}
```

##### Response
```
{
    "error": null,
    "id": null,
    "jsonrpc": "2.0",
    "result": "33000000"
}
```

## getblockbyheight

##### Request
```
{
    "method":"getblockbyheight",
    "params":{
        "height":0
    }
}
```

##### Response
```
{
    "error": null,
    "id": null,
    "jsonrpc": "2.0",
    "result": {
        "hash": "451e906b7d30e7d1e226675283432ab34e6a44839078d3ab7ef5bd17d8b38281",
        "confirmations": 1,
        "strippedsize": 392,
        "size": 392,
        "weight": 1568,
        "height": 0,
        "version": 0,
        "versionhex": "00000000",
        "merkleroot": "577c2f13b806d953e875bc2b10b7b5c14e9d3abcf21128f782b08ce0357faa92",
        "tx": [
            {
                "txid": "28cb087f7feabb9ff2fc34fb541946e4052e7d0aba50ebbc0773a69c9ca952fe",
                "hash": "28cb087f7feabb9ff2fc34fb541946e4052e7d0aba50ebbc0773a69c9ca952fe",
                "size": 124,
                "vsize": 124,
                "version": 0,
                "locktime": 0,
                "vin": [
                    {
                        "txid": "0000000000000000000000000000000000000000000000000000000000000000",
                        "vout": 0,
                        "sequence": 0
                    }
                ],
                "vout": [
                    {
                        "value": "33000000",
                        "n": 0,
                        "address": "EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                        "assetid": "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                        "outputlock": 0,
                        "outputtype": 0,
                        "outputpayload": null
                    }
                ],
                "blockhash": "451e906b7d30e7d1e226675283432ab34e6a44839078d3ab7ef5bd17d8b38281",
                "confirmations": 1,
                "time": 1513936800,
                "blocktime": 1513936800,
                "type": 0,
                "payloadversion": 4,
                "payload": {
                    "CoinbaseData": ""
                },
                "attributes": [
                    {
                        "usage": 0,
                        "data": "4d65822107fcfd52"
                    }
                ],
                "programs": []
            },
            {
                "txid": "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                "hash": "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                "size": 47,
                "vsize": 47,
                "version": 0,
                "locktime": 0,
                "vin": [],
                "vout": [],
                "blockhash": "451e906b7d30e7d1e226675283432ab34e6a44839078d3ab7ef5bd17d8b38281",
                "confirmations": 1,
                "time": 1513936800,
                "blocktime": 1513936800,
                "type": 1,
                "payloadversion": 0,
                "payload": {
                    "Asset": {
                        "Name": "ELA",
                        "Description": "",
                        "Precision": 8,
                        "AssetType": 0,
                        "RecordType": 0
                    },
                    "Amount": "0",
                    "Controller": "000000000000000000000000000000000000000000"
                },
                "attributes": [],
                "programs": []
            }
        ],
        "time": 1513936800,
        "mediantime": 1513936800,
        "nonce": 2083236893,
        "bits": 486801407,
        "difficulty": "536872896",
        "chainwork": "00000000",
        "previousblockhash": "0000000000000000000000000000000000000000000000000000000000000000",
        "nextblockhash": "0000000000000000000000000000000000000000000000000000000000000000",
        "auxpow": "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        "minerinfo": ""
    }
}
```