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
    }

}
