@Library("shared-library") _
pipeline {
    agent any
    stages {

        stage("Test") {
            steps {
                helloWorld("Jean Andrew", "DevOps Engineer")
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
                docker build -t codeandrew/jaf-fastapi:latest .
                docker push codeandrew/jaf-fastapi:latest 
                
                '''
            }
        }
        
      
    }
}