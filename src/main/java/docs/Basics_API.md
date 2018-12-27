
# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/


## genprivatekey
description:generate private key 

##### Request
```
{
    "method":"genprivatekey"
}
```

##### Response
```
{
    "result": "32A78B9FA6792310AAB5093DE8FDD915426832EA1C0844B789013192B0AA42B1",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

 ## genpublickey
description:obtain the public key based on the private key

##### Request
```
{
    "method":"genpublickey",
    "params": { "privatekey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"}
}
```

##### Response
```
{
    "result": "03B462F4DB3F67A6A71E51BF3034A183022F092E8E6ED0C91F139E4871F5BA0B57",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## genaddress
description:obtain the address based on the private key 

##### Request
```
{
    "method":"genaddress",
    "params": {"privatekey":"bddeadcdef4391dd95e3f48de69c607fa4c84ebf9452baf816e2311520ee9344"}
}

```

##### Response
```
{
    "result": "ERfBqxDFty7NMj6ZVLrVtmbfCKtKgF5jmz",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## genprivpubaddr
description:generate private key and public key and address

##### Request
```
{
    "method":"genprivpubaddr"
}
```

##### Response
```
{
    "result": {
        "privatekey": "B9A74F7C3F134D92932379CEDA431B3D52BC8D2800F1995803BE970A6727E4FA",
        "publickey": "02218B25A023D1C4BAF8857BFF7D13D6C83F48767DB02C6519ABF681E6FC5D54C2",
        "address": "EaupPuQPhcWvHDUDWdL9RUmAjCQZp637F1"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## genidentityid
description:obtain the identity id based on the private key

##### Request
```
{
    "method":"genidentityid",
    "params":{"privatekey":"4EA80EDBFC783A19FAC1072D15893AC7A20B4EDE1402FD57DE76D02EA61E28E4"}
}

```

##### Response
```
{
    "result": "iUEuVMUGV89NEcZtC2UVu2grUaMaEb2jcT",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## gengenesisaddress
description:obtain the genesis address based on the genesis block hash

##### Request
```
{
    "method":"gengenesisaddress",
    "params":{"blockhash":"56be936978c261b2e649d58dbfaf3f23d4a868274f5522cd2adb4308a955c4a3"}
}

```

##### Response
```
{
    "result": null,
    "error": {
        "code": 51027,
        "id": null,
        "message": "blockHash key can not be empty"
    },
    "jsonrpc": "2.0"
}

```

## checkaddress
description:verify that the address is ela address

##### Request
```
{
    "method":"checkaddress",
    "params":{
            "addresses":["EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZzz","1C1mCxRukix1KfegAY5zQQJV7samAciZpv","8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT","XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU"]
        }
}
```

##### Response
```
{
    "result": {
        "EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZzz": false,
        "1C1mCxRukix1KfegAY5zQQJV7samAciZpv": false,
        "8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT": true,
        "XQd1DCi6H62NQdWZQhJCRnrPn7sF9CTjaU": false
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```

## genmultisignaddress
description:generate multi sign address, M is sign number

##### Request
```
{
    "method":"genmultisignaddress",
    "params":{
			"privatekeys":[
                {
                    "privatekey":"37b1ae4caac014db4d585b07af19be90da4e0767f39753177711860765aeb4b2"
                },
                {
                    "privatekey":"3587efd8a04b77d626532bd46a32d24879ee9e9b6e16298faa7320bf6b32a5d6"
                },
                {
                    "privatekey":"73541c05f4f4c7407663e3abde883a6eb9ef682b1dcaa691efdcb6231a7f17a9"
                },
                {
                    "privatekey":"f358893425c842d0d48cb4eb498ac995d9f31bb7a151742cb3b14e97f656284f"
                }
            ],
            "m":3       
        }
}
```

##### Response
```
{
    "result": "8XQuPkk7TtFWNUNDZnXVb4syAcKDTaBTrn",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}

```


## genneocontracthashandaddress
description:obtain the neo contract hash and address based on the contract

##### Request
```
{
    "method":"genneocontracthashandaddress",
    "params":{"contract":"00c56b6161012b61047465737452c168124e656f2e52756e74696d652e4e6f7469667961616c7566"}
}

```

##### Response
```
{
    "result": {
        "contracthash": "1C1A894CE949A9DE9B466405032C3E4F542158C96A",
        "contractaddress": "CJtCcq7PUNz7LwYNfXq1ZhxS5VxKe9YC4y"
    },
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```

## genneocontractaddress
description:obtain the neo contract address based on the contract hash

##### Request
```
{
    "method":"genneocontractaddress",
    "params":{"contracthash":"1C1A894CE949A9DE9B466405032C3E4F542158C96A"}
}

```

##### Response
```
{
    "result": "CJtCcq7PUNz7LwYNfXq1ZhxS5VxKe9YC4y",
    "id": null,
    "jsonrpc": "2.0",
    "error": null
}
```


