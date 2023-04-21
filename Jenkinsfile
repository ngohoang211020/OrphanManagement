pipeline {
  agent any
  stages {
    stage('Build image') {
      steps {
        sh 'docker build -t 211020/orphan-management:${latest} .'
      }
    }
  }
}
