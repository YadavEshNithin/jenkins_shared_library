def call (Map configMap){
    pipeline {
        agent {
            label "AGENT-1"
        }
        environment {
            COURSE = "JENKINS"
            greeting = configMap.get('greeting')
        }
        options {
            timeout(time: 30, unit: 'MINUTES') 
            disableConcurrentBuilds()
        }
        parameters {
            string(name: 'PERSON', defaultValue: 'Mr Jenkins NEW', description: 'Who should I say hello to?')
            text(name: 'BIOGRAPHY', defaultValue: 'JB', description: 'Enter some information about the person')
            booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value')
            choice(name: 'CHOICE', choices: ['One', 'Two', 'Three'], description: 'Pick something')
            password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'Enter a password') 
        }
        stages {
            stage('Build') {
                steps {
                    script {
                        sh """
                            echo 'Building..'
                            env
                            sleep 8
                            echo "hello ${params.PERSON}"
                            echo "hello NEW"  
                        """
                    }
                }
            }
            stage('Test') {
                steps {
                    echo 'Testing..'
                }
            }
            stage('Deploy') {
                input {
                    message "Should we continue?"
                    ok "Yes, we should."
                    submitter "alice,bob"
                    parameters {
                        string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
                    }
                }
                steps {
                    echo 'Deploying....'
                    echo "Hello, ${PERSON}, nice to meet you."
                }
            }
        }

        post {
            always {
                echo " always hello"
                deleteDir()
            }
            success {
                echo " success hello"
            }
            failure {
                echo " failure hello"
            }
        }
}
}