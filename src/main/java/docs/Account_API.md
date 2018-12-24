# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/

## Account
```
Create account to generate keystore.dat file under Java program directory
```

## gentxbyaccount

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
| password | string | account password| 
| address | string |account address | 
| ChangeAddress | string | Change address | 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:30.1ELA | 


- ##### java-config.json

```
{
  "Host": "127.0.0.1:11336",
  "Fee":"5000",
  "Confirmation":"16"
}

```
##### Request
```
Transaction type is 3,use the payload.recordType cannot be Chinese
{
    "method":"gentxbyaccount",
    "params":{
        "transaction":{
            "account":[
                {
                    "password":"12345",
                    "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"
                }
            ],
            "outputs":[
                {
                    "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                    "amount":"3"
                }
            ],
            "changeAddress":"Edi5WWMFBsEL2qgggrFhnJe1HTjDnw447H"
        }
    }
}

```

##### Response
```
{
    "result": {
        "rawTx": "0200010013383331353530313537353535363835373030350DBAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0000000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010000000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40000000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B000000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0100000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010100000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40100000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B010000000000BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0200000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA47010200000000007C4D0C33BBAEAADC4AB4D2B1C66A682C40868544DE892886E1A9FDCDC07592D40200000000000B00C24D895DD1FFDAD36071B7AC63F793C7445B346504B2EF6E12EBD915EB5B020000000000FE52A99C9CA67307BCEB50BA0A7D2E05E4461954FB34FCF29FBBEA7F7F08CB2800000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A300A3E1110000000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D4249B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A32053DF0E55B90B000000000021E1782FF3A250484A368CA7B426B15451CF8A01AF00000000014140FFFB15F277E4A6C2698B221BAF81680549D46A55C3D75426AD594BC8D37F592B174044F70CB30D2EDAEDE667AC147CB88AD92A1CF8586302DA3F83D828CB61B12321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC",
        "txHash": "B75FD6415A4E9A3B17BE1279FDC53A9946E4662A28369A52B8486F84F6E3D9C6"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## createaccount
description:create account 

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 

##### Request
```
{
    "method":"createaccount",
    "id":0,
    "params":{"account":[
        {
            "password":"12345"
        }
    ]}
}
```

##### Response
```
{
    "result": [
        {
            "address": "EQPJ8sfFvWhCTUuyxMypK9H3iqshmHbk5Y",
            "encryptedPrivateKey": "4YI3NxrXgkoXmGnYgDJYMmEaUCRAzaFTLGnsA3udUfzpmnXNkwHcCOSI7jim24Xx",
            "salt": "kas+nHNj13mtFSVhT5P68A==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ],
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## importaccount

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 
| privateKey| string | import privateKey | 

##### Request
```
{
    "method":"importaccount",
    "params":{
       "account":[
            {
                "password":"12345",
                "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
            }
        ]
	}
}
```

##### Response
```
{
    "result": [
        {
            "address": "Eby3aa39hc33dF4RfKCP7iaFnrDuNDhRHB",
            "encryptedPrivateKey": "55y15OPlQxVkVHjl5GsI+UzE9KdpmJXzM1z5Mda2JILuCEvmQhjTBLbumtLqqV8B",
            "salt": "4yE1lSiNpolckdvdBAbg9g==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        },
        {
            "address": "EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
            "encryptedPrivateKey": "C0cOobOPPuo8wPydhUwn4yErtgI3E0sFZNaVrCp8aNcM3rEsK/Ye8mIlUjeZumRZ",
            "salt": "I2wgDwPwCyLrHTXH9CddTw==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ],
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## removeaccount

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 
| address| string | remove account | 

##### Request
```
{
    "method":"removeaccount",
    "params":{
       "account":[
            {
                "password":"12345",
                "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"
            }
        ]
	}
}
```

##### Response
```
{
    "result": [
        {
            "address": "Eby3aa39hc33dF4RfKCP7iaFnrDuNDhRHB",
            "encryptedPrivateKey": "55y15OPlQxVkVHjl5GsI+UzE9KdpmJXzM1z5Mda2JILuCEvmQhjTBLbumtLqqV8B",
            "salt": "4yE1lSiNpolckdvdBAbg9g==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ],
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## exportprivatekey

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 
| address| string | export account | 

##### Request
```
{
    "method":"exportprivatekey",
    "params":{
       "account":[
            {
                "password":"12345",
                "address":"Eby3aa39hc33dF4RfKCP7iaFnrDuNDhRHB"
            }
        ]
	}
}
```

##### Response
```
{
    "result": [
        "883d7a9d52359252cfbb75352765963e80edc822de9e0879baafb7952a7cda87"
    ],
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```


## getaccounts
description:get all account information

##### Request
```
{
       "method":"getaccounts"
   }
```

##### Response
```
{
    "result": [
        {
            "address": "Eby3aa39hc33dF4RfKCP7iaFnrDuNDhRHB",
            "encryptedPrivateKey": "55y15OPlQxVkVHjl5GsI+UzE9KdpmJXzM1z5Mda2JILuCEvmQhjTBLbumtLqqV8B",
            "salt": "4yE1lSiNpolckdvdBAbg9g==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        },
        {
            "address": "EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
            "encryptedPrivateKey": "C0cOobOPPuo8wPydhUwn4yErtgI3E0sFZNaVrCp8aNcM3rEsK/Ye8mIlUjeZumRZ",
            "salt": "I2wgDwPwCyLrHTXH9CddTw==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ],
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## getaccountaddresses
description:get all account address

##### Request
```
{
    "method":"getaccountaddresses"
}
```

##### Response
```
{
    "result": "[Eby3aa39hc33dF4RfKCP7iaFnrDuNDhRHB, EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB]",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```


## getaccountamount
description:get all account address

##### Request
```
{
    "method":"getaccountamount"
}
```

##### Response
```
{
    "result": {
        "EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB": "33000000.16742768",
        "Eby3aa39hc33dF4RfKCP7iaFnrDuNDhRHB": "0"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```
