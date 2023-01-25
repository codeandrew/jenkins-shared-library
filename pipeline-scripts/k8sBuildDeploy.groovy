@Library("shared-library") _
pipeline {
    environment{
        app_version = sh(script: "echo \$(date +'%y.%m.%d')-$BUILD_NUMBER", returnStdout: true).trim()
    }

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
                echo $app_version
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
              setupKubectl()
              setupHelm()
            }
        }
        stage('Build'){
            steps {
                dockerBuild(
                    image_repository: "codeandrew",
                    container_name: "fastapi",
                    tag: "${env.app_version}"
                )
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