#!/bin/bash

####
# This script will create the service principle to authenticate with, it will output sensitive login information.
####

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
if [ -f $SCRIPT_DIR/.env ]
then
  export $(cat $SCRIPT_DIR/.env | sed 's/#.*//g' | xargs)
fi

# https://docs.microsoft.com/en-us/cli/azure/create-an-azure-service-principal-azure-cli
az ad sp create-for-rbac --name $AZ_NAME --role Contributor