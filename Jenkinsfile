def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
]
pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-11'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -q -U clean install deploy -Dmaven.test.skip=true'
            }
        }
    }
}
