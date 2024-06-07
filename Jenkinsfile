pipeline {

    agent any

    tools {
        gradle 'gradle8.8'
    }

    stages {
        stage('checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/thecoderscombr/bytebeans-core.git'
            }
        }

        stage('clean'){
            steps{
                sh 'gradle clean'
            }
        }

        stage('build'){
            steps{
                sh 'gradle shadowJar'
            }
        }

        stage('archive') {
            steps {
                archiveArtifacts artifacts: '**/bytebeans-core.jar', followSymlinks: false
            }
        }
    }
}