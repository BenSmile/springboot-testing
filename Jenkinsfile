pipeline{
    agent any

    stages{
        stage('Build Artifact') {
            steps{
                sh "./mvnw clean package -DskipTests=true"
                archive 'target/*.jar'
            }
        }

         stage('Unit Tests') {
                steps{
                    sh "./mvnw test"
                }
         }
    }
}