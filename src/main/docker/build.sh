cp ../../../target/customer-api.war .
docker build -f Dockerfile -t luiza-labs-customer-api:1.0-SNAPSHOT .
rm customer-api.war
