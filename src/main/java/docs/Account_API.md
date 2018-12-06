# Web Request

 - start command : java -cp Elastos.ELA.Utilities.Java.jar  org.elastos.elaweb.HttpServer
 - suggestion：java version "1.8.0_161"
 - local IP : http://127.0.0.1:8989/

## Account
```
Create account to generate keystore.dat file under Java program directory
```

## genTxByAccount

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
    "method":"genTxByAccount",
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
                            "amount":"30.1"
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
    "Action": "genTxByAccount",
    "Desc": "SUCCESS",
    "Result": {
        "rawTx": "0200010013363731323032A214F6036D8221E7F1C010000000000B67901062723993D9FBADC32C33807EE7B9FCB370D777C5A955A3056DF3B9CB40100000000002B20EB60E4191B374DFC69503E1ED6888E7456FDE2554E77E1D9A1AB3360562F1D6FF4BAC",
        "txHash": "98FCA1FABB606AEDF84921E24CDF7A0931FF11DAFDDC1B8584206881A5229060"
    }
}

```

## createAccount
description:create account 

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 

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

## importAccount

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 
| privateKey| string | import privateKey | 

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

## removeAccount

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 
| address| string | remove account | 

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

## exportPrivateKey

#### Parameter Description

| name  | type | description |
| ------ | ---- | ----------- |
| password| string | account password | 
| address| string | export account | 

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


## getAccounts
description:get all account information

##### Request
```
{
    "method":"getAccounts",
    "id":0,
    "params":[]
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

## getAccountAddresses
description:get all account address

##### Request
```
{
    "method":"getAccountAddresses",
    "id":0,
    "params":[]
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

