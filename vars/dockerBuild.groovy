def call(Map config = [:]) {
  sh """
  IMAGE="${config.image_repository}/${config.container_name}:${config.tag}"
  docker build -t \$IMAGE .
  docker push \$IMAGE
  """
}
