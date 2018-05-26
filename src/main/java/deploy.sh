#!/bin/bash
MAIN_PATH=$GOPATH/src/ela_tool/src/main/java
ELA_PATH=$GOPATH/src/Elastos.ELA
NODE_PATH=$MAIN_PATH/nodes
NODE=${2:-4}
NET_TYPE=${3:-RegNet}
MINING=${4:-false}


run()
{
    cd $NODE_PATH/ && ./ela >> /dev/null &

    ps -ef | grep ela | grep -v grep | awk '{print $2}'
}

stop()
{
#    kill -9 `ps -ef | grep ela | grep -v grep | awk '{print $2}'`
    killall ela
}

restart_nodes()
{
    stop
    NODE=$(ls -l nodes | awk '/ela/{ print $0 }' | wc -l)
    run
}

clean()
{
    rm -rf $NODE_PATH/Chain
    rm -rf $NODE_PATH/Logs
}

if [[ "$1" == "" || "$1" == "run" ]]; then
    stop
    run
    exit 0
elif [ "$1" == "stop" ]; then
    stop
    exit 0
elif [ "$1" == "restart" ]; then
    restart_nodes
    exit 0
elif [ "$1" == "clean" ]; then
    clean
    exit 0
fi
