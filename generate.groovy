def generateChild(curDir, depth) {
  if (depth < 0) {
    return
  }
  def pom_file = new File(curDir, "pom.xml")
  assert pom_file.exists() == true

  def pom = new XmlParser().parse(pom_file)
  assert pom != null
  def artifactId = pom.artifactId[0].text()

  def leftId = "${artifactId}L"
  def rightId = "${artifactId}R"

  createSubModule(pom_file, leftId)
  createSubModule(pom_file, rightId)

  def modules = new Node(pom, 'modules')
  new Node(modules, "module", leftId.toString())
  new Node(modules, "module", rightId.toString())
  pom.packaging[0].setValue("pom")

  printPom(pom, pom_file)
  generateChild(new File(curDir, leftId), depth - 1)
  generateChild(new File(curDir, rightId), depth - 1)
}

def createSubModule(parent_pom_file, newId) {
  def pom = new XmlParser().parse(parent_pom_file)

  if (pom.parent.size() == 0) {
    def parent = new Node(pom, "parent")
    new Node(parent, "artifactId", pom.artifactId[0].text())
    new Node(parent, "groupId", pom.groupId[0].text())
    new Node(parent, "version", pom.version[0].text())
  } else {
    pom.parent[0].artifactId[0].setValue(pom.artifactId[0].text())
  }

  pom.artifactId[0].setValue(newId)
  def subDir = new File(parent_pom_file.getParentFile(), newId)
  subDir.deleteDir()
  subDir.mkdir()
  printPom(pom, new File(subDir, "pom.xml"))
}

def printPom(pom, file) {
  def s = new StringWriter()
  def xmlNodePrinter = new XmlNodePrinter(new PrintWriter(s))
  xmlNodePrinter.preserveWhitespace = true
  xmlNodePrinter.print(pom)
  file.write(s.toString())
}


def current = new File(".")
builder = new AntBuilder()
builder.copy(tofile: "pom.xml", file:"seed.pom.xml", overwrite:true)
generateChild(current, args[0].toInteger() - 1)