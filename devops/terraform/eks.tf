module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  version         = "~> 19.0"
  cluster_name    = "shop-app"
  cluster_version = "1.24"

  cluster_endpoint_public_access = true

  vpc_id     = module.shop-vpc.vpc_id
  subnet_ids = module.shop-vpc.private_subnets

  tags = {
    environment = "dev"
    application = "shop-app"
  }

  eks_managed_node_groups = {
    dev = {
      min_size     = 1
      max_size     = 3
      desired_size = 2

      instance_types = ["t2.medium"]
    }
  }
}