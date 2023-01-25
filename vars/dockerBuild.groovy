def call(Map config = [:]) {
  sh """
  IMAGE="${config.image_repository}/${config.container_name}:${config.tag}"
  echo Hello World, I am "${config.name}"
  echo and Im a ${config.job}
  docker build -t ${IMAGE} .
  docker push ${IMAGE}
  """
}
