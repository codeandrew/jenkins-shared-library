pipeline {
    agent any
    
    stages {
        stage('Fail') {
            steps {
                sh 'echo "Task failed!"'
                sh 'exit 1' // Exit with failure status
            }
        }
    }
    
    post {
        failure {
            emailext (
                subject: "Pipeline Failed: ${currentBuild.fullDisplayName}",
                body: "The pipeline ${currentBuild.fullDisplayName} has failed. Please check the Jenkins console output for more details.",
                to: "jeanandrewfuentes@gmail.com"
            )
        }
    }
}
