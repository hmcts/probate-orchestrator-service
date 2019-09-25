provider "azurerm" {
  version = "1.19.0"
}

locals {
  aseName = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  //java_proxy_variables: "-Dhttp.proxyHost=${var.proxy_host} -Dhttp.proxyPort=${var.proxy_port} -Dhttps.proxyHost=${var.proxy_host} -Dhttps.proxyPort=${var.proxy_port}"

  //probate_frontend_hostname = "probate-frontend-aat.service.core-compute-aat.internal"
  previewVaultName    = "${var.raw_product}-aat"
  nonPreviewVaultName = "${var.raw_product}-${var.env}"
  vaultName           = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"
  localenv            = "${(var.env == "preview" || var.env == "spreview") ? "aat": "${var.env}"}"
}

data "azurerm_key_vault" "probate_key_vault" {
  name                = "${local.vaultName}"
  resource_group_name = "${local.vaultName}"
}

data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-probate-backend"
  vault_uri = "https://s2s-${local.localenv}.vault.azure.net/"
}

data "azurerm_key_vault_secret" "probate_mail_host" {
  name      = "probate-mail-host"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "probate_mail_username" {
  name      = "probate-mail-username"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "probate_mail_password" {
  name      = "probate-mail-password"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "probate_mail_port" {
  name      = "probate-mail-port"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "probate_mail_sender" {
  name      = "probate-mail-sender"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "probate_mail_recipient" {
  name      = "probate-mail-recipient"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "idamRedirectUrl" {
  name      = "idamRedirectUrl"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "payCaseWorkerUser" {
  name      = "payCaseWorkerUser"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "payCaseWorkerPass" {
  name      = "payCaseWorkerPass"
  vault_uri = "${data.azurerm_key_vault.probate_key_vault.vault_uri}"
}
