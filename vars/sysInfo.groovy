def call(){
  sh '''
  set +x
  # Get OS Name
  os_name=$(cat /etc/*-release | grep ^NAME | awk -F'=' '{print $2}')

  # Get OS Version
  os_version=$(cat /etc/*-release | grep ^VERSION | awk -F'=' '{print $2}')

  # Get Number of CPUs
  num_cpus=$(lscpu | grep '^CPU(s):' | awk '{print $2}')

  # Get RAM
  ram=$(free -h | grep Mem: | awk '{print $2}')

  # Get Network Interface Information
  interface_info=$(ifconfig -a)

  # Get IP Addresses
  ip_addresses=$(ip -4 addr show scope global | grep -oP '(?<=inet\\s)\\d+(\\.\\d+){3}')

  # Print System Information
  echo "OS Name: $os_name"
  echo "OS Version: $os_version"
  echo "Number of CPUs: $num_cpus"
  echo "RAM: $ram"
  echo "Network Interface Information: $interface_info"
  echo "IP Addresses: $ip_addresses"


  # List of tools to check for
  tools=("docker" "docker-compose" "ping" "wget" "curl" "traceroute" "dig" "network-manager")

  # Function to check if a tool is present
  check_tool () {
      if command -v $1 > /dev/null; then
          echo "$1 is present"
      else
          echo "$1 is not present"
      fi
  }

  # Loop through the list of tools and check if they are present
  for tool in "${tools[@]}"; do
      check_tool $tool
  done

  echo "[+] Checking HTTP CONNECTION"
  response=$(curl -s -o /dev/null -w "%{http_code}" example.com)

  if [ $response -eq 200 ]; then
      echo "HTTP connection ... Succesful"
  else
      echo "[-] HTTP connection ... Failed"
  fi
  '''
}
