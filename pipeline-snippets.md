# Pipeline Snippets

## Git Events

the BRANCH_NAME environment variable is used to identify the triggered branch. If the branch is "staging", it performs a specific code block and echoes "Hello". If the branch is "production", it exits with an error. For any other branch, it echoes that the branch is neither staging nor production.

```groovy
pipeline {
    agent any
    
    stages {
        stage('Identify Branch') {
            steps {
                script {
                    def branchName = env.BRANCH_NAME
                    echo "Triggered branch: $branchName"
                    
                    if (branchName == 'staging') {
                        echo "Branch is staging"
                        // Perform some code specific to staging
                        echo "Hello"
                    } else if (branchName == 'production') {
                        error "Branch is production. Exiting with error."
                    } else {
                        echo "Branch is neither staging nor production"
                    }
                }
            }
        }
    }
}

```

stages that check for specific branch conditions:

the pipeline has a stage called "Identify Branch" where it checks the branch name and performs specific actions based on the conditions. If the branch name is "develop", it echoes "Merged to develop branch". If the branch name starts with "release/", it echoes "Release branch created".

```groovy
pipeline {
    agent any

    stages {
        stage('Identify Branch') {
            steps {
                script {
                    def branchName = env.BRANCH_NAME
                    echo "Triggered branch: $branchName"

                    // Check if merged to develop branch
                    if (branchName == 'develop') {
                        echo "Merged to develop branch"
                    }

                    // Check if release/* branch is created
                    if (branchName.startsWith('release/')) {
                        echo "Release branch created"
                    }
                }
            }
        }
    }
}

```
