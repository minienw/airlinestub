docker build -f latest_noconfig.Dockerfile -t airline_stub:latest_noconfig .
docker tag airline_stub:latest_noconfig stevekellaway/airline_stub:latest_noconfig
docker push stevekellaway/airline_stub:latest_noconfig


