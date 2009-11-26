println "Hallo"

def dir = new File(project.basedir, "target/gen/configuration/org.eclipse.equinox.simpleconfigurator")

ant.mkdir(dir: dir);

def dependencyMap = new HashMap();

for ( dep in project.dependencies ) {  
  dependencyMap.put(dep.groupId + ":" + dep.artifactId + (dep.classifier == null ? "" : ":jar:" + dep.classifier), 
    dep);
}


def bundles = new XmlParser().parse(new File(project.basedir, "src/main/assembly/bundles.xml"));
def lines = new ArrayList();

for (bundle in bundles.dependencySet.bundle ) {
	def startLevel = bundle.'@start-level' != null ? bundle.'@start-level' : 4;
	def autoStart = bundle.'@auto-start' != null ? bundle.'@auto-start' : false;
	def id = dependencyMap[bundle.text()].groupId + "." + dependencyMap[bundle.text()].artifactId;
	def version = dependencyMap[bundle.text()].version;
	
	lines.add(id + "," + version + ",plugins/" + id + "_" + version + ".jar," + startLevel + "," + autoStart);
}

for (bundle in bundles.manual.bundle ) {
	def startLevel = bundle.'@start-level' != null ? bundle.'@start-level' : 4;
	def autoStart = bundle.'@auto-start' != null ? bundle.'@auto-start' : false;
	def id = bundle.text();
	def version = bundle.'@version';
	def path = bundle.'@path';

	if ( path ) {
		lines.add(id + "," + version + ",plugins/" + id + "_" + version + "/," + startLevel + "," + autoStart);
	} else {
		lines.add(id + "," + version + ",plugins/" + id + "_" + version + ".jar," + startLevel + "," + autoStart);
	}
}

lines.sort();

def writer = new PrintWriter(new FileWriter(new File(dir, "bundles.info")));

writer.println("#version=1");

for ( line in lines ) {
	writer.println(line);
}

writer.flush();
writer.close();

