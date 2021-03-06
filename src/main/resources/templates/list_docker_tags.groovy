import groovy.json.JsonSlurper
import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.domains.*

url = "${registry_url}/v2/${repository}/${component}/tags/list"
tags = []

credentialsStore = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
if (credentialsStore != null) {
  domain = new Domain(null, null, Collections.<DomainSpecification>emptyList())
  credentials = credentialsStore.getCredentials(domain)
  creds = credentials.find({ it.id == "${credentialId}"})
  if (creds != null) {

    auth = creds.username +":"+ creds.password.getPlainText()
    basic = auth.getBytes().encodeBase64().toString()

    URLConnection connection = new URL(url).openConnection()
    connection.setRequestProperty("Authorization", "Basic " + basic)
    response = new JsonSlurper().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())))
    tags = response.tags.sort().reverse()
  }
}
tags
