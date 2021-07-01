#!/usr/bin/env bash
set -euo pipefail
# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

init() {
  test -n "${K8S_ENVIRONMENT}"

  if [ -z "${SENTINEL_BASE_DIR:-}" ]; then SENTINEL_BASE_DIR=/sentinel; fi
  cd $SENTINEL_BASE_DIR

  SYSTEM_PROPERTIES=()

  if [ -z "${SENTINEL_ENV:-}" ]; then SENTINEL_ENV=$K8S_ENVIRONMENT; fi
  if [ -z "${VV_URL:-}" ]; then VV_URL=https://$K8S_LOAD_BALANCER; fi
}

main() {
  addToSystemProperties "sentinel" "${SENTINEL_ENV}"
  addToSystemProperties "sentinel.veteran-verification.url" "${VV_URL}"
  addToSystemProperties "edipi-confirmed-first-name" "${EDIPI_CONFIRMED_FIRST_NAME}"
  addToSystemProperties "edipi-confirmed-last-name" "${EDIPI_CONFIRMED_LAST_NAME}"
  addToSystemProperties "edipi-confirmed-birth-date" "${EDIPI_CONFIRMED_BIRTH_DATE}"
  addToSystemProperties "edipi-confirmed-ssn" "${EDIPI_CONFIRMED_SSN}"
  addToSystemProperties "icn-confirmed-first-name" "${ICN_CONFIRMED_FIRST_NAME}"
  addToSystemProperties "icn-confirmed-last-name" "${ICN_CONFIRMED_LAST_NAME}"
  addToSystemProperties "icn-confirmed-birth-date" "${ICN_CONFIRMED_BIRTH_DATE}"
  addToSystemProperties "icn-confirmed-ssn" "${ICN_CONFIRMED_SSN}"
  addToSystemProperties "v5-status-first-name" "${V5_STATUS_FIRST_NAME}"
  addToSystemProperties "v5-status-last-name" "${V5_STATUS_LAST_NAME}"
  addToSystemProperties "v5-status-birth-date" "${V5_STATUS_BIRTH_DATE}"
  addToSystemProperties "v5-status-ssn" "${V5_STATUS_SSN}"
  addToSystemProperties "no-emis-user-first-name" "${NO_EMIS_USER_FIRST_NAME}"
  addToSystemProperties "no-emis-user-last-name" "${NO_EMIS_USER_LAST_NAME}"
  addToSystemProperties "no-emis-user-birth-date" "${NO_EMIS_USER_BIRTH_DATE}"
  addToSystemProperties "no-emis-user-ssn" "${NO_EMIS_USER_SSN}"
  addToSystemProperties "confirmed-status-icn" "${CONFIRMED_STATUS_ICN}"
  addToSystemProperties "v5-status-icn" "${V5_STATUS_ICN}"
  addToSystemProperties "no-emis-user-status-icn" "${NO_EMIS_USER_STATUS_ICN}"

  java-tests \
    --module-name "veteran-verification-tests" \
    --regression-test-pattern ".*IT\$" \
    --smoke-test-pattern ".*IT\$" \
    ${SYSTEM_PROPERTIES[@]} \
    $@ \
    2>&1 | grep -v "WARNING: "

  exit $?
}

# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

addToSystemProperties() {
  SYSTEM_PROPERTIES+=("-D$1=$2")
}

# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

init
main $@
