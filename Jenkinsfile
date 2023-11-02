pipeline {

  agent any

  stages {
stage('Build Backend') {
      steps {
        sh 'chmod +x mvnw'
        sh './mvnw clean install -DskipTests'
       
      }
      post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar'
                }
            }
    }
    
    
      stage("Docker build backend"){
         steps {
         sh 'docker build -t devops_back_end .'
         sh 'docker image list'
         sh 'docker tag devops_back_end houissa1998/devops_back_end:latest'
        
        withCredentials([string(credentialsId: 'DockerhubCred', variable: 'PASSWORD')]) {
            sh 'docker login -u houissa1998 -p $PASSWORD'
        }
       }
  }
    stage("Push Backend Image to Docker Hub"){
      steps {
       sh 'docker push  houissa1998/devops_back_end:latest'

    }
    }

/*    stage('Test Backend') {
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
    }*/
   /* stage('SonarQube Analysis') {
          
		  environment {
             scannerHome = tool 'sonarscanner'
          }

          steps {
            withSonarQubeEnv('sonarserver') {
               sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=DevOps_Project \
                   -Dsonar.projectName=DevOps_Project \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/main \
                   -Dsonar.java.binaries=target/classes/tn/esprit/devops_project/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
            }

            timeout(time: 5, unit: 'MINUTES') {
               waitForQualityGate abortPipeline: true
            }
          }
        }*/
      /*  stage("Publish to Nexus Repository Manager") {
        steps {
            nexusArtifactUploader(
                            nexusVersion: "nexus3",
                            protocol: "http",
                            nexusUrl: "192.168.33.10:8081",
                            groupId: "tn.esprit",
                            version: "1.0",
                            repository: "maven-releases",
                            credentialsId: "NexusUserCreds",
                            artifacts: [
                                [artifactId: "DevOps_Project",
                                classifier: '',
                                file: "/var/lib/jenkins/.m2/repository/tn/esprit/DevOps_Project/1.0/DevOps_Project-1.0.jar",
                                type: "jar"]
                            ]
                        );
        }
    }*/
	
  }
  post{
        always{
            sh 'docker logout'
        }
    }
}
