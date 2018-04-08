import groovy.json.JsonSlurper

url = "https://api.github.com/repos/${repository}/${project}/branches"

branches = []

URLConnection connection = new URL(url).openConnection()
response = new JsonSlurper().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())))


branches = response
  .collect { it.name }
  .sort()

branches
