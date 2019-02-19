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
code=$(curl --silent -XPOST \
  -H "Authorization: Basic ${authString}" \
  http://localhost:4501/oauth2/authorize  \
  -d response_type=code \
  -d client_id=probate \
  -d redirect_uri=http://localhost:3451/oauth2redirect)

code=${code#"$prefix"}
code=${code%"$suffix"}

token=$(curl --silent -XPOST \
  http://localhost:4501/oauth2/token  \
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

id=$(curl --silent -XGET \
  -H "Authorization: Bearer ${token}" \
  http://localhost:4501/details)

echo
echo 'User Details: '
echo $id

serviceToken=$(curl --silent -X POST http://localhost:4502/testing-support/lease -d '{"microservice":"probate_frontend"}' -H "CONTENT-TYPE:application/json")

echo
echo 'Service Token: '
echo $serviceToken
echo