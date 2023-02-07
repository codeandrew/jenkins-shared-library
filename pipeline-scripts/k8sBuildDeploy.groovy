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

        stage('Scan Vulnerabilities') {
            steps{
                setupTrivy()
                trivyScanner(
                    targetImage: "codeandrew/fastapi:${env.app_version}",
                    outputPath:"/tmp/scan_results"
                )
            }
        }

        stage('Package Helm'){ 
            steps {
                helmPackage(
                    version: "${env.app_version}",
                    environment: "test"
                )
            }
        }

        stage('Deploy To Kubernetes'){
            steps {
                k8sDeploy(
                    namespace: "app",
                    environment: "test",
                    version: "${env.app_version}"
                )
                
            }
        }

    }
}