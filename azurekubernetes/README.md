# Azure environment for lms-cv
The scripts in this directory allow you to deploy and manage the AKS (Azure Kubernetes Service) cluster.

# Prerequisites

## Docker
https://docs.docker.com/get-docker/

## Azure CLI
For creating and managing resources in Azure.
https://docs.microsoft.com/en-us/cli/azure/install-azure-cli

## Kubectl
For managing the Kubernetes cluster (deploy, view pods, troubleshoot, use the dahsboard).
https://kubernetes.io/docs/tasks/tools/

## Azure account
https://azure.microsoft.com/en-us/free/

## Resource group
Within the Azure account, you need to have a resource group to deploy the resources to. In this group, you at least need to have "Contributor" permissions (Inzender).

If you have admin permissions in your account, you can use `prereq_resource-group.sh` script and let it create the group you setup in the .env file for you.

## File share
A file share is used for configuring the services, it can be created by using the `prereq_file-share.sh` script.

## Env file
Create your own local .env file in this directory (don't confuse it with the .env file in the root). You can inspire it on the .env.example file.
This file is not supposed to be checked in into scm, so make sure to store any secrets safely in case the file gets deleted!

## OPTIONAL: App registration with client secret and access on the Resource group
If you would like to use an App registration to login to Azure, a token based login can be used, with this you can delegate the login to someone who does not have admin permissions in your account.  
For this we can use the client secret of an App registration with Contributor permissions in the Resource group.

If you have admin permissions in your account, you can use `prereq_app-registration.sh` script and let it create the app using the AZ_NAME you setup in the .env file for you, with a new client secret. **Make sure to store the secret (you will only see it once), and set it in your .env file as AZ_PASSWORD together with the corresponding AZ_APP_ID and AZ_TENANT_ID**.

**To use the script, you need to be logged in (`az login`).**

Alternatively, if you have portal access, you could also do this in the portal by creating an App registration and add a "Client secrets" under "Certificates & secrets". Lookup the previously mentioned variables and store them in the correct place, the secret (password) is only shown once, when creating it.

Next, you need to add the App to the resource group, to have Contributor access.

**Do not forget to update your .env file and maybe store the secrets in a password manager**

# Installation

## Step 1. Login on the cli
If you want to use an App registration, you should have set the AZ_APP_ID, AZ_TENANT_ID and AZ_PASSWORD earlier.  
Use the `app_login.sh` script to login.

If you do not want to use an App registration, but have sufficient rights on your resource group, using `az login` will suffice.

## Step 2. Create the AKS K8s cluster
The docker containers will run on Kubernetes (the instances are called pods there), we use a managed AKS cluster from Azure.
This cluster is running on a multiple node environment, and uses volumes mounted from managed premium storage disks in Azure. This ensures production grade speeds on the persisted volumes.
A loadbalancer is created with a public IP and assigned a DNS subdomain on azure.

The domain is build up in this format, using the environment variables: `${AZ_NAME}.${AZ_LOCATION}.cloudapp.azure.com`

Use the `setup_aks.sh` script to deploy the AKS cluster.

## Step 3. Log into the AKS cluster with kubectl
To be able to run any kubectl commands (used in the scripts), we need to login with kubectl, the Azure cli will do this for us.

Use the `kubectl_login.sh` script to login.

## Step 4. Add the configuration files
Create the configuration files and add them to the correct file share directory (the `airline` and `validation` directories should have been created when using the `prereq_file-share.sh` script).

## Step 5. Deploy the pods, networking and volumes
To deploy the whole cluster at once, use the `deploy_aks.sh` script.

This script can be used at any time, any unchanged deployment configuration will not trigger an update. The pods will always be recreated, because they use the `latest` tag.

## Step 6. Install the dashboard
For easy management, we use the K8s dashboard.

Use the `setup_k8s_dashboard.sh` script to install the dashboard.

# Managing the cluster

## Dashboard
The most conveniant way of getting a look into the cluster state, and interacting with the pods (view logs, open bash) is by using the integrated dashboard.

This dashboard is only available when proxying is started through the script, this has security reasons.

Use the tooling script `tooling/dashboard.sh`.

## Display pods
`kubectl get pods`

## Tooling
Some cli commands which are handy for troubleshooting or managing the cluster are provided in the tooling directory.

|script|description|example|
|-|-|-|
|check-deployment.sh|displays useful info about latests deployments|tooling/check-deployment.sh|
|dashboard.sh|Manage the cluster and pods in your browser|tooling/dashboard.sh|
|describe.sh|Get info about a specif pod. Also useful when a pod won't start or seems to be hanging|tooling/describe.sh wallet|
|get-public-ip.sh|Displays the loadbalancer IP|tooling/get-public-ip.sh|
|hard-restart.sh|Completely shutdown a pod and boot one new instance (with downtime)|tooling/hard-restart.sh|
|logs.sh|Display logs for an application|tooling/logs.sh wallet|
|rolling-update.sh|Do a blue/green deployment (without downtime) of an app|tooling/rolling-update.sh wallet|
|rollout-status.sh|Check the status of a specific deployment|tooling/rollout-status.sh wallet|

## Updating pod resources or environment variables
Anything defined in the container configuration in deploy.template.yaml can be changed, as long as you don't change any deployment types or namings, you can just do a redeployment.

Use `deploy_aks.sh` again to update your changes to the cluster.

# Deleting the cluster
If you want to say goodbye to the cluster, run `delete_aks.sh`. A confirmation will be required, of course.

# Troubleshooting
- A (new) pod is stuck in Pending.
  Probably no resources available to schedule the pod, the the output of the describe tooling for more detailed info about the pod.