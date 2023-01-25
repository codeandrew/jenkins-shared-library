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
            }
        }

        stage('Setup'){
            steps {
              sh '''

                echo "[+] Setup Kubectl"
                curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
                curl -LO "https://dl.k8s.io/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl.sha256"
                echo "$(cat kubectl.sha256)  kubectl" | sha256sum --check
                sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

                # For non root
                # chmod +x kubectl
                # mkdir -p ~/.local/bin
                # mv ./kubectl ~/.local/bin/kubectl

                curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
                chmod 700 get_helm.sh
                ./get_helm.sh
            
                helm version
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
        stage('Package Helm'){ 
            steps {
                sh '''#!/bin/bash
                echo ${BUILD_NUMBER}
                VERSION=$(date +'%y.%m.%d')-$BUILD_NUMBER
                sed -i "s/APPVERSION/$VERSION/" chart/Chart.yaml
                sed -i "s/APPVERSION/$VERSION/" chart/values-*.yaml

                ENVIRONMENT=dev
                helm template ./chart --values ./chart/values.yaml \
                    --values ./chart/values-dev.yaml \
                    --set app.tag=$VERSION \
                    --set env.app_version=$VERSION \
                    --set app.environment=$ENVIRONMENT \
                    --output-dir ./helmtemplates
                ls -altR ./helmtemplates

                cat ./helmtemplates/fastapi/templates/*.yaml
                '''
            }
        }

        stage('Deploy To Kubernetes'){
            steps {
                sh '''#!/bin/bash
                NAMESPACE=app
                VERSION=$(date +'%y.%m.%d')-$BUILD_NUMBER
                echo "======================================================"
                echo "APPLICATION VERSION : $VERSION"
                echo "======================================================"

                kubectl apply -f helmtemplates/fastapi/templates -n $NAMESPACE
                kubectl get pods -n app
                kubectl get ingress -n app
                
                '''
            }
        }



    }
}