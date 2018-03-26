#ela_tool

 - ela_tools is a tool for generating transaction signature,there are two forms of signature,one is to call the API，the other one is the web request




## API


 - #####getPrivateKey
```
/**
* 生成私钥
* @return 返回 json 字符串 
*/
public static String genPrivateKey(){}
```

 - #####getPublicKey
```
/**
* 生成公钥
* @param privateKey 私钥 * @return 返回 json 字符串 
*/
public static String genPublicKey(String privateKey){}
```


 - #####getAddress
```
/**
* 生成地址
* @param privateKye 私钥 *@return 返回Json字符串 
*/
public static String genAddress(String privateKye){}
```


 - #####generate privateKey 、 publickKey 、 address
```
/**
* 生成私钥、公钥、地址 *@return 返回json字符串 
*/
public static String gen_priv_pub_addr(){}
```


 - #####getRawTransaction
```
/**
* 生成 RawTrnsaction
* @param inputsAddOutpus 交易输入和交易输出的 json 字符串 *@return 返回RawTransaction的json字符串
* @throws Exception
*/
public static String genRawTransaction(JSONObject inputsAddOutpus) throws Exception{}
```


 - #####sendRawTransaction
```
/**
* 发送 Rawtransaction 
* @param rawTx
*/
public static void sendRawTransaction(String rawTx){}
```
     
     
     
## Web Request

 - start command : java -cp ela_tool.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/


 - ####getRawTransaction

#####Request
```
{"method":"genRawTransaction","id":0,"params":[{"Transactions":[{"UTXOInputs":[{"txid":"61c22a83bb96d958f473148fa64f3b2be02653c66ede506e83b82e522200d446","index":0,"privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB","address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"},{"txid":"a91b63ba6ffdb13379451895c51abd25c54678bc89268db6e6c3dcbb7bb07062","index":0,"privateKey":"A65E9FB6735C5FD33F839036B15D2DA373E15AED38054B69386E322C6BE52994","address":"EgSph8GNaNSMwpv6UseAihsAc5sqSrA7ga"}],"Outputs":[{"address":"ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt","amount":200},{"address":"EKjeZEmLSXyyJ42xxjJP4QsKJYWwEXabuC","amount":240 }]}]}]}
```

#####Response
```
{"Action":"genRawTransaction","Desc":"SUCCESS","Result":{"rawTx:":"02000100123433323833393539333335353438313239370246D40022522EB8836E50DE6EC65326E02B3B4FA68F1473F458D996BB832AC2610000000000006270B07BBBDCC3E6B68D2689BC7846C525BD1AC59518457933B1FD6FBA631BA900000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3C800000000000000000000002160DB4AE4630D3909CFCEEC9728FC323C00A6089BB037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3F00000000000000000000000211C51ECECC3AC485ED4A6D8253C2B043A160AA07400000000024140C4DC19D47117EE8A6674678E53BE92C326B4B158B78A9A54D4C896500D5536986D2F68E5999EC286872BFB8A0B14286A8C8B8F03166861695C06F235E714EFAD2321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC4140DBC22F20FE590898783C7D75295AF5BB32D0D544F832C45C124562959EA9E9828ADED2AE0A73FE48DE82B4165BF1BA5FBC5E1A25C17C6123ACD516DE25F6354B23210209E8279048B8416373677AC482F4F09131B13B648EEF428885A5F8AFB44EE38FAC","txHash:":"B14A65207B801E991292FED3A4CAB06E29D54A792115BC3D45B7F8235C1A0CF6"}}

```

 - ####getPriavateKey

#####Request
```
{"method":"genPrivateKey","id":0,"params":[]}
```

#####Response
```
{"Action":"genPrivateKey","Desc":"SUCCESS","Result":"94F2D1492963E991EA2878C55754293A627277108C2205C7F0EBC592896726D8"}

```

 - ####getPublicKey

#####Request
```
{"method":"genPublicKey","id":0,"params":[{"PrivateKey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"}]}
```

#####Response
```
{"Action":"genPublicKey","Desc":"SUCCESS","Result":"03B462F4DB3F67A6A71E51BF3034A183022F092E8E6ED0C91F139E4871F5BA0B57"}

```

 - ####getAddress

#####Request
```
{"method":"genAddress","id":0,"params":[{"PrivateKey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"}]}
```

#####Response
```
{"Action":"genAddress","Desc":"SUCCESS","Result":"EPUhMEA8RVxqMEvxGDtC95Cwmm1gjtcsB3"}

```


 - ####get_priv_pub_addr

#####Request
```
{"method":"gen_priv_pub_addr","id":0,"params":[]}
```

#####Response
```
{"Action":"genAddress","Desc":"SUCCESS","Result":{"PrivateKey":"579750E68061727B023FD0AB8A5ABFEE9FC00491220BA2C82402463E5AF3E84A","PublicKey":"0278421F86F850D73A458680EEA36B49679CD09BE3F0D56E969AF8F0761E94BC46","Address":"EZ4u7ewRX3LhUCJYZGENpRVPbeCWU2AdXQ"}}

```

### JAR package
 ```
File -> Project Structure -> Artifacts -> + -> JAR -> From modules with 
1、-> Main Class
2、-> extract to the target JAR
3、-> META-INF PATH （C:\DNA\src\ela_tool\src\main\resources）
4、ok ->  Include in project build -> Apply ->ok

run : java -cp ela_tool.jar  org.elastos.elaweb.HttpServer
 ```

###notice
```
 - Execute the following command in the JAR directory to avoid invalid signature file exception information.
 - Execute ： zip -d ela_tool.jar 'META-INF/*.SF' 'META-INF/*.RSA' 'META-INF/*SF'
```
