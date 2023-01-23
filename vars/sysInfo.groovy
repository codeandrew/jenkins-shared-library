def call(){
  sh """
  cat /etc/*-release
  #lscpu  | grep -E 'Architecture:|CPU\(s\):'
  lscpu  | grep -E 'Architecture:|CPU(s):'

  free -g

  echo "[+] Checking HTTP CONNECTION"
  response=$(curl -s -o /dev/null -w "%{http_code}" example.com)

  if [ $response -eq 200 ]; then
      echo "HTTP connection ... Succesful"
  else
      echo "[-] HTTP connection ... Failed"
  fi
  """
}
