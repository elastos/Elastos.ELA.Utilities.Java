#  Elastos.ELA.Utilities.Java

 -  Elastos.ELA.Utilities.Java is a tool for generating transaction signature,there are two forms of signature,one is to call the API，the other one is the web request


## API


 - ##### genPrivateKey
```
/**
* generate privatekey
* @return returns a json string 
*/
public static String genPrivateKey(){}
```

 - ##### genPublicKey
```
/**
* generate publickey
* @param privateKey  
* @return returns a json string 
*/
public static String genPublicKey(String privateKey){}
```


 - ##### genAddress
```
/**
* generate Address
* @param privateKye  
* @return returns a json string 
*/
public static String genAddress(String privateKye){}
```


 - ##### generate privateKey 、 publickKey 、 address
```
/**
* generate privatekey、publickey、address 
* @return returns a json string  
*/
public static String gen_priv_pub_addr(){}
```


 - ##### genRawTransaction
```
/**
* generate rawTrnsaction
* @param  inputsAddOutpus , The json string of transaction input and transaction output
* @return returns a json string
* @throws Exception
*/
public static String genRawTransaction(JSONObject inputsAddOutpus) throws Exception{}
```


 - ##### sendRawTransaction
```
/**
* send Rawtransaction 
* @param rawTx
*/
public static void sendRawTransaction(String rawTx){}
```
   
 - ##### decodeRawTransaction
```
 /**
 * Anti parsing rawTransaction gets TXid,address,value
 * @param rawTransaction
 * @return
 * @throws IOException
 */
 public static String decodeRawTransaction(String rawTransaction) throws IOException {
```     

- ##### checkAddress
 ```
/**
 * whether the checkout address is ELA legal address
 * @param addresses The address of a dictionary format or an array format
 * @return
 */
public static String checkAddress(JSONObject addresses){
 ```      

## Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/


 - #### genRawTransaction

##### Request
```

Note: amount is an integer and the smallest unit is sela (1/100000000).

Index is the 'Vout' of the transaction information.
Index is the 'n' of the block information Vout.

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
                        },
                        {
                            "txid":"a91b63ba6ffdb13379451895c51abd25c54678bc89268db6e6c3dcbb7bb07062",
                            "index":0,
                            "privateKey":"A65E9FB6735C5FD33F839036B15D2DA373E15AED38054B69386E322C6BE52994"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt",
                            "amount":200
                        },
                        {
                            "address":"EKjeZEmLSXyyJ42xxjJP4QsKJYWwEXabuC",
                            "amount":240
                        }
                    ]
                }
            ]
        }
    ]
}

or
Transaction type is 3,use the payload.

{
    "method":"genRawTransaction",recordType cannot be Chinese
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
                        },
                        {
                            "txid":"a91b63ba6ffdb13379451895c51abd25c54678bc89268db6e6c3dcbb7bb07062",
                            "index":0,
                            "privateKey":"A65E9FB6735C5FD33F839036B15D2DA373E15AED38054B69386E322C6BE52994"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt",
                            "amount":200
                        },
                        {
                            "address":"EKjeZEmLSXyyJ42xxjJP4QsKJYWwEXabuC",
                            "amount":240
                        }
                    ],
                    "PayloadRecord":{
                            "recordType":"en",
                            "recordData":"enen"
                    },
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
        "rawTx":"02000100123433323833393539333335353438313239370246D40022522EB8836E50DE6EC65326E02B3B4FA68F1473F458D996BB832AC2610000000000006270B07BBBDCC3E6B68D2689BC7846C525BD1AC59518457933B1FD6FBA631BA900000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3C800000000000000000000002160DB4AE4630D3909CFCEEC9728FC323C00A6089BB037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3F00000000000000000000000211C51ECECC3AC485ED4A6D8253C2B043A160AA07400000000024140C4DC19D47117EE8A6674678E53BE92C326B4B158B78A9A54D4C896500D5536986D2F68E5999EC286872BFB8A0B14286A8C8B8F03166861695C06F235E714EFAD2321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC4140DBC22F20FE590898783C7D75295AF5BB32D0D544F832C45C124562959EA9E9828ADED2AE0A73FE48DE82B4165BF1BA5FBC5E1A25C17C6123ACD516DE25F6354B23210209E8279048B8416373677AC482F4F09131B13B648EEF428885A5F8AFB44EE38FAC",
        "txHash":"B14A65207B801E991292FED3A4CAB06E29D54A792115BC3D45B7F8235C1A0CF6"
    }
}

```

 - #### decodeRawTransaction

