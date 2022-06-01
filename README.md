# Airline Stub

Development stub for airline landing page.







After building and testing code changes, build the new image using:

    docker build -f latest_noconfig.Dockerfile -t airline_stub:latest_noconfig .

Then deploy to the repository of your choice e.g.

    docker tag airline_stub:latest_noconfig stevekellaway/airline_stub:latest_noconfig
    docker push stevekellaway/airline_stub:latest_noconfig

For deploying to a local Docker server, see the 'deploy' folder.

To create an image suitable for deploying in a kubernetes cluster, see the 'deploy-azk' folder, then deploy to Azure using the instructions from the folder 'azurekubernetes'.