pipeline {

  agent any

tools {
        maven "M2_HOME"
}

  stages {
stage('Build Backend') {
      steps {
        sh 'chmod +x mvnw'
        sh 'mvn deploy -DskipTests=true'
       
      }
      post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar'
                }
            }
    }
    
    

    stage('Test Backend') {
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

	  
 /*       stage('Deploy Backend to Nexus') {
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
                 }
*/

	   stage("Deploy Artifact to Nexus") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: "nexus3",
                            protocol: "http",
                            nexusUrl: "192.168.33.10:8081",
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: "maven-releases",
                            credentialsId: "NexusUserCreds",
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
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
