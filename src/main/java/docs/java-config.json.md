# java-config.json explanation
 java-config.json files need to be placed in the same level directory of the Java program to connect nodes to get utxo

## Inline Explanation

```json5
{
  "Host": "127.0.0.1:20336",  // Server IP and RPC ports where the node program resides
  "Fee":"0.0005",             // The single output or multiple input and output fees of a transaction are the same
  "Confirmation":1,           // Number of block confirmation transaction
  "RegisterAssetFee":"10",    // Token register transaction fee for registered assets 10 ela
  "RpcConfiguration":{        
    "User": "ElaUser",        // Check the username when use rpc interface
    "Pass": "Ela123"          // Check the password when use rpc interface
  }
}

```