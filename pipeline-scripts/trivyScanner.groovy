@Library("shared-library@trivy") _

def images =[
    'python', 'nginx', 'node'
]

pipeline {
    agent {
        label 'dev'
    }

    stages {

        stage('update') {
             steps{
                sh """ #!/bin/bash
                trivy -v
                GITHUB_TOKEN=********** trivy image --download-db-only
                """ 
            }
        }

        stage('scan') {
             steps{
                trivyScanner(
                    targetImage: 'nginx:latest',
                    outputPath:"/tmp/scan_results"
                )
            }
        }

        stage('multi scan'){
            steps {
                script{
                    images.each{ image ->
                        trivyScanner(
                            targetImage: "${image}:latest",
                            outputPath: "/tmp/scan_results"
                        )

                    }
                }
            }
        }



    }
}