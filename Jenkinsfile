pipeline {

    agent any

    stages {
            stage('Build'){
                    steps {
                        git 'https://github.com/AnasHouissa/DevopsProjectBackend.git'
                        sh 'chmod +x mvnw'
                        sh './mvnw clean compile'
                    }
            }

            stage('Test'){
                     steps {
                              sh './mvnw test'


                     }
                     post {
                             success {
                                 jacoco(
                                     execPattern: '**/build/jacoco/*.exec',
                                     classPattern: '**/build/classes/java/main',
                                     sourcePattern: '**/src/main'
                                 )
                             }
                         }
            }
    }
}