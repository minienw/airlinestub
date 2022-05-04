#!/bin/bash

####
# This script will create the resource group, where all resources in the next steps will be created.
####

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
if [ -f $SCRIPT_DIR/.env ]
then
  export $(cat $SCRIPT_DIR/.env | sed 's/#.*//g' | xargs)
fi

az group create --name $AZ_RESOURCE_GROUP --location westeurope