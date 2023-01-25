def call(Map config = [:]) {
  sh """ #!/bin/bash
  NAMESPACE="${config.namespace}"
  VERSION="${config.version}"
  echo "======================================================"
  echo "APPLICATION VERSION : \$VERSION"
  echo "Environment: ${config.environment}"
  echo "======================================================"

  kubectl apply -f helmtemplates/fastapi/templates -n \$NAMESPACE
  kubectl get pods -n \$NAMESPACE
  kubectl get ingress -n \$NAMESPACE
  """

}
