# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/

## genregistertx
description: register asset transaciton

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| vout| int | the serial number of the tx in which the balance is available 
| privatekey | string | Utxo address corresponds to the private key| 
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
    "method":"genregistertx",
	"params":{
	        "transaction":{
	                "inputs":[
	                    {
	                        "txid":"8add3578e31d57606ab4843805ab98bb9c9fd4bab1f84431efdc1e16e590a916",
	                        "vout":2,
	                        "privatekey":"bed9478121e264145c0734f2e3ef391a402e9fa270d526d151a01771613e054a"
	                    }
	                ],
	                "outputs":[{
	                        "address":"ETGJLEtN79oepAHAuKgUJjuwY7WvqsWHvf",
	                        "amount":"32999879"
	                }],
	                "payload":{
	                        "name":"gaopp",
	                        "description":"123",	
	                	    "precision":18,
	                	    "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
	                	    "amount":10000
	                }
	        }
	}
}
```

##### Response
```
{
    "result": {
        "rawTx": "01000567616F7070033132331200001027000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D4249010013353836373137363831323535383032393533360116A990E5161EDCEF3144F8B1BAD49F9CBB98AB053884B46A60571DE37835DD8A02000000000002E572EE241B5A2F495CCADAE7D08778FCBA95B37E2AC30C5FC3B14DE62C32F9F50A021E19E0C9BAB240000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D4249B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A300E78A4E52B90B0000000000216EE6D18D1E8824BFC2BFDF7EF320D5A92F296BAD0000000001414047B75C7711C9EAA5E6710513A83B066828FA6707D591A95187F78D1FC2668E10B86ED75548BB3859104B40A183F2648297CB3EE3C02EB210E36B0B828E4541E82321039212D6AAF85B2FC952EFDAC9D3F5E4D3798BBF682999B17E88503DF1E96F3CD0AC",
        "txHash": "5AB2CE56073EFE89D3C0CF05B171378A3231F59ADAE4FFEBE1BC41FDFF2DCC24",
        "assetid": "F5F9322CE64DB1C35F0CC32A7EB395BAFC7887D0E7DACA5C492F5A1B24EE72E5"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## genregistertxbyprivatekey
 
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
| privatekey | string | Utxo address corresponds to the private key| 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:32.2ELA | 
| name | string | asset name , only letters and numbers are allowed in English| 
| description | string | asset description | 
| precision | int | asset precision ,0-18 | 
| address | string | register asset address | 
| amount | long | Initial assets balance | 
| changeaddress | string | change address | 

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
    "method":"genregistertxbyprivatekey",
    "params":{
        "transaction":{
                "privatekeys":[
                    {
                        "privatekey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                    }
                ],
                "payload":{
                        "name":"gaopp",
                        "description":"123",
                	    "precision":18,
                	    "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                	    "amount":10000
                },
                "changeaddress":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"
            }
    }
}
```

##### Response
```
{
    "result": {
        "rawTx": "01000567616F707003313233120000102700000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B010013313334393831353930313034343536333130310DBAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0000000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010000000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40000000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B000000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0100000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010100000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40100000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B010000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0200000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010200000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40200000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B020000000000FE52A99C9CA67307BCEB50BA0A7D2E05E4461954FB34FCF29FBBEA7F7F08CB2800000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A370EF26E554B90B000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178BE572EE241B5A2F495CCADAE7D08778FCBA95B37E2AC30C5FC3B14DE62C32F9F50A021E19E0C9BAB24000000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B00000000014140C12AD3A7EF9DBB2591E88F8E119A616F9603F4571CF806D7AD7ADC21397DB1ACF153C32C7E7CA128EB52BBB9118F58893D2525DB4321D7D458324159C90BD07F2321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC",
        "txHash": "B32A9DE867C0E56D0E705B6EF1B1E8F6B50F2A92D6F848A2114410E7D52EBD5D",
        "assetid": "F5F9322CE64DB1C35F0CC32A7EB395BAFC7887D0E7DACA5C492F5A1B24EE72E5"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## gentokentx
description:asset transaction

#### Tips
- Inputs must have an ELA utxo, which is used to construct transaction fee
- Transfer of assets does not consume part of the assets as a fee. Note that the total amount of transferred assets (outputs) must be consistent with the utxo amount (inputs)

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| vout| int | the serial number of the tx in which the balance is available 
| privatekey | string | Utxo address corresponds to the private key|
| assetid | string | asset unique identification | 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:200.01ELA | 

##### Request
```
{
   "method":"gentokentx",
   "params":{
        "transaction":{
                "inputs":[
                    {
                        "txid":"4bd22df531d291d6e35aea6ab4bd31586950037985f1c5459b57d033acb0ae2c",
                        "vout":1,
                        "privatekey":"3807d593ebd67569999cbe011616a21930d42a2b45e81574f2c52792ed1b6414"
                    },
                    {
                        "txid":"c24cee36ebf8938315976c8c4bdeb29ff4d0d328de53caeaaa2a6d8790ccb87f",
                        "vout":1,
                        "privatekey":"3807d593ebd67569999cbe011616a21930d42a2b45e81574f2c52792ed1b6414"
                    }
                ],
                "outputs":[
                	{
                    	"assetid":"2b57ea7d168b4ce199a6e47c9ec35d8b23aefc12e1776b911e717afca7ca3936",
                        "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                        "amount":"100"
                    },
                    {
                    	"assetid":"a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                        "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                        "amount":"0.00000001"
                    }
                ]
        } 
    }
}
```

##### Response
```
{
    "result": {
        "rawTx": "02000100142D32313639343737383933333930323236373738022CAEB0AC33D0579B45C5F185790350695831BDB46AEA5AE3D691D231F52DD24B0100000000007FB8CC90876D2AAAEACA53DE28D3D0F49FB2DE4B8C6C97158393F8EB36EE4CC2010000000000023639CAA7FC7A711E916B77E112FCAE238B5DC39E7CE4A699E14C8B167DEA572B09056BC75E2D6310000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D4249B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3010000000000000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D424900000000014140040A42ED8A114A03DB1304C6EF274BE7811C2B833DD1F3A7C1A528B360E7F0D861C7F4BBF6E549C265CFE5D1FAF20097392C662F5C8A2C160C99FD8F56040FF1232103EBB255ECD2564EE7BC6A53943B077CB07800190EBE325F304F1579E3DA8C2F35AC",
        "txHash": "EEC9C9EB6B15E1A052F3E4B334C58A0A79884394003A7187AD7DDA8C01254029"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## gentokentxbyprivatekey

#### Tips：
- Do not calculate utxo
- No need to calculate the amount of the change address，Get the utxo sorted from small to large, and automatically calculate the change amount according to the output amount.
- java-config.json files need to be placed in the same level directory of the Java program to connect nodes to get utxo

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| privatekey | string | Utxo address corresponds to the private key| 
| assetid | string | asset unique identification | 
| address | string | arrival address | 
| amount | string | asset transfer amount | 
| changeaddress | string | change address | 


##### Request
```
{
    "method":"gentokentxbyprivatekey",
    "params":{
        "transaction":{
                "privatekeys":[
                    {
                        "privatekey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                    }
                ],
                "outputs":[
                	{
                    	"assetid":"f5f9322ce64db1c35f0cc32a7eb395bafc7887d0e7daca5c492f5a1b24ee72e5",
                        "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                        "amount":"100"
                    }
                ],
                "changeaddress":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"
            }
    }
}
```

##### Response
```
{
    "result": {
        "rawTx": "02000100142D333030343733363932343732303830323032320EBAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0000000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010000000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40000000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B000000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0100000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010100000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40100000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B010000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0200000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010200000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40200000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B020000000000FE52A99C9CA67307BCEB50BA0A7D2E05E4461954FB34FCF29FBBEA7F7F08CB28000000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF00000000000003E572EE241B5A2F495CCADAE7D08778FCBA95B37E2AC30C5FC3B14DE62C32F9F509056BC75E2D631000000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178BE572EE241B5A2F495CCADAE7D08778FCBA95B37E2AC30C5FC3B14DE62C32F9F50B1B4C002C33DF0ADDE7C0000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178BB037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3CB651200000000000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B00000000014140D5A2E70EAADC83DACF4536F8EF41A0D15A9886BAC03656D4DF97E6CEADE26C4C2E732A856B99153BFFA2854953CBC407D4C8568311FE7BCDEB4311CAB94A79022321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC",
        "txHash": "0133BDD6212C1411561260ED15EC31641A11F5223951B6C1DD53D01AC1684C3A"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

- #### gentokenmultisigntx
description:multi sign asset transaction

#### Tips
- Inputs must have an ELA utxo, which is used to construct transaction fee
- Transfer of assets does not consume part of the assets as a fee. Note that the total amount of transferred assets (outputs) must be consistent with the utxo amount (inputs)

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| vout| int | the serial number of the tx in which the balance is available 
| privatekey | string | Utxo address corresponds to the private key|
| assetid | string | asset unique identification | 
| address | string | arrival address | 
| amount | string | asset transfer amount | 
| privatekeyscripte |  |private key for generating multi-signature addresses | 
| M | int | sign number | 

##### Request
```
      
{
    "method":"gentokenmultisigntx",
    "params":{
            "transaction":{
                    "inputs":[
                        {
                            "txid":"4bd22df531d291d6e35aea6ab4bd31586950037985f1c5459b57d033acb0ae2c",
                            "vout":0,
                            "address":"8XEsZFhuwEwmy4uT8SL5USCzWAHY6vdVk7"
                        },
                    	{
                            "txid":"5bd488a43e9b27d5096d9832c795e2f9210c8a50e4671e4108c12868c73c6e32",
                            "vout":0,
                            "address":"8XEsZFhuwEwmy4uT8SL5USCzWAHY6vdVk7"
                        }
                    ],
                    "outputs":[
                    	{
                        	"assetid":"2b57ea7d168b4ce199a6e47c9ec35d8b23aefc12e1776b911e717afca7ca3936",
                            "address":"8XEsZFhuwEwmy4uT8SL5USCzWAHY6vdVk7",
                            "amount":"1000"
                        },
                        {
                        	"assetid":"a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0",
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":"0.00018500"
                        }
                    ],
                    "privatekeyscripte":[
                        {
                            "privatekey":"A9FE7748161227F5232B4A37336BD4EA2DBB633304F95F62F20F68C94A2A4E5B"
                        },
                        {
                            "privatekey":"FA667242EEB4AAB69433CF406001520BE1CEE3F2E5266E8BAE17614E7B241FE5"
                        },
                        {
                            "privatekey":"3315EA8114B7758E98AADA635C5A99E1523576EA72B42F156ECBE5B73447E9D6"
                        },
                        {
                            "privatekey":"4849048B13242F83107CAD9F8C0DF4A3698A0DFB37055F11B91A2E5F044557C2"
                        }
                    ],
                	"m":3
                }
        }
}
```

##### Response
```
{
    "result": {
        "rawTx": "020001001331363230333338303732313739313939353037022CAEB0AC33D0579B45C5F185790350695831BDB46AEA5AE3D691D231F52DD24B000000000000326E3CC76828C108411E67E4508A0C21F9E295C732986D09D5279B3EA488D45B000000000000023639CAA7FC7A711E916B77E112FCAE238B5DC39E7CE4A699E14C8B167DEA572B093635C9ADC5DEA000000000000012B136738A98DF218169E1D0CA1E9156FA35BCBD26B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3444800000000000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D42490000000001C340CAF0CC466BECEEADAC7F6C1E02289C992722A3A65DAE1C6318CAAAFDCADCE7D009A23F81533A9A945131015AA2B14C28996D9D6325F44EFDCA060015BC3BA3DF40999337104F71CAA3E5579F3F8FA93E040170A813A71418BA87A0C3852BD27A437C66B3B5639788C0A09EA8EF5E1C1387DBEE3EFEB37510CD3442D15CBE489BE54044A227C7DF9BBDB08A7B3F91DA02001BD69736B7E1B1054B4B740B40B3CB44CAF1E0AE2D2613538FBCE6925C622A218F793954A89D9743DFAE1B68EA20ACD1BF8B53210204A6EA89B5B89CC9D5CBAFF29EA7C59E4F57800722DE2EB560AD31977900CD8F21023D0FEED52F56C39D084E7C320A3B2BFDD62F3AD3149E90B92172C930B608C5B121026013CB2CD04CD5BEFD1FF13AC4C9828FD6240033990DA6CC1936C72A9B9DAE3C210369B9FF8A9B78A5A57A9ADC62115DFFA4CE35577CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "257066E963351A3E77DCC6C649B13209CB2ECD4005C47C41F829C6FF417C0502"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```