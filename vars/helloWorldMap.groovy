def call(Map config = [:]) {
  sh """
  echo Hello World, I am ${config.name}
  echo and Im a ${config.job}
  """
}
