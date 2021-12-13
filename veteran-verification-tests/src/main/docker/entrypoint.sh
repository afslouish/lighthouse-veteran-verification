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
  if [ -n "${CONFIRMED_STATUS_USER:-}" ]; then addToSystemProperties "confirmed-status-user" "${CONFIRMED_STATUS_USER}"; fi
  if [ -n "${V5_STATUS_USER:-}" ]; then addToSystemProperties "v5-status-user" "${V5_STATUS_USER}"; fi
  if [ -n "${NO_EMIS_USER_STATUS_USER:-}" ]; then addToSystemProperties "no-emis-user-status-user" "${NO_EMIS_USER_STATUS_USER}"; fi
  if [ -n "${DISABILITY_RATING_USER:-}" ]; then addToSystemProperties "disability-rating-user" "${DISABILITY_RATING_USER}"; fi
  if [ -n "${NO_BGS_USER:-}" ]; then addToSystemProperties "no-bgs-user" "${NO_BGS_USER}"; fi
  if [ -n "${SERVICE_HISTORY_USER:-}" ]; then addToSystemProperties "service-history-user" "${SERVICE_HISTORY_USER}"; fi
  if [ -n "${NO_EMIS_EPISODES_USER:-}" ]; then addToSystemProperties "no-emis-episodes-user" "${NO_EMIS_EPISODES_USER}"; fi

  populateVeteranCredentialsProperties

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

populateVeteranCredentialsProperties() {

  if [ -z "${WEBDRIVER_CHROME_DRIVER:-}" ]; then addToSystemProperties "webdriver.chrome.driver" "/usr/bin/chromedriver"; fi

  if [ -n "${OAUTH_VETERAN_CREDENTIALS_CREDENTIALS_TYPE:-}" ]; then addToSystemProperties "oauth.veteran-credentials.credentials-type" "ID_ME"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_TOKEN_URL:-}" ]; then addToSystemProperties "oauth.veteran-credentials.token-url" "${OAUTH_VETERAN_CREDENTIALS_TOKEN_URL}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_USER_PASSWORD:-}" ]; then addToSystemProperties "oauth.veteran-credentials.user-password" "${OAUTH_VETERAN_CREDENTIALS_USER_PASSWORD}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_AUTHORIZE_URL:-}" ]; then addToSystemProperties "oauth.veteran-credentials.authorize-url" "${OAUTH_VETERAN_CREDENTIALS_AUTHORIZE_URL}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_REDIRECT_URL:-}" ]; then addToSystemProperties "oauth.veteran-credentials.redirect-url" "${OAUTH_VETERAN_CREDENTIALS_REDIRECT_URL}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_CLIENT_ID:-}" ]; then addToSystemProperties "oauth.veteran-credentials.client-id" "${OAUTH_VETERAN_CREDENTIALS_CLIENT_ID}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_CLIENT_SECRET:-}" ]; then addToSystemProperties "oauth.veteran-credentials.client-secret" "${OAUTH_VETERAN_CREDENTIALS_CLIENT_SECRET}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_STATE:-}" ]; then addToSystemProperties "oauth.veteran-credentials.state" "${OAUTH_VETERAN_CREDENTIALS_STATE}"; fi
  if [ -n "${OAUTH_VETERAN_CREDENTIALS_AUDIENCE:-}" ]; then addToSystemProperties "oauth.veteran-credentials.audience" "${OAUTH_VETERAN_CREDENTIALS_AUDIENCE}"; fi
}

# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

init
main $@
