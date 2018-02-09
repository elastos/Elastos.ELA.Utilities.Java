#!/bin/bash
MAIN_PATH=$GOPATH/src/ela_tool/src/main/java
ELA_PATH=$GOPATH/src/ELA
NODE_PATH=$MAIN_PATH/nodes
PASSWORD=elatest
NODE=${2:-4}
NET_TYPE=${3:-RegNet}
MINING=${4:-false}

build()
{
    if [ -e $NODE_PATH ]
    then
        cd $NODE_PATH
    else
        mkdir $NODE_PATH && cd $NODE_PATH && mkdir $NODE_PATH/origin
        mkdir $NODE_PATH/wallets
    fi

    cp $ELA_PATH/{node,nodectl,config.json} $NODE_PATH/origin
    rm -rf $NODE_PATH/addresses.bak
    rm -rf $NODE_PATH/public_keys.bak

    if [[ $NODE -gt 49 ]]
    then
        echo "the count of node can be at most 49"
        exit 1
    fi

    for i in `seq 1 $NODE`
    do
        if [ -e $NODE_PATH/node$i ]
        then
            cd $NODE_PATH/node$i

            rm -rf node
            rm -rf nodectl
            rm -rf Chain
            rm -rf Log
            rm -rf config.json

            cp $NODE_PATH/origin/{node,nodectl,config.json} $NODE_PATH/node$i

            ./nodectl wallet --reset -p $PASSWORD
            ./nodectl wallet -l account -p $PASSWORD | awk 'END { print $2 }' > address.bak
            ./nodectl wallet -l account -p $PASSWORD | awk 'END { print $NF }' > public_key.bak

            cd ..
        else
            mkdir node$i
            cp $NODE_PATH/origin/{node,nodectl,config.json} $NODE_PATH/node$i && cd $NODE_PATH/node$i

            ./nodectl wallet -c -p $PASSWORD | awk 'END { print $2 }' > address.bak
            ./nodectl wallet -l account -p $PASSWORD | awk 'END { print $NF }' > public_key.bak

            cd ..
        fi
    done

    for i in `seq 1 $NODE`
    do
        cd $NODE_PATH/node$i && cat address.bak >> ../addresses.bak
        cd $NODE_PATH/node$i && cat public_key.bak >> ../public_keys.bak
        cd $MAIN_PATH && python3 config.py $i $NET_TYPE $MINING > $NODE_PATH/node$i/config.json
    done
}

run()
{
    for i in `seq 1 $NODE`
    do
        cd $NODE_PATH/node$i && ./node -p $PASSWORD >> /dev/null &
    done

    ps -ef | grep node | grep -v grep | awk '{print $2}'
}

stop()
{
    kill -9 `ps -ef | grep node | grep -v grep | awk '{print $2}'`
}

clean()
{
    rm -rf $NODE_PATH
    stop
}

restart_nodes()
{
    stop
    NODE=$(ls -l nodes | awk '/node/{ print $0 }' | wc -l)
    run
}

if [[ "$1" == "" || "$1" == "run" ]]; then
    stop
    build
    run
    exit 0
elif [ "$1" == "stop" ]; then
    stop
    exit 0
elif [ "$1" == "build" ]; then
    build
    exit 0
elif [ "$1" == "clean" ]; then
    clean
    exit 0
elif [ "$1" == "restart" ]; then
    restart_nodes
    exit 0
fi
