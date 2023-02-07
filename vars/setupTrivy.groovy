def call(Map config = [:]) {
  sh """
  curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin v0.36.1
  trivy -v
  trivy image --download-db-only
  """
}
