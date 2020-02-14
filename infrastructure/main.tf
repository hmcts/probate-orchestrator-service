provider "azurerm" {
  version = "1.22.1"
}



locals {
  aseName = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  //java_proxy_variables: "-Dhttp.proxyHost=${var.proxy_host} -Dhttp.proxyPort=${var.proxy_port} -Dhttps.proxyHost=${var.proxy_host} -Dhttps.proxyPort=${var.proxy_port}"

  //probate_frontend_hostname = "probate-frontend-aat.service.core-compute-aat.internal"
  previewVaultName = "${var.raw_product}-aat"
  nonPreviewVaultName = "${var.raw_product}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"
  localenv = "${(var.env == "preview" || var.env == "spreview") ? "aat": "${var.env}"}"

}

data "azurerm_key_vault" "probate_key_vault" {
  name = "${local.vaultName}"
  resource_group_name = "${local.vaultName}"
}

data "azurerm_key_vault" "s2s_vault" {
  name = "s2s-${local.localenv}"
  resource_group_name = "rpe-service-auth-provider-${local.localenv}"
}

data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-probate-backend"
  key_vault_id = "${data.azurerm_key_vault.s2s_vault.id}"
}

data "azurerm_key_vault_secret" "probate_mail_host" {
  name = "probate-mail-host"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "probate_mail_username" {
  name = "probate-mail-username"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "probate_mail_password" {
  name = "probate-mail-password"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "probate_mail_port" {
  name = "probate-mail-port"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "probate_mail_sender" {
  name = "probate-mail-sender"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "probate_mail_recipient" {
  name = "probate-mail-recipient"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "idamRedirectUrl" {
  name = "idamRedirectUrl"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "payCaseWorkerUser" {
  name = "payCaseWorkerUser"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "payCaseWorkerPass" {
  name = "payCaseWorkerPass"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

data "azurerm_key_vault_secret" "idam_secret_probate" {
  name = "ccidam-idam-api-secrets-probate"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "cronKeyCaveatExpiry" {
  name = "cronKeyCaveatExpiry"
  key_vault_id = "${data.azurerm_key_vault.probate_key_vault.id}"
}

module "probate-orchestrator-service" {
  source = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product = "${var.product}-${var.microservice}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  is_frontend  = false
  subscription = "${var.subscription}"
  asp_name     = "${var.asp_name}"
  capacity     = "${var.capacity}"
  common_tags  = "${var.common_tags}"
  asp_rg       = "${var.asp_rg}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"

  app_settings = {

	  // Logging vars
    REFORM_TEAM = "${var.product}"
    REFORM_SERVICE_NAME = "${var.microservice}"
    REFORM_ENVIRONMENT = "${var.env}"


    DEPLOYMENT_ENV= "${var.deployment_env}"

    MAIL_USERNAME = "${data.azurerm_key_vault_secret.probate_mail_username.value}"
    MAIL_PASSWORD = "${data.azurerm_key_vault_secret.probate_mail_password.value}"
    MAIL_HOST = "${data.azurerm_key_vault_secret.probate_mail_host.value}"
    MAIL_PORT = "${data.azurerm_key_vault_secret.probate_mail_port.value}"
    MAIL_JAVAMAILPROPERTIES_SENDER = "${data.azurerm_key_vault_secret.probate_mail_sender.value}"
    MAIL_JAVAMAILPROPERTIES_RECIPIENT = "${data.azurerm_key_vault_secret.probate_mail_recipient.value}"

    S2S_AUTH_TOTP_SECRET = "${data.azurerm_key_vault_secret.s2s_key.value}"
    IDAM_API_REDIRECT_URL = "${data.azurerm_key_vault_secret.idamRedirectUrl.value}"
    PAYMENT_CASEWORKER_USERNAME = "${data.azurerm_key_vault_secret.payCaseWorkerUser.value}"
    PAYMENT_CASEWORKER_PASSWORD = "${data.azurerm_key_vault_secret.payCaseWorkerPass.value}"
    AUTH2_CLIENT_SECRET= "${data.azurerm_key_vault_secret.idam_secret_probate.value}"

    MAIL_JAVAMAILPROPERTIES_SUBJECT = "${var.probate_mail_subject}"
    MAIL_JAVAMAILPROPERTIES_MAIL_SMTP_AUTH = "${var.probate_mail_use_auth}"
    MAIL_JAVAMAILPROPERTIES_MAIL_SMTP_SSL_ENABLE = "${var.probate_mail_use_ssl}"
    SERVICES_PERSISTENCE_BASEURL = "${var.services_persistence_baseUrl}"
    AUTH_PROVIDER_SERVICE_CLIENT_BASEURL = "${var.idam_service_api}"
    SERVICES_CORECASEDATA_BASEURL = "${var.ccd_baseUrl}"
    SERVICES_CORECASEDATA_ENABLED = "${var.ccd_enabled}"

    SERVICES_AUTH_PROVIDER_BASEURL = "${var.services_auth_provider_baseurl}"
    AUTH_IDAM_CLIENT_BASEURL = "${var.auth_idam_client_baseurl}"
    SUBMIT_SERVICE_API_URL = "${var.submit_service_api_url}"
    BUSINESS_SERVICE_API_URL = "${var.business_service_api_url}"
    BACK_OFFICE_API_URL = "${var.back_office_api_url}"
    MIGRATION_JOB_SCHEDULER = "false"
    IDAM_CLIENT_ID = "probate"

    java_app_name = "${var.microservice}"
    LOG_LEVEL = "${var.log_level}"
    //ROOT_APPENDER = "JSON_CONSOLE" //Remove json logging
    TESTING = "TESTING"

    CRON_KEY_CAVEAT_EXPIRY = "${data.azurerm_key_vault_secret.cronKeyCaveatExpiry.value}"    
  }
}