##### Request

```
{
    "method":"decodeRawTransaction",
    "id":0,
    "params":[
        {
            "RawTransaction":"02000100142D37323733373430363730363936353637333337015220F787A81709244D9987606E77A74411F61D7E20930924F81A1F4815DEBA2200000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A30070AE1993A70A000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B00000000014140E62D5E3E8E14B33377F7EA7301968B81163959A572178CC555F184B2F5239BB683B62E6F178E4C07D6B0D43F780A289488634E4B477197196B8F95581ACA1322232102EE009B86F9377820B1DE396888E7456FDE2554E77E1D9A1AB3360562F1D6FF4BAC"
        }
    ]
}

```

##### Response
```
{
    "Action":"decodeRawTransaction",
    "Desc":"SUCCESS",
    "Result":{
        "UTXOInputs":[
            {
                "Txid":"22BADE15481F1AF8240993207E1DF61144A7776E6087994D240917A887F72052"
            }
        ],
        "Outputs":[
            {
                "Address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                "Value":2999000000000000
            }
        ]
    }
}
```

 - #### genPrivateKey

##### Request
```
{
    "method":"genPrivateKey",
    "id":0,
    "params":[

    ]
}
```

##### Response
```
{
    "Action":"genPrivateKey",
    "Desc":"SUCCESS",
    "Result":"94F2D1492963E991EA2878C55754293A627277108C2205C7F0EBC592896726D8"
}

```

 - #### genPublicKey

##### Request
```
{
    "method":"genPublicKey",
    "id":0,
    "params":[
        {
            "PrivateKey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"
        }
    ]
}
```

##### Response
```
{
    "Action":"genPublicKey",
    "Desc":"SUCCESS",
    "Result":"03B462F4DB3F67A6A71E51BF3034A183022F092E8E6ED0C91F139E4871F5BA0B57"
}

```

 - #### genAddress

##### Request
```
{
    "method":"genAddress",
    "id":0,
    "params":[
        {
            "PrivateKey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"
        }
    ]
}

```

##### Response
```
{
    "Action":"genAddress",
    "Desc":"SUCCESS",
    "Result":"EPUhMEA8RVxqMEvxGDtC95Cwmm1gjtcsB3"
}

```

 - #### genIdentityID

##### Request
```
{
    "method":"genIdentityID",
    "id":0,
    "params":[
        {
            "PrivateKey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"
        }
    ]
}

```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genIdentityID",
    "Result": "iUEuVMUGV89NEcZtC2UVu2grUaMaEb2jcT"
}

```


 - #### gen_priv_pub_addr

##### Request
```
{
    "method":"gen_priv_pub_addr",
    "id":0,
    "params":[

    ]
}
```

##### Response
```
{
    "Action":"genAddress",
    "Desc":"SUCCESS",
    "Result":{
        "PrivateKey":"579750E68061727B023FD0AB8A5ABFEE9FC00491220BA2C82402463E5AF3E84A",
        "PublicKey":"0278421F86F850D73A458680EEA36B49679CD09BE3F0D56E969AF8F0761E94BC46",
        "Address":"EZ4u7ewRX3LhUCJYZGENpRVPbeCWU2AdXQ"
    }
}
```


 - #### genRawTransactionByPrivateKey


```
java-config.json configuration to the Elastos.ELA.AutoTest.Java.jar same level directory，Host is the IP and port of getting the node server，Fee is a transfer transaction fee,confirmations is a transaction  confirmations number(default 16)

{
  "Host": "127.0.0.1:11336",
  "Fee":5000，
  "Confirmation":16
}
```

##### Request
```
 Note: amount is an integer and the smallest unit is sela (1/100000000)
 
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
                        },
                        {
                            "privateKey":"4C573939323F11BCDB57B61CCE095D4B1E55E986F9944F88072141F3DFA883A3"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                            "amount":28900000
                        },
                        {
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":60000000
                        }
                    ],
                    "ChangeAddress":"Edi5WWMFBsEL2qgggrFhnJe1HTjDnw447H"
                }
            ]
        }
    ]
}

