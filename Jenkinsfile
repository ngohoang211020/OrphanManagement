def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
]
pipeline {
    agent any

    environment {
         DOCKER_CREDENTIALS = credentials('docker-builder')
         BUILD_USER         = 'Jenkins'
         PROJECT            = 'Java'
         VERSION            = 'latest'
         DOCKER_IMAGE_NAME = '211020/orphan-management'
         DOCKER_REGISTRY_CREDENTIALS = credentials('docker-hub-credentials')

    }
    stages {
        stage('Build') {
            agent {
                docker {
                     image 'maven:3.6.3-jdk-11'
                     args '-v /root/.m2:/root/.m2'
                     }
                }
            steps {
                sh 'mvn -q -U clean install -Dmaven.test.skip=true -P server'
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
