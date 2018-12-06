
# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/


## genPrivateKey
description:generate private key 

##### Request
```
{
    "method":"genPrivateKey",
    "id":0,
    "params":[]
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

 ## genPublicKey
description:obtain the public key based on the private key

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

## genAddress
description:obtain the address based on the private key 

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

## gen_priv_pub_addr
description:generate private key and public key and address

##### Request
```
{
    "method":"gen_priv_pub_addr",
    "id":0,
    "params":[]
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

## genIdentityID
description:obtain the identity id based on the private key

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

## genGenesisAddress
description:obtain the genesis address based on the genesis block hash

##### Request
```
{
    "method":"genGenesisAddress",
    "id":0,
    "params":[
        {
            "BlockHash":"56be936978c261b2e649d58dbfaf3f23d4a868274f5522cd2adb4308a955c4a3"
        }
    ]
}

```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genGenesisAddress",
    "Result": "XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ"
}

```

## checkAddress
description:verify that the address is ela address

##### Request
```
{
    "method":"checkAddress",
    "id":0,
    "params":[
        {
            "Addresses":["EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZzz","1C1mCxRukix1KfegAY5zQQJV7samAciZpv","8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT","XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU"]
        }
    ]
}

```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "checkAddress",
    "Result": {
        "EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZzz": false,
        "1C1mCxRukix1KfegAY5zQQJV7samAciZpv": false,
        "8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT": true,
        "XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU": false
    }
}

```

## genMultiSignAddress
description:generate multi sign address, M is sign number

##### Request
```
{
    "method":"genMultiSignAddress",
    "id":0,
    "params":[
        {
			"PrivateKeys":[
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

```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genMultiSignAddress",
    "Result": "8QaosBQZLPNMF5qR3iY3ozmp5KJcDLUgiC"
}

```


## genNeoContractHashAndAddress
description:obtain the neo contract hash and address based on the contract

##### Request
```
{
    "method":"genNeoContractHashAndAddress",
    "id":0,
    "params":[
		{
            "Contract":"00c56b6161012b61047465737452c168124e656f2e52756e74696d652e4e6f7469667961616c7566"
        }
    ]
}

```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genNeoContractHashAndAddress",
    "Result": {
        "ContractHash": "1C1A894CE949A9DE9B466405032C3E4F542158C96A",
        "ContractAddress": "CJtCcq7PUNz7LwYNfXq1ZhxS5VxKe9YC4y"
    }
}
```

## genNeoContractHashAndAddress
description:obtain the neo contract address based on the contract hash

##### Request
```
{
    "method":"genNeoContractAddress",
    "id":0,
    "params":[
		{
            "ContractHash":"1C1A894CE949A9DE9B466405032C3E4F542158C96A"
        }
    ]
}

```

##### Response
```
{
    "Desc": "SUCCESS",
    "Action": "genNeoContractAddress",
    "Result": "CJtCcq7PUNz7LwYNfXq1ZhxS5VxKe9YC4y"
}
```


