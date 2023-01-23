def call(Map config = [:]) {
  sh """
  echo Hello World, I am ${config.name}
  echo and Im a ${config.job}

  if [[ -f microservice-templates ]]; then
    cd microservice-templates
  else
    git clone https://github.com/codeandrew/microservice-templates.git
    cd microservice-templates
  fi

  mkdir -p $HOME/mongo/crud
  docker volume create --driver local --opt type=none --opt device=$HOME/mongo/crud --opt o=bind mongo_crud
  """
}
