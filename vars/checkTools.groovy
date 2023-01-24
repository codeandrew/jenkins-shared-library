def call(String name, String job){
  sh '''
    tools=("docker" "docker-compose" "helm" "ping" "wget" "curl" "traceroute" "dig" "netcat" "nc")

    # Function to check if a tool is present
    check_tool () {
        if command -v $1 > /dev/null; then
            echo "[+] $1 is present"
        else
            echo "[-] $1 is not present"
        fi
    }
  '''
}

