pipeline {
    agent any

    tools {
        maven 'Maven' 
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'echo Running build'
		dir('CloudAdmin') {
			sh 'mvn clean package'
		}
            }
        }
        stage('Test') {
            steps {
                sh 'echo Running tests'
		dir('CloudAdmin') {
			sh 'mvn test'
		}
            }
        }
        stage('Deploy') {
            steps {
                // 여기에 WAR 파일을 특정 위치로 이동하는 명령어 추가
                sh 'echo Deploying the build'
		dir('CloudAdmin') {
			sh 'cp target/firstSamplePro-1.0.0.war /kwsong/'
			sh 'cd /kwsong && mv firstSamplePro-1.0.0.war CloudAdmin.war'
		}
            }
        }
	// Deploy Admin 단계를 stages 블록 내로 이동
        stage('Deploy Admin') {
            steps {
                script {
                    sh '/kwsong/web_a.sh'
                }
            }
        }
    }
}
