# Jenkins Shared Library

## Scenarios on consuming the shared library

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
