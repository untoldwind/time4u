println "Hallo"

def dir = new File(project.basedir, "target/gen/configuration/org.eclipse.equinox.simpleconfigurator")

ant.mkdir(dir: dir);

def writer = new PrintWriter(new FileWriter(new File(dir, "bundles.info")));

writer.println("#version=1");

for ( dep in project.dependencies ) {
  writer.print(dep.groupId);
  writer.print(".");
  writer.print(dep.artifactId);
  writer.print(",");
  writer.print(dep.version);
  writer.print(",plugins/");
  writer.print(dep.groupId);
  writer.print(".");
  writer.print(dep.artifactId);
  writer.print("_");
  writer.print(dep.version);
  writer.print(".jar");
  writer.println(",4,false");
  
  println dep.groupId + "." + dep.artifactId + "," + dep.version + ",";
}

writer.flush();
writer.close();
