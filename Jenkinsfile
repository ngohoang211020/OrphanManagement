pipeline {
    agent any
    environment {
             VERSION            = 'latest'
             PROJECT = 'orphan-management'
             DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    }
    stages {
        stage("Maven build") {
                    agent {
                        docker {
                            image 'maven:3.6.3-jdk-11'
                            args '-v /home/jenkins/.m2:/root/.m2 --network=host'
                            reuseNode true
                        }
                    }
                    steps {
                        sh 'mvn -s /root/.m2/settings-docker.xml -q -U clean install -Dmaven.test.skip=true -P server'
                    }
        }
         stage("Docker build") {
                    steps {
                        sh "docker build --network=host --tag 211020/${PROJECT}:${VERSION} ."
                    }
         }
         stage("Docker Push") {
                    steps {
                        sh "docker push 211020/${PROJECT}:${VERSION}"
                    }
         }
         stage("Deploy") {
                    steps {
                        sh "docker-compose pull"
                        sh "docker-compose down | echo IGNORE"
                        sh "docker-compose up -d"
                    }
         }
    }

}