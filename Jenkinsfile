pipeline {

  agent any

tools {
        maven "M2_HOME"
}

  stages {
stage('Build Backend') {
      steps {
        sh 'mvn clean install'
       
      }
      post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar'
                }
            }
    }
    
    

    stage('Test Backend') {
      steps {
        sh 'mvn test'

      }
      post {
        success {
          jacoco(
            execPattern: '**/target/*.exec',
          )
        }
      }
    }



	  stage('Deploy Artifact to nexus') {
      steps {
               sh 'mvn deploy -DskipTests=true'
       
      }
     
    }
    
	  stage("Docker build backend"){
         steps {
         sh 'docker build -t devops_back_end .'
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

stage("Running Docker compose"){
      steps {
	sh 'docker compose up -d'	
    }
    }
	 
    stage('SonarQube Analysis') {
          
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
        }
	
  }
  post{
        always{
            sh 'docker logout'
        }
        success {            
            mail to :'anashouissa@gmail.com',
            subject : 'Successful Jenkins Build Of The Backend',
            body : 'Great news! The Jenkins build of the spring boot backend is a success.',
            from : 'fastyappesprit@gmail.com'
        }
        failure {
            mail to :'anashouissa@gmail.com',
            subject : 'Jenkins Build Of The Backend Failed',
            body : 'Unfortunately, the Jenkins build of the spring boot backend has encountered an issue and failed.',
            from : 'fastyappesprit@gmail.com'
        }
    }
}
