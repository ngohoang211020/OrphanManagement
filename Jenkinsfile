def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
]
pipeline {
    agent any
    environment {
             VERSION            = 'latest'
             DOCKER_IMAGE_NAME = '211020/orphan-management'
             DOCKER_HUB_PASSWORD = 'Giacbavanh@2110'
             DOCKER_HUB_USERNAME ='hoanggg2110@gmail.com'
                 DOCKERHUB_CREDENTIALS = credentials('dockerhub')

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
                 stage('Login') {
                      steps {
                        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                      }
                    }
                stage("Docker Push") {
                                 steps {
                                         sh "docker push ${DOCKER_IMAGE_NAME}:${VERSION}"
                                     }

                }
    }

}