or
Transaction type is 3,use the payload.recordType cannot be Chinese
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
                        },
                        {
                            "privateKey":"4C573939323F11BCDB57B61CCE095D4B1E55E986F9944F88072141F3DFA883A3"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                            "amount":28900000
                        },
                        {
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":60000000
                        }
                    ],
                    "PayloadRecord":{
                            "recordType":"en",
                            "recordData":"enen"
                    },
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
        "rawTx": "0200010013323235393337353731373836393133373537300578CBEA93AEFD96213B537422745D59543AA0E9A82307703D6A21F4E33368D0950100000000004192AD2CAEF73174B7F7CA5A88E734C1320B07CC682AA5A2ACC3C8AB3A34C6D5000000000000E16BBF9C6AAE6485B58733189BFDBB45068FFF99C9A4C60B0B4E0F95A033213900000000000078CBEA93AEFD96213B537422745D59543AA0E9A82307703D6A21F4E33368D095000000000000F8E3223A17EBF1E9740F5B75697C26EEF0EFAC68B484D0905DCD9C623C0BCF5800000000000003B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3A0FAB801000000000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178BB037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3008793030000000000000000214FFBC4FB3B3C30A626A3B298BFA392A0121D4249B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A37FC81000000000000000000021E1782FF3A250484A368CA7B426B15451CF8A01AF00000000024140580BCA995B68554A20655729868DDF99B7EE9FF69CA48BD9EE59403DB7A91167AC127697EA7F92A7423BCDDC1B61A63241E7A80FFCE9299A9AD0C4DF35DE0E512321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC4140200A8BD3C6E908A7DC78CCE95BA13A883CE92E0716513B00ED73FD7D439AF80CD44B41A5834FE1B1C61465A700B9E000E6A8DBB1A426C8CE5F9463B37ED85ECA232102EE009B86F9377820B1DE396888E7456FDE2554E77E1D9A1AB3360562F1D6FF4BAC",
        "txHash": "A32203B48C740552AF0CDB1E77ECCEBE147C5CDA51B2BD80BA9C59662CDCD322"
    }
}
```

 - #### checkAddress

##### Request
```
checkout address support dictionary format and array format

{
    "method":"checkAddress",
    "id":0,
    "params":[
        {
            "Addresses":[
            	{
            		"address":"EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZz"
            	},
            	{
            		"address":"1C1mCxRukix1KfegAY5zQQJV7samAciZpv"
            	},
            	{
            		"address":"8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT"
            	},
            	{
            		"address":"XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU"
            	}
            	]
        }
    ]
}

or 

{
    "method":"checkAddress",
    "id":0,
    "params":[
        {
            "Addresses":["EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZz","1C1mCxRukix1KfegAY5zQQJV7samAciZpv","8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT","XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU"]
        }
    ]
}
```

##### Response
```
{
    "Action": "checkAddress",
    "Desc": "SUCCESS",
    "Result": {
        "EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZz": true,
        "1C1mCxRukix1KfegAY5zQQJV7samAciZpv": false,
        "8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT": true,
        "XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU": false
    }
}

```
### Account
```
Create account to generate keystore.dat file under Java program directory
```


 - #### genRawTransactionByAccount

##### Request
```
{
    "method":"genRawTransactionByAccount",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "Account":[
                        {
                            "password":"12345",
                            "address":"ENKj2J5dGjSRgHHxBZ3yLjB6RyXvHikW5K"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":3
                        }
                    ],
                    "PayloadRecord":{
                            "recordType":"en",
                            "recordData":"enen"
                    },
                    "ChangeAddress":"Edi5WWMFBsEL2qgggrFhnJe1HTjDnw447H"
                }
            ]
        }
    ]
}

