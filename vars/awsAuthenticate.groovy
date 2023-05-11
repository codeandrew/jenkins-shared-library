def call(Map config = [:]) {
  sh """
  aws configure set aws_access_key_id ${config.AWS_ACCESS_KEY_ID}'
  aws configure set aws_secret_access_key ${config.AWS_SECRET_ACCESS_KEY}'
  aws configure set region ${config.REGION}
  aws sts get-caller-identity
  """
}
