Summary: Time4U server MySQL backend
Name: time4u-server-mysql
Version: ${rpm.version}
Release: ${rpm.release}
License: MIT
Group: Productivity/Networking/Web/Servers
Source: https://sourceforge.net/projects/time4u/
BuildArch: noarch
Requires: time4u-server mysql-connector-java
BuildRoot: /tmp/time4u-mysql
%description
Use MySql as database backend.

%prep
JBOSS_VERSION=5.0.1.GA
rm -rf jboss-$JBOSS_VERSION
rm -rf time4u-with-jboss
unzip $RPM_SOURCE_DIR/jboss-$JBOSS_VERSION-jdk6.zip
mv jboss-$JBOSS_VERSION time4u-with-jboss
tar xjf $RPM_SOURCE_DIR/time4u-assemblies-deploy.tar.bz2

%install
rm -rf $RPM_BUILD_ROOT/srv/time4u-with-jboss
mkdir -p $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/messaging
mkdir -p $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/conf
sed "s/@db.user@/time4u/ ; s/@db.name@/time4u/ ; s/@db.password@/time4u/ ; s/@db.host@/localhost/" $RPM_BUILD_DIR/time4u-assemblies-deploy-*/db/mysql/time4u-ds.xml > $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/time4u-ds.xml
mv $RPM_BUILD_DIR/time4u-assemblies-deploy-*/db/mysql/time4u-hibernate.cfg.xml $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/conf
cp $RPM_SOURCE_DIR/time4u-mysql-init.sql $RPM_BUILD_ROOT/srv/time4u-with-jboss
mkdir -p $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/messaging
sed "s/DefaultDS/Time4UDS/ ; 149d" $RPM_BUILD_DIR/time4u-with-jboss/docs/examples/jms/mysql-persistence-service.xml > $RPM_BUILD_ROOT/srv/time4u-with-jboss/server/time4u/deploy/messaging/mysql-persistence-service.xml

%files
%defattr(-,wwwrun,www)
/srv/time4u-with-jboss/server/time4u/deploy/time4u-ds.xml
/srv/time4u-with-jboss/server/time4u/conf/time4u-hibernate.cfg.xml
/srv/time4u-with-jboss/time4u-mysql-init.sql
/srv/time4u-with-jboss/server/time4u/deploy/messaging/mysql-persistence-service.xml

%post
ln -s /usr/share/java/mysql-connector-java.jar /srv/time4u-with-jboss/server/time4u/lib
rm -f /srv/time4u-with-jboss/server/time4u/deploy/messaging/hsqldb-persistence-service.xml

