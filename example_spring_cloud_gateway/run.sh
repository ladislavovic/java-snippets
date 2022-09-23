#/bin/bash

# kills all background processes at the exit
trap 'kill $(jobs -p)' EXIT

# variables
api_1_dir="backend-api-1"
api_1_target=${api_1_dir}/target
api_1_jar_name="example-spring-cloud-gateway-backend-api-1-0.0.1-SNAPSHOT.jar"
api_1_jar_path=${api_1_target}/${api_1_jar_name}
gateway_dir="gateway"
gateway_target=${gateway_dir}/target
gateway_jar_name="example-spring-cloud-gateway-gateway-0.0.1-SNAPSHOT.jar"
gateway_jar_path=${gateway_target}/${gateway_jar_name}

echo "Building Backend Api 1 ..."
cd $api_1_dir
mvn clean package &> /dev/null
ret=$?
if [[ $ret -ne 0 ]]; then
  echo "FAILED"
  exit $ret
fi
cd ..
echo "Building Backend Api 1 - success"

echo "Building Gateway ..."
cd $gateway_dir
mvn clean package &> /dev/null
ret=$?
if [[ $ret -ne 0 ]]; then
  echo "FAILED"
  exit $ret
fi
cd ..
echo "Building Gateway - success"


# Start Backend Api 1
echo "Starting applications ..."
java -jar ${api_1_jar_path} > /dev/null &
java -jar ${gateway_jar_path} > /dev/null &
sleep 5

printf "\nTEST1: the :8888/api1/operation1 should be redirected to the backend api:"
http -v :8888/api1/operation1
