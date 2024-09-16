terraform {
  backend "s3" {
    bucket = "lenoox"
    region = "eu-west-3"
    key    = "eks/terraform.tfstate"
  }
}