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
  addToSystemProperties "confirmed-status-icn" "${CONFIRMED_STATUS_ICN}"
  addToSystemProperties "v5-status-icn" "${V5_STATUS_ICN}"
  addToSystemProperties "no-emis-user-status-icn" "${NO_EMIS_USER_STATUS_ICN}"
  addToSystemProperties "disability-rating-icn" "${DISABILITY_RATING_ICN}"
  addToSystemProperties "no-bgs-user-disability-rating-icn" "${NO_BGS_USER_DISABILITY_RATING_ICN}"
  addToSystemProperties "service-history-icn" "${SERVICE_HISTORY_ICN}"
  addToSystemProperties "service-history-icn-null-end-date" "${SERVICE_HISTORY_ICN_NULL_END_DATE}"
  addToSystemProperties "no-mpi-user-icn" "${NO_MPI_USER_ICN}"
  addToSystemProperties "no-emis-episodes-user" "${NO_EMIS_EPISODES_USER}"

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
