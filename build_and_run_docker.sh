docker build -t airline_stub:latest .
#docker tag airline_stub:latest stevekellaway/airline_stub:latest
#docker push stevekellaway/airline_stub:latest
docker run -p 8081:8081/tcp --name airline_stub_latest airline_stub:latest