or
Transaction type is 3,use the payload.recordType cannot be Chinese
{
    "method":"genRawTransactionByAccount",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "Account":[
                        {
                            "password":"12345",
                            "address":"ENKj2J5dGjSRgHHxBZ3yLjB6RyXvHikW5K"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":3
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
    "Action": "genRawTransactionByAccount",
    "Desc": "SUCCESS",
    "Result": {
        "rawTx": "0200010013363731323032A214F6036D8221E7F1C010000000000B67901062723993D9FBADC32C33807EE7B9FCB370D777C5A955A3056DF3B9CB40100000000002B20EB60E4191B374DFC69503E1ED6888E7456FDE2554E77E1D9A1AB3360562F1D6FF4BAC",
        "txHash": "98FCA1FABB606AEDF84921E24CDF7A0931FF11DAFDDC1B8584206881A5229060"
    }
}

```

 - #### createAccount

##### Request
```
{
    "method":"createAccount",
    "id":0,
    "params":[
        {
            "password":"12345"
        }
    ]
}
```

##### Response
```
{
    "Action": "createAccount",
    "Desc": "SUCCESS",
    "Result": [
        {
            "address": "EbrydLF4BuJ7mPJYpxk7qzwx1CirFCDcPg",
            "encryptedPrivateKey": "8xHdZwJFeYLs0zRRf4uvxZThhGUA0jwwkae/WFxIXbf1aBy3Hm+iTbuVtkusFYiA",
            "salt": "ZUPbh6H6LcNv6PZ64e1HPw==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ]
}
```

 - #### importAccount

##### Request
```
{
    "method":"importAccount",
    "id":0,
    "params":[
    	{
	       "Account":[
	            {
	                "password":"12345",
	                "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
	            }
	        ]
    	}
    ]
}
```

##### Response
```
{
    "Action": "importAccount",
    "Desc": "SUCCESS",
    "Result": [
        {
            "address": "EKNh1wS42ur6Pfai6DthfH61vDUmg8M93v",
            "encryptedPrivateKey": "sbMP2vWA/5rDMUT0aB22rLkS+QA7zHVXS3xEhJjhzEX0YPR4+HkPaQ8QSAg5LRJw",
            "salt": "sE7vPJSD2pnd1qDjIsHXnw==",
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
            "encryptedPrivateKey": "+PVIa2LLfWWwsfRIWGE1BwGGqg1YtnG5jBHepI6x26TtEB12y2jf0m6FCp7Tc9BN",
            "salt": "Zu0sQcTq14ce7mCcP9emkA==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ]
}
```

 - #### removeAccount

##### Request
```
{
    "method":"removeAccount",
    "id":0,
    "params":[
    	{
	       "Account":[
	            {
	                "password":"12345",
	                "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"
	            }
	        ]
    	}
    ]
}
```

##### Response
```
{
    "Action": "removeAccount",
    "Desc": "SUCCESS",
    "Result": [
        {
            "address": "EKNh1wS42ur6Pfai6DthfH61vDUmg8M93v",
            "encryptedPrivateKey": "sbMP2vWA/5rDMUT0aB22rLkS+QA7zHVXS3xEhJjhzEX0YPR4+HkPaQ8QSAg5LRJw",
            "salt": "sE7vPJSD2pnd1qDjIsHXnw==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ]
}
```

 - #### exportPrivateKey

##### Request
```
{
    "method":"exportPrivateKey",
    "id":0,
    "params":[
    	{
	       "Account":[
	            {
	                "password":"12345",
	                "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"
	            },
	            {
	                "password":"12345",
	                "address":"EKNh1wS42ur6Pfai6DthfH61vDUmg8M93v"
	            }
	        ]
    	}
    ]
}
```

##### Response
```
{
    "Action": "exportPrivateKey",
    "Desc": "SUCCESS",
    "Result": [
        "5fa927e5664e563f019f50dcd4d7e2d9404f2d5d49e31f9482912e23d6d7b9eb",
        "06d243f6835ced1253c6cd939de12d4f482922b86c165d532384368ea2bbe72b"
    ]
}
```


 - #### getAccounts

##### Request
```
{
    "method":"getAccounts",
    "id":0,
    "params":[
    
    ]
}
```

##### Response
```
{
    "Action": "getAccounts",
    "Desc": "SUCCESS",
    "Result": [
        {
            "address": "EKNh1wS42ur6Pfai6DthfH61vDUmg8M93v",
            "encryptedPrivateKey": "sbMP2vWA/5rDMUT0aB22rLkS+QA7zHVXS3xEhJjhzEX0YPR4+HkPaQ8QSAg5LRJw",
            "salt": "sE7vPJSD2pnd1qDjIsHXnw==",
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
            "encryptedPrivateKey": "+PVIa2LLfWWwsfRIWGE1BwGGqg1YtnG5jBHepI6x26TtEB12y2jf0m6FCp7Tc9BN",
            "salt": "Zu0sQcTq14ce7mCcP9emkA==",
            "scrypt": {
                "dkLen": 64,
                "n": 16384,
                "p": 8,
                "r": 8
            },
            "version": "1.0"
        }
    ]
}
```

 - #### getAccountAddresses

##### Request
```
{
    "method":"getAccountAddresses",
    "id":0,
    "params":[
    ]
}
```

##### Response
```
{
    "Action": "getAccountAddresses",
    "Desc": "SUCCESS",
    "Result": "[EKNh1wS42ur6Pfai6DthfH61vDUmg8M93v, EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB]"
}
```

 - #### genMultiSignAddress

##### Request
```
{
    "method":"genMultiSignAddress",
    "id":0,
    "params":[
        {
			"PrivateKeys":[
                {
                    "privateKey":"A9FE7748161227F5232B4A37336BD4EA2DBB633304F95F62F20F68C94A2A4E5B"
                },
                {
                    "privateKey":"FA667242EEB4AAB69433CF406001520BE1CEE3F2E5266E8BAE17614E7B241FE5"
                },
                {
                    "privateKey":"3315EA8114B7758E98AADA635C5A99E1523576EA72B42F156ECBE5B73447E9D6"
                },
                {
                    "privateKey":"4849048B13242F83107CAD9F8C0DF4A3698A0DFB37055F11B91A2E5F044557C2"
                }
            ],
            "M":3       
        }
   ]
}
```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genMultiSignAddress",
    "Result": "8XEsZFhuwEwmy4uT8SL5USCzWAHY6vdVk7"
}
```



 - #### genMultiSignRawTransaction

##### Request
```
      
{
    "method":"genMultiSignRawTransaction",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "UTXOInputs":[
                        {
                            "txid":"0b46ee22fd9a074e6c1a2a8af4a6e06d91061c64250633ccf2ef2b7da74db3b0",
                            "index":0,
                            "address":"8XEsZFhuwEwmy4uT8SL5USCzWAHY6vdVk7"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"8XEsZFhuwEwmy4uT8SL5USCzWAHY6vdVk7",
                            "amount":200000
                        }
                    ],
                    "PrivateKeyScripte":[
                        {
                            "privateKey":"A9FE7748161227F5232B4A37336BD4EA2DBB633304F95F62F20F68C94A2A4E5B"
                        },
                        {
                            "privateKey":"FA667242EEB4AAB69433CF406001520BE1CEE3F2E5266E8BAE17614E7B241FE5"
                        },
                        {
                            "privateKey":"3315EA8114B7758E98AADA635C5A99E1523576EA72B42F156ECBE5B73447E9D6"
                        },
                        {
                            "privateKey":"4849048B13242F83107CAD9F8C0DF4A3698A0DFB37055F11B91A2E5F044557C2"
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
    "Action": "genMultiSignRawTransaction",
    "Result": {
        "rawTx": "02000100132D39353032333632323639300001B037DB964A033990D77CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "0605EE84FA7C28B353806E00CC40477487586A9A03AAAD7154DBE0AD4197E15F"
    }
}
```



 - #### genCrossChainRawTransaction

##### Request
```
  {
      "method":"genCrossChainRawTransaction",
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
                              "amount":70000
                          },
                          {
                              "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                              "amount":999800000
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
                              "amount":60000
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
    "Action": "genCrossChainRawTransaction",
    "Result": {
        "rawTx": "02000100132D39353032333632323639300001B037DB964A033990D77CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "0605EE84FA7C28B353806E00CC40477487586A9A03AAAD7154DBE0AD4197E15F"
    }
}
```


 - #### genCrossChainRawTransaction

##### Request
```
      
{
    "method":"genCrossChainMultiSignRawTransaction",
    "id":0,
    "params":[
        {
            "Transactions":[
                {
                    "UTXOInputs":[
                        {
                            "txid":"723523b9f92394b9428c7abf4d13d7049efeba3c3a154e1b88e96ff6b95bd608",
                            "index":0,
                            "address":"8NRxtbMKScEWzW8gmPDGUZ8LSzm688nkZZ"
                        }
                    ],
                    "Outputs":[
                        {
                            "address":"XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ",
                            "amount":30000
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
                            "privateKey":"22e388e026234863ba077fe18783bbf7935c49ed08898995e7f5f64db8d51cef"
                        }
                    ],
                    "CrossChainAsset":[
                        {
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                            "amount":20000
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
    "Action": "genCrossChainMultiSignRawTransaction",
    "Result": {
        "rawTx": "02000100132D39353032333632323639300001B037DB964A033990D77CBFD9E9BE08651456BB7C2A0854AE",
        "txHash": "0605EE84FA7C28B353806E00CC40477487586A9A03AAAD7154DBE0AD4197E15F"
    }
}
```

### JAR package
 ```
File -> Project Structure -> Artifacts -> + -> JAR -> From modules with 
1、-> Main Class
2、-> extract to the target JAR
3、-> META-INF PATH （C:\DNA\src\ela_tool\src\main\resources）
4、ok ->  Include in project build -> Apply ->ok

run : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 ```

### notice
```
 - Execute the following command in the JAR directory to avoid invalid signature file exception information.
 - Execute ： zip -d Elastos.ELA.Utilities.Java.jar 'META-INF/*.SF' 'META-INF/*.RSA' 'META-INF/*SF'
```
