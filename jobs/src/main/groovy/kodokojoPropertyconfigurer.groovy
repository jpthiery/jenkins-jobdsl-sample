import fr.xebia.jpthiery.jenkins.jobs.MavenJobBuilder

def dslFactory = this

def jobBuilder = new MavenJobBuilder("property-configurer")
jobBuilder.repositoryUrl('https://github.com/kodokojo/property-configurer.git')
  .labels('jkmaster')

jobBuilder.lookupGithubBranch('kodokojo', 'property-configurer')
jobBuilder.mvnCmd("mvn -B clean verify")

jobBuilder.build(dslFactory)
