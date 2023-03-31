@Library("shared-library@poc-topapps") _

def config = [
    output_path: '/tmp/scan_results',
    target_image: "codeandrew/fastapi:latest"
]

pipeline {
    agent any
    environment {
        image_repository = "codeandrew"
        container_name = "fastapi"
        app_version = sh(script: "echo \$(date +'%y.%m.%d')-$BUILD_NUMBER", returnStdout: true).trim()
    }
    stages {
        stage('Checkout Git Branch') {
            steps{
                git url: "https://github.com/codeandrew/fastapi-poc.git", branch: 'main'
            }
        }

        stage('Scan Repository'){
            steps{
                trivyScannerRepo(
                    outputPath: "${config.output_path}"
                )
                dir("${config.output_path}"){
                    archiveArtifacts artifacts: "$BUILD_ID-*", followSymlinks: false
                }
            }
        }

        stage('Build And Push Docker Image') {
            steps {
                dockerBuild(
                    image_repository: "${env.image_repository}",
                    container_name: "${env.container_name}",
                    tag: "${env.app_version}"
                )
            }
        }

        stage('Scan Image') {
             steps{
                trivyScannerContainer(
                    targetImage: "${config.target_image}",
                    outputPath:"${config.output_path}"
                )

                dir("${config.output_path}"){
                    archiveArtifacts artifacts: "$BUILD_ID-*", followSymlinks: false
                }
            }
        }
    }
}