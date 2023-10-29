pipeline {

    agent any

    stages {
            stage('Build Backend'){
                    steps {
                        git 'https://github.com/AnasHouissa/DevopsProjectBackend.git'
                        sh 'chmod +x mvnw'
                        sh './mvnw clean compile'
                    }
            }

            stage('Test Backend'){
                     steps {
                              sh './mvnw test'


                     }
                     post {
                             success {
                                 jacoco(
                                     execPattern: '**/target/*.exec',
                                 )
                             }
                         }
            }
    }
}