#!/bin/bash
## Usage: ./idam-login.sh email [password]
##
## Options:
##    - email: Email address
##    - password: User's password. Default to `password`.
##
## Login an IDAM user using Basic auth.

email={$1:-testusername1@test.com}
password=${2:-password}
redirectUrl=${3:-http://localhost:3451/oauth2redirect}

idamSecret=123456
prefix='{"code":"'
suffix='"}'

tokenPrefix='{"access_token":"'
tokenSuffix='","token_type":"Bearer","expires_in":28800}'
authString=$(echo -n 'testusername1@test.com:password' | base64)
code=$(curl -x proxyout.reform.hmcts.net:8080 --silent -XPOST \
  -H "Authorization: Basic ${authString}" \
  https://preprod-idamapi.reform.hmcts.net:3511/oauth2/authorize  \
  -d response_type=code \
  -d client_id=probate \
  -d redirect_uri=http://localhost:3451/oauth2redirect)

code=${code#"$prefix"}
code=${code%"$suffix"}

token=$(curl -x proxyout.reform.hmcts.net:8080 --silent -XPOST \
  https://preprod-idamapi.reform.hmcts.net:3511/oauth2/token  \
  -d code=$code \
  -d client_id=probate \
  -d client_secret=123456 \
  -d redirect_uri=http://localhost:3451/oauth2redirect \
  -d grant_type=authorization_code)

token=${token#"$tokenPrefix"}
token=${token%"$tokenSuffix"}

echo
echo 'User Token: '
echo $token

id=$(curl -x proxyout.reform.hmcts.net:8080 --silent -XGET \
  -H "Authorization: Bearer ${token}" \
  https://preprod-idamapi.reform.hmcts.net:3511/details)

echo
echo 'User Details: '
echo $id

serviceToken=$(curl -x proxyout.reform.hmcts.net:8080 --silent -X POST http://rpe-service-auth-provider-aat.service.core-compute-aat.internal//testing-support/lease -d '{"microservice":"probate_frontend"}' -H "CONTENT-TYPE:application/json")

echo
echo 'Service Token: '
echo $serviceToken
echo