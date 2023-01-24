pipeline {
    agent any

    parameters {
        container_registry defaultValue: "docker.io/codeandrew"
        container_image_name defaultValue: "flask_crud"
        tag defaultValue: "1.0.0"
        build_dir defaultValue: "."
        environment defaultValue: "test,dev"
    }

    stages {
        stage("Build") {
            steps {
                script {
                    sh "docker build -t ${params.container_registry}/${params.container_image_name}:${params.tag} ${params.build_dir}"
                }
            }
        }
        stage("Packaging") {
            steps {
                script {
                    sh "docker push ${params.container_registry}/${params.container_image_name}:${params.tag}"
                }
            }
        }
        stage("Deploy Environment") {
            steps {
                script {
                    for (env in params.environment.split(",")) {
                        sh "docker-compose -f docker-compose.yml -f ${env}.yml up -d"
                    }
                }
            }
        }
    }
}
