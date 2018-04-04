job('seed') {
  scm {
    git {
      remote {
        name('origin')
        url('https://github.com/jpthiery/jenkins-jobdsl-sample.git')
        branch('master')
      }
    }
  }
  triggers {
    scm 'H/5 * * * *'
  }
  label('jkmaster')
  steps {
    shell './gradlew cleanAll build libs'
  }
  steps {
    dsl {
      external('jobs/src/main/groovy/*.groovy')
      additionalClasspath(['build/libs/*.jar', 'build/*.jar'].join('\n'))
      removeAction('DELETE')
      removeViewAction('DELETE')
    }
  }
  logRotator(15, 10)
}
