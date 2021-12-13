## Veteran Verification Tests
Veteran Verification integration tests.

### Local Testing
Run `start-processes-for-integration-tests.sh` to launch mock-mpi, mock-emis, and veteran-verification locally.

Use `local-docker-image` to build and run a local docker test image.

To run direct path tests on locally running applications use this command:
`./local-docker-image br smoke-test`

To run tests blocked by kong gateway, first decrypt lighthouse-veteran-verfication-deployment files.
export VV_DU_PATH repo with direct path to the DU.
`./local-docker-image b`
`./local-docker-image rc smoke-test`
