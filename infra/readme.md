# Jenkin Infra as Code

## Overrides
Never do this in Production, only for Debugging Purposes 
``` 
docker exec -ti -u 0 jenkins-blueocean /bin/bash      
apt -y install sudo vim

vi /etc/sudoers
jenkins ALL=(ALL) NOPASSWD:ALL
```