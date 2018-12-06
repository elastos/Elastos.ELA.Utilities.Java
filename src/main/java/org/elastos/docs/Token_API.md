# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/

## genRegisterTx
description: register asset transaciton

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| privateKey | string | Utxo address corresponds to the private key| 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:32.2ELA | 
| name | string | asset name , only letters and numbers are allowed in English| 
| description | string | asset description | 
| precision | int | asset precision ,0-18 | 
| address | string | register asset address | 
| amount | long | Initial assets balance | 

##### Request
```
{
    "method":"genRegisterTx",
    "id":0,
	"params":[
	    {
	        "Transactions":[
	            {
	                "UTXOInputs":[
	                    {
	                        "txid":"8add3578e31d57606ab4843805ab98bb9c9fd4bab1f84431efdc1e16e590a916",
	                        "index":2,
	                        "privateKey":"bed9478121e264145c0734f2e3ef391a402e9fa270d526d151a01771613e054a"
	                    }
	                ],
	                "Outputs":[{
	                        "address":"ETGJLEtN79oepAHAuKgUJjuwY7WvqsWHvf",
	                        "amount":"32.2"
	                }],
	                "Payload":{
	                        "name":"Lei",
	                        "description":"asset info",	
	                	    "precision":18,
	                	    "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
	                	    "amount":10000
	                }
	            }
	        ]
	    }
	]
}
```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genRegisterTx",
    "Result": {
        "rawTx": "01000567616F7070034D379******8BBF682999B17E88503DF1E96F3CD0AC",
        "txHash": "18F4B7466D41A60B50A028089A156CA261D0463812AA7E7DB80809F11B1284A8"
    }
}

```

## genRegisterTxByPrivateKey
 
#### Tips：
- Do not calculate utxo
- No need to calculate the amount of the change address，Get the utxo sorted from small to large, and automatically calculate the change amount according to the output amount.
- java-config.json files need to be placed in the same level directory of the Java program to connect nodes to get utxo

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| Host| string | Server IP and RPC ports where the node program resides | 
| Fee| string | The single output or multiple input and output fees of a transaction are the same | 
| Confirmation| string | Number of block confirmation transactions | 
| RegisterAssetFee| string | Minimum transaction fee for registered assets 10 ela |
| privateKey | string | Utxo address corresponds to the private key| 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:32.2ELA | 
| name | string | asset name , only letters and numbers are allowed in English| 
| description | string | asset description | 
| precision | int | asset precision ,0-18 | 
| address | string | register asset address | 
| amount | long | Initial assets balance | 
| ChangeAddress | string | change address | 

- ##### java-config.json

```
{
  "Host": "127.0.0.1:11336",
  "Fee":"5000",
  "Confirmation":"16",
  "RegisterAssetFee":"10"
}

```

##### Request
```
{
    "method":"genRegisterTxByPrivateKey",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "PrivateKeys":[
                        {
                            "privateKey":"4C573939323F11BCDB57B61CCE095D4B1E55E986F9944F88072141F3DFA883A3"
                        }
                    ],
                    "Outputs":[{
	                        "address":"ETGJLEtN79oepAHAuKgUJjuwY7WvqsWHvf",
	                        "amount":"32.2"
	                }],
                    "Payload":{
                            "name":"gaopp",
                            "description":"123",
                    	    "precision":18,
                    	    "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                    	    "amount":10000
                    },
                    "ChangeAddress":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"
                }
            ]
        }
    ]
}
```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genRegisterTxByPrivateKey",
    "Result": {
        "rawTx": "01000567616F7070034D379******8BBF682999B17E88503DF1E96F3CD0AC",
        "txHash": "18F4B7466D41A60B50A028089A156CA261D0463812AA7E7DB80809F11B1284A8"
    }
}

```

## genTokenTx
description:asset transaction

#### Tips
- Inputs must have an ELA utxo, which is used to construct transaction fee
- Transfer of assets does not consume part of the assets as a fee. Note that the total amount of transferred assets (outputs) must be consistent with the utxo amount (inputs)

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| privateKey | string | Utxo address corresponds to the private key|
| assetId | string | asset unique identification | 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:200.01ELA | 

