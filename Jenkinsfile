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
			sh 'cp target/firstSamplePro-1.0.0.war /hivesystem/'
		}
            }
        }
        stage('Deploy Admin') {
            steps {
                script {
                    // 스크립트 실행
                    sh '/hivesystem/sh/deploy_admin.sh'
                }
            }
        }
    }
}
