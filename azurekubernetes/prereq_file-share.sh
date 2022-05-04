#!/bin/bash

####
# This script will create the service principle to authenticate with, it will output sensitive login information.
####

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
if [ -f $SCRIPT_DIR/.env ]
then
  export $(cat $SCRIPT_DIR/.env | sed 's/#.*//g' | xargs)
fi

az storage account create --resource-group $AZ_RESOURCE_GROUP --name $AZ_NAME
az storage share-rm create --resource-group $AZ_RESOURCE_GROUP --storage-account $AZ_NAME --name $AZ_NAME
# Create the directories for the config files
az storage directory create --account-name $AZ_NAME --name airline --share-name $AZ_NAME
az storage directory create --account-name $AZ_NAME --name validation --share-name $AZ_NAME