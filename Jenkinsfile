pipeline {
    agent any

    stages {
            stage('Build'){
                        steps {
                        git 'https://github.com/AnasHouissa/DevopsProjectBackend.git'
                        sh './mvnw clean compile'
                    }
            }

            stage('Test'){
                         steps {
                         sh 'chmod +x mvnw'
                              sh './mvnw test'
                                }
                        }
    }
}