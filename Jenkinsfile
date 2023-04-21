def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
]
pipeline {
    agent any

    environment {
         BUILD_USER         = 'Jenkins'
         PROJECT            = 'Java'
         VERSION            = 'latest'
         DOCKER_HUB_REPO = '211020/orphan-management'
         DOCKER_HUB_USERNAME = credentials('hoanggg2110@gmail.com')
         DOCKER_HUB_PASSWORD = credentials('Giacbavanh@2110')

    }
    stages {
        stage('Checkout') {
            steps {
                    checkout scm
            }
        }
        stage("Maven build") {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    args '-v /home/jenkins/.m2:/root/.m2 --network=host'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn -s /root/.m2/settings.xml -q -U clean install -Dmaven.test.skip=true -P server'
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                 script {
                    def dockerImage = docker.build("${DOCKER_HUB_REPO}:${VERSION}")
                    docker.withRegistry("https://registry.hub.docker.com", "docker-hub-credentials") {
                    dockerImage.push()
                    }
                 }
            }
        }
        stage("Deploy") {
            steps {
                sh "docker-compose up -d"
            }
        }
    }
    post {
        success {
            sh 'echo "Deployment succeeded!"'
            }

        failure {
            sh 'echo "Deployment failed!"'
            }
        }
}
