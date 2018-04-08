import fr.xebia.jpthiery.jenkins.jobs.MavenJobBuilder

def dslFactory = this

def repository = 'kodokojo'
def projects = ['property-configurer']

def environments = [
  'dev1',
  'dev2',
  'dev3',
  'int1',
  'int2',
  'int3',
  'uat1',
  'uat2',
  'uat3',
  'recette',
  'prodlike1',
  'prodlike2',
  'prodlike3',
  'production'
]

environments.each { env ->
  folder(env)
  projects.each { project ->

    def jobBuilder = new MavenJobBuilder("${env}/${project}")
    jobBuilder.repositoryUrl("https://github.com/${repository}/${project}.git")
      .labels('jkmaster')

    jobBuilder.lookupGithubBranch(repository, project)
    jobBuilder.mvnCmd("mvn -B clean verify")
    jobBuilder.logRotator {
      ctx ->
        ctx.daysToKeep(15)
        ctx.numToKeep(10)
    }

    jobBuilder.build(dslFactory)
  }
}

