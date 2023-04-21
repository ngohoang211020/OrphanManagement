def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
]
pipeline {
    agent any
    environment {
             VERSION            = 'latest'
             DOCKER_IMAGE_NAME = '211020/orphan-management'
        }
    stages {
        stage("Maven build") {
                    agent {
                        docker {
                            image 'maven:3.6.3-jdk-11'
                            args '-v /home/jenkins/.m2:/root/.m2 --network=host'
                        }
                    }
                    steps {
                        sh 'mvn -s /root/.m2/settings-docker.xml -q -U clean install -Dmaven.test.skip=true -P server'
                    }
        }
         stage("Docker build") {
                        steps {
                            sh "docker build --network=host --tag ${DOCKER_IMAGE_NAME}:${VERSION} ."
                        }
                }
                stage("Docker Push") {
                        steps {
                            sh "docker push ${DOCKER_IMAGE_NAME}:${VERSION}"
                        }
                }
    }

}
