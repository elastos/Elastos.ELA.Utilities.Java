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
     
     
## Web Request

 - start command : java -cp ela_tool.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/


 - #### genRawTransaction

##### Request
```
Note: amount is an integer and the smallest unit is sela (1/100000000)

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
                            "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB",
                            "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"
                        },
                        {
                            "txid":"a91b63ba6ffdb13379451895c51abd25c54678bc89268db6e6c3dcbb7bb07062",
                            "index":0,
                            "privateKey":"A65E9FB6735C5FD33F839036B15D2DA373E15AED38054B69386E322C6BE52994",
                            "address":"EgSph8GNaNSMwpv6UseAihsAc5sqSrA7ga"
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
Java-config.json configuration to the Elastos.ELA.AutoTest.Java.jar statistics directory，Host is the IP and port of getting the node server，Fee is a transfer transaction fee

{
  "Host": "127.0.0.1:11336",
  "Fee":5000
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
                    "ZeroAddress":"Edi5WWMFBsEL2qgggrFhnJe1HTjDnw447H"
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
