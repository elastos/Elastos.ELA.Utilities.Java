#!/bin/bash
COMMAND=$1
WALLETPATH=$2
PASSWD=$3

cd $WALLETPATH

case $COMMAND in
    "getaddresses" )
        ./nodectl wallet --list account -p $PASSWD | awk 'NR>2 { print $2 }' ;;
    "resetwallet" )
        ./nodectl wallet --reset -p $PASSWD ;;
    "changepassword" )
        NEWPASSWD=$4
        echo "${ARG}\n${NEWPASSWD}\n${NEWPASSWD}" > passwd.bak && ./nodectl wallet --changepassword < passwd.bak && rm passwd.bak;;
    "addaccount" )
        COUNT=$4
        ./nodectl wallet --addaccount $COUNT -p $PASSWD ;;
    "getbalances")
        ./nodectl wallet --list balance -p $PASSWD | awk 'NR>2 { print $NF }' ;;
    "createwallet")
        COUNT=$(($(ls -l ../wallets | wc -l | awk '{ print $1 }') + 1))
        ./nodectl wallet -c -p $PASSWD -n wallet$COUNT.dat | awk 'END { print $2,",",$NF }'
        mv wallet$COUNT.dat ../wallets ;;
    "addmultisigaccount")
        STRING=$4
        ./nodectl wallet --addmultisigaccount $STRING -p $PASSWD ;;
    "getmultisignaccount" )
        ./nodectl wallet --list multisig  -p $PASSWD | awk '/^Address/{ print $NF }' ;;
esac





