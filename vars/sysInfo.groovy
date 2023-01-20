def call(){
  sh """
  cat /etc/*-release
  lscpu  | grep -E 'Architecture:|CPU\(s\):'
  free -g
  """
}
