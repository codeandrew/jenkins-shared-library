def call(Map config = [:]) {
  sh """ #!/bin/bash
  IMAGE="${config.image_repository}/${config.container_name}:${config.tag}"
  docker build -t \$IMAGE .
  docker push \$IMAGE



  VERSION="${config.version}"
  ENVIRONMENT="${config.environment}"
  sed -i "s/APPVERSION/\$VERSION/" chart/Chart.yaml
  sed -i "s/APPVERSION/\$VERSION/" chart/values-*.yaml
  
  helm template ./chart --values ./chart/values.yaml \
  --values ./chart/values-dev.yaml \
  --set app.tag=\$VERSION \
  --set env.app_version=\$VERSION \
  --set app.environment=\$ENVIRONMENT \
  --output-dir ./helmtemplates
  ls -altR ./helmtemplates

  cat ./helmtemplates/fastapi/templates/*.yaml
  """
}
