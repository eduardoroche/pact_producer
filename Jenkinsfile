#!groovy
pipeline {

  agent any


  parameters {
    string(name: 'pactConsumerTags', defaultValue: 'test')
  }

  tools {
	maven 'maven'
  }
  
  stages {
    stage ('Build') {
      steps {
		sh "mvn clean verify -Dpact.provider.version=${GIT_COMMIT} -Dpact.verifier.publishResults=true  -Dpactbroker.tags=${params.pactConsumerTags}"
      }
    }
    stage('Check Pact Verifications') {
      steps {
        sh 'curl -LO https://github.com/pact-foundation/pact-ruby-standalone/releases/download/v1.61.1/pact-1.61.1-linux-x86_64.tar.gz'
        sh 'tar xzf pact-1.61.1-linux-x86_64.tar.gz'
        dir('pact/bin') {
          sh "./pact-broker can-i-deploy -a user-service -b http://pact_broker -e ${GIT_COMMIT}"
        }
      }
    }
    stage('Deploy') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploying to prod now...'
      }
    }
  }

}