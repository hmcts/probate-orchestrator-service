env = "aat"
deployment_env = "preprod"

vault_section = "preprod"

packages_environment = "preprod"
packages_version = "3.0.0"

capacity = "1"   //changed until internal memory to changed to persistence

idam_service_api  = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal"
ccd_baseUrl = "http://ccd-data-store-api-aat.service.core-compute-aat.internal"
services_persistence_baseUrl = "http://probate-persistence-service-aat.service.core-compute-aat.internal"

s2s_service_api = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal"
auth_provider_service_client_baseurl = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal"
services_corecasedata_baseurl = "http://ccd-data-store-api-aat.service.core-compute-aat.internal"
services_auth_provider_baseurl = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal"
auth_idam_client_baseurl = "https://preprod-idamapi.reform.hmcts.net:3511"
submit_service_api_url = "http://probate-submit-service-aat.service.core-compute-aat.internal"
spring_application_json = "{"registries":[{"address":"Digital Application\nOxford District Probate Registry\nCombined Court Building\nSt Aldates\nOxford\nOX1 1LY","email":"test.probate.inbox@gmail.com","id":0,"name":"oxford"},{"address":"Digital Application\nBirmingham Probate Registry\nThe Priory Courts\n33 Bull Street\nBirmingham\nWest Midlands\nB4 6DU\n","email":"test.probate.inbox@gmail.com","id":1,"name":"birmingham"},{"address":"Digital Applications\nManchester District Probate Registry\nCivil Justice Centre\nGround Floor\n1 Bridge Street West\nP.O. BOX 4240\nManchester\nM60 1WJ","email":"test.probate.inbox@gmail.com","id":2,"name":"manchester"}]}"


asp_name = "probate-aat"
asp_rg = "probate-aat"