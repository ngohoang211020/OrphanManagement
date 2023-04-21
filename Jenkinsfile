pipeline {
  agent {
    docker {
      image 'docker:latest'
      args '-v /var/run/docker.sock:/var/run/docker.sock'
    }
  }
  stages {
    stage('Build image') {
      steps {
        sh 'docker build -t 211020/orphan-management:${latest} .'
      }
    }
  }
}
