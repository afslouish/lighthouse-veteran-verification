#!/usr/bin/env bash

ENDPOINT_DOMAIN_NAME="$K8S_LOAD_BALANCER"
ENVIRONMENT="$K8S_ENVIRONMENT"
BASE_PATH="$BASE_PATH"

#Put Health endpoints here if you got them, all that's here is a WAG
PATHS=("/actuator/health" "/actuator/info")

SUCCESS=0

FAILURE=0

# New phone who this?
usage() {
cat <<EOF
Commands
  smoke-test [--endpoint-domain-name|-d <endpoint>] [--environment|-e <env>] [--base-path|-p <base_path>] [--birth-date|-b <birth-date>] [--first-name|-f <first-name>] [--last-name|-l <last-name>] [--middle-name|-m <middle-name>] [--ssn|-s <ssn>] [--gender|-g <gender>]
  regression-test [--endpoint-domain-name|-d <endpoint>] [--environment|-e <env>] [--base-path|-p <base_path>] [--birth-date|-b <birth-date>] [--first-name|-f <first-name>] [--last-name|-l <last-name>] [--middle-name|-m <middle-name>] [--ssn|-s <ssn>] [--gender|-g <gender>]

Example
  smoke-test
    --endpoint-domain-name=http://localhost:8080
    --environment=qa
    --base-path=""
    --ssn="796013145"
    --first-name="Willard"
    --middle-name="Tim"
    --last-name="Riley"
    --gender="M"
    --birth-date="1959-02-25"

$1
EOF
exit 1
}

# Keeps track of successes and failures
trackStatus () {
  if [ "$status_code" == 200 -o "$status_code" == 201 ]
    then
      SUCCESS=$((SUCCESS + 1))
      echo "$request_url: $status_code - Success"
    else
      FAILURE=$((FAILURE + 1))
      echo "$request_url: $status_code - Fail"
  fi
}

#

# Send requests to all of veteran-verification's available endpoints
httpListenerTests () {

  if [[ ! "$ENDPOINT_DOMAIN_NAME" == http* ]]; then
      ENDPOINT_DOMAIN_NAME="https://$ENDPOINT_DOMAIN_NAME"
  fi

  for path in "${PATHS[@]}"
    do
      request_url="$ENDPOINT_DOMAIN_NAME$BASE_PATH$path"
      status_code=$(curl -k --write-out %{http_code} --silent --output /dev/null "$request_url")
      trackStatus
    done

  path="/v0/status"
  request_url="$ENDPOINT_DOMAIN_NAME$BASE_PATH$path"
  status_code=$(curl -X POST -k --write-out %{http_code} --silent --output /dev/null "$request_url" -H 'Content-Type: application/json' -d "
  {
    \"ssn\": \"$SSN\",
    \"first_name\": \"$FIRST_NAME\",
    \"last_name\": \"$LAST_NAME\",
    \"birth_date\": \"$BIRTH_DATE\",
    \"middle_name\": \"$MIDDLE_NAME\",
    \"gender\": \"$GENDER\"
  }")
  trackStatus
}

printResults () {
  TOTAL=$((SUCCESS + FAILURE))

  echo "=== TOTAL: $TOTAL | SUCCESS: $SUCCESS | FAILURE: $FAILURE ==="

  if [[ "$FAILURE" -gt 0 ]]; then
  exit 1
  fi
}

# Send some smoke signals
smokeTest () {
  httpListenerTests
  printResults
}

# Regress
regressionTest () {
  httpListenerTests
  printResults
}

# Let's get down to business
ARGS=$(getopt -n $(basename ${0}) \
    -l "endpoint-domain-name:,environment:,base-path:,ssn:,first-name:,last-name:,middle-name:,birth-date:,gender:,help" \
    -o "d:e:p:s:f:l:m:b:g:h" -- "$@")
[ $? != 0 ] && usage
eval set -- "$ARGS"
while true
do
  case "$1" in
    -d|--endpoint-domain-name) ENDPOINT_DOMAIN_NAME=$2;;
    -e|--environment) ENVIRONMENT=$2;;
    -p|--base-path) BASE_PATH=$2;;
    -s|--ssn) SSN=$2;;
    -f|--first-name) FIRST_NAME=$2;;
    -l|--last-name) LAST_NAME=$2;;
    -m|--middle-name) MIDDLE_NAME=$2;;
    -b|--birth-date) BIRTH_DATE=$2;;
    -g|--gender) GENDER=$2;;
    -h|--help) usage "I need a hero! I'm holding out for a hero...";;
    --) shift;break;;
  esac
  shift;
done

if [[ -z "$ENDPOINT_DOMAIN_NAME" || -e "$ENDPOINT_DOMAIN_NAME" ]]; then
  usage "Missing variable K8S_LOAD_BALANCER or option --endpoint-domain-name|-d."
fi

if [[ -z "$ENVIRONMENT" || -e "$ENVIRONMENT" ]]; then
  usage "Missing variable K8S_ENVIRONMENT or option --environment|-e."
fi

if [ -z "$SSN" ]; then
  usage "Missing variable SSN or option --ssn|-s."
fi

if [ -z "$FIRST_NAME" ]; then
  usage "Missing variable FIRST_NAME or option --first-name|-f."
fi

if [ -z "$LAST_NAME" ]; then
  usage "Missing variable LAST_NAME or option --last-name|-l."
fi

if [ -z "$BIRTH_DATE" ]; then
  usage "Missing variable BIRTH_DATE or option --birth-date|-b."
fi

[ $# == 0 ] && usage "No command specified"
COMMAND=$1
shift

case "$COMMAND" in
  s|smoke-test) smokeTest;;
  r|regression-test) regressionTest;;
  *) usage "Unknown command: $COMMAND";;
esac

exit 0
