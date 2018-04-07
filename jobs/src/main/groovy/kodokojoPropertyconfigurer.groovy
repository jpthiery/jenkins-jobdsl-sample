

job('property-configurer') {
  scm {
    git {
      remote {
        name('origin')
        url('https://github.com/kodokojo/property-configurer.git')
        branch('master')
      }
    }
  }
  label('docker')
  steps {
    shell 'mvn clean verify'
  }
  logRotator(15, 10)
}
