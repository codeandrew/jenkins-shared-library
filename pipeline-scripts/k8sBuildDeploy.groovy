@Library("shared-library") _
pipeline {
    environment{
        app_version = sh(script: "echo \$(date +'%y.%m.%d')-$BUILD_NUMBER", returnStdout: true).trim()
    }

    agent any
    stages {
        stage("Clone"){
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
                dir("/tmp/scan"){
                    trivyScanner(
                        targetImage: "codeandrew/fastapi:${env.app_version}",
                        outputPath:"/tmp/scan_results",
                        scannerURL: "http://206.189.148.20:5000/store",
                        gitSecrets: "codeandrew-github-private-repo"
                    )
                }
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