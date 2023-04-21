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
        agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    args '-v /root/.m2:/root/.m2'
                }
        }
        stage("Maven build") {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}
