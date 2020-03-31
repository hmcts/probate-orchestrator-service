// Infrastructural variables
variable "product" {}

variable "raw_product" {
  default = "probate" // jenkins-library overrides product for PRs and adds e.g. pr-118-cmc
}

variable "location" {
  default = "UK South"
}

variable "env" {
  type = "string"
}

variable "ilbIp" { }

variable "tenant_id" {
  description = "(Required) The Azure Active Directory tenant ID that should be used for authenticating requests to the key vault. This is usually sourced from environemnt variables and not normally required to be specified."
}

variable "component" {}

variable "subscription" {}

// CNP settings
variable "jenkins_AAD_objectId" {
  type                        = "string"
  description                 = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}
