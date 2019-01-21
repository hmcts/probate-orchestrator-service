// Infrastructural variables
variable "product" {}

variable "raw_product" {
  default = "probate" // jenkins-library overrides product for PRs and adds e.g. pr-118-cmc
}

variable "microservice" {
  default = "orchestrator-service"
}

variable "location" {
  default = "UK South"
}

variable "env" {
  type = "string"
}

variable "ilbIp" { }

variable "deployment_env" {
  type = "string"
}

variable "tenant_id" {
  description = "(Required) The Azure Active Directory tenant ID that should be used for authenticating requests to the key vault. This is usually sourced from environemnt variables and not normally required to be specified."
}

variable "component" {
  default = "backend"
}

variable "subscription" {}


variable "vault_section" {
  type = "string"
}

// CNP settings
variable "jenkins_AAD_objectId" {
  type                        = "string"
  description                 = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}


variable "outbound_proxy" {
  default = "http://proxyout.reform.hmcts.net:8080/"
}

variable "no_proxy" {
  default = "localhost,127.0.0.0/8,127.0.0.1,127.0.0.1*,local.home,reform.hmcts.net,*.reform.hmcts.net,betaDevBprobateApp01.reform.hmcts.net,betaDevBprobateApp02.reform.hmcts.net,betaDevBccidamAppLB.reform.hmcts.net,*.internal,*.platform.hmcts.net"
}

variable "appinsights_instrumentation_key" {
  description = "Instrumentation key of the App Insights instance this webapp should use. Module will create own App Insights resource if this is not provided"
  default = ""
}

variable "services_persistence_baseUrl" {
  default = "/"
}


variable "idam_service_api" {
  type = "string"
}

variable "ccd_baseUrl" {
  default = "/"
}

variable "ccd_enabled" {
  default = "true"
}

variable "probate_mail_subject" {
  default = "Probate Application"
}

variable "probate_mail_use_auth" {
  default = "false"
}

variable "probate_mail_use_ssl" {
  default = "false"
}

variable "log_level" {
  default = "INFO"
}

variable "capacity" {
  default = "1"
}

variable "common_tags" {
  type = "map"
}

variable "asp_rg" {
  
}

variable "asp_name" {
  
}

variable "spring_application_json" {

}

variable "services_auth_provider_baseurl" {

}

variable "auth_idam_client_baseurl" {

}

variable "submit_service_api_url" {

}