##### Request
```
{
   "method":"genTokenTx",
   "id":0,
   "params":[
    {
        "Transactions":[
            {
                "UTXOInputs":[
                    {
                        "txid":"4bd22df531d291d6e35aea6ab4bd31586950037985f1c5459b57d033acb0ae2c",
                        "index":1,
                        "privateKey":"3807d593ebd67569999cbe011616a21930d42a2b45e81574f2c52792ed1b6414"
                    },
                    {
                        "txid":"c24cee36ebf8938315976c8c4bdeb29ff4d0d328de53caeaaa2a6d8790ccb87f",
                        "index":1,
                        "privateKey":"3807d593ebd67569999cbe011616a21930d42a2b45e81574f2c52792ed1b6414"
                    }
                ],
                "Outputs":[
                	{
                    	"assetId":"2b57ea7d168b4ce199a6e47c9ec35d8b23aefc12e1776b911e717afca7ca3936",
                        "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                        "amount":"100"
                    },
                    {
                    	"assetId":"a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                        "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                        "amount":"0.00000001"
                    }
                ]
            }
        ]    
    }]
}
```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genTokenTx",
    "Result": {
        "rawTx": "020001001335343739078******00190EBE325F304F1579E3DA8C2F35AC",
        "txHash": "4F2845CE8A4DA430A900E197541FF84D7A0AD616DC9AAE474823D6DA8F00EBA5"
    }
}

```

## genTokenTxByPrivateKey

#### Tips：
- Do not calculate utxo
- No need to calculate the amount of the change address，Get the utxo sorted from small to large, and automatically calculate the change amount according to the output amount.
- java-config.json files need to be placed in the same level directory of the Java program to connect nodes to get utxo

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| privateKey | string | Utxo address corresponds to the private key| 
| assetId | string | asset unique identification | 
| address | string | arrival address | 
| amount | string | asset transfer amount | 
| ChangeAddress | string | change address | 


##### Request
```
{
    "method":"genTokenTxByPrivateKey",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "PrivateKeys":[
                        {
                            "privateKey":"4C573939323F11BCDB57B61CCE095D4B1E55E986F9944F88072141F3DFA883A3"
                        }
                    ],
                    "Outputs":[
                    	{
                        	"assetId":"f5f9322ce64db1c35f0cc32a7eb395bafc7887d0e7daca5c492f5a1b24ee72e5",
                            "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                            "amount":"100"
                        }
                    ],
                    "ChangeAddress":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"
                }
            ]
        }
    ]
}
```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genTokenTxByPrivateKey",
    "Result": {
        "rawTx": "020001001335343739078******00190EBE325F304F1579E3DA8C2F35AC",
        "txHash": "4F2845CE8A4DA430A900E197541FF84D7A0AD616DC9AAE474823D6DA8F00EBA5"
    }
}

```

- #### genTokenMultiSignTx
description:multi sign asset transaction

#### Tips
- Inputs must have an ELA utxo, which is used to construct transaction fee
- Transfer of assets does not consume part of the assets as a fee. Note that the total amount of transferred assets (outputs) must be consistent with the utxo amount (inputs)

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| privateKey | string | Utxo address corresponds to the private key|
| assetId | string | asset unique identification | 
| address | string | arrival address | 
| amount | string | asset transfer amount | 
| PrivateKeyScripte |  |private key for generating multi-signature addresses | 
| M | int | sign number | 

##### Request
```
      
{
    "method":"genTokenMultiSignTx",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "UTXOInputs":[
                        {
                            "txid":"4bd22df531d291d6e35aea6ab4bd31586950037985f1c5459b57d033acb0ae2c",
                            "index":0,
                            "address":"8QaosBQZLPNMF5qR3iY3ozmp5KJcDLUgiC"
                        },
                    	{
                            "txid":"5bd488a43e9b27d5096d9832c795e2f9210c8a50e4671e4108c12868c73c6e32",
                            "index":0,
                            "address":"8QaosBQZLPNMF5qR3iY3ozmp5KJcDLUgiC"
                        }
                    ],
                    "Outputs":[
                    	{
                        	"assetId":"2b57ea7d168b4ce199a6e47c9ec35d8b23aefc12e1776b911e717afca7ca3936",
                            "address":"8QaosBQZLPNMF5qR3iY3ozmp5KJcDLUgiC",
                            "amount":"1000"
                        },
                        {
                        	"assetId":"a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":"0.00018500"
                        }
                    ],
                    "PrivateKeyScripte":[
                        {
                            "privateKey":"927f1ff719047e0243150447b9c009fc2f17d67fd413beb965b9a9449d42b9b1"
                        },
                        {
                            "privateKey":"22e388e026234863ba077fe18783bbf7935c49ed08898995e7f5f64db8d51cef"
                        },
                        {
                            "privateKey":"8d57d983f5960f6b3b2ed1d4f7350cfa7fb985580eaf4b9a2d8501384ce27369"
                        },
                        {
                            "privateKey":"38CE04F4EAD95659DB9AD45ED5A77026736890CCA05CFCDD9CA83980CB05D560"
                        }
                    ],
                	"M":3
                }
            ]
        }
    ]
}
```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genTokenMultiSignTx",
    "Result": {
        "rawTx": "020001001335343739078******00190EBE325F304F1579E3DA8C2F35AC",
        "txHash": "4F2845CE8A4DA430A900E197541FF84D7A0AD616DC9AAE474823D6DA8F00EBA5"
    }
}

```