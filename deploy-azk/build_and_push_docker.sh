#Replace 'ghcr.io/minienw' with the repo of your choice
docker build -t airline_stub:aks_latest .
docker tag airline_stub:aks_latest ghcr.io/minienw/airline_stub:aks_latest
docker push ghcr.io/minienw/airline_stub:aks_latest


