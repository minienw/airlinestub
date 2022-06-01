docker build -t airline_stub:latest .
docker-compose -p airline up
docker tag airline_stub:aks_latest stevekellaway/airline_stub:aks_latest
docker push stevekellaway/airline_stub:aks_latest


