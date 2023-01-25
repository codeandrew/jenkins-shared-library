@Library("shared-library") _
pipeline {
    agent any
    stages {

        stage("Test") {
            steps {
                helloWorld("Jean Andrew", "DevOps Engineer")
                sh ''' #!/bin/bash
                docker ps
                docker images
                echo "[+] BUILD NUMBER:"
                echo ${BUILD_NUMBER}
                '''
            }
        }
        
        stage("Git"){
            steps{
                git url: "https://github.com/codeandrew/fastapi-poc.git", branch: 'main'
                sh '''
                ls -latr 
                pwd
                #printenv
                cat /etc/*-release
                '''
            }
        }
        
        stage('Setup'){
            steps {
              sh """
                curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
                chmod 700 get_helm.sh
                ./get_helm.sh
            
                helm version
              """
              
            }
        }
        stage('Package'){
            steps {
                sh '''#!/bin/bash
                VERSION=test-latest
                sed -i "s/APPVERSION/$VERSION/" chart/Chart.yaml
                sed -i "s/APPVERSION/$VERSION/" chart/values-*.yaml
                ENVIRONMENT=dev
                helm template ./chart --values ./chart/values.yaml \
                    --values ./chart/values-dev.yaml \
                    --set app.environment=$ENVIRONMENT \
                    --output-dir ./helmtemplates
                '''
            }
        }
        stage('Build'){
            steps {
                sh ''' #!/bin/bash
                pwd; ls -latr
                VERSION=$(date +'%y.%m.%d')-${BUILD_NUMBER}
                docker build -t codeandrew/fastapi:$VERSION .
                docker push codeandrew/fastapi:$VERSION 
                
                '''
            }
        }
        stage('Deploy'){
            steps {
                sh ''' #!/bin/bash
                VERSION=$(date +'%y.%m.%d')-${BUILD_NUMBER}
                IMAGE=codeandrew/fastapi:$VERSION
                CONTAINER_NAME=fastapi
                APP_VERSION="$VERSION"

                echo "[+] DOCKER RUN : $IMAGE"
                docker pull $IMAGE
                
                if [ ! "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
                    docker stop $CONTAINER_NAME &>/dev/null
                    if [ "$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)" ]; then
                        docker rm $CONTAINER_NAME  &>/dev/null
                    fi
                    docker run --name $CONTAINER_NAME -d --env app_verions="$APP_VERSION" -p 80:80 -v $(pwd):/app $IMAGE 
                fi
                sleep 1
                docker ps 
                docker container ls -a
                '''
            }
        }
      
    }
}