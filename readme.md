# Jenkins Shared Library

## Scenarios on consuming the shared library

### Setup Library 
Setup the shared library first 

```
$JENKINS_URL/manage/configure
Configure System -> Global Pipeline Libraries
name: shared-library 
version: main  
Source Code Management: Git
- Project Repository: https://github.com/codeandrew/jenkins-shared-library.git

```



### String Parameter
vars/helloWorld.groovy
```groovy
@Library("shared-library") _
pipeline {
    agent any
    stages {

        stage("Test") {
            steps {
                helloWorld("Jean Andrew", "DevOps Engineer")
            }
        }
    }
}

```

### Map Config
vars/helloWorldMap.groovy
```groovy
@Library("shared-library") _
pipeline {
    agent any
    stages {

        stage("Test") {
            steps {
                helloWorldMap(name:"Jean Andrew", job: "DevOps Engineer")
            }
        }
    }
}
```

## BRANCHING STRATEGY

| Branch     | Stage Triggered                   |
|------------|-----------------------------------|
| Any Branch | Build Stage                       |
| develop    | Deploy to Integration Environment |
| release/*  | Deploy to Staging Environment     |
| master     | Deploy to Production Environment  |


```groovy
pipeline {
    agent any

    stages {
        stage('Build') {
            when {
                anyOf {
                    changeset '*'
                    branch 'main'
                }
            }
            steps {
                // Build the application
            }
        }

        stage('Deploy to Integration') {
            when {
                branch 'develop'
            }
            steps {
                // Deploy to integration environment
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'release/*'
            }
            steps {
                // Deploy to staging environment
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                // Deploy to production environment
            }
        }
    }
}

```

## Secrets Management

### Private Repo


## References:
- https://www.jenkins.io/doc/book/pipeline/shared-libraries/
- Creating a CI/CD Pipeline for Your Shared Libraries: https://www.youtube.com/watch?v=DDBzm-KT24E
- Concise Pipelines with Shared Libraries: https://www.youtube.com/watch?v=FXoW3HP1ebk
- infra as code: https://www.digitalocean.com/community/tutorials/how-to-automate-jenkins-setup-with-docker-and-jenkins-configuration-as-code
- Pipeline as Yaml: https://plugins.jenkins.io/pipeline-as-yaml/
- Adding an agent: https://www.jenkins.io/doc/book/using/using-agents/