def call(String name, String job){
  sh """
  echo Hello World, I am ${name}
  echo and Im a ${job}
  """
}

