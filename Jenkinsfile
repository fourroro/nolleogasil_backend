pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')  // Jenkins Credentials ID 사용
    }


    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/fourroro/nolleogasil.git', credentialsId: 'nolleogasil']]
                ])
            }
        }
        stage('Build Spring Boot') {
            steps {
                script {
                    // Spring Boot Docker 이미지 빌드
                    sh '''
                    docker build -t nolleogasil -f Dockerfile.spring .
                    '''
                }
            }
        }
        stage('Push Docker Images') {
            steps {
                script {
                    // Docker Hub 로그인
                    sh '''
                    echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin
                    '''

                    // Spring Boot 이미지 푸시
                    sh 'docker tag my-spring-boot-app $DOCKER_CREDENTIALS_USR/nolleogasil'
                    sh 'docker push $DOCKER_CREDENTIALS_USR/nolleogasil'
                }
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([
                                      string(credentialsId: 'spring-rabbitmq-username', variable: 'SPRING_RABBITMQ_USERNAME'),
                                      string(credentialsId: 'spring-rabbitmq-password', variable: 'SPRING_RABBITMQ_PASSWORD'),
                                      string(credentialsId: 'spring-rabbitmq-host', variable: 'SPRING_RABBITMQ_HOST'),
                                      string(credentialsId: 'spring-rabbitmq-port', variable: 'SPRING_RABBITMQ_PORT'),
                                      string(credentialsId: 'database-url', variable: 'DATABASE_URL'),
                                      string(credentialsId: 'database-username', variable: 'DATABASE_USERNAME'),
                                      string(credentialsId: 'database-password', variable: 'DATABASE_PASSWORD'),
                                      string(credentialsId: 'openai-api-key', variable: 'OPENAI_API_KEY'),
                                      string(credentialsId: 'openai-api-url', variable: 'OPENAI_API_URL'),
                                      string(credentialsId: 'kakao-client-id', variable: 'KAKAO_CLIENT_ID')
                                   ]){
                    script {
                           sh '''
                           docker stop spring-container || true
                           docker rm spring-container || true
                           docker run -d -p 8080:8080 --name spring-container \
                               -e SPRING_RABBITMQ_USERNAME=$SPRING_RABBITMQ_USERNAME \
                               -e SPRING_RABBITMQ_PASSWORD=$SPRING_RABBITMQ_PASSWORD \
                               -e SPRING_RABBITMQ_HOST=$SPRING_RABBITMQ_HOST \
                               -e SPRING_RABBITMQ_PORT=$SPRING_RABBITMQ_PORT \
                               -e SPRING_DATASOURCE_URL=$DATABASE_URL \
                               -e SPRING_DATASOURCE_USERNAME=$DATABASE_USERNAME \
                               -e SPRING_DATASOURCE_PASSWORD=$DATABASE_PASSWORD \
                               -e OPENAI_API_KEY=$OPENAI_API_KEY \
                               -e OPENAI_API_URL=$OPENAI_API_URL \
                               -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT_ID=$KAKAO_CLIENT_ID \
                               $DOCKER_USERNAME/nolleogasil:$BUILD_TAG
                           '''
                    }
                }
            }
        }
    }
    post {
            failure {
                script {
                    currentBuild.result = 'FAILURE'
                    echo "Build failed with status: ${currentBuild.result}"
                }
            }
            always {
                echo 'Build and deployment completed.'
            }
        }
}
