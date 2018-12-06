# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/

## genRawTransaction
description:constructing single sign transaction

#### Tips :

- You need to calculate the zero-finding address balance, which is equal to input-outputs-fee, and write the zero-finding address and balance on the last line of outputs.

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| privateKey | string | Utxo address corresponds to the private key| 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:200.01ELA | 


##### Request
```
{
    "method":"genRawTransaction",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "UTXOInputs":[
                        {
                            "txid":"61c22a83bb96d958f473148fa64f3b2be02653c66ede506e83b82e522200d446",
                            "index":0,  
                            "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt",
                            "amount":"200.01"
                        }
                    ]
                }
            ]
        }
    ]
}
```

##### Response
```
{
    "Action":"genRawTransaction",
    "Desc":"SUCCESS",
    "Result":{
        "rawTx":"02000100123433323833393506******E29D54A792115BC3D45B7F8235C1A0CF6"
    }
}

```

## genRawTransactionByPrivateKey

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
| privateKey | string | Utxo address corresponds to the private key，Automatic acquisition of utxo| 
| address | string | arrival address | 
| amount | string | the unit is ELA,Namely:289.1ELA | 
| ChangeAddress | string | Change address | 

#### 接口名：genRawTransactionByPrivateKey

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
{
    "method":"genRawTransactionByPrivateKey",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "PrivateKeys":[
                        {
                            "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                            "amount":"289.1"
                        }
                    ],
                    "ChangeAddress":"Edi5WWMFBsEL2qgggrFhnJe1HTjDnw447H"
                }
            ]
        }
    ]
}

```

##### Response

```
{
    "Action": "genRawTransaction",
    "Desc": "SUCCESS",
    "Result": {
        "rawTx": "0200010013323235E5F9463B37EDE39688**********8E7456FDE2554E77E1D9A1AB3360562F1D6FF4BAC",
        "txHash": "A32203B48C740552AF0CDB1E77ECCEBE147C5CDA51B2BD80BA9C59662CDCD322"
    }
}
```



## genMultiSignTx
description:multi sign transaction

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| address | string | multi sign address| 
| amount | string | the unit is ELA,Namely:200.01ELA | 
| PrivateKeyScripte |  |private key for generating multi-signature addresses | 
| M | int | sign number | 

##### Request

```
      
{
    "method":"genMultiSignTx",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "UTXOInputs":[
                        {
                            "txid":"02d79aeae97d879f850e0c7ebb9bb62d93d1fbe4665d8e895106636e9e2656ae",
                            "index":1,
                            "address":"8QaosBQZLPNMF5qR3iY3ozmp5KJcDLUgiC"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt",
                            "amount":"20"
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
    "Action": "genMultiSignTx",
    "Result": {
        "rawTx": "02000100133534303833363530******AF0BED79099FB1C48FDB3F54AE",
        "txHash": "21EC2F2F892518A770D4438935FEA0BA7F918019FEBCD63224ABA24BFA6A6D88"
    }
}
```

## genCrossChainTx
description:cross chain single sign transcation

#### Tips：
- recharge：The x address is generated by hash in the Genesis Block of the Side Chain
- withdraw：address = "0000000000000000000000000000000000"

#### Parameter Description
| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| PrivateKeyScripte |  |Private key corresponding to the address of inputs | 
| CrossChainAsset |  |Side Chain to Account Address and Balance | 

##### Request
```
  {
      "method":"genCrossChainTx",
      "id":0,
      "params":[
          {
              "Transactions":[
                  {
                      "UTXOInputs":[
                          {
                              "txid":"3a6b2653dc2dcc0f065e7d955bbe0e3bc71a2d7f44900fc1cb75402af89fd978",
                              "index":1,
                              "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"
                          }
                      ],
                      "Outputs":[
                          {
                              "address":"XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ",
                              "amount":"70"
                          },
                          {
                              "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                              "amount":"99"
                          }
                      ],
                      "PrivateKeySign":[
                          {
                              "privateKey":"4849048B13242F83107CAD9F8C0DF4A3698A0DFB37055F11B91A2E5F044557C2"
                          }
                      ],
                      "CrossChainAsset":[
                          {
                              "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                              "amount":"60"
                          }
                      ]
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
    "Action": "genCrossChainTx",
    "Result": {
        "rawTx": "02000100132D39353032333******B964A033990D77CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "0605EE84FA7C28B353806E00CC40477487586A9A03AAAD7154DBE0AD4197E15F"
    }
}
```


## genCrossChainMultiSignTx
description:cross chain multi sign transcation

#### Tips：
- recharge：The x address is generated by hash in the Genesis Block of the Side Chain
- withdraw：address = "0000000000000000000000000000000000"

#### Parameter Description
| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| PrivateKeyScripte |  |Private key corresponding to the address of inputs | 
| CrossChainAsset |  |Side Chain to Account Address and Balance | 
| PrivateKeyScripte |  |private key for generating multi-signature addresses | 
| M | int | sign number | 

##### Request
```
      
{
    "method":"genCrossChainMultiSignTx",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "UTXOInputs":[
                        {
                            "txid":"723523b9f92394b9428c7abf4d13d7049efeba3c3a154e1b88e96ff6b95bd608",
                            "index":0,
                            "address":"8QaosBQZLPNMF5qR3iY3ozmp5KJcDLUgiC"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ",
                            "amount":"30000"
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
                    "CrossChainAsset":[
                        {
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":"20000"
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
    "Action": "genCrossChainMultiSignTx",
    "Result": {
        "rawTx": "02000100132D3935303233363232363******3990D77CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "0605EE84FA7C28B353806E00CC40477487586A9A03AAAD7154DBE0AD4197E15F"
    }
}
```

## genCrossChainTxByPrivateKey

#### Tips：
- Do not calculate utxo
- No need to calculate the amount of the change address，Get the utxo sorted from small to large, and automatically calculate the change amount according to the output amount.
- recharge：The x address is generated by hash in the Genesis Block of the Side Chain
- withdraw：address = "0000000000000000000000000000000000"

#### Parameter Description：

| name  | type | description |
| ------ | ---- | ----------- |
| privateKey | string | Utxo address corresponds to the private key，Automatic acquisition of utxo| 
| address | string | arrival address | 
| CrossChainAsset |  |Side Chain to Account Address and Balance | 
| ChangeAddress | string | Change address | 

##### Request
```
        
{
    "method":"genCrossChainTxByPrivateKey",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "PrivateKeys":[
                        {
                            "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                        }
                    ],  
                    "Outputs":[
                        {
                            "address":"XLC69K4932zZf1SRwJCDbv5HGk7DbDYZ9H",
                            "amount":"100000"
                        }
                    ],
                    "CrossChainAsset":[
                        {
                            "address":"ESH5SrT7GZ4uxTH6aQF3ne7X8AUzWdREzz",
                            "amount":"20000"
                        }
                    ],
                   
                    "ChangeAddress":"Edi5WWMFBsEL2qgggrFhnJe1HTjDnw447H"
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
    "Action": "genCrossChainTxByPrivateKey",
    "Result": {
        "rawTx": "02000100132D393530323336323236393000******DB964A033990D77CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "0605EE84FA7C28B353806E00CC40477487586A9A03AAAD7154DBE0AD4197E15F"
    }
}
```