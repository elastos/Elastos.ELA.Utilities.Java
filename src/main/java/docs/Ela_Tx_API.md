# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/

## genrawtransaction
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
    "method":"genrawtransaction",
    "params":{
        "transaction":{
            "inputs":[
                {
                    "txid":"28cb087f7feabb9ff2fc34fb541946e4052e7d0aba50ebbc0773a69c9ca952fe",
                    "index":0,
                    "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                }
            ],
            "outputs":[
                {
                    "address":"ENvYuRCpY42hX8gYAcdbwiD2GNZmUWWSix",
                    "amount":"2"
                },
                {
                    "address":"ENvYuRCpY42hX8gYAcdbwiD2GNZmUWWSix",
                    "amount":"2"
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
        "rawTx": "02000100142D3233373338323136323931343236333631343501FE52A99C9CA67307BCEB50BA0A7D2E05E4461954FB34FCF29FBBEA7F7F08CB2800000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A300C2EB0B0000000000000000213F4A290A0337A43B5B1473ECD5E7697608E8B84DB037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A300C2EB0B0000000000000000213F4A290A0337A43B5B1473ECD5E7697608E8B84D000000000141403A1D92C100A5BE406465B7B3B40CAD8FB182B9DDA9101D8CBA7A862C59ED3C2EA7CFCC72F662949807466AF63FFC1DFA220A1A5BA5A1A623F9A06C821451337C2321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC",
        "txHash": "4382093E71F362AF07E3D5157503E687BBA007D3A974EC27A6A832C785B4D2B0"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## genrawtransactionbyprivatekey

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

#### 接口名：genrawtransactionbyprivatekey

- ##### java-config.json

```
{
  "Host": "127.0.0.1:11336",
  "Fee":"0.0005",
  "Confirmation":16
}

```

##### Request

```
{
    "method":"genrawtransactionbyprivatekey",
    "params":{
        "transaction":{
            "privateKeys":[
                {
                    "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                }
            ],
            "outputs":[
                {
                    "address":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1",
                    "amount":"0.01"
                }
            ],
            "changeAddress":"Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"
        }
    }
}

```

##### Response

```
{
    "result": {
        "rawTx": "02000100133438343038373131363635333332393832303901BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF00000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A340420F00000000000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178BB037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A38B230300000000000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B0000000001414067ABA5E897F183F155A3DFB62D1BF1CE6002CE07CE999DBC1CDDAD391486631BD1F9BF9191F0F48813186727EC0EDF3ED38EDFD38EB793A9FAEE0F18F42D69D32321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC",
        "txHash": "CE0285BC67E040057D5EBC4B3E14D02DC2326DA8ABB2D209965EE5D8463812A7"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```



## genmultisigntx
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
    "method":"genmultisigntx",
    "params":{
        "transaction":{
            "inputs":[
                {
                    "txid":"02d79aeae97d879f850e0c7ebb9bb62d93d1fbe4665d8e895106636e9e2656ae",
                    "index":1,
                    "address":"8ZNizBf4KhhPjeJRGpox6rPcHE5Np6tFx3"
                }
            ],
            "outputs":[
                {
                    "address":"ERz34iKa4nGaGYVtVpRWQZnbavJEe6PRDt",
                    "amount":"20"
                }
            ],
            "privateKeyScripte":[
                {
                    "privateKey":"927f1ff719047e0243150447b9c009fc2f17d67fd413beb965b9a9449d42b9b1"
                },
                {
                    "privateKey":"8d57d983f5960f6b3b2ed1d4f7350cfa7fb985580eaf4b9a2d8501384ce27369"
                },
                {
                    "privateKey":"eebfbd55819ea095107b776616669536d9f0bb6d3cd9b21665ea1fb02405cfed"
                },
                {
                    "privateKey":"22e388e026234863ba077fe18783bbf7935c49ed08898995e7f5f64db8d51cef"
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
        "rawTx": "020001001239303731353430353939303037303337323601AE56269E6E630651898E5D66E4FBD1932DB69BBB7E0C0E859F877DE9EA9AD70201000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A30094357700000000000000002160DB4AE4630D3909CFCEEC9728FC323C00A6089B0000000001C340159032863262D13DD7FBA59E173C788C49FCE45BD71ED921C08AC447CC721E87D2B4C73DF99C7B544CBD18F0493E8054BC237EE97CBB53E7BB855C8E5AF6553040BE188D76A1900DA880DF3CC2267D9469C3736EA2CAAB8E74A7DE2A43A642E2145E14B49614A8C3AEE866A91643E0DE1468C120D693829F30BB4D2FA3CFA5D18540847B4D37836495AED68D7BFEF7570D935371B8B0940FE16D5C59E54555B8FBDB994DDB9066FEBE97DDB2376C0456D1AE2FB414192079E0323F5BC1204DE828E08B5321032F4540E915134F38BA24CDC08621AD7F5B8B62DB36843AE8FA9422C047A04BE82102E63EFAB72413B320A341B054D5D6CE5F1565A3B466AA4925DE69D152027CCE202102F212915764714D1A7CC85100D924F4657E7797DA269EA2EB4968146E57CA364C2102F3346A807786C4D040C7F7DF75B2F4B64CC6F9F95AAF0BED79099FB1C48FDB3F54AE",
        "txHash": "4C1D416975650CB03228694116FBF5937BD6458E0F846B0843FF685868465DE5"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
}
```

## gencrosschaintx
description:cross chain single sign transcation

#### Tips：
- recharge：The x address is generated by hash in the Genesis Block of the Side Chain
- withdraw：address = "0000000000000000000000000000000000"

#### Parameter Description
| name  | type | description |
| ------ | ---- | ----------- |
| txid | string | transaction where the available balance of address is located |
| index| int | the serial number of the tx in which the balance is available ,and the vout returned by RPC form utxo is the index| 
| privateKeySign |  |Private key corresponding to the address of inputs | 
| crossChainAsset |  |Side Chain to Account Address and Balance | 

##### Request
```
{
    "method":"gencrosschaintx",
    "params":{
	    "transaction":{
            "inputs":[
                {
                    "txid":"4bb237fb16a233dbd2414a014da7ca02c215bb6ffd976acc84b5cf4ec0439ba0",
                    "index":0,
                    "address":"Eco3fa2sZfyY6P524JTK9WpvzFjKEaoF5U"
                }
            ],
            "outputs":[
                {
                    "address":"XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ",
                    "amount":"0.00039899"
                }
            ],
            "privateKeySign":[
                {
                    "privateKey":"E6710E4FC7E8057BDEE6E129949A70AACE72A0EA0722E7B90E41765D6C1C407E"
                }
            ],
            "crossChainAsset":[
                {
                    "address":"Eco3fa2sZfyY6P524JTK9WpvzFjKEaoF5U",
                    "amount":"0.00029899"
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
        "rawTx": "0800012245636F33666132735A66795936503532344A544B395770767A466A4B45616F46355500CB740000000000000100132D34303531313536333131383233363337383501A09B43C04ECFB584CC6A97FD6FBB15C202CAA74D014A41D2DB33A216FB37B24B00000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3DB9B000000000000000000004B5929CBD09401EB2CE4134CB5EE117A01152C387E00000000014140AA501C3B1E5171834E695888D5F58871EAC49981F77AC186AD7ABA130393056B16290E42DB9D8F26205D43593D67B3363630C8F99AD3DFDABCB5515251AFBF382321035F5142F731101CC7295054584BE0CD9661DECE3F0D94E0278DF1EB4249D3B7B2AC",
        "txHash": "F9F961F5BCA14F8C005CA4A5B3334B802CE719C4DDA8504CC99BD83B744EEE9E"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```


## gencrosschainmultisigntx
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
    "method":"gencrosschainmultisigntx",
    "params":{
        "transaction":{
            "inputs":[
                {
                    "txid":"723523b9f92394b9428c7abf4d13d7049efeba3c3a154e1b88e96ff6b95bd608",
                    "index":0,
                    "address":"8NRxtbMKScEWzW8gmPDGUZ8LSzm688nkZZ"
                }
            ],
            "outputs":[
                {
                    "address":"XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ",
                    "amount":"0.3"
                }
            ],
            "privateKeyScripte":[
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
            "crossChainAsset":[
                {
                    "address":"EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB",
                    "amount":"0.3"
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
        "rawTx": "0800012245515370557A453458594A68425378356A37546632637465614B64466469786656420080C3C90100000000010013343930313739353636373239333130303335360108D65BB9F66FE9881B4E153A3CBAFE9E04D7134DBF7A8C42B99423F9B923357200000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A380C3C90100000000000000004B5929CBD09401EB2CE4134CB5EE117A01152C387E0000000001C34068602178D5C93F42B210D1E115BB06AF5BD770ADD573D19E0806FDE1BD8DA7FF0BA33475DAB4F1991A6CA7C09614D326EC3AFF797DAB4952F38E46C999856C434002B6B29B60DC90DFDED52F67B5D3EEAE43E031F8FACD6440B74E10C89BE83E234AA1ED6C82DB8A50DFECB19F46E8985C9990E8416AABE0B049889EEDD7EA80FE407240FE6FA95507D439E0D6942E66DC1CEDCAEF6013A4D6918A18ABA6504E499D8A6A5B7FAEF4A0653708B1F95133B8EEA63C6265B605955F47A1D25FE823A99E695321032F4540E915134F38BA24CDC08621AD7F5B8B62DB36843AE8FA9422C047A04BE82102E63EFAB72413B320A341B054D5D6CE5F1565A3B466AA4925DE69D152027CCE202102F212915764714D1A7CC85100D924F4657E7797DA269EA2EB4968146E57CA364C53AE",
        "txHash": "AB23BE6A44CFE3C1F77A1A6997CF923169E0611A6E18AE2E3B4C48007ECFA2D0"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## gencrosschaintxbyprivatekey

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
    "method":"gencrosschaintxbyprivatekey",
    "params":{
        "transaction":{
            "privateKeys":[
                {
                    "privateKey":"5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB"
                }
            ], 
            "outputs":[
                {
                    "address":"XLC69K4932zZf1SRwJCDbv5HGk7DbDYZ9H",
                    "amount":"0.02"
                }
            ],
            "crossChainAsset":[
                {
                    "address":"ESH5SrT7GZ4uxTH6aQF3ne7X8AUzWdREzz",
                    "amount":"0.01"
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
        "rawTx": "080001224553483553725437475A347578544836615146336E6537583841557A576452457A7A0040420F000000000001001232333532333933333132373830343036333502BAD4926FAA46F0EC32C7195C6840EDD5D9B6149731A8C0F7EB281C0EE680A8AF0000000000007E6651E24BE075BB0B9BF7CAAA9B8A21E662988FDC3A98DA26F0BFA3A8BA470100000000000002B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A380841E0000000000000000004B60FE1F6636415DC4E8EB8A70F15EFE925FE0ACF8B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3660A0700000000000000000021E1782FF3A250484A368CA7B426B15451CF8A01AF00000000014140B3A2B899AD3E096480F30907BC9A33AB6D6ABA3C52789070F9166F2ECE401EF1CF656BB80E132E30BEDA85CB23CF3639C1A5CC9289D0DE47AF988031BDBE86692321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC",
        "txHash": "9BDE361D8420A52DBB2F50804A8FBC9F6FC85556AA25B1E2A8FFBBF92AAEC26F"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```