resource "aws_ecr_repository" "lil_ecr_repo" {
  name = "shop-app"
}


output "ecr_repository_url" {
  value = "${aws_ecr_repository.lil_ecr_repo.repository_url}"
}