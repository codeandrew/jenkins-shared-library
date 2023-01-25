# Jenkins Shared Library

## Scenarios on consuming the shared library

### Setup Library 
Setup the shared library first 

```
$JENKINS_URL/manage/configure
Configure System -> Global Pipeline Libraries
name: shared-library 
version: main 
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


## References:
- https://www.jenkins.io/doc/book/pipeline/shared-libraries/
- Creating a CI/CD Pipeline for Your Shared Libraries: https://www.youtube.com/watch?v=DDBzm-KT24E
- Concise Pipelines with Shared Libraries: https://www.youtube.com/watch?v=FXoW3HP1ebk
- infra as code: https://www.digitalocean.com/community/tutorials/how-to-automate-jenkins-setup-with-docker-and-jenkins-configuration-as-code
- Pipeline as Yaml: https://plugins.jenkins.io/pipeline-as-yaml/
- Adding an agent: https://www.jenkins.io/doc/book/using/using-agents/