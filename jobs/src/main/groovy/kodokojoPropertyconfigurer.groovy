import fr.xebia.jpthiery.jenkins.jobs.DockerMavenJobBuilder

def dslFactory = this

def jobBuilder = new DockerMavenJobBuilder("property-configurer")
jobBuilder.repositoryUrl('https://github.com/kodokojo/property-configurer.git')
  .labels('jkmaster')

jobBuilder.mvnCmd("mvn -B clean verify")

jobBuilder.build(dslFactory)
