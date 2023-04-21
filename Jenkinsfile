pipeline {
  agent any
  stages {
    stage('Build image') {
        steps {
        sh '''
          docker version
          docker info
          docker compose version
          curl --version
          jq --version
        '''
      }
    }
  }
}
