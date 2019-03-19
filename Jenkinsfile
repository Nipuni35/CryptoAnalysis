pipeline {
    agent any
    stages {

        stage('Build') {
            steps {
                sh 'cd CryptoAnalysis; mvn clean compile'
            }
        }

	    stage('Test') {
	        steps {
	            sh 'cd CryptoAnalysis; mvn test'
	        }
		}


		stage('Deploy'){
		    when { 
		    	branch 'master'
			}
	        steps {
				configFileProvider(
	        		[configFile(fileId: '1d7d4c57-de41-4f04-8e95-9f3bb6382327', variable: 'MAVEN_SETTINGS')]) {
	      		  		sh 'cd CryptoAnalysis; mvn -s $MAVEN_SETTINGS clean deploy -DskipTests'
				}
	        }
		}

    }
}